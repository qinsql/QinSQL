/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.DbType;

@SuppressWarnings("unused")
public class TpccThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(TpccThread.class);
    private static final boolean DEBUG = logger.isDebugEnabled();

    /**
     * Dedicated JDBC connection for this thread.
     */
    Connection conn;

    Driver driver;

    int number;
    int port;
    int is_local;
    int num_ware;
    int num_conn;
    String db_user;
    String db_password;
    String driverClassName;
    String jdbcUrl;
    int fetchSize;

    private int[] success;
    private int[] late;
    private int[] retry;
    private int[] failure;

    private int[][] success2;
    private int[][] late2;
    private int[][] retry2;
    private int[][] failure2;

    private boolean joins;

    // TpccStatements pStmts;

    public TpccThread(DbType dbType, int number, int port, int is_local, String db_user,
            String db_password, int num_ware, int num_conn, String driverClassName, String dURL,
            int fetchSize, int[] success, int[] late, int[] retry, int[] failure, int[][] success2,
            int[][] late2, int[][] retry2, int[][] failure2, boolean joins) {

        this.number = number;
        this.port = port;
        this.db_password = db_password;
        this.db_user = db_user;
        this.is_local = is_local;
        this.num_conn = num_conn;
        this.num_ware = num_ware;
        this.driverClassName = driverClassName;
        this.jdbcUrl = dURL;
        this.fetchSize = fetchSize;

        this.success = success;
        this.late = late;
        this.retry = retry;
        this.failure = failure;

        this.success2 = success2;
        this.late2 = late2;
        this.retry2 = retry2;
        this.failure2 = failure2;
        this.joins = joins;

        connectToDatabase(dbType);

        // Create a driver instance.
        driver = new Driver(conn, fetchSize, success, late, retry, failure, success2, late2, retry2,
                failure2, joins);
    }

    @Override
    public void run() {
        try {
            if (DEBUG) {
                logger.debug("Starting driver with: number: " + number + " num_ware: " + num_ware
                        + " num_conn: " + num_conn);
            }
            driver.runTransaction(number, num_ware, num_conn);
        } catch (Throwable e) {
            logger.error("Unhandled exception", e);
        }
    }

    private Connection connectToDatabase(DbType dbType) {
        logger.info("Connection to database: driver: " + driverClassName + " url: " + jdbcUrl);
        try {
            Properties prop = new Properties();
            prop.put("user", db_user);
            prop.put("password", db_password);

            conn = DriverManager.getConnection(jdbcUrl, prop);
            if (dbType == DbType.LEALONE) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("set LOCK_TIMEOUT 1000000");
                stmt.close();
            }
            if (dbType == DbType.MYSQL) {
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            }
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
        return conn;
    }
}
