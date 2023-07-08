/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc.db;

import org.qinsql.bench.tpcc.bench.TpccBench;

public class H2Bench {
    public static void main(String[] args) {
        System.setProperty("db.config", "h2/db.properties");
        TpccBench.main(args);
    }
}
