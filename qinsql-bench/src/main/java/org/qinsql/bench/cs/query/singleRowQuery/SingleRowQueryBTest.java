/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.query.singleRowQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.qinsql.bench.cs.query.ClientServerQueryBTest;

public abstract class SingleRowQueryBTest extends ClientServerQueryBTest {

    protected AtomicInteger id = new AtomicInteger();
    protected Random random = new Random();

    public SingleRowQueryBTest() {
        threadCount = 16;
        outerLoop = 15;
        innerLoop = 5;
        sqlCountPerInnerLoop = 50;
        rowCount = 10000;
        sqls = new String[rowCount];
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists SingleRowQueryBTest");
        String sql = "create table if not exists SingleRowQueryBTest(pk int primary key, f1 int)";
        statement.executeUpdate(sql);

        sql = "insert into SingleRowQueryBTest values(?,1)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int row = 1; row <= rowCount; row++) {
            ps.setInt(1, row);
            ps.addBatch();
            if (row % 100 == 0 || row == rowCount) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        for (int i = 1; i <= rowCount; i++) {
            sqls[i - 1] = "select * from SingleRowQueryBTest where pk=" + i;
        }
        close(statement, ps, conn);
    }

    @Override
    protected QueryThreadBase createQueryThread(int id, Connection conn) {
        return new QueryThread(id, conn);
    }

    private class QueryThread extends QueryThreadBase {
        int start;

        QueryThread(int id, Connection conn) {
            super(id, conn);
            start = 0;
        }

        @Override
        protected String nextSql() {
            return "select * from SingleRowQueryBTest where pk=" + random.nextInt(rowCount);
        }

        @SuppressWarnings("unused")
        protected String nextSql0() {
            return sqls[start++];
        }
    }
}
