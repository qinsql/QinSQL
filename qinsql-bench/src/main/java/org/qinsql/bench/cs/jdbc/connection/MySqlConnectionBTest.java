/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc.connection;

import java.sql.Connection;

public class MySqlConnectionBTest extends ConnectionBTest {

    public static void main(String[] args) throws Exception {
        new MySqlConnectionBTest().run();
    }

    @Override
    protected Connection getConnection() throws Exception {
        return org.qinsql.bench.cs.ClientServerBTest.getMySQLConnection();
    }
}
