/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.storage.file;

import java.util.Map;

import org.lealone.storage.StorageBase;
import org.lealone.storage.StorageMap;
import org.lealone.storage.type.StorageDataType;

public class LocalFileStorage extends StorageBase {

    public LocalFileStorage() {
        super(null);
    }

    @Override
    public <K, V> StorageMap<K, V> openMap(String name, StorageDataType keyType,
            StorageDataType valueType, Map<String, String> parameters) {
        return null;
    }

    @Override
    public String getStoragePath() {
        return null;
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public String getStorageName() {
        return LocalFileStorageEngine.NAME;
    }
}
