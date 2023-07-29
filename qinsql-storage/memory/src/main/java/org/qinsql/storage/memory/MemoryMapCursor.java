/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.storage.memory;

import java.util.Iterator;
import java.util.Map.Entry;

import org.lealone.storage.StorageMapCursor;

public class MemoryMapCursor<K, V> implements StorageMapCursor<K, V> {

    private final Iterator<Entry<K, V>> iterator;
    private Entry<K, V> e;

    public MemoryMapCursor(Iterator<Entry<K, V>> iterator) {
        this.iterator = iterator;
    }

    @Override
    public K getKey() {
        return e.getKey();
    }

    @Override
    public V getValue() {
        return e.getValue();
    }

    @Override
    public boolean next() {
        if (iterator.hasNext()) {
            e = iterator.next();
            return true;
        }
        return false;
    }
}
