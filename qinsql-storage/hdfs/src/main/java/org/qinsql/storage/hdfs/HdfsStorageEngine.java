/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.storage.hdfs;

import org.lealone.storage.StorageBuilder;
import org.lealone.storage.StorageEngineBase;

public class HdfsStorageEngine extends StorageEngineBase {

    public static final String NAME = "hdfs";

    public HdfsStorageEngine() {
        super(NAME);
    }

    @Override
    public StorageBuilder getStorageBuilder() {
        return new HdfsStorageBuilder();
    }

    public static class HdfsStorageBuilder extends StorageBuilder {
        @Override
        public HdfsStorage openStorage() {
            return new HdfsStorage();
        }
    }
}
