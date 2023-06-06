/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.ClientServerBTest;

public abstract class ClientServerQueryBTest extends ClientServerBTest {

    @Override
    protected void run(int threadCount, Connection[] conns, boolean warmUp) throws Exception {
        QueryThreadBase[] threads = new QueryThreadBase[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = createQueryThread(i, conns[i]);
        }
        long totalTime = 0;
        for (int i = 0; i < threadCount; i++) {
            threads[i].setCloseConn(false);
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            totalTime += threads[i].getTotalTime();
        }
        System.out.println(
                getBTestName() + " sql count: " + (threadCount * innerLoop * sqlCountPerInnerLoop)
                        + ", total time: " + TimeUnit.NANOSECONDS.toMillis(totalTime / threadCount)
                        + " ms" + (warmUp ? " (***WarmUp***)" : ""));
    }

    protected abstract QueryThreadBase createQueryThread(int id, Connection conn);

    protected abstract class QueryThreadBase extends Thread {

        protected Connection conn;
        protected Statement stmt;
        protected boolean closeConn = true;
        protected long startTime;
        protected long endTime;

        public QueryThreadBase(int id, Connection conn) {
            super(getBTestName() + "Thread-" + id);
            this.conn = conn;
            try {
                this.stmt = conn.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void setCloseConn(boolean closeConn) {
            this.closeConn = closeConn;
        }

        protected abstract String nextSql();

        @Override
        public void run() {
            try {
                startTime = System.nanoTime();
                if (async)
                    executeQueryAsync(stmt);
                else
                    executeQuery(stmt);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                endTime = System.nanoTime();
                close(stmt);
                if (closeConn)
                    close(conn);
            }
        }

        public long getTotalTime() {
            return endTime - startTime;
        }

        protected void executeQueryAsync(Statement statement) throws Exception {
            JdbcStatement stmt = (JdbcStatement) statement;
            for (int j = 0; j < innerLoop; j++) {
                CountDownLatch latch = new CountDownLatch(sqlCountPerInnerLoop);
                long t1 = System.nanoTime();

                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    stmt.executeQueryAsync(nextSql()).onComplete(ar -> {
                        latch.countDown();
                    });
                }

                latch.await();
                long t2 = System.nanoTime();
                if (printInnerLoopResult)
                    System.out.println(getBTestName() + ": "
                            + TimeUnit.NANOSECONDS.toMillis(t2 - t1) / sqlCountPerInnerLoop + " ms");
            }
        }

        protected void executeQuery(Statement statement) throws Exception {
            for (int j = 0; j < innerLoop; j++) {
                long t1 = System.nanoTime();

                for (int i = 0; i < sqlCountPerInnerLoop; i++)
                    statement.executeQuery(nextSql());

                long t2 = System.nanoTime();
                if (printInnerLoopResult)
                    System.out.println(getBTestName() + ": "
                            + TimeUnit.NANOSECONDS.toMillis(t2 - t1) / sqlCountPerInnerLoop + " ms");
            }
        }
    }
}
