/*
 * Copyright (c) 2013 DataTorrent, Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.netlet.util.Slice;
import com.datatorrent.netlet.NetletThrowable.NetletRuntimeException;
import com.datatorrent.netlet.util.CircularBuffer;

/**
 * <p>
 * Abstract AbstractClient class.</p>
 *
 * @since 1.0.0
 */
public abstract class AbstractClient extends AbstractClientListener
{
  private static final int THROWABLES_COLLECTION_SIZE = 4;
  public static final int MAX_SENDBUFFER_SIZE;

  protected final CircularBuffer<NetletThrowable> throwables;
  protected final CircularBuffer<CircularBuffer<Slice>> bufferOfBuffers;
  protected final CircularBuffer<Slice> freeBuffer;
  protected CircularBuffer<Slice> sendBuffer4Offers, sendBuffer4Polls;
  protected final ByteBuffer writeBuffer;
  protected boolean write = true;

  public AbstractClient(int writeBufferSize, int sendBufferSize)
  {
    this(ByteBuffer.allocateDirect(writeBufferSize), sendBufferSize);
  }

  public AbstractClient(int sendBufferSize)
  {
    this(8 * 1 * 1024, sendBufferSize);
  }

  public AbstractClient()
  {
    this(8 * 1 * 1024, 1024);
  }

  public AbstractClient(ByteBuffer writeBuffer, int sendBufferSize)
  {
    int i = 1;
    int n = 1;
    do {
      n *= 2;
      i++;
    }
    while (n != MAX_SENDBUFFER_SIZE);
    bufferOfBuffers = new CircularBuffer<CircularBuffer<Slice>>(i);

    this.throwables = new CircularBuffer<NetletThrowable>(THROWABLES_COLLECTION_SIZE);
    this.writeBuffer = writeBuffer;
    if (sendBufferSize == 0) {
      sendBufferSize = 1024;
    }
    else if (sendBufferSize % 1024 > 0) {
      sendBufferSize += 1024 - (sendBufferSize % 1024);
    }
    sendBuffer4Polls = sendBuffer4Offers = new CircularBuffer<Slice>(sendBufferSize, 10);
    freeBuffer = new CircularBuffer<Slice>(sendBufferSize, 10);
  }

  @Override
  public void connected()
  {
    write = false;
  }

  @Override
  public void disconnected()
  {
    write = true;
  }

  @Override
  public final void read() throws IOException
  {
    SocketChannel channel = (SocketChannel)key.channel();
    int read;
    if ((read = channel.read(buffer())) > 0) {
      this.read(read);
    }
    else if (read == -1) {
      try {
        channel.close();
      }
      finally {
        disconnected();
        unregistered(key);
        key.attach(Listener.NOOP_CLIENT_LISTENER);
      }
    }
    else {
      logger.debug("{} read 0 bytes", this);
    }
  }

  /**
   * @since 1.2.0
   */
  @Override
  public boolean isReadSuspended()
  {
    return super.isReadSuspended();
  }

  /**
   * @since 1.2.0
   */
  @Override
  public boolean suspendReadIfResumed()
  {
    return super.suspendReadIfResumed();
  }

  /**
   * @since 1.2.0
   */
  @Override
  public boolean resumeReadIfSuspended()
  {
    return super.resumeReadIfSuspended();
  }

  /**
   * @deprecated As of release 1.2.0, replaced by {@link #suspendReadIfResumed()}
   */
  @Deprecated
  public void suspendRead()
  {
    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
  }

  /**
   * @deprecated As of release 1.2.0, replaced by {@link #resumeReadIfSuspended()}
   */
  @Deprecated
  public void resumeRead()
  {
    key.interestOps(key.interestOps() | SelectionKey.OP_READ);
    key.selector().wakeup();
  }

  @Override
  public final void write() throws IOException
  {
    /*
     * at first when we enter this function, our buffer is in fill mode.
     */
    int remaining, size;
    if ((size = sendBuffer4Polls.size()) > 0 && (remaining = writeBuffer.remaining()) > 0) {
      do {
        Slice f = sendBuffer4Polls.peekUnsafe();
        if (remaining < f.length) {
          writeBuffer.put(f.buffer, f.offset, remaining);
          f.offset += remaining;
          f.length -= remaining;
          break;
        }
        else {
          writeBuffer.put(f.buffer, f.offset, f.length);
          remaining -= f.length;
          freeBuffer.offer(sendBuffer4Polls.pollUnsafe());
        }
      }
      while (--size > 0);
    }

    /*
     * switch to the read mode!
     */
    writeBuffer.flip();

    SocketChannel channel = (SocketChannel)key.channel();
    while ((remaining = writeBuffer.remaining()) > 0) {
      remaining -= channel.write(writeBuffer);
      if (remaining > 0) {
        /*
         * switch back to the fill mode.
         */
        writeBuffer.compact();
        return;
      }
      else if (size > 0) {
        /*
         * switch back to the write mode.
         */
        writeBuffer.clear();

        remaining = writeBuffer.capacity();
        do {
          Slice f = sendBuffer4Polls.peekUnsafe();
          if (remaining < f.length) {
            writeBuffer.put(f.buffer, f.offset, remaining);
            f.offset += remaining;
            f.length -= remaining;
            break;
          }
          else {
            writeBuffer.put(f.buffer, f.offset, f.length);
            remaining -= f.length;
            freeBuffer.offer(sendBuffer4Polls.pollUnsafe());
          }
        }
        while (--size > 0);

        /*
         * switch to the read mode.
         */
        writeBuffer.flip();
      }
    }

    /*
     * switch back to fill mode.
     */
    writeBuffer.clear();
    synchronized (bufferOfBuffers) {
      if (sendBuffer4Polls.isEmpty()) {
        if (sendBuffer4Offers == sendBuffer4Polls) {
          key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
          write = false;
        }
        else if (bufferOfBuffers.isEmpty()) {
          sendBuffer4Polls = sendBuffer4Offers;
        }
        else {
          sendBuffer4Polls = bufferOfBuffers.pollUnsafe();
        }
      }
    }
  }

  public boolean send(byte[] array)
  {
    return send(array, 0, array.length);
  }

  public boolean send(byte[] array, int offset, int len)
  {
    if (!throwables.isEmpty()) {
      NetletThrowable.Util.throwRuntime(throwables.pollUnsafe());
    }

    Slice f;
    if (freeBuffer.isEmpty()) {
      f = new Slice(array, offset, len);
    }
    else {
      f = freeBuffer.pollUnsafe();
      f.buffer = array;
      f.offset = offset;
      f.length = len;
    }

    if (sendBuffer4Offers.offer(f)) {
      synchronized (bufferOfBuffers) {
        if (!write) {
          key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
          write = true;
          key.selector().wakeup();
        }
      }

      return true;
    }

    if (!throwables.isEmpty()) {
      NetletThrowable.Util.throwRuntime(throwables.pollUnsafe());
    }

    if (sendBuffer4Offers.capacity() != MAX_SENDBUFFER_SIZE) {
      synchronized (bufferOfBuffers) {
        if (sendBuffer4Offers != sendBuffer4Polls) {
          bufferOfBuffers.add(sendBuffer4Offers);
        }

        sendBuffer4Offers = new CircularBuffer<Slice>(sendBuffer4Offers.capacity() << 1);
        sendBuffer4Offers.add(f);
        if (!write) {
          key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
          write = true;
          key.selector().wakeup();
        }

        return true;
      }
    }

    return false;
  }

  @Override
  public void handleException(Exception cce, EventLoop el)
  {
    super.handleException(cce, el);
    throwables.offer(NetletThrowable.Util.rewrap(cce, el));
  }

  public abstract ByteBuffer buffer();

  public abstract void read(int len);

  @Override
  public void unregistered(SelectionKey key)
  {
    synchronized (bufferOfBuffers) {
      final CircularBuffer<Slice> SEND_BUFFER = sendBuffer4Offers;
      sendBuffer4Offers = new CircularBuffer<Slice>(0)
      {
        @Override
        public boolean isEmpty()
        {
          return SEND_BUFFER.isEmpty();
        }

        @Override
        public boolean offer(Slice e)
        {
          throw new NetletRuntimeException(new UnsupportedOperationException("Client does not own the socket any longer!"), null);
        }

        @Override
        public int size()
        {
          return SEND_BUFFER.size();
        }

        @Override
        public Slice pollUnsafe()
        {
          return SEND_BUFFER.pollUnsafe();
        }

        @Override
        public Slice peekUnsafe()
        {
          return SEND_BUFFER.peekUnsafe();
        }

      };
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

  /* implemented here since it requires access to logger. */
  static {
    int size = 32 * 1024;
    final String key = "NETLET.MAX_SENDBUFFER_SIZE";
    String property = System.getProperty(key);
    if (property != null) {
      try {
        size = Integer.parseInt(property);
        if (size <= 0) {
          throw new IllegalArgumentException(key + " needs to be a positive integer which is also power of 2.");
        }

        if ((size & (size - 1)) != 0) {
          size--;
          size |= size >> 1;
          size |= size >> 2;
          size |= size >> 4;
          size |= size >> 8;
          size |= size >> 16;
          size++;
          logger.warn("{} set to {} since {} is not power of 2.", key, size, property);
        }
      }
      catch (Exception exception) {
        logger.warn("{} set to {} since {} could not be parsed as an integer.", key, size, property, exception);
      }
    }
    MAX_SENDBUFFER_SIZE = size;
  }
}
