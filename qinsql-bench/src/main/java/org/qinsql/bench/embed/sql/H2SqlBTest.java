/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.sql;

import java.sql.Connection;

public class H2SqlBTest extends SqlBenchTest {

    public static void main(String[] args) throws Exception {
        new H2SqlBTest().run();
    }

    @Override
    protected Connection getConnection() throws Exception {
        return getH2Connection();
    }
}
