/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.bench.TpccBench;

public class MySQLBench {
    public static void main(String[] args) {
        System.setProperty("db.config", "mysql/db.properties");
        TpccBench.main(args);
    }
}
