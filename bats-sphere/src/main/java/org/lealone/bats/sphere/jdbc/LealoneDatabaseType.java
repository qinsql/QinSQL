/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import java.util.Collection;
import java.util.Collections;

import org.apache.shardingsphere.infra.database.type.BranchDatabaseType;

/**
 * Database type of Lealone.
 */
public abstract class LealoneDatabaseType implements BranchDatabaseType {

    @Override
    public Collection<String> getJdbcUrlPrefixes() {
        return Collections.singleton(String.format("jdbc:%s:", "lealone"));
    }

    @Override
    public LealoneDataSourceMetaData getDataSourceMetaData(final String url, final String username) {
        return new LealoneDataSourceMetaData(url);
    }
}
