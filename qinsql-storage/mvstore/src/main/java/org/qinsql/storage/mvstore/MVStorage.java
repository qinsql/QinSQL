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

import java.util.Map;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.lealone.storage.StorageBase;
import org.lealone.storage.StorageMap;
import org.lealone.storage.type.StorageDataType;

public class MVStorage extends StorageBase {

    private final MVStore mvStore;

    public MVStorage(MVStore mvStore, Map<String, Object> config) {
        super(config);
        this.mvStore = mvStore;
    }

    @Override
    public <K, V> StorageMap<K, V> openMap(String name, StorageDataType keyType, StorageDataType valueType,
            Map<String, String> parameters) {
        MVMap.Builder<K, V> builder = new MVMap.Builder<>();
        builder.keyType(new MVDataType(keyType));
        builder.valueType(new MVDataType(valueType));
        MVMap<K, V> mvMap = mvStore.openMap(name, builder);
        MVStorageMap<K, V> map = new MVStorageMap<>(name, keyType, valueType, this, mvMap);
        maps.put(name, map);
        return map;
    }

    @Override
    public void save() {
        mvStore.commit();
    }

    @Override
    public void close() {
        super.close();
        mvStore.close();
    }

    @Override
    public void closeImmediately() {
        super.closeImmediately();
        mvStore.closeImmediately();
    }
}
