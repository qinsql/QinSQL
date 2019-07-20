/**
 * Copyright (C) 2016 DataTorrent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.netlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.netlet.util.Slice;

public class WriteOnlyClient extends AbstractClientListener
{
  private static final Logger logger = LoggerFactory.getLogger(WriteOnlyClient.class);

  private static class ForwardingQueue extends AbstractQueue<Slice>
  {
    private final Queue<Slice> queue;

    private ForwardingQueue(Queue<Slice> queue)
    {
      this.queue = queue;
    }

    @Override
    public Iterator<Slice> iterator()
    {
      return queue.iterator();
    }

    @Override
    public int size()
    {
      return queue.size();
    }

    @Override
    public boolean offer(Slice slice)
    {
      return queue.offer(slice);
    }

    @Override
    public Slice poll()
    {
      return queue.poll();
    }

    @Override
    public Slice peek()
    {
      return queue.peek();
    }
  }

  protected final ByteBuffer writeBuffer;
  protected Queue<Slice> sendQueue;
  protected final SpscArrayQueue<Slice> freeQueue;
  protected final Queue<NetletThrowable> throwables = new ConcurrentLinkedQueue<NetletThrowable>();
  protected boolean isWriteEnabled = true;
  protected Lock lock = new ReentrantLock();

  public WriteOnlyClient()
  {
    this(64 * 1024, 1024);
  }

  public WriteOnlyClient(final int writeBufferCapacity, final int sendQueueCapacity)
  {
    this(ByteBuffer.allocateDirect(writeBufferCapacity), sendQueueCapacity);
  }

  public WriteOnlyClient(final ByteBuffer writeBuffer, final int sendQueueCapacity)
  {
    this.writeBuffer = writeBuffer;
    sendQueue = new SpscArrayQueue<Slice>(sendQueueCapacity);
    freeQueue = new SpscArrayQueue<Slice>(sendQueueCapacity);
  }

  @Override
  public void connected()
  {
    super.connected();
    shutdownIO(true);
    suspendReadIfResumed();
    try {
      lock.lock();
      isWriteEnabled = !isWriteSuspended();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void unregistered(SelectionKey key)
  {
    try {
      lock.lock();
      if (sendQueue.peek() == null && !isWriteEnabled) {
        super.unregistered(key);
      } else {
        sendQueue = new ForwardingQueue(sendQueue)
        {
          @Override
          public boolean offer(Slice f)
          {
            throw new RuntimeException(String.format("Client %s does not accept new data.", WriteOnlyClient.this));
          }
        };
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void read() throws IOException
  {
    if (suspendReadIfResumed()) {
      logger.warn("{} OP_READ should be disabled", this);
    } else {
      logger.error("{} read is not expected", this);
    }
  }

  @Override
  public boolean resumeReadIfSuspended()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write() throws IOException
  {
    /*
     * at first when we enter this function, our buffer is in fill mode.
     */
    int remaining = writeBuffer.remaining();
    Slice slice;
    while ((slice = sendQueue.peek()) != null) {
      while (remaining < slice.length) {
        if (remaining > 0) {
          writeBuffer.put(slice.buffer, slice.offset, remaining);
          slice.offset += remaining;
          slice.length -= remaining;
        }
        if (channelWrite() == 0) {
          return;
        }
        remaining = writeBuffer.remaining();
      }
      writeBuffer.put(slice.buffer, slice.offset, slice.length);
      slice.buffer = null;
      remaining -= slice.length;
      freeQueue.offer(sendQueue.poll());
    }
    channelWrite();
  }

  protected int channelWrite() throws IOException
  {
    if (writeBuffer.flip().remaining() > 0) {
      final SocketChannel channel = (SocketChannel)key.channel();
      final int write = channel.write(writeBuffer);
      writeBuffer.compact();
      return write;
    } else {
      writeBuffer.clear();
      try {
        lock.lock();
        if (sendQueue.peek() == null) {
          /*
           * No more data to send. Clear OP_WRITE, so this client key is not selected into selector.selectedKeys().
           * Listener.DisconnectingListener.disconnect() also uses OP_WRITE to indicate empty send queue
           */
          suspendWriteIfResumed();
          isWriteEnabled = false;
        }
      } finally {
        lock.unlock();
      }
      return 0;
    }
  }

  public boolean send(byte[] array)
  {
    return send(array, 0, array.length);
  }

  public boolean send(byte[] array, int offset, int len)
  {
    Slice f = freeQueue.poll();
    if (f == null) {
      f = new Slice(array, offset, len);
    } else {
      if (f.buffer != null) {
        throw new RuntimeException("Unexpected slice " + f.toString());
      }
      f.buffer = array;
      f.offset = offset;
      f.length = len;
    }
    return send(f);
  }

  public boolean send(Slice f)
  {
    final NetletThrowable throwable = throwables.poll();
    if (throwable != null) {
      NetletThrowable.Util.throwRuntime(throwable);
    }

    if (sendQueue.offer(f)) {
      try {
        lock.lock();
        if (!isWriteEnabled) {
          resumeWriteIfSuspended();
          isWriteEnabled = true;
        }
      } finally {
        lock.unlock();
      }
      return true;
    }
    return false;
  }

  @Override
  public void handleException(Exception e, EventLoop el)
  {
    if (!key.isValid()) {
      super.handleException(e, el);
      throwables.offer(NetletThrowable.Util.rewrap(e, el));
    } else if (key.attachment() == this) {
      if (e instanceof IOException) {
        logger.error("Disconnecting {} because of an exception.", this, e);
        if (isConnected()) {
          el.disconnect(this);
        }
      } else {
        super.handleException(e, el);
        throwables.offer(NetletThrowable.Util.rewrap(e, el));
      }
    } else {
      logger.error("Ignoring exception received after discarding the connection.", e);
    }
  }
}
