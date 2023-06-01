/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.olap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

import org.lealone.client.jdbc.JdbcPreparedStatement;
import org.lealone.client.jdbc.JdbcStatement;
import org.lealone.db.Constants;

public class VectorPerfTest {

    public static void main(String[] args) throws Exception {
        // insert();
        query();
    }

    public static void query() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:lealone:tcp://localhost:"
                + Constants.DEFAULT_TCP_PORT + "/lealone?NETWORK_TIMEOUT=10000000", "root", "");
        JdbcStatement stmt = (JdbcStatement) conn.createStatement();
        stmt.executeUpdate("set QUERY_CACHE_SIZE 0");
        stmt.executeUpdate("SET olap_threshold 1"); // 启动向量化引擎
        stmt.executeUpdate("set olap_batch_size 128"); // 启动向量化引擎
        for (int i = 0; i < 20; i++) {
            query(stmt);
        }
        stmt.close();
        conn.close();
    }

    public static void query(JdbcStatement stmt) throws Exception {
        String sql = "SELECT count(*), sum(f1+f2), sum(f1+f2), sum(f1+f2) FROM test3";
        int count = 5;
        CountDownLatch latch = new CountDownLatch(count);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            stmt.executeQueryAsync(sql).onComplete(ar -> {
                if (ar.isFailed())
                    ar.getCause().printStackTrace();
                else
                    try {
                        ar.getResult().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                latch.countDown();
            });
        }
        latch.await();
        long t2 = System.currentTimeMillis();
        System.out.println("time: " + (t2 - t1) + "ms");
    }

    public static void insert() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:lealone:tcp://localhost:"
                + Constants.DEFAULT_TCP_PORT + "/lealone?NETWORK_TIMEOUT=10000000", "root", "");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS test4");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test4(name varchar(20), f1 int, f2 int)");

        JdbcPreparedStatement ps = (JdbcPreparedStatement) conn
                .prepareStatement("INSERT INTO test4 VALUES(?,?,?)");
        int count = 3000;// 100000;// 1043469;// 200 * 10000;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            ps.setString(1, "n" + i);
            ps.setInt(2, i);
            ps.setInt(3, i * 10);
            ps.executeUpdateAsync().onComplete(ar -> {
                if (ar.isFailed())
                    ar.getCause().printStackTrace();
                latch.countDown();
                if (latch.getCount() % 10000 == 0) {
                    System.out.println(latch.getCount());
                }
            });
        }
        latch.await();
        ps.close();
        stmt.close();
        conn.close();
    }
}
