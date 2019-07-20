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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

/**
 * Provides a premium implementation of circular buffer<p>
 * <br>
 *
 * @param <T> type of the objects in this buffer.
 * @since 1.0.0
 */
public class CircularBuffer<T> implements UnsafeBlockingQueue<T>
{
  // largest supported buffer size
  static final int MAX_SIZE = 1 << 30;

  private final T[] buffer;
  private final int buffermask;
  private final int spinMillis;
  protected volatile long tail;
  protected volatile long head;


  /**
   *
   * Constructing a circular buffer of 'n' integers<p>
   * <br>
   *
   * @param n size of the buffer to be constructed
   * @param spin time in milliseconds for which to wait before checking for expected value if it's missing
   * <br>
   */
  @SuppressWarnings("unchecked")
  public CircularBuffer(int n, int spin)
  {
    if (n < 0) {
      throw new IllegalArgumentException(String.format("Error: value cannot be negative: %d", n));
    }
    if (n > MAX_SIZE) {
      throw new IllegalArgumentException(String.format("Error: value too large: %d", n));
    }

    final int size = (0 == n) ? 1
        : (0 == (n & (n - 1))) ? n
        : (1 << (32 - Integer.numberOfLeadingZeros(n)));

    buffer = (T[])new Object[size];
    buffermask = size - 1;
    spinMillis = spin;
  }

  private CircularBuffer(T[] buffer, int buffermask, int spinMillis)
  {
    this.buffer = buffer;
    this.buffermask = buffermask;
    this.spinMillis = spinMillis;
  }

  /**
   *
   * Constructing a circular buffer of 'n' integers<p>
   * <br>
   *
   * @param n size of the buffer to be constructed
   * <br>
   */
  public CircularBuffer(int n)
  {
    this(n, 10);
  }

  @Override
  public boolean add(T e)
  {
    if (head - tail <= buffermask) {
      buffer[(int)(head & buffermask)] = e;
      head++;
      return true;
    }

    throw new IllegalStateException("Collection is full");
  }

  @Override
  public T remove()
  {
    if (head > tail) {
      int pos = (int)(tail & buffermask);
      T t = buffer[pos];
      buffer[pos] = null;
      tail++;
      return t;
    }

    throw new IllegalStateException("Collection is empty");
  }

  @Override
  public T peek()
  {
    if (head > tail) {
      return buffer[(int)(tail & buffermask)];
    }

    return null;
  }

  @Override
  public int size()
  {
    return (int)(head - tail);
  }

  /**
   *
   * Total design capacity of the buffer<p>
   * <br>
   *
   * @return Total return capacity of the buffer
   * <br>
   */
  public int capacity()
  {
    return buffermask + 1;
  }

  @Override
  public int drainTo(Collection<? super T> container)
  {
    int size = size();

    while (head > tail) {
      container.add(buffer[(int)(tail & buffermask)]);
      tail++;
    }

    return size;
  }

  @Override
  public String toString()
  {
    return "head=" + head + ", tail=" + tail + ", capacity=" + (buffermask + 1);
  }

  @Override
  public boolean offer(T e)
  {
    if (head - tail <= buffermask) {
      buffer[(int)(head & buffermask)] = e;
      head++;
      return true;
    }

    return false;
  }

  @Override
  @SuppressWarnings("SleepWhileInLoop")
  public void put(T e) throws InterruptedException
  {
    int spinMillis = 0;
    do {
      if (head - tail < buffermask) {
        buffer[(int)(head & buffermask)] = e;
        head++;
        return;
      }

      Thread.sleep(spinMillis);
      spinMillis = Math.min(this.spinMillis, spinMillis + 1);
    }
    while (true);
  }

  @Override
  @SuppressWarnings("SleepWhileInLoop")
  public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException
  {
    long millis = unit.toMillis(timeout);
    int spinMillis = 0;
    do {
      if (head - tail < buffermask) {
        buffer[(int)(head & buffermask)] = e;
        head++;
        return true;
      }

      Thread.sleep(spinMillis);
      spinMillis = Math.min(this.spinMillis, spinMillis + 1);
    }
    while ((millis -= spinMillis) >= 0);

    return false;
  }

  @Override
  @SuppressWarnings("SleepWhileInLoop")
  public T take() throws InterruptedException
  {
    int spinMillis = 0;
    do {
      if (head > tail) {
        int pos = (int)(tail & buffermask);
        T t = buffer[pos];
        buffer[pos] = null;
        tail++;
        return t;
      }

      Thread.sleep(spinMillis);
      spinMillis = Math.min(this.spinMillis, spinMillis + 1);
    }
    while (true);
  }

  @Override
  @SuppressWarnings("SleepWhileInLoop")
  public T poll(long timeout, TimeUnit unit) throws InterruptedException
  {
    long millis = unit.toMillis(timeout);
    int spinMillis = 0;
    do {
      if (head > tail) {
        int pos = (int)(tail & buffermask);
        T t = buffer[pos];
        buffer[pos] = null;
        tail++;
        return t;
      }

      Thread.sleep(spinMillis);
      spinMillis = Math.min(this.spinMillis, spinMillis + 1);
    }
    while ((millis -= spinMillis) >= 0);

    return null;
  }

  @Override
  public int remainingCapacity()
  {
    return buffermask + 1 - (int)(head - tail);
  }

  @Override
  public boolean remove(Object o)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean contains(Object o)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int drainTo(final Collection<? super T> collection, final int maxElements)
  {
    int i = -1;
    int pos;
    while (++i < maxElements && head > tail) {
      pos = (int)(tail & buffermask);
      collection.add(buffer[pos]);
      buffer[pos] = null;
      tail++;
    }

    return i;
  }

  @Override
  public T poll()
  {
    if (head > tail) {
      int pos = (int)(tail & buffermask);
      T t = buffer[pos];
      buffer[pos] = null;
      tail++;
      return t;
    }

    return null;
  }

  @Override
  public T pollUnsafe()
  {
    int pos = (int)(tail & buffermask);
    T t = buffer[pos];
    buffer[pos] = null;
    tail++;
    return t;
  }

  @Override
  public T element()
  {
    if (head > tail) {
      return buffer[(int)(tail & buffermask)];
    }

    throw new IllegalStateException("Collection is empty");
  }

  @Override
  public boolean isEmpty()
  {
    return head == tail;
  }

  private class FrozenIterator implements Iterator<T>, Iterable<T>, Cloneable
  {
    private final long frozenHead;
    private final long frozenTail;
    private long tail;

    FrozenIterator()
    {
      this(CircularBuffer.this.head, CircularBuffer.this.tail);
    }

    FrozenIterator(long frozenHead, long frozenTail)
    {
      this.frozenHead = frozenHead;
      this.frozenTail = frozenTail;
      this.tail = frozenTail;
    }

    @Override
    public boolean hasNext()
    {
      return tail < frozenHead;
    }

    @Override
    public T next()
    {
      return buffer[(int)(tail++ & buffermask)];
    }

    @Override
    public void remove()
    {
      buffer[(int)((tail - 1) & buffermask)] = null;
    }

    @Override
    public Iterator<T> iterator()
    {
      return new FrozenIterator(frozenHead, frozenTail);
    }

  }

  public Iterator<T> getFrozenIterator()
  {
    return new FrozenIterator();
  }

  public Iterable<T> getFrozenIterable()
  {
    return new FrozenIterator();
  }

  @Override
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
    {
      @Override
      public boolean hasNext()
      {
        return head > tail;
      }

      @Override
      public T next()
      {
        int pos = (int)(tail & buffermask);
        T t = buffer[pos];
        buffer[pos] = null;
        tail++;
        return t;
      }

      @Override
      public void remove()
      {
      }

    };
  }

  @Override
  public Object[] toArray()
  {
    final int count = (int)(head - tail);
    Object[] array = new Object[count];
    int pos;
    for (int i = 0; i < count; i++) {
      pos = (int)(tail & buffermask);
      array[i] = buffer[pos];
      buffer[pos] = null;
      tail++;
    }

    return array;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a)
  {
    int count = (int)(head - tail);
    if (a.length < count) {
      a = (T[])new Object[count];
    }

    int pos;
    for (int i = 0; i < count; i++) {
      pos = (int)(tail & buffermask);
      a[i] = (T)buffer[pos];
      buffer[pos] = null;
      tail++;
    }

    return a;
  }

  @Override
  public boolean containsAll(Collection<?> c)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean addAll(Collection<? extends T> c)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean removeAll(Collection<?> c)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean retainAll(Collection<?> c)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void clear()
  {
    head = 0;
    tail = 0;
    Arrays.fill(buffer, null);
  }

  @Override
  public T peekUnsafe()
  {
    return buffer[(int)(tail & buffermask)];
  }

  public CircularBuffer<T> getWhitehole(final String exceptionMessage)
  {
    CircularBuffer<T> cb = new CircularBuffer<T>(buffer, buffermask, spinMillis)
    {
      @Override
      public boolean add(T e)
      {
        throw new IllegalStateException(exceptionMessage);
      }

      @Override
      @SuppressWarnings("SleepWhileInLoop")
      public void put(T e) throws InterruptedException
      {
        while (true) {
          sleep(spinMillis);
        }
      }

      @Override
      public boolean offer(T e)
      {
        return false;
      }

      @Override
      public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException
      {
        long millis = unit.toMillis(timeout);
        sleep(millis);
        return false;
      }

      @Override
      public int remainingCapacity()
      {
        return 0;
      }

      @Override
      public boolean addAll(Collection<? extends T> c)
      {
        throw new IllegalStateException(exceptionMessage);
      }

    };
    cb.head = head;
    cb.tail = tail;

    return cb;
  }

  private static final Logger logger = LoggerFactory.getLogger(CircularBuffer.class);
}
