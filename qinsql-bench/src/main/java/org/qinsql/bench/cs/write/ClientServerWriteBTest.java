/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.lealone.client.jdbc.JdbcPreparedStatement;
import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.ClientServerBTest;

public abstract class ClientServerWriteBTest extends ClientServerBTest {

    @Override
    protected void run(int threadCount, Connection[] conns, boolean warmUp) throws Exception {
        UpdateThreadBase[] threads = new UpdateThreadBase[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = createUpdateThread(i, conns[i]);
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

    protected abstract UpdateThreadBase createUpdateThread(int id, Connection conn);

    protected abstract class UpdateThreadBase extends Thread {

        protected Connection conn;
        protected Statement stmt;
        protected PreparedStatement ps;
        protected boolean closeConn = true;
        protected long startTime;
        protected long endTime;

        public UpdateThreadBase(int id, Connection conn) {
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

        protected void prepare() {
        }

        @Override
        public void run() {
            try {
                startTime = System.nanoTime();
                if (async)
                    executeUpdateAsync(stmt);
                else
                    executeUpdate(stmt);
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

        protected void executeUpdateAsync(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            JdbcStatement stmt = (JdbcStatement) statement;
            for (int j = 0; j < innerLoop; j++) {
                CountDownLatch latch = new CountDownLatch(sqlCountPerInnerLoop);
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    stmt.executeUpdateAsync(nextSql()).onComplete(ar -> {
                        latch.countDown();
                    });
                }
                latch.await();
                if (!autoCommit)
                    conn.commit();
            }
            printInnerLoopResult(t1);
        }

        protected void executeUpdate(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    statement.executeUpdate(nextSql());
                }
                if (!autoCommit)
                    conn.commit();
            }
            printInnerLoopResult(t1);
        }

        protected void executePreparedUpdateAsync() throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            JdbcPreparedStatement ps2 = (JdbcPreparedStatement) ps;
            for (int j = 0; j < innerLoop; j++) {
                CountDownLatch latch = new CountDownLatch(sqlCountPerInnerLoop);
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps2.executeUpdateAsync().onComplete(ar -> {
                        latch.countDown();
                    });
                }
                latch.await();
                if (!autoCommit)
                    conn.commit();
            }
            printInnerLoopResult(t1);
        }

        protected void executePreparedUpdate() throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps.executeUpdate();
                }
                if (!autoCommit)
                    conn.commit();
            }
            printInnerLoopResult(t1);
        }

        private void printInnerLoopResult(long t1) {
            if (printInnerLoopResult) {
                long t2 = System.nanoTime();
                System.out.println(getBTestName() + " sql count: " + (innerLoop * sqlCountPerInnerLoop) //
                        + " total time: " + TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");
            }
        }
    }
}
