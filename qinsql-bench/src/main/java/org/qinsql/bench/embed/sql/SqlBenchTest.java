/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.qinsql.bench.BenchTest;

public abstract class SqlBenchTest extends BenchTest {

    protected abstract Connection getConnection() throws Exception;

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("drop table IF EXISTS SqlBenchTest");
        stmt.executeUpdate(
                "create table IF NOT EXISTS SqlBenchTest(f1 int primary key , f2 varchar(20))");
        // stmt.executeUpdate("create index index0 on SqlBenchTest(f2)");
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            stmt.executeUpdate("insert into SqlBenchTest values(" + i + ",'value-" + i + "')");
        }
        long t2 = System.currentTimeMillis();
        printResult("insert row count: " + rowCount + ", total time: " + (t2 - t1) + " ms");
        stmt.close();
        conn.close();
    }

    protected void update(Statement stmt, int start, int end) throws Exception {
        for (int i = start; i < end; i++) {
            int f1 = i;
            if (isRandom())
                f1 = randomKeys[i];
            String sql = "update SqlBenchTest set f2 = 'value2' where f1 =" + f1;
            stmt.executeUpdate(sql);
            notifyOperationComplete();
        }
    }

    protected void prepare(Connection conn, int start, int end) throws Exception {
        for (int i = start; i < end; i++) {
            int f1 = i;
            if (isRandom())
                f1 = randomKeys[i];
            String sql = "update SqlBenchTest set f2 = 'value2' where f1 =" + f1;
            conn.prepareStatement(sql);
            notifyOperationComplete();
        }
    }

    protected void prepare(PreparedStatement ps, int start, int end) throws Exception {
        for (int i = start; i < end; i++) {
            int f1 = i;
            if (isRandom())
                f1 = randomKeys[i];
            ps.setInt(1, f1);
            ps.executeUpdate();
            notifyOperationComplete();
        }
    }

    @Override
    protected SqlBenchTestTask createBenchTestTask(int start, int end) throws Exception {
        return new SqlBenchTestTask(start, end);
    }

    protected class SqlBenchTestTask extends BenchTestTask {
        final Connection conn;
        final Statement stmt;
        final PreparedStatement ps;

        protected SqlBenchTestTask(int start, int end) throws Exception {
            super(start, end);
            this.conn = getConnection();
            this.stmt = conn.createStatement();
            String sql = "update SqlBenchTest set f2 = 'value2' where f1 =?";
            this.ps = conn.prepareStatement(sql);
        }

        @Override
        public void startBenchTest() throws Exception {
            // update(stmt, start, end);
            // prepare(conn, start, end);
            prepare(ps, start, end);
        }

        @Override
        public void stopBenchTest() throws Exception {
            stmt.close();
            ps.close();
            conn.close();
        }
    }
}
