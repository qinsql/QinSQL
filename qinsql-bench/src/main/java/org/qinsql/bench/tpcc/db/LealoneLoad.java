/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.load.TpccLoad;

public class LealoneLoad {
    public static void main(String[] args) {
        System.setProperty("db.config", "lealone/db.properties");
        TpccLoad.main(args, "lealone/create_tables.sql", "lealone/add_fkey_idx.sql");
    }
}
