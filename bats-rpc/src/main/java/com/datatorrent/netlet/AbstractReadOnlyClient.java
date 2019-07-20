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
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReadOnlyClient extends AbstractClientListener
{
  private static final Logger logger = LoggerFactory.getLogger(AbstractReadOnlyClient.class);

  public abstract ByteBuffer buffer();

  public abstract void read(int len);

  @Override
  public void connected()
  {
    super.connected();
    shutdownIO(false);
    suspendWriteIfResumed();
  }

  @Override
  public final void read() throws IOException
  {
    final SocketChannel channel = (SocketChannel)key.channel();
    final ByteBuffer buffer = buffer();
    final int read = channel.read(buffer);
    if (read > 0) {
      this.read(read);
    } else if (read == -1) {
      try {
        channel.close();
      } finally {
        disconnected();
        unregistered(key);
      }
    } else {
      /*
       * no data was read into the buffer
       */
      if (buffer.remaining() > 0) {
        logger.error("{} read {} bytes into byte buffer {}", this, read, buffer);
      } else {
        suspendReadIfResumed();
      }
    }
  }

  @Override
  public final void write() throws IOException
  {
    if (suspendWriteIfResumed()) {
      logger.warn("{} OP_WRITE should be disabled", this);
    } else {
      logger.error("{} write is not expected", this);
    }
  }

  @Override
  public boolean resumeWriteIfSuspended()
  {
    throw new UnsupportedOperationException();
  }

}
