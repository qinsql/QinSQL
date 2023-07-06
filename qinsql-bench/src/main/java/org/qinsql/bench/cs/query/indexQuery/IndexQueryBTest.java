/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.query.indexQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.qinsql.bench.DbType;
import org.qinsql.bench.cs.query.ClientServerQueryBTest;

public abstract class IndexQueryBTest extends ClientServerQueryBTest {

    public IndexQueryBTest() {
        threadCount = 16;
        benchTestLoop = 10;
        outerLoop = 15;
        innerLoop = 5;
        sqlCountPerInnerLoop = 50;
        rowCount = 30000;
    }

    @Override
    protected void init() throws Exception {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("drop table if exists IndexQueryBTest");
        String sql = "create table if not exists IndexQueryBTest(name varchar(20), f1 int, f2 int)";
        statement.executeUpdate(sql);

        if (dbType == DbType.MYSQL) {
            try {
                statement.executeUpdate("drop index i_name_IndexQueryBTest on IndexQueryBTest");
            } catch (Exception e) {
            }
            try {
                statement.executeUpdate("create index i_name_IndexQueryBTest on IndexQueryBTest(name)");
            } catch (Exception e) {
            }
        } else {
            statement.executeUpdate("drop index if exists i_name_IndexQueryBTest");
            statement.executeUpdate(
                    "create index if not exists i_name_IndexQueryBTest on IndexQueryBTest(name)");
        }

        sql = "insert into IndexQueryBTest values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 1; i <= rowCount; i++) {
            ps.setString(1, "n" + i);
            ps.setInt(2, i);
            ps.setInt(3, i * 10);
            ps.addBatch();
            if (i % 100 == 0 || i == rowCount) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        close(statement, ps, conn);
    }

    @Override
    protected QueryThreadBase createQueryThread(int id, Connection conn) {
        return new QueryThread(id, conn);
    }

    private class QueryThread extends QueryThreadBase {
        QueryThread(int id, Connection conn) {
            super(id, conn);
        }

        @Override
        protected String nextSql() {
            return "select * from IndexQueryBTest where name='n" + random.nextInt(rowCount) + "'";
        }
    }
}
