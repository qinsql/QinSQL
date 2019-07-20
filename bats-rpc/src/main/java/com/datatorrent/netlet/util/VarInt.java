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
package com.datatorrent.netlet.util;

import java.nio.ByteBuffer;

/**
 * <p>VarInt class.</p>
 *
 * @since 1.0.0
 */
public class VarInt
{
  /**
   * Writes the Variable Sized Integer of Length 32. Assumes that the buffer has 5 positions at least starting with offset.
   *
   * @param value
   * @param buffer
   * @param offset
   * @return int
   */
  public static int write(int value, byte[] buffer, int offset)
  {
    while (true) {
      if ((value & ~0x7F) == 0) {
        buffer[offset++] = (byte)value;
        return offset;
      }
      else {
        buffer[offset++] = (byte)((value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  public static int write(int value, byte[] buffer, int offset, int size)
  {
    int i = write(value, buffer, offset);
    int expectedOffset = offset + size;
    if (i < expectedOffset--) {
      buffer[i - 1] |= 0x80;
      while (i < expectedOffset) {
        buffer[i++] = (byte)0x80;
      }
      buffer[i++] = 0;
    }

    return i;
  }

  public static int write(int value, ByteBuffer buffer)
  {
    int i = 1;
    while ((value & ~0x7F) != 0) {
      buffer.put((byte)((value & 0x7F) | 0x80));
      value >>>= 7;
      ++i;
    }
    buffer.put((byte)value);
    return i;
  }

  public static int getSize(int value)
  {
    int offset = 0;
    while (true) {
      if ((value & ~0x7F) == 0) {
        return ++offset;
      }
      else {
        ++offset;
        value >>>= 7;
      }
    }
  }

  public static class MutableInt
  {
    public int integer;
  }

  /**
   *
   * @param readBuffer The array of bytes which contains the data to be parsed.
   * @param offset The offset where we should start reading the first 7 bits of varint.
   * @param limit The length of the slice of the data array where in which we are reading varint.
   * @param newOffset If the varint is read successfully, newOffset contains the position after varint.
   * @return varint value read
   */
  public static int read(byte[] readBuffer, int offset, int limit, MutableInt newOffset)
  {
    if (offset < limit) {
      byte tmp = readBuffer[offset++];
      if (tmp >= 0) {
        newOffset.integer = offset;
        return tmp;
      }
      else if (offset < limit) {
        int integer = tmp & 0x7f;
        tmp = readBuffer[offset++];
        if (tmp >= 0) {
          newOffset.integer = offset;
          return integer | tmp << 7;
        }
        else if (offset < limit) {
          integer |= (tmp & 0x7f) << 7;
          tmp = readBuffer[offset++];

          if (tmp >= 0) {
            newOffset.integer = offset;
            return integer | tmp << 14;
          }
          else if (offset < limit) {
            integer |= (tmp & 0x7f) << 14;
            tmp = readBuffer[offset++];
            if (tmp >= 0) {
              newOffset.integer = offset;
              return integer | tmp << 21;
            }
            else if (offset < limit) {
              integer |= (tmp & 0x7f) << 21;
              tmp = readBuffer[offset++];
              if (tmp >= 0) {
                newOffset.integer = offset;
                return integer | tmp << 28;
              }
              else {
                newOffset.integer = -5;
              }
            }
            else {
              newOffset.integer = -4;
            }
          }
          else {
            newOffset.integer = -3;
          }
        }
        else {
          newOffset.integer = -2;
        }
      }
      else {
        newOffset.integer = -1;
      }
    }
    else {
      newOffset.integer = 0;
    }

    return 0;
  }
}
