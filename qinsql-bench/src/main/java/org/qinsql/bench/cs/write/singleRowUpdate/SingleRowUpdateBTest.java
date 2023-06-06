/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.singleRowUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.qinsql.bench.cs.write.ClientServerWriteBTest;

//-XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx800M
public abstract class SingleRowUpdateBTest extends ClientServerWriteBTest {

    protected SingleRowUpdateBTest() {
        threadCount = 1;
        rowCount = threadCount;
        sqls = new String[rowCount];
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists SingleRowUpdateBTest");
        String sql = "create table if not exists SingleRowUpdateBTest(pk int primary key, f1 int)";
        statement.executeUpdate(sql);

        sql = "insert into SingleRowUpdateBTest values(?,1)";
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
            sqls[i - 1] = "update SingleRowUpdateBTest set f1=10 where pk=" + i;
        }
        close(statement, ps, conn);
    }

    @Override
    protected UpdateThreadBase createUpdateThread(int id, Connection conn) {
        return new UpdateThread(id, conn);
    }

    private class UpdateThread extends UpdateThreadBase {
        String sql;

        UpdateThread(int id, Connection conn) {
            super(id, conn);
            this.sql = sqls[id];
        }

        @Override
        protected String nextSql() {
            return sql;
        }
    }
}
