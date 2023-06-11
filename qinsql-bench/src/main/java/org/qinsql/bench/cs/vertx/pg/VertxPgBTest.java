/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.vertx.pg;

import org.qinsql.bench.cs.vertx.VertxBTest;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public abstract class VertxPgBTest extends VertxBTest {

    public static void run(String name, String sql) throws Throwable {
        PgConnectOptions connectOptions = new PgConnectOptions();
        connectOptions.setPort(5432).setHost("localhost");
        connectOptions.setDatabase("test").setUser("postgres").setPassword("zhh");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        SqlClient client = PgPool.client(connectOptions, poolOptions);
        run(client, name, sql);
        client.close();
    }
}
