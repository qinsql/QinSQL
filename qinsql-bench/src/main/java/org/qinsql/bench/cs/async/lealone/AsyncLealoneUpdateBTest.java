/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.async.lealone;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.lealone.client.jdbc.JdbcStatement;

public class AsyncLealoneUpdateBTest extends AsyncLealoneBTest {

    public static void main(String[] args) throws Throwable {
        Connection conn = getLealoneConnection();
        Statement statement = conn.createStatement();
        run("AsyncLealoneUpdate", statement);
        statement.close();
        conn.close();
    }

    protected static void initData(Statement statement) throws Exception {
        statement.executeUpdate("drop table if exists test");
        statement.executeUpdate("create table if not exists test(name varchar, f1 int,f2 int)");

        statement.getConnection().setAutoCommit(false);
        statement.executeUpdate("insert into test values('abc1',1,2)");
        statement.executeUpdate("insert into test values('abc2',2,2)");
        statement.executeUpdate("insert into test values('abc3',3,2)");
        statement.executeUpdate("insert into test values('abc1',1,2)");
        statement.executeUpdate("insert into test values('abc2',2,2)");
        statement.executeUpdate("insert into test values('abc3',3,2)");
        statement.getConnection().commit();
        statement.getConnection().setAutoCommit(true);
    }

    public static void run(String name, Statement statement) throws Throwable {
        initData(statement);
        JdbcStatement stmt = (JdbcStatement) statement;
        String sql = "update test set f1=2 where name='abc1'";
        int count = 1000;
        CountDownLatch latch1 = new CountDownLatch(count * 5);
        for (int i = 0; i < count * 5; i++) {
            stmt.executeUpdateAsync(sql).onComplete(ar -> {
                latch1.countDown();
            });
        }
        latch1.await();

        int loop = 30 * 10;
        for (int j = 0; j < loop; j++) {
            CountDownLatch latch = new CountDownLatch(count);
            long t1 = System.nanoTime();
            for (int i = 0; i < count; i++) {
                stmt.executeUpdateAsync(sql).onComplete(ar -> {
                    latch.countDown();
                });
            }
            latch.await();
            long t2 = System.nanoTime();
            System.out.println(name + ": " + TimeUnit.NANOSECONDS.toMicros(t2 - t1) / count);
        }
        System.out.println();
        System.out.println("time: 微秒");
        System.out.println("loop: " + loop + " * " + count);
        System.out.println("sql : " + sql);
    }
}
