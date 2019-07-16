/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Utility and factory methods for Linq4j.
 */
public abstract class EnumeratorUtils {
  private EnumeratorUtils() {}

  private static final Object DUMMY = new Object();
 

  /**
   * Adapter that converts an enumerator into an iterator.
   *
   * <p><b>WARNING</b>: The iterator returned by this method does not call
   * {@link org.apache.calcite.util.Enumerator#close()}, so it is not safe to
   * use with an enumerator that allocates resources.</p>
   *
   * @param enumerator Enumerator
   * @param <T> Element type
   *
   * @return Iterator
   */
  public static <T> Iterator<T> enumeratorIterator(Enumerator<T> enumerator) {
    return new EnumeratorIterator<>(enumerator);
  }

  /**
   * Adapter that converts an iterable into an enumerator.
   *
   * @param iterable Iterable
   * @param <T> Element type
   *
   * @return enumerator
   */
  public static <T> Enumerator<T> iterableEnumerator(
      final Iterable<? extends T> iterable) {
//    if (iterable instanceof Enumerable) {
//      @SuppressWarnings("unchecked") final Enumerable<T> enumerable =
//          (Enumerable) iterable;
//      return enumerable.enumerator();
//    }
    return new IterableEnumerator<>(iterable);
  }
 
  /**
   * Returns an enumerator that is the cartesian product of the given
   * enumerators.
   *
   * <p>For example, given enumerator A that returns {"a", "b", "c"} and
   * enumerator B that returns {"x", "y"}, product(List(A, B)) will return
   * {List("a", "x"), List("a", "y"),
   * List("b", "x"), List("b", "y"),
   * List("c", "x"), List("c", "y")}.</p>
   *
   * <p>Notice that the cardinality of the result is the product of the
   * cardinality of the inputs. The enumerators A and B have 3 and 2
   * elements respectively, and the result has 3 * 2 = 6 elements.
   * This is always the case. In
   * particular, if any of the enumerators is empty, the result is empty.</p>
   *
   * @param enumerators List of enumerators
   * @param <T> Element type
   *
   * @return Enumerator over the cartesian product
   */
  public static <T> Enumerator<List<T>> product(
      List<Enumerator<T>> enumerators) {
    return new CartesianProductListEnumerator<>(enumerators);
  }

  /** Returns the cartesian product of an iterable of iterables. */
  public static <T> Iterable<List<T>> product(
      final Iterable<? extends Iterable<T>> iterables) {
    return () -> {
      final List<Enumerator<T>> enumerators = new ArrayList<>();
      for (Iterable<T> iterable : iterables) {
        enumerators.add(iterableEnumerator(iterable));
      }
      return enumeratorIterator(
          new CartesianProductListEnumerator<>(enumerators));
    };
  }


  /** Closes an iterator, if it can be closed. */
  private static <T> void closeIterator(Iterator<T> iterator) {
    if (iterator instanceof AutoCloseable) {
      try {
        ((AutoCloseable) iterator).close();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  /** Iterable enumerator.
   *
   * @param <T> element type */
  @SuppressWarnings("unchecked")
  static class IterableEnumerator<T> implements Enumerator<T> {
    private final Iterable<? extends T> iterable;
    Iterator<? extends T> iterator;
    T current;

    IterableEnumerator(Iterable<? extends T> iterable) {
      this.iterable = iterable;
      iterator = iterable.iterator();
      current = (T) DUMMY;
    }

    public T current() {
      if (current == DUMMY) {
        throw new NoSuchElementException();
      }
      return current;
    }

    public boolean moveNext() {
      if (iterator.hasNext()) {
        current = iterator.next();
        return true;
      }
      current = (T) DUMMY;
      return false;
    }

    public void reset() {
      iterator = iterable.iterator();
      current = (T) DUMMY;
    }

    public void close() {
      final Iterator<? extends T> iterator1 = this.iterator;
      this.iterator = null;
      closeIterator(iterator1);
    }
  } 

  /** Iterator that reads from an underlying {@link Enumerator}.
   *
   * @param <T> element type */
  private static class EnumeratorIterator<T>
      implements Iterator<T>, AutoCloseable {
    private final Enumerator<T> enumerator;
    boolean hasNext;

    EnumeratorIterator(Enumerator<T> enumerator) {
      this.enumerator = enumerator;
      hasNext = enumerator.moveNext();
    }

    public boolean hasNext() {
      return hasNext;
    }

    public T next() {
      T t = enumerator.current();
      hasNext = enumerator.moveNext();
      return t;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public void close() {
      enumerator.close();
    }
  }

  /** Enumerates over the cartesian product of the given lists, returning
   * a list for each row.
   *
   * @param <E> element type */
  private static class CartesianProductListEnumerator<E>
      extends CartesianProductEnumerator<E, List<E>> {
    CartesianProductListEnumerator(List<Enumerator<E>> enumerators) {
      super(enumerators);
    }

    public List<E> current() {
      return Arrays.asList(elements.clone());
    }
  }
}

// End Linq4j.java
