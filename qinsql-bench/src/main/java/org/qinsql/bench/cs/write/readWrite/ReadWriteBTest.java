/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.readWrite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.write.ClientServerWriteBTest;

public abstract class ReadWriteBTest extends ClientServerWriteBTest {

    protected ReadWriteBTest() {
        rowCount = 10000;
        benchTestLoop = 5;
        outerLoop = 20;
        threadCount = 48;
        sqlCountPerInnerLoop = 20;
        innerLoop = 10;
        autoCommit = false;
        // printInnerLoopResult = true;
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists ReadWriteBTest_W");
        String sql = "create table if not exists ReadWriteBTest_W(pk int primary key, f1 int)";
        statement.executeUpdate(sql);

        statement.executeUpdate("drop table if exists ReadWriteBTest_R");
        sql = "create table if not exists ReadWriteBTest_R(pk int primary key, f1 int)";
        statement.executeUpdate(sql);

        sql = "insert into ReadWriteBTest_R values(?,1)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int row = 1; row <= rowCount; row++) {
            ps.setInt(1, row);
            ps.addBatch();
            if (row % 100 == 0 || row == rowCount) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        close(statement, ps, conn);
    }

    @Override
    protected UpdateThreadBase createBTestThread(int id, Connection conn) {
        return new UpdateThread(id, conn);
    }

    private class UpdateThread extends UpdateThreadBase {

        UpdateThread(int id, Connection conn) {
            super(id, conn);
        }

        @Override
        protected void executeUpdateAsync(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            JdbcStatement stmt = (JdbcStatement) statement;
            AtomicInteger counter = new AtomicInteger(sqlCountPerInnerLoop * innerLoop * 2);
            CountDownLatch latch = new CountDownLatch(1);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    String sql = "select * from ReadWriteBTest_R where pk=" + random.nextInt(rowCount);
                    stmt.executeQueryAsync(sql).onComplete(ar -> {
                        if (counter.decrementAndGet() == 0) {
                            endTime = System.nanoTime();
                            latch.countDown();
                        }
                    });
                    sql = "insert into ReadWriteBTest_W values(" + id.incrementAndGet() + ",1)";
                    stmt.executeUpdateAsync(sql).onComplete(ar -> {
                        if (counter.decrementAndGet() == 0) {
                            endTime = System.nanoTime();
                            latch.countDown();
                        }
                    });
                }
            }
            latch.await();
            if (!autoCommit)
                conn.commit();
            printInnerLoopResult(t1);
        }

        @Override
        protected void executeUpdate(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    String sql = "select * from ReadWriteBTest_R where pk=" + random.nextInt(rowCount);
                    statement.executeQuery(sql);
                    sql = "insert into ReadWriteBTest_W values(" + id.incrementAndGet() + ",1)";
                    statement.executeUpdate(sql);
                }
            }
            if (!autoCommit)
                conn.commit();
            printInnerLoopResult(t1);
        }

        @Override
        protected String nextSql() {
            return null;
        }
    }
}
