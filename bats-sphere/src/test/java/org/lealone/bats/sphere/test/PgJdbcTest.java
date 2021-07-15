/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.lealone.bats.sphere.postgresql.server.PgServer;

public class PgJdbcTest {

    public static void main(String[] args) throws Exception {
        Connection conn = getConnection();
        MySQLJdbcTest.crud(conn);
    }

    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:" + PgServer.DEFAULT_PORT + "/test";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "zhh");
        return DriverManager.getConnection(url, info);
    }
}
