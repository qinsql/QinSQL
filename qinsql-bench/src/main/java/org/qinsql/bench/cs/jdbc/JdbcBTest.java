/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;

import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.ClientServerBTest;

//测试同步和异步jdbc api的性能
public abstract class JdbcBTest extends ClientServerBTest {

    protected final Random random = new Random();

    protected abstract void write(JdbcStatement stmt, int start, int end) throws Exception;

    protected abstract void read(JdbcStatement stmt, int start, int end, boolean random) throws Exception;

    @Override
    public void run(int loop) throws Exception {
        init();
        super.run(loop);
    }

    @Override
    protected void resetFields() {
        super.resetFields();
        pendingOperations.set(rowCount * 3);
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getLealoneConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS JdbcBTest");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JdbcBTest (f1 int primary key, f2 long)");
        stmt.close();
        conn.close();
    }

    @Override
    protected BenchTestTask createBenchTestTask(int start, int end) throws Exception {
        return new JdbcBenchTestTask(start, end);
    }

    private class JdbcBenchTestTask extends BenchTestTask {
        final Connection conn;
        final JdbcStatement stmt;

        JdbcBenchTestTask(int start, int end) throws Exception {
            super(start, end);
            this.conn = getLealoneConnection();
            this.stmt = (JdbcStatement) conn.createStatement();
        }

        @Override
        public void startBenchTest() throws Exception {
            write(stmt, start, end);
            read(stmt, start, end, false);
            read(stmt, start, end, true);
        }

        @Override
        public void stopBenchTest() throws Exception {
            stmt.close();
            conn.close();
        }
    }
}
