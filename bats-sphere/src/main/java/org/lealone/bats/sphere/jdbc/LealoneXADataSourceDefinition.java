/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.jdbc;

import java.util.Collection;
import java.util.Collections;

import org.apache.shardingsphere.transaction.xa.jta.datasource.properties.XADataSourceDefinition;
import org.lealone.client.jdbc.JdbcDataSource;

/**
 * XA data source definition for Lealone.
 */
public abstract class LealoneXADataSourceDefinition implements XADataSourceDefinition {
    @Override
    public Collection<String> getXADriverClassName() {
        return Collections.singletonList(JdbcDataSource.class.getName());
    }
}
