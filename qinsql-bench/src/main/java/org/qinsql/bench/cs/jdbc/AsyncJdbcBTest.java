/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc;

import java.sql.ResultSet;

import org.lealone.client.jdbc.JdbcStatement;
import org.lealone.db.async.AsyncHandler;
import org.lealone.db.async.AsyncResult;

public class AsyncJdbcBTest extends JdbcBTest {

    public static void main(String[] args) throws Exception {
        new AsyncJdbcBTest().run();
    }

    @Override
    protected void write(JdbcStatement stmt, int start, int end) throws Exception {
        for (int i = start; i < end; i++) {
            String sql = "INSERT INTO JdbcBTest(f1, f2) VALUES(" + i + "," + i * 10 + ")";
            stmt.executeUpdateAsync(sql).onComplete(res -> {
                notifyOperationComplete();
            });
        }
    }

    @Override
    protected void read(JdbcStatement stmt, int start, int end, boolean random) throws Exception {
        AsyncHandler<AsyncResult<ResultSet>> handler = ac -> {
            ResultSet rs = ac.getResult();
            try {
                // TODO 有可能为null吗？
                if (rs != null)
                    while (rs.next()) {
                        // System.out.println("f1=" + rs.getInt(1) + " f2=" + rs.getLong(2));
                    }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                notifyOperationComplete();
            }
        };
        for (int i = start; i < end; i++) {
            if (!random)
                stmt.executeQueryAsync("SELECT * FROM JdbcBTest where f1 = " + i).onComplete(handler);
            else
                stmt.executeQueryAsync("SELECT * FROM JdbcBTest where f1 = " + this.random.nextInt(end))
                        .onComplete(handler);
        }
    }
}
