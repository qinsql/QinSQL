package org.lealone.bats.engine.storage;

import org.apache.drill.common.logical.StoragePluginConfigBase;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(LealoneStoragePluginConfig.NAME)
public class LealoneStoragePluginConfig extends StoragePluginConfigBase {

    public static final String NAME = "lealone";

    @JsonCreator
    public LealoneStoragePluginConfig() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31;
    }
}