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
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.netlet.util.DTThrowable;
import com.datatorrent.netlet.util.VarInt;

/**
 * <p>Abstract AbstractLengthPrependerClient class.</p>
 *
 * @since 1.0.0
 */
public abstract class AbstractLengthPrependerClient extends AbstractClient
{
  protected byte[] buffer;
  protected ByteBuffer byteBuffer;
  protected int size, writeOffset, readOffset;

  public AbstractLengthPrependerClient()
  {
    this(new byte[64 * 1024], 0, 1024);
  }

  public AbstractLengthPrependerClient(int readBufferSize, int sendBufferSize)
  {
    this(new byte[readBufferSize], 0, sendBufferSize);
  }

  public AbstractLengthPrependerClient(byte[] readbuffer, int position, int sendBufferSize)
  {
    super(sendBufferSize);
    buffer = readbuffer;
    byteBuffer = ByteBuffer.wrap(readbuffer);
    byteBuffer.position(position);
    writeOffset = position;
    readOffset = position;
  }

  @Override
  public ByteBuffer buffer()
  {
    return byteBuffer;
  }

  public int readSize()
  {
    if (readOffset < writeOffset) {
      int offset = readOffset;

      byte tmp = buffer[readOffset++];
      if (tmp >= 0) {
        return tmp;
      }
      else if (readOffset < writeOffset) {
        int integer = tmp & 0x7f;
        tmp = buffer[readOffset++];
        if (tmp >= 0) {
          return integer | tmp << 7;
        }
        else if (readOffset < writeOffset) {
          integer |= (tmp & 0x7f) << 7;
          tmp = buffer[readOffset++];

          if (tmp >= 0) {
            return integer | tmp << 14;
          }
          else if (readOffset < writeOffset) {
            integer |= (tmp & 0x7f) << 14;
            tmp = buffer[readOffset++];
            if (tmp >= 0) {
              return integer | tmp << 21;
            }
            else if (readOffset < writeOffset) {
              integer |= (tmp & 0x7f) << 21;
              tmp = buffer[readOffset++];
              if (tmp >= 0) {
                return integer | tmp << 28;
              }
              else {
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
      }
      else if (writeOffset == buffer.length) {
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
        }
        else {
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
      }
      else {       /* need to read more */
        endMessage();
        return;
      }
    }
    while (true);
  }

  public boolean write(byte[] message)
  {
    return write(message, 0, message.length);
  }

  public boolean write(byte[] message1, byte[] message2)
  {
    if (sendBuffer4Offers.remainingCapacity() < 3 && sendBuffer4Offers.capacity() == MAX_SENDBUFFER_SIZE) {
      return false;
    }

    if (intOffset > INT_ARRAY_SIZE) {
      intBuffer = new byte[INT_ARRAY_SIZE + 5];
      intOffset = 0;
    }

    int newOffset = VarInt.write(message1.length + message2.length, intBuffer, intOffset);
    if (send(intBuffer, intOffset, newOffset - intOffset)) {
      intOffset = newOffset;
      if (send(message1, 0, message1.length) && send(message2, 0, message2.length)) {
        return true;
      }

      logger.debug("Exiting sendBuffer for Offers = {}, socket = {}", sendBuffer4Offers, key.channel());
      System.exit(0);
      throw new IllegalStateException("Only partial data could be written!");
    }

    logger.debug("sendBuffer for Offers = {}, socket = {}", sendBuffer4Offers, key.channel());
    return false;
  }

  private int intOffset;
  private static final int INT_ARRAY_SIZE = 4096 - 5;
  private byte[] intBuffer = new byte[INT_ARRAY_SIZE + 5];

  public boolean write(byte[] message, int offset, int size)
  {
    if (sendBuffer4Offers.remainingCapacity() < 2 && sendBuffer4Offers.capacity() == MAX_SENDBUFFER_SIZE) {
      return false;
    }

    if (intOffset > INT_ARRAY_SIZE) {
      intBuffer = new byte[INT_ARRAY_SIZE + 5];
      intOffset = 0;
    }

    int newOffset = VarInt.write(size, intBuffer, intOffset);
    if (send(intBuffer, intOffset, newOffset - intOffset)) {
      intOffset = newOffset;
      if (send(message, offset, size)) {
        return true;
      }

      logger.debug("Exiting sendBuffer for Offers = {}, socket = {}", sendBuffer4Offers, key.channel());
      System.exit(0);
      throw new IllegalStateException("Only partial data could be written!");
    }

    return false;
  }

  public void beginMessage()
  {
  }

  /**
   * Discard remaining data currently in the read buffer.
   *<br><br>
   * This method can be called from within an {@link #onMessage} call to discard the remaining data currently in the read
   * buffer and not process the remaining messages using the {@link #onMessage} call.
   *<br><br>
   * A scenario in which this can be used is where multiple clients are chained together to process the messages. Each
   * client once it processes the messages it is responsible for transfers control to the next client in the chain to process
   * the rest of the data. The remaining data is copied over to the next client. This method can then be called to discard
   * the remaining data from this client so that it is not processed again in this client.
   */
  protected void discardReadBuffer() {
    readOffset = writeOffset - size;
  }

  public abstract void onMessage(byte[] buffer, int offset, int size);

  public void endMessage()
  {
  }

  @Override
  public void handleException(Exception cce, EventLoop el)
  {
    if (!key.isValid()) {
      super.handleException(cce, el);
    } else if (key.attachment() == this) {
      if (cce instanceof IOException) {
        logger.error("Disconnecting {} because of an exception.", this, cce);
        if (isConnected()) {
          el.disconnect(this);
        }
      } else {
        super.handleException(cce, el);
      }
    } else {
      logger.error("Ignoring exception received after discarding the connection.", cce);
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(AbstractLengthPrependerClient.class);
}
