/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc.connection;

import java.sql.Connection;

import org.qinsql.bench.cs.ClientServerBTest;

//测试创建jdbc connection的性能
public abstract class ConnectionBTest extends ClientServerBTest {

    private final int connectionCount = 100;

    @Override
    protected abstract Connection getConnection() throws Exception;

    @Override
    public void run(int loop) throws Exception {
        Connection[] connections = new Connection[connectionCount];

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < connectionCount; i++) {
            connections[i] = getConnection();
        }
        long t2 = System.currentTimeMillis();

        printResult(loop, ", create connection count: " + connectionCount + ", total time: " + (t2 - t1)
                + " ms" + ", avg time: " + (t2 - t1) / (connectionCount * 1.0) + " ms");

        for (int i = 0; i < connectionCount; i++) {
            connections[i].close();
        }
    }
}
