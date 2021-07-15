/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.mysql.jdbc;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.lealone.bats.sphere.jdbc.LealoneDatabaseType;

public final class LealoneMySQLDatabaseType extends LealoneDatabaseType {

    @Override
    public String getName() {
        return "LealoneMySQL";
    }

    @Override
    public DatabaseType getTrunkDatabaseType() {
        return DatabaseTypeRegistry.getActualDatabaseType("MySQL");
    }
}
