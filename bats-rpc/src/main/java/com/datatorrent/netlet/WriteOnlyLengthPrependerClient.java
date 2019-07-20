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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.netlet.util.Slice;
import com.datatorrent.netlet.util.VarInt;

public class WriteOnlyLengthPrependerClient extends WriteOnlyClient
{
  private static final Logger logger = LoggerFactory.getLogger(WriteOnlyLengthPrependerClient.class);

  private boolean newMessage = true;

  public WriteOnlyLengthPrependerClient(final int sendBufferCapacity)
  {
    this(64 * 1024, sendBufferCapacity);
  }

  public WriteOnlyLengthPrependerClient(final int writeBufferCapacity, final int sendBufferCapacity)
  {
    super(writeBufferCapacity, sendBufferCapacity);
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
      if (newMessage) {
        if (remaining < 5 && (channelWrite() == 0 || (remaining = writeBuffer.remaining()) < 5)) {
          return;
        }
        remaining -= VarInt.write(slice.length, writeBuffer);
        newMessage = false;
      }
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
      newMessage = true;
    }
    channelWrite();
  }
}
