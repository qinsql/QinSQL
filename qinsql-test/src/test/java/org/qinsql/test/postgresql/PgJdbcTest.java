/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.qinsql.postgresql.server.PgServer;

public class PgJdbcTest {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:" + PgServer.DEFAULT_PORT + "/postgres";
        Properties info = new Properties();
        info.put("user", "postgres");
        info.put("password", "postgres");
        return DriverManager.getConnection(url, info);
    }

    public static void sqlException(SQLException e) {
        while (e != null) {
            System.err.println("SQLException:" + e);
            System.err.println("-----------------------------------");
            System.err.println("Message  : " + e.getMessage());
            System.err.println("SQLState : " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            System.err.println();
            System.err.println();
            e = e.getNextException();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate("drop table if exists pet");
            statement.executeUpdate("create table if not exists pet(name varchar(20), age int)");
            statement.executeUpdate("insert into pet values('pet1', 2)");

            ResultSet rs = statement.executeQuery("select count(*) from pet");
            rs.next();
            System.out.println("Statement.executeQuery count: " + rs.getInt(1));
            rs.close();
            statement.close();

            String sql = "select name, age from pet where name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "pet1");
            rs = ps.executeQuery();
            rs.next();
            String name = rs.getString(1);
            int age = rs.getInt(2);
            System.out.println("PreparedStatement.executeQuery name: " + name + ", age: " + age);

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            sqlException(e);
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
