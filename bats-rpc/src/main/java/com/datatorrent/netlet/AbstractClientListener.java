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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.netlet.Listener.ClientListener;

public abstract class AbstractClientListener implements ClientListener
{
  private static final Logger logger = LoggerFactory.getLogger(AbstractClientListener.class);

  private static final SelectionKey invalidSelectionKey = new SelectionKey()
  {
    @Override
    public SelectableChannel channel()
    {
      return null;
    }

    @Override
    public Selector selector()
    {
      return null;
    }

    @Override
    public boolean isValid()
    {
      return false;
    }

    @Override
    public void cancel()
    {

    }

    @Override
    public int interestOps()
    {
      return 0;
    }

    @Override
    public SelectionKey interestOps(int ops)
    {
      return this;
    }

    @Override
    public int readyOps()
    {
      return 0;
    }

    @Override
    public String toString()
    {
      return "Invalid Selection key";
    }
  };

  /*
     * access to the key is not thread safe. It is read/write on the default event loop and read only on other threads,
     * so other threads may get stale value.
     */
  protected SelectionKey key = invalidSelectionKey;

  private boolean isSuspended(final int ops)
  {
    return (key.interestOps() & ops) == 0;
  }

  private boolean suspendIfResumed(final int ops)
  {
    final int interestOps = key.interestOps();
    if ((interestOps & ops) == ops) {
      key.interestOps(interestOps & ~ops);
      logger.debug("{} suspended {}", this, ops == SelectionKey.OP_READ ? "read" : "write");
      return true;
    } else {
      return false;
    }
  }

  private boolean resumeIfSuspended(final int ops)
  {
    final int interestOps = key.interestOps();
    if ((interestOps & ops) == 0) {
      key.interestOps(interestOps | ops).selector().wakeup();
      logger.debug("{} resumed {}", this, ops == SelectionKey.OP_READ ? "read" : "write");
      return true;
    } else {
      return false;
    }
  }

  protected void shutdownIO(final boolean read)
  {
    if (!isConnected()) {
      throw new NotYetConnectedException();
    }
    final SocketChannel channel = (SocketChannel)key.channel();
    try {
      final Method method = channel.getClass().getDeclaredMethod(read ? "shutdownInput" : "shutdownOutput");
      method.setAccessible(true);
      method.invoke(channel);
      return;
    } catch (NoSuchMethodException e) {
      logger.warn("{}", this, e);
    } catch (IllegalAccessException e) {
      logger.warn("{}", this, e);
    } catch (InvocationTargetException e) {
      logger.warn("{}", this, e);
    }
    final Socket socket = channel.socket();
    try {
      if (read) {
        socket.shutdownInput();
      } else {
        socket.shutdownOutput();
      }
    } catch (IOException e) {
      logger.warn("{}", this, e);
    }
  }

  public boolean isConnected()
  {
    return key.isValid() && ((SocketChannel)key.channel()).isConnected();
  }

  public boolean isReadSuspended()
  {
    return isSuspended(SelectionKey.OP_READ);
  }

  public boolean isWriteSuspended()
  {
    return isSuspended(SelectionKey.OP_WRITE);
  }

  public boolean suspendReadIfResumed()
  {
    return suspendIfResumed(SelectionKey.OP_READ);
  }

  public boolean suspendWriteIfResumed()
  {
    return suspendIfResumed(SelectionKey.OP_WRITE);
  }

  public boolean resumeReadIfSuspended()
  {
    return resumeIfSuspended(SelectionKey.OP_READ);
  }

  public boolean resumeWriteIfSuspended()
  {
    return resumeIfSuspended(SelectionKey.OP_WRITE);
  }

  @Override
  public void registered(SelectionKey key)
  {
    if (this.key == invalidSelectionKey) {
      this.key = key;
    } else {
      logger.error("{} is registered with the different key: registered key={}, key={}.", this, this.key, key);
      unregistered(this.key);
      this.key = key;
    }
  }

  @Override
  public void unregistered(SelectionKey key)
  {
    if (this.key != key) {
      logger.warn("{} is registered with the different key: registered key={}, key={}.", this, this.key, key);
    } else {
      this.key = invalidSelectionKey;
    }
  }

  @Override
  public void connected()
  {
    logger.debug("{}", this);
  }

  @Override
  public void disconnected()
  {
    logger.debug("{}", this);
  }

  @Override
  public void handleException(Exception cce, EventLoop el)
  {
    logger.error("Exception in the {} on the {}", el, this, cce);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(getClass().getName())
        .append('@')
        .append(Integer.toHexString(System.identityHashCode(this)))
        .append('{')
        .append("key=").append(key);
    if (key.isValid()) {
      sb.append(", channel=").append(key.channel())
          .append(", interestOps=").append(key.interestOps());
    }
    return sb.append('}').toString();
  }
}
