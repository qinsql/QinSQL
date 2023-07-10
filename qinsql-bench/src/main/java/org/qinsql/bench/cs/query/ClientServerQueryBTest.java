/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.query;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lealone.client.jdbc.JdbcPreparedStatement;
import org.lealone.client.jdbc.JdbcStatement;
import org.qinsql.bench.cs.ClientServerBTest;

public abstract class ClientServerQueryBTest extends ClientServerBTest {

    public ClientServerQueryBTest() {
        reinit = false;
    }

    protected abstract class QueryThreadBase extends ClientServerBTestThread {

        public QueryThreadBase(int id, Connection conn) {
            super(id, conn);
        }

        @Override
        protected void execute() throws Exception {
            startTime = System.nanoTime();
            if (async) {
                if (prepare)
                    executePreparedQueryAsync();
                else
                    executeQueryAsync(stmt);
            } else {
                if (prepare)
                    executePreparedQuery();
                else
                    executeQuery(stmt);
                endTime = System.nanoTime();
            }
        }

        protected void executeQueryAsync(Statement statement) throws Exception {
            JdbcStatement stmt = (JdbcStatement) statement;
            AtomicInteger counter = new AtomicInteger(sqlCountPerInnerLoop * innerLoop);
            CountDownLatch latch = new CountDownLatch(1);
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    stmt.executeQueryAsync(nextSql()).onComplete(ar -> {
                        if (counter.decrementAndGet() == 0) {
                            endTime = System.nanoTime();
                            latch.countDown();
                        }
                    });
                }
            }
            latch.await();
            printInnerLoopResult(t1);
        }

        protected void executePreparedQueryAsync() throws Exception {
            JdbcPreparedStatement ps2 = (JdbcPreparedStatement) ps;
            AtomicInteger counter = new AtomicInteger(sqlCountPerInnerLoop * innerLoop);
            CountDownLatch latch = new CountDownLatch(1);
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps2.executeQueryAsync().onComplete(ar -> {
                        if (counter.decrementAndGet() == 0) {
                            endTime = System.nanoTime();
                            latch.countDown();
                        }
                    });
                }
            }
            latch.await();
            printInnerLoopResult(t1);
        }

        protected void executeQuery(Statement statement) throws Exception {
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++)
                    statement.executeQuery(nextSql());
            }
            printInnerLoopResult(t1);
        }

        protected void executePreparedQuery() throws Exception {
            long t1 = System.nanoTime();
            for (int j = 0; j < innerLoop; j++) {
                for (int i = 0; i < sqlCountPerInnerLoop; i++) {
                    prepare();
                    ps.executeQuery();
                }
            }
            printInnerLoopResult(t1);
        }

        private void printInnerLoopResult(long t1) {
            if (printInnerLoopResult) {
                long t2 = System.nanoTime();
                System.out.println(getBTestName() + ": "
                        + TimeUnit.NANOSECONDS.toMillis(t2 - t1) / sqlCountPerInnerLoop + " ms");
            }
        }
    }
}
