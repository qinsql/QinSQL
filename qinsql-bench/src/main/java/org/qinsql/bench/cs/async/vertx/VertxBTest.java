/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.async.vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.qinsql.bench.cs.ClientServerBTest;

import io.vertx.sqlclient.SqlClient;

public abstract class VertxBTest extends ClientServerBTest {

    public static void run(SqlClient client, String name, String sql) throws Throwable {
        int count = 1000;
        CountDownLatch latch1 = new CountDownLatch(count * 5);
        for (int i = 0; i < count * 5; i++) {
            client.query(sql).execute(ar -> {
                latch1.countDown();
                if (ar.failed()) {
                    ar.cause().printStackTrace();
                }
                // if (ar.succeeded()) {
                // RowSet<Row> result = ar.result();
                // System.out.println("Got " + result.size() + " rows ");
                // } else {
                // ar.cause().printStackTrace();
                // }
            });
        }
        latch1.await();

        int loop = 20;
        for (int j = 0; j < loop; j++) {
            CountDownLatch latch2 = new CountDownLatch(count);
            long t1 = System.nanoTime();
            for (int i = 0; i < count; i++) {
                client.query(sql).execute(ar -> {
                    latch2.countDown();
                });
            }
            latch2.await();
            long t2 = System.nanoTime();
            System.out.println(name + ": " + TimeUnit.NANOSECONDS.toMicros(t2 - t1) / count);
        }
        System.out.println();
        System.out.println("time: 微秒");
        System.out.println("loop: " + loop + " * " + count);
        System.out.println("sql : " + sql);
        client.close();
    }
}
