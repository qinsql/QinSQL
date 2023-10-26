/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qinsql.storage.mvstore;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.lealone.db.value.ValueLong;
import org.lealone.storage.CursorParameters;
import org.lealone.storage.Storage;
import org.lealone.storage.StorageMapBase;
import org.lealone.storage.StorageMapCursor;
import org.lealone.storage.type.StorageDataType;

public class MVStorageMap<K, V> extends StorageMapBase<K, V> {

    private final MVMap<K, V> mvMap;

    public MVStorageMap(String name, StorageDataType keyType, StorageDataType valueType, Storage storage,
            MVMap<K, V> mvMap) {
        super(name, keyType, valueType, storage);
        this.mvMap = mvMap;
        setMaxKey(lastKey());
    }

    @Override
    public int hashCode() {
        return mvMap.hashCode();
    }

    @Override
    public V get(K key) {
        return mvMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return mvMap.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return mvMap.putIfAbsent(key, value);
    }

    @Override
    public String getName() {
        return mvMap.getName();
    }

    @Override
    public V remove(K key) {
        return mvMap.remove(key);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return mvMap.replace(key, oldValue, newValue);
    }

    @Override
    public K firstKey() {
        return mvMap.firstKey();
    }

    @Override
    public K lastKey() {
        return mvMap.lastKey();
    }

    @Override
    public K lowerKey(K key) {
        return mvMap.lowerKey(key);
    }

    @Override
    public K floorKey(K key) {
        return mvMap.floorKey(key);
    }

    @Override
    public boolean equals(Object obj) {
        return mvMap.equals(obj);
    }

    @Override
    public K higherKey(K key) {
        return mvMap.higherKey(key);
    }

    @Override
    public K ceilingKey(K key) {
        return mvMap.ceilingKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean areValuesEqual(Object a, Object b) {
        return a == b || a != null && b != null && mvMap.getValueType().compare((V) a, (V) b) == 0;
        // 在1.4.199版本中已经没有这个方法
        // return mvMap.areValuesEqual(a, b);
    }

    @Override
    public long size() {
        return mvMap.sizeAsLong();
    }

    @Override
    public boolean containsKey(K key) {
        return mvMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return mvMap.isEmpty();
    }

    @Override
    public boolean isInMemory() {
        return mvMap.isVolatile();
    }

    @Override
    public StorageMapCursor<K, V> cursor(CursorParameters<K> parameters) {
        return new MVStorageMapCursor<>(mvMap.cursor(parameters.from));
    }

    @Override
    public void clear() {
        mvMap.clear();
    }

    @Override
    public void remove() {
        mvMap.clear();
    }

    @Override
    public boolean isClosed() {
        return !storage.hasMap(name);
    }

    @Override
    public void close() {
        storage.closeMap(name);
        // MVMap的close不是public的，并且没实际用处
    }

    @Override
    public void save() {
        MVStore store = mvMap.getStore();
        if (!store.isClosed())
            store.commit();
    }

    @Override
    public K append(V value) {
        @SuppressWarnings("unchecked")
        K k = (K) ValueLong.get(mvMap.size() + 1);
        mvMap.append(k, value);
        return k;
    }
}
