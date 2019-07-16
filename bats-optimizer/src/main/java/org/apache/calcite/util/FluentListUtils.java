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
import java.util.Collection;
import java.util.List;

/**
 * Utility methods for expressions, including a lot of factory methods.
 */
@SuppressWarnings("unchecked")
public abstract class FluentListUtils {
    private FluentListUtils() {
    }

    /**
     * Creates an empty fluent list.
     */
    public static <T> FluentList<T> list() {
        return new FluentArrayList<>();
    }

    /**
     * Creates a fluent list with given elements.
     */
    @SafeVarargs
    public static <T> FluentList<T> list(T... ts) {
        return new FluentArrayList<>(Arrays.asList(ts));
    }

    /**
     * Creates a fluent list with elements from the given collection.
     */
    public static <T> FluentList<T> list(Iterable<T> ts) {
        return new FluentArrayList<>(toList(ts));
    }

    private static <T> List<T> toList(Iterable<? extends T> iterable) {
        if (iterable == null) {
            return null;
        }
        if (iterable instanceof List) {
            return (List<T>) iterable;
        }
        final List<T> list = new ArrayList<>();
        for (T parameter : iterable) {
            list.add(parameter);
        }
        return list;
    }

    private static <T> Collection<T> toCollection(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return (Collection<T>) iterable;
        }
        return toList(iterable);
    }

    /** Fluent list.
     *
     * @param <T> element type */
    public interface FluentList<T> extends List<T> {
        FluentList<T> append(T t);

        FluentList<T> appendIf(boolean condition, T t);

        FluentList<T> appendIfNotNull(T t);

        FluentList<T> appendAll(Iterable<T> ts);

        FluentList<T> appendAll(T... ts);
    }

    /** Fluent array list.
     *
     * @param <T> element type */
    private static class FluentArrayList<T> extends ArrayList<T> implements FluentList<T> {
        FluentArrayList() {
            super();
        }

        FluentArrayList(Collection<? extends T> c) {
            super(c);
        }

        public FluentList<T> append(T t) {
            add(t);
            return this;
        }

        public FluentList<T> appendIf(boolean condition, T t) {
            if (condition) {
                add(t);
            }
            return this;
        }

        public FluentList<T> appendIfNotNull(T t) {
            if (t != null) {
                add(t);
            }
            return this;
        }

        public FluentList<T> appendAll(Iterable<T> ts) {
            addAll(toCollection(ts));
            return this;
        }

        public FluentList<T> appendAll(T... ts) {
            addAll(Arrays.asList(ts));
            return this;
        }
    }
}

// End Expressions.java
