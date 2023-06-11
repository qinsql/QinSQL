/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.vertx.lealone;

import org.qinsql.bench.misc.vertx.VertxBTest;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public abstract class VertxLealoneBTest extends VertxBTest {

    public static void run(String name, String sql) throws Throwable {
        PgConnectOptions connectOptions = new PgConnectOptions();
        connectOptions.setPort(PG_PORT).setHost("localhost");
        connectOptions.setDatabase("test").setUser("test").setPassword("test");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        SqlClient client = PgPool.client(connectOptions, poolOptions);
        client.query("set QUERY_CACHE_SIZE 0").execute(ar -> {
        });
        run(client, name, sql);
        client.close();
    }
}
