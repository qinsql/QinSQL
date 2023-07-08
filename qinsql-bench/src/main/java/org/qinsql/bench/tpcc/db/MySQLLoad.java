/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.load.TpccLoad;

public class MySQLLoad {
    public static void main(String[] args) {
        System.setProperty("db.config", "mysql/db.properties");
        TpccLoad.main(args, "mysql/create_tables.sql", "mysql/add_fkey_idx.sql");
    }
}
