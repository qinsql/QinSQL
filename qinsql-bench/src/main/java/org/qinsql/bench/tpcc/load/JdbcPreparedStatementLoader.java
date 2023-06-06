/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data loader using prepared statements and batches. This is slower than the JdbcStatementLoader which uses
 * bulk inserts.
 */
public class JdbcPreparedStatementLoader implements RecordLoader {

    Connection conn;

    PreparedStatement pstmt;

    String tableName;

    String columnName[];

    boolean ignore;

    int maxBatchSize;

    int currentBatchSize;

    public JdbcPreparedStatementLoader(Connection conn, String tableName, String columnName[],
            boolean ignore, int maxBatchSize) {
        this.conn = conn;
        this.tableName = tableName;
        this.columnName = columnName;
        this.ignore = ignore;
        this.maxBatchSize = maxBatchSize;

        StringBuilder b = new StringBuilder();
        b.append("INSERT ");
        if (ignore) {
            b.append("IGNORE ");
        }
        // b.append("INTO `").append(tableName).append("` (");
        b.append("INTO ").append(tableName).append(" (");
        for (int i = 0; i < columnName.length; i++) {
            if (i > 0) {
                b.append(',');
            }
            b.append(columnName[i].trim());
        }
        b.append(") VALUES (");
        for (int i = 0; i < columnName.length; i++) {
            if (i > 0) {
                b.append(',');
            }
            b.append('?');
        }
        b.append(')');
        final String sql = b.toString();

        try {
            this.conn.setAutoCommit(false);
            this.pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to prepare: " + sql, e);
        }
    }

    @Override
    public void load(Record r) throws Exception {
        for (int i = 0; i < columnName.length; i++) {
            pstmt.setObject(i + 1, r.getField(i));
        }
        pstmt.addBatch();
        if (++currentBatchSize == maxBatchSize) {
            executeCurrentBatch();
        }
    }

    private void executeCurrentBatch() throws Exception {
        pstmt.executeBatch();
        currentBatchSize = 0;
    }

    @Override
    public void commit() throws Exception {
        conn.commit();
    }

    @Override
    public void close() throws Exception {
        executeCurrentBatch();
        pstmt.close();
        conn.commit();
    }
}
