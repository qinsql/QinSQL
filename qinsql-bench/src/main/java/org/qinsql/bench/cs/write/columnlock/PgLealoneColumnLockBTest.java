/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.columnlock;

import java.sql.Connection;

public class PgLealoneColumnLockBTest extends ColumnLockBTest {

    public static void main(String[] args) {
        new PgLealoneColumnLockBTest().start();
    }

    @Override
    public Connection getConnection() throws Exception {
        return getPgConnection(PG_PORT);
    }
}
