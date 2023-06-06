/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.lealone.client.jdbc.JdbcPreparedStatement;
import org.lealone.client.jdbc.JdbcStatement;

public class LealoneSqlBTest extends SqlBenchTest {

    public static void main(String[] args) throws Exception {
        new LealoneSqlBTest().run(args);
    }

    @Override
    protected Connection getConnection() throws Exception {
        return getLealoneConnection();
    }

    @Override
    protected void update(Statement stmt, int start, int end) throws Exception {
        JdbcStatement statement = (JdbcStatement) stmt;
        for (int i = start; i < end; i++) {
            int f1 = i;
            if (isRandom())
                f1 = randomKeys[i];
            statement.executeUpdateAsync("update SqlPerfTest set f2 = 'value2' where f1 =" + f1)
                    .onComplete(ar -> {
                        notifyOperationComplete();
                    });
        }
    }

    @Override
    protected void prepare(PreparedStatement ps, int start, int end) throws Exception {
        JdbcPreparedStatement ps2 = (JdbcPreparedStatement) ps;
        for (int i = start; i < end; i++) {
            int f1 = i;
            if (isRandom())
                f1 = randomKeys[i];
            ps2.setInt(1, f1);
            ps2.executeUpdateAsync().onComplete(ar -> {
                notifyOperationComplete();
            });
        }
    }
}
