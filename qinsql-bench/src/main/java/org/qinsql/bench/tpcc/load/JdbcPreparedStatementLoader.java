/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Data loader using prepared statements and batches. 
 * This is slower than the JdbcStatementLoader which uses bulk inserts.
 */
public class JdbcPreparedStatementLoader implements RecordLoader {

    private String tableName;
    private String[] columnNames;
    private int maxBatchSize;
    private ArrayList<Future<?>> futures = new ArrayList<>();
    private ArrayList<Record> records = new ArrayList<>();
    private String sql;

    public JdbcPreparedStatementLoader(String tableName, String[] columnNames, boolean ignore,
            int maxBatchSize) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.maxBatchSize = maxBatchSize;

        StringBuilder b = new StringBuilder();
        b.append("INSERT ");
        if (ignore) {
            b.append("IGNORE ");
        }
        // b.append("INTO `").append(tableName).append("` (");
        b.append("INTO ").append(tableName).append(" (");
        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) {
                b.append(',');
            }
            b.append(columnNames[i].trim());
        }
        b.append(") VALUES (");
        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) {
                b.append(',');
            }
            b.append('?');
        }
        b.append(')');
        sql = b.toString();
    }

    @Override
    public void load(Record r) throws Exception {
        records.add(r.copy());
        if (records.size() == maxBatchSize) {
            executeBatch();
        }
    }

    @Override
    public void close() throws Exception {
        if (!records.isEmpty()) {
            executeBatch();
        }
        for (Future<?> f : futures)
            f.get();
    }

    private void executeBatch() throws Exception {
        ArrayList<Record> records = new ArrayList<>(this.records);
        this.records.clear();
        futures.add(TpccLoad.submit(() -> executeBatch(records)));
    }

    private void executeBatch(ArrayList<Record> records) {
        try {
            Connection conn = TpccLoad.getNextConnection();
            synchronized (conn) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                for (Record r : records) {
                    for (int i = 0; i < columnNames.length; i++) {
                        pstmt.setObject(i + 1, r.getField(i));
                    }
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
                pstmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading into table '" + tableName + "' with SQL: " + sql,
                    e);
        }
    }
}
