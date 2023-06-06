/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.jdbc.connection;

import java.sql.Connection;

public class LealoneConnectionBTest extends ConnectionBTest {

    public static void main(String[] args) throws Exception {
        new LealoneConnectionBTest().run();
    }

    @Override
    protected Connection getConnection() throws Exception {
        return getLealoneSharedConnection(10);
    }
}
