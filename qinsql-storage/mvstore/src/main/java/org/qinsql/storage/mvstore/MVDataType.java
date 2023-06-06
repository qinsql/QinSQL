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

import java.nio.ByteBuffer;

import org.h2.mvstore.WriteBuffer;
import org.lealone.db.DataBuffer;
import org.lealone.storage.type.StorageDataType;

public class MVDataType extends org.h2.mvstore.type.BasicDataType<Object> {

    private final StorageDataType type;

    public MVDataType(StorageDataType type) {
        this.type = type;
    }

    @Override
    public int compare(Object a, Object b) {
        return type.compare(a, b);
    }

    @Override
    public int getMemory(Object obj) {
        return type.getMemory(obj);
    }

    @Override
    public void write(WriteBuffer buff, Object obj) {
        DataBuffer db = DataBuffer.create();
        type.write(db, obj);
        buff.put(db.getAndFlipBuffer());
        db.close();
    }

    @Override
    public void write(WriteBuffer buff, Object obj, int len) {
        DataBuffer db = DataBuffer.create();
        for (int i = 0; i < len; i++) {
            db.reset();
            type.write(db, cast(obj)[i]);
            buff.put(db.getAndFlipBuffer());
        }
        db.close();
    }

    @Override
    public Object read(ByteBuffer buff) {
        return type.read(buff);
    }

    @Override
    public void read(ByteBuffer buff, Object obj, int len) {
        for (int i = 0; i < len; i++) {
            cast(obj)[i] = read(buff);
        }
    }

    @Override
    public Object[] createStorage(int size) {
        return new Object[size];
    }
}
