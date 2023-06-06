/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.storage.mvstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

//因为update语句会转成removeRow和addRow操作，所以不会跟delete语句产生死锁
public class DeadLockTest {
    static Connection getConnection(String url) throws Exception {
        Properties prop = new Properties();
        prop.setProperty("MULTI_THREADED", "true");
        return DriverManager.getConnection(url, prop);
    }

    static String url = "jdbc:h2:mem:mydb";;

    public static void main(String[] args) throws Exception {
        Connection conn = getConnection(url);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("DROP TABLE IF EXISTS DeadLockTest");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS DeadLockTest (f1 int primary key, f2 long, f3 long)");
        stmt.executeUpdate("INSERT INTO DeadLockTest(f1, f2, f3) VALUES(10, 20, 30)");
        stmt.executeUpdate("INSERT INTO DeadLockTest(f1, f2, f3) VALUES(100, 200, 300)");

        new Thread(() -> {
            try {
                Connection conn1 = getConnection(url);
                conn1.setAutoCommit(false);
                Statement stmt1 = conn1.createStatement();
                stmt1.executeQuery("SELECT f2 FROM DeadLockTest WHERE f1=100 for update");
                stmt1.executeUpdate("UPDATE DeadLockTest SET f2=21"
                        + " where f1=10 AND f2 <= (SELECT f2 FROM DeadLockTest WHERE f1=100)");
                conn1.commit();
                conn1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Connection conn2 = getConnection(url);
                Statement stmt2 = conn2.createStatement();
                stmt2.executeUpdate("UPDATE DeadLockTest SET f2=2 where f1=100");
                conn2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        conn.close();
    }

    public static void main3(String[] args) throws Exception {
        Connection conn = getConnection(url);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("DROP TABLE IF EXISTS DeadLockTest");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS DeadLockTest (f1 int primary key, f2 long, f3 long)");
        stmt.executeUpdate("CREATE INDEX IF NOT EXISTS DeadLockIndexTest1 ON DeadLockTest(f2)");
        stmt.executeUpdate("CREATE INDEX IF NOT EXISTS DeadLockIndexTest2 ON DeadLockTest(f3)");
        stmt.executeUpdate("INSERT INTO DeadLockTest(f1, f2, f3) VALUES(10, 20, 30)");

        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS DeadLockTest2 (f1 int primary key, f2 long, f3 long)");
        stmt.executeUpdate("INSERT INTO DeadLockTest2(f1, f2, f3) VALUES(10, 20, 30)");

        new Thread(() -> {
            try {
                Connection conn1 = getConnection(url);
                Statement stmt1 = conn1.createStatement();
                ResultSet rs = stmt1.executeQuery("select * from DeadLockTest2 where f2=20");
                if (rs.next()) {
                    System.out.println(rs.getInt(1));
                }
                rs.close();
                rs = stmt1.executeQuery("select * from DeadLockTest where f3=30");
                if (rs.next()) {
                    System.out.println(rs.getInt(1));
                } else {
                    System.out.println("empty");
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Connection conn2 = getConnection(url);
                Statement stmt2 = conn2.createStatement();
                stmt2.executeUpdate("delete from DeadLockTest where f1=10");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        conn.close();
    }

    public static void main2(String[] args) throws Exception {
        Connection conn = getConnection(url);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("DROP TABLE IF EXISTS DeadLockTest");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DeadLockTest (f1 int primary key, f2 long)");
        stmt.executeUpdate("CREATE INDEX IF NOT EXISTS DeadLockIndexTest ON DeadLockTest(f2)");
        stmt.executeUpdate("INSERT INTO DeadLockTest(f1, f2) VALUES(10, 12)");

        new Thread(() -> {
            try {
                Connection conn1 = getConnection(url);
                Statement stmt1 = conn1.createStatement();
                stmt1.executeUpdate("UPDATE DeadLockTest SET f2=13 where f1=10");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Connection conn2 = getConnection(url);
                Statement stmt2 = conn2.createStatement();
                stmt2.executeUpdate("delete from DeadLockTest where f1=10");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        conn.close();
    }
}
