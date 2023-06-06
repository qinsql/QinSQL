/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.storage.file;

import org.lealone.storage.StorageBuilder;
import org.lealone.storage.StorageEngineBase;

public class LocalFileStorageEngine extends StorageEngineBase {

    public static final String NAME = "file";

    public LocalFileStorageEngine() {
        super(NAME);
    }

    @Override
    public StorageBuilder getStorageBuilder() {
        return new FileStorageBuilder();
    }

    public static class FileStorageBuilder extends StorageBuilder {
        @Override
        public LocalFileStorage openStorage() {
            return new LocalFileStorage();
        }
    }
}
