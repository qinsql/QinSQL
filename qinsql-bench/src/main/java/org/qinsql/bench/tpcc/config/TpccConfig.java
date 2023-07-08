/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Properties;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.DbType;

public abstract class TpccConfig implements TpccConstants {

    protected static final Logger logger = LoggerFactory.getLogger(TpccConfig.class);

    protected static final String VERSION = "1.0.1";

    protected static final String TPCC_PROPERTIES_FILE = "tpcc.properties";
    protected static final String DB_PROPERTIES_FILE = "db.properties";

    protected static final String WAREHOUSE_COUNT = "warehouse_count";
    protected static final String CONNECTIONS = "connections";

    protected static final String DB_TYPE = "db_type";
    protected static final String DRIVER = "driver";
    protected static final String JDBC_URL = "jdbc_url";
    protected static final String USER = "user";
    protected static final String PASSWORD = "password";

    /* Global SQL Variables */
    protected String javaDriver;
    protected String jdbcUrl;
    protected String dbUser;
    protected String dbPassword;
    protected DbType dbType;
    protected int numWare;
    protected int numConn;
    protected int fetchSize;

    protected Properties properties;

    protected void loadConfig() {
        properties = new Properties();

        URL url = getTpccConfigURL();
        logger.info("Loading tpcc config from: " + url);
        loadConfig(properties, url);

        url = getDBConfigURL();
        logger.info("Loading database config from: " + url);
        loadConfig(properties, url);

        javaDriver = properties.getProperty(DRIVER);
        jdbcUrl = properties.getProperty(JDBC_URL);
        dbUser = properties.getProperty(USER);
        dbPassword = properties.getProperty(PASSWORD);
        String dbType = properties.getProperty(DB_TYPE).toUpperCase();
        this.dbType = DbType.valueOf(dbType);
        numWare = getIntProperty(WAREHOUSE_COUNT);
        numConn = getIntProperty(CONNECTIONS);
        fetchSize = getIntProperty("jdbc_fetch_size");
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public Connection getConnection() {
        logger.info("Connection to database: driver: " + javaDriver + " url: " + jdbcUrl);
        Connection conn;
        try {
            Properties prop = new Properties();
            prop.setProperty("user", dbUser);
            prop.setProperty("password", dbPassword);

            conn = DriverManager.getConnection(jdbcUrl, prop);
            if (dbType == DbType.LEALONE) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("set LOCK_TIMEOUT 1000000");
                // stmt.executeUpdate("set query_cache_size 0");
                stmt.close();
            } else if (dbType == DbType.H2) {
                Statement stmt = conn.createStatement();
                // stmt.executeUpdate("set OPTIMIZE_REUSE_RESULTS 0");
                stmt.close();
            } else if (dbType == DbType.MYSQL) {
                prop.setProperty("useServerPrepStmts", "true");
                prop.setProperty("cachePrepStmts", "true");
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            }
            // conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
        return conn;
    }

    public static void loadConfig(Properties properties, URL url) {
        try (InputStream inputStream = url.openStream()) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + url, e);
        }
    }

    public static URL getTpccConfigURL() {
        String configUrl = System.getProperty("tpcc.config", TPCC_PROPERTIES_FILE);
        return getConfigURL(configUrl);
    }

    public static URL getDBConfigURL() {
        String configUrl = System.getProperty("db.config", DB_PROPERTIES_FILE);
        return getConfigURL(configUrl);
    }

    public static URL getConfigURL(String configUrl) {
        URL url;
        try {
            url = new URL(configUrl);
            url.openStream().close(); // catches well-formed but bogus URLs
        } catch (Exception e) {
            try {
                File file = new File(configUrl).getCanonicalFile();
                url = file.toURI().toURL();
                url.openStream().close();
                return url;
            } catch (Exception e2) {
            }
            url = TpccConfig.class.getClassLoader().getResource(configUrl);
        }
        return url;
    }

    public static void dumpInformation(String[] argv) {
        logger.info("TPCC version " + VERSION + " Number of Arguments: " + argv.length);
        // dump information about the environment we are running in
        String sysProp[] = {
                "os.name",
                "os.arch",
                "os.version",
                "java.runtime.name",
                "java.vm.version",
                "java.library.path" };

        for (String s : sysProp) {
            logger.info("System Property: " + s + " = " + System.getProperty(s));
        }

        DecimalFormat df = new DecimalFormat("#,##0.0");
        logger.info("maxMemory = " + df.format(Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0))
                + " MB");
    }
}
