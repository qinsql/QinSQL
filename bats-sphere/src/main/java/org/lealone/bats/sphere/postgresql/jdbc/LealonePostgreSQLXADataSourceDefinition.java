/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.postgresql.jdbc;

import org.lealone.bats.sphere.jdbc.LealoneXADataSourceDefinition;

/**
 * XA data source definition for Lealone.
 */
public class LealonePostgreSQLXADataSourceDefinition extends LealoneXADataSourceDefinition {

    @Override
    public String getDatabaseType() {
        return "LealonePostgreSQL";
    }
}
