/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc;

import java.sql.ResultSet;

import org.lealone.client.jdbc.JdbcStatement;

public class SyncJdbcBTest extends JdbcBTest {

    public static void main(String[] args) throws Exception {
        new SyncJdbcBTest().run();
    }

    @Override
    protected void write(JdbcStatement stmt, int start, int end) throws Exception {
        for (int i = start; i < end; i++) {
            String sql = "INSERT INTO JdbcBTest(f1, f2) VALUES(" + i + "," + i * 10 + ")";
            stmt.executeUpdate(sql);
            notifyOperationComplete();
        }
    }

    @Override
    protected void read(JdbcStatement stmt, int start, int end, boolean random) throws Exception {
        for (int i = start; i < end; i++) {
            ResultSet rs;
            if (!random)
                rs = stmt.executeQuery("SELECT * FROM JdbcBTest where f1 = " + i);
            else
                rs = stmt.executeQuery("SELECT * FROM JdbcBTest where f1 = " + this.random.nextInt(end));
            while (rs.next()) {
                // System.out.println("f1=" + rs.getInt(1) + " f2=" + rs.getLong(2));
            }
            notifyOperationComplete();
        }
    }
}
