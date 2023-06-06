/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.async.vertx.pg;

public class VertxPgQueryBTest extends VertxPgBTest {

    public static void main(String[] args) throws Throwable {
        String sql = "select count(*) from test where f1+f2>1";
        run("VertxPgQuery", sql);
    }
}
