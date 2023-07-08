/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.load.TpccLoad;

public class H2Load {
    public static void main(String[] args) {
        System.setProperty("db.config", "h2/db.properties");
        TpccLoad.main(args, "h2/create_tables.sql", "h2/add_fkey_idx.sql");
    }
}
