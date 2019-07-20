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

import java.io.Serializable;
import java.util.Arrays;

import org.getopt.util.hash.MurmurHash;

/**
 * <p>Slice class.</p>
 *
 * @since 1.0.0
 */
public class Slice implements Serializable, Cloneable
{
  public byte[] buffer;
  public int offset;
  public int length;

  @SuppressWarnings("unused")
  private Slice()
  {
    /* needed for some serializers */
  }

  public Slice(byte[] array, int offset, int length)
  {
    if (array != null && (offset | (length - 1) | (array.length - (offset + length))) < 0) {
      throw new IllegalArgumentException("Invalid slice: offset=" + offset + ", length=" + length + " array.length=" + array.length);
    }
    buffer = array;
    this.offset = offset;
    this.length = length;
  }

  public Slice(byte[] array)
  {
    buffer = array;
    this.offset = 0;
    this.length = array == null ? 0 : array.length;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 59 * hash + MurmurHash.hash(buffer, hash, offset, length);
    hash = 59 * hash + this.length;
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Slice other = (Slice)obj;
    if (this.length != other.length) {
      return false;
    }

    final int offset1 = this.offset;
    final byte[] buffer1 = this.buffer;
    int i = offset1 + this.length;

    final byte[] buffer2 = other.buffer;
    int j = other.offset + other.length;

    while (i-- > offset1) {
      if (buffer1[i] != buffer2[--j]) {
        return false;
      }
    }

    return true;
  }

  /**
   * Create deep copy of the slice.
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  public Slice clone() throws CloneNotSupportedException
  {
    Slice clone = (Slice)super.clone();
    clone.buffer = buffer.clone();
    return clone;
  }

  public byte[] toByteArray()
  {
    return Arrays.copyOfRange(this.buffer, this.offset, this.offset + this.length);
  }

  /**
   * Constructs a new {@code String} by decoding the specified subarray of
   * bytes using the platform's default charset. In other words it invokes
   * the constructor String(buffer, offset, length).
   *
   * @return the string object backed by the array.
   */
  public String stringValue()
  {
    return new String(buffer, offset, length);
  }

  @Override
  @SuppressWarnings("ImplicitArrayToString")
  public String toString()
  {
    return getClass().getSimpleName() + '{' + (length > 256 ? "buffer=" + buffer + ", offset=" + offset + ", length=" + length : Arrays.toString(Arrays.copyOfRange(buffer, offset, offset + length))) + '}';
  }

  private static final long serialVersionUID = 201311151835L;
}
