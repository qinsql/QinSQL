/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.async.lealone;

import java.sql.Connection;
import java.sql.Statement;

import org.lealone.db.ConnectionSetting;
import org.lealone.db.Constants;
import org.qinsql.bench.cs.ClientServerBTest;

public abstract class AsyncLealoneBTest extends ClientServerBTest {

    public static Connection getLealoneConnection() throws Exception {
        String url = getUrl();
        Connection conn = getConnection(url, "root", "");
        Statement statement = conn.createStatement();
        statement.executeUpdate("set QUERY_CACHE_SIZE 0;");

        statement.close();
        return conn;
    }

    public static String getUrl() {
        String url = "jdbc:lealone:tcp://localhost:" + Constants.DEFAULT_TCP_PORT + "/lealone";
        url += "?" + ConnectionSetting.NETWORK_TIMEOUT + "=" + Integer.MAX_VALUE;
        return url;
    }
}
