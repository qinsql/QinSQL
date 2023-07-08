/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.load.TpccLoad;

public class PgLoad {
    public static void main(String[] args) {
        System.setProperty("db.config", "postgresql/db.properties");
        TpccLoad.main(args, "postgresql/create_tables.sql", "postgresql/add_fkey_idx.sql");
    }
}
