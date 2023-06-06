/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.DbType;
import org.qinsql.bench.tpcc.bench.TpccConstants;

public abstract class Tpcc implements TpccConstants {

    protected static final Logger logger = LoggerFactory.getLogger(Tpcc.class);

    protected static final String VERSION = "1.0.1";

    protected static final String DRIVER = "DRIVER";
    protected static final String WAREHOUSECOUNT = "WAREHOUSECOUNT";
    protected static final String USER = "USER";
    protected static final String PASSWORD = "PASSWORD";
    protected static final String JDBCURL = "JDBCURL";
    protected static final String DBTYPE = "DBTYPE";
    protected static final String PROPERTIESFILE = "tpcc.properties";

    /* Global SQL Variables */
    protected String javaDriver;
    protected String jdbcUrl;
    protected String dbUser;
    protected String dbPassword;
    protected DbType dbType;
    protected int numWare;

    protected Properties properties;
    protected InputStream inputStream;

    protected void loadConfig() {
        URL url = getConfigURL();
        logger.info("Using the tpcc.properties file for the load configuration.");
        logger.info("Loading properties from: " + url);

        properties = new Properties();
        try {
            inputStream = url.openStream();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file", e);
        }
        javaDriver = properties.getProperty(DRIVER);
        jdbcUrl = properties.getProperty(JDBCURL);
        dbUser = properties.getProperty(USER);
        dbPassword = properties.getProperty(PASSWORD);
        String dbType = properties.getProperty(DBTYPE).toUpperCase();
        this.dbType = DbType.valueOf(dbType);
        numWare = Integer.parseInt(properties.getProperty(WAREHOUSECOUNT));
    }

    public static URL getConfigURL() {
        String configUrl = System.getProperty("tpcc.config", PROPERTIESFILE);
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
            url = TpccBench.class.getClassLoader().getResource(configUrl);
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
