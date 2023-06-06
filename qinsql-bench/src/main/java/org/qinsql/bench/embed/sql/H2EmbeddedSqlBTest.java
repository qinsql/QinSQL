/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.sql;

import java.sql.Connection;

import org.qinsql.bench.start.H2BenchTestServer;

public class H2EmbeddedSqlBTest extends SqlBenchTest {

    public static void main(String[] args) throws Exception {
        H2BenchTestServer.setH2Properties();
        new H2EmbeddedSqlBTest().run();
    }

    @Override
    protected Connection getConnection() throws Exception {
        return getH2Connection(true);
    }
}
