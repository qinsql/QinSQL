/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;
import org.lealone.test.client.ClientTestBase;

public class JdbcDatabaseMetaDataTest extends ClientTestBase {
    @Test
    public void run() throws Exception {
        Connection conn = MySQLJdbcTest.getMySQLConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("create schema if not exists test");
        statement.executeUpdate("use test");
        statement.executeUpdate("create table if not exists VVV(f1 int)");
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getCatalogs();
        printResultSet(rs);
        rs.close();
        rs = md.getTables(null, "test", "vvv", null);
        printResultSet(rs);
        rs.close();
        rs = md.getTables(null, "test", "VVV", null);
        printResultSet(rs);
        rs.close();
        conn.close();
    }
}
