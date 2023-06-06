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

import org.h2.mvstore.MVStore;
import org.lealone.storage.StorageBuilder;

public class MVStorageBuilder extends StorageBuilder {

    private final MVStore.Builder builder = new MVStore.Builder();

    @Override
    public MVStorage openStorage() {
        // 禁用自动提交，交由上层的事务引擎负责
        builder.autoCommitDisabled();
        return new MVStorage(builder.open(), config);
    }

    @Override
    public StorageBuilder storagePath(String storagePath) {
        builder.fileName(storagePath);
        return super.storagePath(storagePath);
    }

    @Override
    public StorageBuilder encryptionKey(char[] password) {
        builder.encryptionKey(password);
        return super.encryptionKey(password);
    }

    @Override
    public StorageBuilder readOnly() {
        builder.readOnly();
        return super.readOnly();
    }

    @Override
    public StorageBuilder inMemory() {
        return super.inMemory();
    }

    @Override
    public StorageBuilder cacheSize(int mb) {
        builder.cacheSize(mb);
        return super.cacheSize(mb);
    }

    @Override
    public StorageBuilder compress() {
        builder.compress();
        return super.compress();
    }

    @Override
    public StorageBuilder compressHigh() {
        builder.compressHigh();
        return super.compressHigh();
    }

    @Override
    public StorageBuilder pageSplitSize(int pageSplitSize) {
        builder.pageSplitSize(pageSplitSize);
        return super.pageSplitSize(pageSplitSize);
    }
}
