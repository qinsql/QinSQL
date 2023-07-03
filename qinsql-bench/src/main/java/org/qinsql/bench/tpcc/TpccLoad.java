/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Properties;

import org.lealone.common.util.ScriptReader;
import org.qinsql.bench.DbType;
import org.qinsql.bench.tpcc.bench.Util;
import org.qinsql.bench.tpcc.load.Load;
import org.qinsql.bench.tpcc.load.LoadConfig;

public class TpccLoad extends Tpcc {

    public static void main(String[] args, String... sqlScripts) {
        dumpInformation(args);
        TpccLoad tpccLoad = new TpccLoad();
        tpccLoad.parseArgs(args);
        tpccLoad.runScript(sqlScripts);
        tpccLoad.runLoad();
    }

    // static int is_local = 1; /* "1" mean local */
    // static int DB_STRING_MAX = 51;
    private static boolean option_debug = false; /* 1 if generating debug output    */

    private static final String MODE = "MODE";
    private static final String OUTPUTDIR = "OUTPUTDIR";
    private static final String SHARDCOUNT = "SHARDCOUNT";
    private static final String SHARDID = "SHARDID";

    private String mode;
    private String outputDir;
    private int shardCount = 0;
    private int shardId = -1;

    private int seed = 0;

    private int particle_flg = 0; /* "1" means particle mode */
    private int part_no = 0; /* 1:items 2:warehouse 3:customer 4:orders */
    private long min_ware = 1;
    private long max_ware;

    public TpccLoad() {
        // Empty.
    }

    private void parseArgs(String[] args) {
        if (args.length == 0) {
            loadConfig();
            mode = properties.getProperty(MODE);
            outputDir = properties.getProperty(OUTPUTDIR);
            shardCount = Integer.parseInt(properties.getProperty(SHARDCOUNT));
            shardId = Integer.parseInt(properties.getProperty(SHARDID));
        } else {
            if ((args.length % 2) != 0) {
                System.out.println("Invalid number of arguments: " + args.length);
                showUsage();
            }
            System.out.println("Using the command line arguments for the load configuration.");
            for (int i = 0; i < args.length; i = i + 2) {
                if (args[i].equals("-m")) {
                    mode = args[i + 1];
                } else if (args[i].equals("-o")) {
                    outputDir = args[i + 1];
                } else if (args[i].equals("-u")) {
                    dbUser = args[i + 1];
                } else if (args[i].equals("-p")) {
                    dbPassword = args[i + 1];
                } else if (args[i].equals("-j")) {
                    javaDriver = args[i + 1];
                } else if (args[i].equals("-l")) {
                    jdbcUrl = args[i + 1];
                } else if (args[i].equals("-s")) {
                    shardCount = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-i")) {
                    shardId = Integer.parseInt(args[i + 1]);
                } else {
                    System.out.println("Incorrect Argument: " + args[i]);
                    showUsage();
                }
            }
        }
    }

    private void runLoad() {
        System.out.printf("********************************************\n");
        System.out.printf("*** Java TPC-C Data Loader version " + VERSION + " ***\n");
        System.out.printf("********************************************\n");

        final long start = System.currentTimeMillis();
        System.out.println("Execution time start: " + start);

        if (mode == null) {
            throw new RuntimeException("Mode is null.");
        }
        boolean jdbcMode = mode.equalsIgnoreCase("JDBC");
        if (jdbcMode) {
            if (dbUser == null) {
                throw new RuntimeException("User is null.");
            }
            if (dbPassword == null) {
                throw new RuntimeException("Password is null.");
            }
        } else if (mode.equalsIgnoreCase("FILE")) {
            if (outputDir == null) {
                throw new RuntimeException("Output dir is null.");
            }
        } else {
            throw new RuntimeException("Invalid mode '" + mode + "': must be CSV or JDBC");
        }

        if (numWare < 1) {
            throw new RuntimeException("Warehouse count has to be greater than or equal to 1.");
        }
        if (javaDriver == null) {
            throw new RuntimeException("Java Driver is null.");
        }
        if (jdbcUrl == null) {
            throw new RuntimeException("JDBC Url is null.");
        }
        if (shardId == -1) {
            throw new RuntimeException("ShardId was not obtained");
        }

        System.out.printf("<Parameters>\n");

        if (jdbcMode) {
            System.out.printf("     [Driver]: %s\n", javaDriver);
            System.out.printf("        [URL]: %s\n", jdbcUrl);
            System.out.printf("       [user]: %s\n", dbUser);
            System.out.printf("       [pass]: %s\n", dbPassword);
        } else {
            System.out.printf(" [Output Dir]: %s\n", outputDir);
        }

        System.out.printf("  [warehouse]: %d\n", numWare);
        System.out.printf("    [shardId]: %d\n", shardId);
        if (particle_flg == 1) {
            System.out.printf("  [part(1-4)]: %d\n", part_no);
            System.out.printf("     [MIN WH]: %d\n", min_ware);
            System.out.printf("     [MAX WH]: %d\n", max_ware);
        }

        // TODO: Pass the seed in as a variable.
        Util.setSeed(seed);

        LoadConfig loadConfig = new LoadConfig();

        /* EXEC SQL WHENEVER SQLERROR GOTO Error_SqlCall; */
        if (jdbcMode) {
            Connection conn;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException("Connection error", e);
            }
            if (dbType == DbType.MYSQL) {
                Statement stmt;
                try {
                    stmt = conn.createStatement();
                } catch (SQLException e) {
                    throw new RuntimeException("Could not create statement", e);
                }
                try {
                    stmt.execute("SET UNIQUE_CHECKS=0");
                } catch (SQLException e) {
                    throw new RuntimeException("Could not set unique checks error", e);
                }
                try {
                    stmt.execute("SET FOREIGN_KEY_CHECKS=0");
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Could not set foreign key checks error", e);
                }
                loadConfig.setJdbcInsertIgnore(true);

                // mysql用JDBC_STATEMENT比JDBC_PREPARED_STATEMENT快
                loadConfig.setLoadType(LoadConfig.LoadType.JDBC_STATEMENT);
            } else {
                loadConfig.setLoadType(LoadConfig.LoadType.JDBC_PREPARED_STATEMENT);
            }
            loadConfig.setConn(conn);
        } else {
            File outputDir = new File(this.outputDir);
            if (outputDir.exists()) {
                String[] list = outputDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".txt");
                    }
                });
                if (list.length > 0) {
                    throw new RuntimeException("All text files must be deleted from " + outputDir
                            + " before generating data");
                }
            } else {
                if (!outputDir.mkdirs()) {
                    throw new RuntimeException("Could not create dir: " + outputDir.getAbsolutePath());
                }
            }
            loadConfig.setLoadType(LoadConfig.LoadType.CSV);
            loadConfig.setOutputDir(outputDir);
        }

        System.out.printf("TPCC Data Load Started...\n");

        try {
            max_ware = numWare;
            if (particle_flg == 0) {
                System.out.printf("Particle flag: %d\n", particle_flg);
                Load.loadItems(loadConfig, option_debug);
                Load.loadWare(loadConfig, shardCount, (int) min_ware, (int) max_ware, option_debug,
                        shardId);
                Load.loadCust(loadConfig, shardCount, (int) min_ware, (int) max_ware, shardId);
                Load.loadOrd(loadConfig, shardCount, (int) max_ware, shardId);
            } else if (particle_flg == 1) {
                switch (part_no) {
                case 1:
                    Load.loadItems(loadConfig, option_debug);
                    break;
                case 2:
                    Load.loadWare(loadConfig, shardCount, (int) min_ware, (int) max_ware, option_debug,
                            shardId);
                    break;
                case 3:
                    Load.loadCust(loadConfig, shardCount, (int) min_ware, (int) max_ware, shardId);
                    break;
                case 4:
                    Load.loadOrd(loadConfig, shardCount, (int) max_ware, shardId);
                    break;
                default:
                    System.out.printf("Unknown part_no\n");
                    System.out.printf("1:ITEMS 2:WAREHOUSE 3:CUSTOMER 4:ORDERS\n");
                }
            }

            System.out.printf("\n...DATA LOADING COMPLETED SUCCESSFULLY.\n");
        } catch (Exception e) {
            System.out.println("Error loading data");
            e.printStackTrace();
        }

        final long end = System.currentTimeMillis();
        final long durationSeconds = (long) ((end - start) / 1000.0f);

        long seconds = durationSeconds % 60;
        long minutes = (durationSeconds - seconds) / 60;

        DecimalFormat df1 = new DecimalFormat("#,##0");
        DecimalFormat df2 = new DecimalFormat("#,##0.000");
        System.out.println(
                "Total execution time: " + df1.format(minutes) + " minute(s), " + df1.format(seconds)
                        + " second(s) (" + df2.format(durationSeconds / 60.0f) + " minutes)");
    }

    private Connection getConnection() throws SQLException {
        Properties jdbcConnectProp = new Properties();
        jdbcConnectProp.setProperty("user", dbUser);
        jdbcConnectProp.setProperty("password", dbPassword);
        if (dbType == DbType.MYSQL) {
            jdbcConnectProp.setProperty("useServerPrepStmts", "true");
            jdbcConnectProp.setProperty("cachePrepStmts", "true");
        }
        return DriverManager.getConnection(jdbcUrl, jdbcConnectProp);
    }

    private void runScript(String... sqlScripts) {
        if (sqlScripts != null && sqlScripts.length > 0) {
            try {
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                for (String sqlScript : sqlScripts) {
                    URL url = getConfigURL(sqlScript);
                    File file = new File(url.toURI());
                    String fileName = file.getCanonicalPath();
                    logger.info("RUNSCRIPT: " + fileName);
                    if (dbType == DbType.MYSQL || dbType == DbType.POSTGRESQL) {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(new FileInputStream(fileName)));
                        ScriptReader r = new ScriptReader(reader);
                        while (true) {
                            String sql = r.readStatement();
                            if (sql == null)
                                break;
                            sql = sql.trim();
                            if (sql.isEmpty() || sql.charAt(0) == '#' || sql.startsWith("--"))
                                continue;
                            // logger.info("sql: " + sql);
                            stmt.executeUpdate(sql);
                        }
                        reader.close();
                    } else
                        stmt.executeUpdate("RUNSCRIPT FROM '" + fileName + "'");
                }
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException("run script error", e);
            }
        }
    }

    private static void showUsage() {
        System.out.println("The possible arguments are as follows: ");
        System.out.println("-m [mode (FILE or JDBC)]");
        System.out.println("-o [file output dir]");
        System.out.println("-u [database username]");
        System.out.println("-p [database password]");
        System.out.println("-w [number of warehouses]");
        System.out.println("-j [java driver]");
        System.out.println("-l [jdbc url]");
        System.out.println("-s [shard count]");
        System.out.println("-i [shard id]");
        System.exit(-1);
    }
}
