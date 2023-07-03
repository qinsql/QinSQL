/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc;

public class PostgreSQLTpccLoad {
    public static void main(String[] args) {
        System.setProperty("tpcc.config", "postgresql/tpcc.properties");
        TpccLoad.main(args, "postgresql/create_tables.sql", "postgresql/add_fkey_idx.sql");
    }
}
