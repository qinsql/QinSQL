/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc;

public class H2TpccLoad {
    public static void main(String[] args) {
        System.setProperty("tpcc.config", "h2/tpcc.properties");
        TpccLoad.main(args, "h2/create_tables.sql", "h2/add_fkey_idx.sql");
    }
}
