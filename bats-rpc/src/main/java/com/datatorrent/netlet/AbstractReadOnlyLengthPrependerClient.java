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

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class AbstractReadOnlyLengthPrependerClient extends AbstractReadOnlyClient
{
  protected byte[] buffer;
  protected ByteBuffer byteBuffer;
  protected int size;
  protected int writeOffset;
  protected int readOffset;

  public AbstractReadOnlyLengthPrependerClient()
  {
    this(64 * 1024);
  }

  public AbstractReadOnlyLengthPrependerClient(int readBufferSize)
  {
    this(new byte[readBufferSize], 0);
  }

  public AbstractReadOnlyLengthPrependerClient(final byte[] buffer, int position)
  {
    this.buffer = buffer;
    byteBuffer = ByteBuffer.wrap(buffer);
    byteBuffer.position(position);
    writeOffset = position;
    readOffset = position;
  }

  protected int readSize()
  {
    if (readOffset < writeOffset) {
      int offset = readOffset;

      byte tmp = buffer[readOffset++];
      if (tmp >= 0) {
        return tmp;
      } else if (readOffset < writeOffset) {
        int integer = tmp & 0x7f;
        tmp = buffer[readOffset++];
        if (tmp >= 0) {
          return integer | tmp << 7;
        } else if (readOffset < writeOffset) {
          integer |= (tmp & 0x7f) << 7;
          tmp = buffer[readOffset++];

          if (tmp >= 0) {
            return integer | tmp << 14;
          } else if (readOffset < writeOffset) {
            integer |= (tmp & 0x7f) << 14;
            tmp = buffer[readOffset++];
            if (tmp >= 0) {
              return integer | tmp << 21;
            } else if (readOffset < writeOffset) {
              integer |= (tmp & 0x7f) << 21;
              tmp = buffer[readOffset++];
              if (tmp >= 0) {
                return integer | tmp << 28;
              } else {
                throw new NumberFormatException("Invalid varint at location " + offset + " => "
                    + Arrays.toString(Arrays.copyOfRange(buffer, offset, readOffset)));
              }
            }
          }
        }
      }

      readOffset = offset;
    }
    return -1;
  }

  public void beginMessage()
  {
  }

  public abstract void onMessage(byte[] buffer, int offset, int size);

  public void endMessage()
  {
  }

  @Override
  public ByteBuffer buffer()
  {
    return byteBuffer;
  }

  /**
   * Upon reading the data from the socket into the byteBuffer, this method is called.
   * read is pronounced "RED", past tense of "read", and not to be confused with the opposite of the "write" method
   *
   * @param len - length of the data in number of bytes read into the byteBuffer during the most recent read.
   */
  @Override
  public void read(int len)
  {
    beginMessage();
    writeOffset += len;
    do {
      while (size == 0) {
        size = readSize();
        if (size == -1) {
          if (writeOffset == buffer.length) {
            if (readOffset > writeOffset - 5) {
              /*
               * we may be reading partial varint, adjust the buffers so that we have enough space to read the full data.
               */
              byte[] newArray = new byte[buffer.length];
              System.arraycopy(buffer, readOffset, newArray, 0, writeOffset - readOffset);
              buffer = newArray;
              writeOffset -= readOffset;
              readOffset = 0;
              byteBuffer = ByteBuffer.wrap(buffer);
              byteBuffer.position(writeOffset);
            }
          }
          size = 0;
          endMessage();
          return;
        }
      }

      if (writeOffset - readOffset >= size) {
        onMessage(buffer, readOffset, size);
        readOffset += size;
        size = 0;
      } else if (writeOffset == buffer.length) {
        if (size > buffer.length) {
          int newsize = buffer.length;
          while (newsize < size) {
            newsize <<= 1;
          }
          //logger.info("resizing buffer to size {} from size {}", newsize, buffer.length);
          byte[] newArray = new byte[newsize];
          System.arraycopy(buffer, readOffset, newArray, 0, writeOffset - readOffset);
          buffer = newArray;
          writeOffset -= readOffset;
          readOffset = 0;
          byteBuffer = ByteBuffer.wrap(newArray);
          byteBuffer.position(writeOffset);
        } else {
          byte[] newArray = new byte[buffer.length];
          System.arraycopy(buffer, readOffset, newArray, 0, writeOffset - readOffset);
          buffer = newArray;
          writeOffset -= readOffset;
          readOffset = 0;
          byteBuffer = ByteBuffer.wrap(buffer);
          byteBuffer.position(writeOffset);
        }
        endMessage();
        return;
      } else {       /* need to read more */
        endMessage();
        return;
      }
    }
    while (true);
  }

}
