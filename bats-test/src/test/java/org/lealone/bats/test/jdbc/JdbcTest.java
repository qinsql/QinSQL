/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.lealone.bats.engine.server.BatsServer;

public class JdbcTest {

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager
                .getConnection("jdbc:lealone:tcp://localhost:" + BatsServer.DEFAULT_TCP_PORT + "/lealone", "root", "");
        Statement stmt = conn.createStatement();
        init(stmt);

        String sql = "SELECT name FROM my_table"; // 用OLTP的SQL引擎
        query(stmt, sql);

        sql = "SELECT name FROM olap.my_table"; // 用OLAP的SQL引擎
        query(stmt, sql);

        sql = "SELECT count(*) FROM cp.`test.csvh`"; // 用OLAP的SQL引擎
        query(stmt, sql);

        stmt.close();
        conn.close();
    }

    static void init(Statement stmt) throws Exception {
        stmt.executeUpdate("DROP TABLE IF EXISTS my_table");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_table(name varchar(20))");
        stmt.executeUpdate("INSERT INTO my_table(name) VALUES('abc')");
    }

    static void query(Statement stmt, String sql) throws Exception {
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next())
            System.out.println(rs.getString(1));
        else
            System.out.println("no data");
        rs.close();
    }
}
