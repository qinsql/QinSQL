/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLShowStatementTest {

    public static void main(String[] args) throws Exception {
        Connection conn = MySQLJdbcTest.getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'lower_case_%'");
        rs.next();
        conn.close();
    }
}
