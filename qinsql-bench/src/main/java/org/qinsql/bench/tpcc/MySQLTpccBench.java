/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.tpcc;

public class MySQLTpccBench {
    public static void main(String[] args) {
        System.setProperty("tpcc.config", "mysql/tpcc.properties");
        TpccBench.main(args);
    }
}
