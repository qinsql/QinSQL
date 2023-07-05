/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.load;

import java.io.IOException;
import java.sql.Connection;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public class LoadConfig {

    public enum LoadType {
        JDBC_STATEMENT,
        JDBC_PREPARED_STATEMENT
    }

    private LoadType loadType = LoadType.JDBC_PREPARED_STATEMENT;
    private Connection conn;
    private boolean jdbcInsertIgnore = false;
    private int jdbcBatchSize = 200;

    public RecordLoader createLoader(String tableName, String columnName[]) throws IOException {
        switch (loadType) {
        case JDBC_STATEMENT:
            return new JdbcStatementLoader(tableName, columnName, jdbcInsertIgnore, jdbcBatchSize);
        case JDBC_PREPARED_STATEMENT:
            return new JdbcPreparedStatementLoader(tableName, columnName, jdbcInsertIgnore,
                    jdbcBatchSize);
        default:
            throw new IllegalStateException();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setJdbcInsertIgnore(boolean jdbcInsertIgnore) {
        this.jdbcInsertIgnore = jdbcInsertIgnore;
    }
}
