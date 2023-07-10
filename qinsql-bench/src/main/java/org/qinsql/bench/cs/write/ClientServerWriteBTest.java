/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lealone.client.jdbc.JdbcPreparedStatement;
import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.ClientServerBTest;

public abstract class ClientServerWriteBTest extends ClientServerBTest {

    protected abstract class UpdateThreadBase extends ClientServerBTestThread {

        public UpdateThreadBase(int id, Connection conn) {
            super(id, conn);
        }

        @Override
        protected void execute() throws Exception {
            startTime = System.nanoTime();
            if (batch) {
                if (prepare)
                    executePreparedBatchUpdate();
                else
                    executeBatchUpdate();
                endTime = System.nanoTime();
            } else {
                if (async) {
                    if (prepare)
                        executePreparedUpdateAsync();
                    else
                        executeUpdateAsync(stmt);
                } else {
                    if (prepare)
                        executePreparedUpdate();
                    else
                        executeUpdate(stmt);
                    endTime = System.nanoTime();
                }
            }
        }

        protected void executeUpdateAsync(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            JdbcStatement stmt = (JdbcStatement) statement;
            AtomicInteger counter = new AtomicInteger(sqlCountPerInnerLoop * innerLoop);
            CountDownLatch latch = new CountDownLatch(1);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    stmt.executeUpdateAsync(nextSql()).onComplete(ar -> {
                        if (counter.decrementAndGet() == 0) {
                            endTime = System.nanoTime();
                            latch.countDown();
                        }
                    });
                }
            }
            if (!autoCommit)
                conn.commit();
            latch.await();
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
            }
            if (!autoCommit)
                conn.commit();
            printInnerLoopResult(t1);
        }

        protected void executePreparedUpdateAsync() throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            JdbcPreparedStatement ps2 = (JdbcPreparedStatement) ps;
            AtomicInteger counter = new AtomicInteger(sqlCountPerInnerLoop * innerLoop);
            CountDownLatch latch = new CountDownLatch(1);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps2.executeUpdateAsync().onComplete(ar -> {
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

        protected void executePreparedUpdate() throws Exception {
            long t1 = System.nanoTime();
            if (!autoCommit)
                conn.setAutoCommit(false);
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps.executeUpdate();
                }
            }
            if (!autoCommit)
                conn.commit();
            printInnerLoopResult(t1);
        }

        protected void executePreparedBatchUpdate() throws Exception {
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            printInnerLoopResult(t1);
        }

        protected void executeBatchUpdate() throws Exception {
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    stmt.addBatch(nextSql());
                }
                stmt.executeBatch();
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
