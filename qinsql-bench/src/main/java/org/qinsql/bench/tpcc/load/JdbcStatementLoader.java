/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Future;

import org.qinsql.bench.tpcc.TpccLoad;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public class JdbcStatementLoader implements RecordLoader {

    private String tableName;
    private String[] columnNames;
    private boolean ignore;
    private int maxBatchSize;
    private int currentBatchSize;
    private StringBuilder b = new StringBuilder();
    private ArrayList<Future<?>> futures = new ArrayList<>();

    public JdbcStatementLoader(String tableName, String[] columnNames, boolean ignore,
            int maxBatchSize) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.ignore = ignore;
        this.maxBatchSize = maxBatchSize;
    }

    @Override
    public void load(Record r) throws Exception {
        if (currentBatchSize == 0) {
            b.append("INSERT ");
            if (ignore) {
                b.append("IGNORE ");
            }
            b.append("INTO `").append(tableName).append("` (");
            for (int i = 0; i < columnNames.length; i++) {
                if (i > 0) {
                    b.append(',');
                }
                b.append(columnNames[i].trim());
            }
            b.append(") VALUES ");
        } else {
            b.append(',');
        }
        b.append('(');
        write(b, r, ",");
        b.append(')');

        if (++currentBatchSize == maxBatchSize) {
            executeBulkInsert();
        }
    }

    @Override
    public void close() throws Exception {
        if (currentBatchSize > 0) {
            executeBulkInsert();
        }
        for (Future<?> f : futures)
            f.get();
    }

    private void executeBulkInsert() throws Exception {
        final String sql = b.toString();
        b.setLength(0);
        currentBatchSize = 0;
        futures.add(TpccLoad.submit(() -> executeBulkInsert(sql)));
    }

    private void executeBulkInsert(String sql) {
        try {
            Connection conn = TpccLoad.getNextConnection();
            synchronized (conn) {
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                conn.commit();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading into table '" + tableName + "' with SQL: " + sql,
                    e);
        }
    }

    private void write(StringBuilder b, Record r, String delim) throws Exception {
        final Object[] field = r.getField();
        for (int i = 0; i < field.length; i++) {
            if (i > 0) {
                b.append(delim);
            }

            final Object fieldValue = field[i];

            if (fieldValue instanceof Date) {
                // b.append("'").append(dateTimeFormat.format((Date)field[i])).append("'");
                b.append("'").append(fieldValue).append("'");
            } else if (fieldValue instanceof String) {
                b.append("'").append(fieldValue).append("'");
            } else {
                b.append(fieldValue);
            }
        }
    }
}
