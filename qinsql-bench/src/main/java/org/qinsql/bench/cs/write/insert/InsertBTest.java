/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.insert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

import org.qinsql.bench.cs.write.ClientServerWriteBTest;

public abstract class InsertBTest extends ClientServerWriteBTest {

    AtomicInteger id = new AtomicInteger();

    protected InsertBTest() {
        outerLoop = 30;
        threadCount = 48;
        sqlCountPerInnerLoop = 20;
        innerLoop = 10;
        // printInnerLoopResult = true;
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists InsertBTest");
        String sql = "create table if not exists InsertBTest(pk int primary key, f1 int)"
                + " parameters(page_split_size='8k')";
        sql = "create table if not exists InsertBTest(pk int primary key, f1 int)";
        statement.executeUpdate(sql);
        close(statement, conn);
    }

    @Override
    protected UpdateThreadBase createUpdateThread(int id, Connection conn) {
        return new UpdateThread(id, conn);
    }

    private class UpdateThread extends UpdateThreadBase {

        UpdateThread(int id, Connection conn) {
            super(id, conn);
            try {
                ps = conn.prepareStatement("insert into InsertBTest values(?,1)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String nextSql() {
            return "insert into InsertBTest values(" + id.incrementAndGet() + ",1)";
        }

        @Override
        protected void prepare() {
            try {
                ps.setInt(1, id.incrementAndGet());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
