/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.batchInsert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.qinsql.bench.cs.write.ClientServerWriteBTest;

public abstract class BatchInsertBTest extends ClientServerWriteBTest {

    protected BatchInsertBTest() {
        batch = true;
        outerLoop = 30;
        threadCount = 16;
        sqlCountPerInnerLoop = 50;
        innerLoop = 30;
        // printInnerLoopResult = true;
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists BatchInsertBTest");
        String sql = "create table if not exists BatchInsertBTest(pk int primary key, f1 int)";
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
                ps = conn.prepareStatement("insert into BatchInsertBTest values(?,1)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String nextSql() {
            return "insert into BatchInsertBTest values(" + id.incrementAndGet() + ",1)";
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
