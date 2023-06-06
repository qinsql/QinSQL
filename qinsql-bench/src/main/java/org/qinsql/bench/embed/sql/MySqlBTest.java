/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.sql;

import java.sql.Connection;

public class MySqlBTest extends SqlBenchTest {

    public static void main(String[] args) throws Exception {
        new MySqlBTest().run(args);
    }

    @Override
    protected Connection getConnection() throws Exception {
        return getMySqlConnection();
    }
}
