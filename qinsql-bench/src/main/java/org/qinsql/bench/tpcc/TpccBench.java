/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.tpcc.bench.NamedThreadFactory;
import org.qinsql.bench.tpcc.bench.RtHist;
import org.qinsql.bench.tpcc.bench.TpccThread;
import org.qinsql.bench.tpcc.bench.Util;

@SuppressWarnings("unused")
public class TpccBench extends Tpcc {

    public static void main(String[] args) {
        dumpInformation(args);
        TpccBench tpccBench = new TpccBench();
        tpccBench.parseArgs(args);
        tpccBench.runBenchmark();
        System.out.println("Terminating process now");
    }

    private static final Logger logger = LoggerFactory.getLogger(TpccBench.class);
    private static final boolean DEBUG = logger.isDebugEnabled();

    private static final String DATABASE = "DATABASE";
    private static final String RAMPUPTIME = "RAMPUPTIME";
    private static final String DURATION = "DURATION";
    private static final String JOINS = "JOINS";

    private static final String TRANSACTION_NAME[] = {
            "NewOrder",
            "Payment",
            "Order Stat",
            "Delivery",
            "Slev" };

    public static volatile int activate_transaction = 0;
    public static volatile boolean counting_on = false;

    private final boolean joins = true;

    private int numConn;
    private int rampupTime;
    private int measureTime;
    private int fetchSize = 100;

    private int num_node; /* number of servers that consists of cluster i.e. RAC (0:normal mode)*/
    private final int[] success = new int[TRANSACTION_COUNT];
    private final int[] late = new int[TRANSACTION_COUNT];
    private final int[] retry = new int[TRANSACTION_COUNT];
    private final int[] failure = new int[TRANSACTION_COUNT];

    private int[][] success2;
    private int[][] late2;
    private int[][] retry2;
    private int[][] failure2;

    private int[] success2_sum = new int[TRANSACTION_COUNT];
    private int[] late2_sum = new int[TRANSACTION_COUNT];
    private int[] retry2_sum = new int[TRANSACTION_COUNT];
    private int[] failure2_sum = new int[TRANSACTION_COUNT];

    private int[] prev_s = new int[5];
    private int[] prev_l = new int[5];

    private double[] max_rt = new double[5];
    private int port = 3306;

    public TpccBench() {
        // Empty.
    }

    private void parseArgs(String[] args) {
        if (args.length == 0) {
            loadConfig();
            rampupTime = Integer.parseInt(properties.getProperty(RAMPUPTIME));
            measureTime = Integer.parseInt(properties.getProperty(DURATION));
            String jdbcFetchSize = properties.getProperty("JDBCFETCHSIZE");
            // joins = Boolean.parseBoolean(properties.getProperty(JOINS));
            if (jdbcFetchSize != null) {
                fetchSize = Integer.parseInt(jdbcFetchSize);
            }
        } else {
            if ((args.length % 2) != 0) {
                System.out.println("Invalid number of arguments: " + args.length);
                showUsage();
            }
            System.out.println("Using the command line arguments for the load configuration.");
            for (int i = 0; i < args.length; i = i + 2) {
                if (args[i].equals("-u")) {
                    dbUser = args[i + 1];
                } else if (args[i].equals("-p")) {
                    dbPassword = args[i + 1];
                } else if (args[i].equals("-w")) {
                    numWare = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-c")) {
                    numConn = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-r")) {
                    rampupTime = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-t")) {
                    measureTime = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-j")) {
                    javaDriver = args[i + 1];
                } else if (args[i].equals("-l")) {
                    jdbcUrl = args[i + 1];
                } else if (args[i].equals("-f")) {
                    fetchSize = Integer.parseInt(args[i + 1]);
                    // } else if (args[i].equals("-J")) {
                    // joins = Boolean.parseBoolean(args[i + 1]);
                } else {
                    showUsage();
                }
            }
        }
    }

    private void runBenchmark() {

        System.out.println("***************************************");
        System.out.println("****** Java TPC-C Load Generator ******");
        System.out.println("***************************************");

        /* initialize */
        RtHist.histInit();
        activate_transaction = 1;

        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            success[i] = 0;
            late[i] = 0;
            retry[i] = 0;
            failure[i] = 0;

            prev_s[i] = 0;
            prev_l[i] = 0;

            max_rt[i] = 0.0;
        }

        /* number of node (default 0) */
        num_node = 0;

        if (num_node > 0) {
            if (numWare % num_node != 0) {
                logger.error(" [warehouse] value must be devided by [num_node].");
                return;
            }
            if (numConn % num_node != 0) {
                logger.error("[connection] value must be devided by [num_node].");
                return;
            }
        }

        if (javaDriver == null) {
            throw new RuntimeException("Java Driver is null.");
        }
        if (jdbcUrl == null) {
            throw new RuntimeException("JDBC Url is null.");
        }
        if (dbUser == null) {
            throw new RuntimeException("User is null.");
        }
        if (dbPassword == null) {
            throw new RuntimeException("Password is null.");
        }
        if (numWare < 1) {
            throw new RuntimeException("Warehouse count has to be greater than or equal to 1.");
        }
        if (numConn < 1) {
            throw new RuntimeException("Connections has to be greater than or equal to 1.");
        }
        if (rampupTime < 1) {
            throw new RuntimeException("Rampup time has to be greater than or equal to 1.");
        }
        if (measureTime < 1) {
            throw new RuntimeException("Duration has to be greater than or equal to 1.");
        }

        // Init 2-dimensional arrays.
        success2 = new int[TRANSACTION_COUNT][numConn];
        late2 = new int[TRANSACTION_COUNT][numConn];
        retry2 = new int[TRANSACTION_COUNT][numConn];
        failure2 = new int[TRANSACTION_COUNT][numConn];

        // long delay1 = measure_time*1000;

        System.out.printf("<Parameters>\n");

        System.out.printf("     [driver]: %s\n", javaDriver);
        System.out.printf("        [URL]: %s\n", jdbcUrl);
        System.out.printf("       [user]: %s\n", dbUser);
        System.out.printf("       [pass]: %s\n", dbPassword);
        System.out.printf("      [joins]: %b\n", joins);

        System.out.printf("  [warehouse]: %d\n", numWare);
        System.out.printf(" [connection]: %d\n", numConn);
        System.out.printf("     [rampup]: %d (sec.)\n", rampupTime);
        System.out.printf("    [measure]: %d (sec.)\n", measureTime);

        Util.seqInit(10, 10, 1, 1, 1);

        /* set up threads */

        if (DEBUG)
            logger.debug("Creating TpccThread");
        ExecutorService executor = Executors.newFixedThreadPool(numConn,
                new NamedThreadFactory("tpcc-thread"));

        // Start each server.

        for (int i = 0; i < numConn; i++) {
            Runnable worker = new TpccThread(dbType, i, port, 1, dbUser, dbPassword, numWare, numConn,
                    javaDriver, jdbcUrl, fetchSize, success, late, retry, failure, success2, late2,
                    retry2, failure2, joins);
            executor.execute(worker);
        }

        if (rampupTime > 0) {
            // rampup time
            System.out.printf("\nRAMPUP START.\n\n");
            try {
                Thread.sleep(rampupTime * 1000);
            } catch (InterruptedException e) {
                logger.error("Rampup wait interrupted", e);
            }
            System.out.printf("\nRAMPUP END.\n\n");
        }

        // measure time
        System.out.printf("\nMEASURING START.\n\n");

        // start counting
        counting_on = true;

        // loop for the measure_time
        final long startTime = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("#,##0.0");
        long runTime = 0;
        while ((runTime = System.currentTimeMillis() - startTime) < measureTime * 1000) {
            System.out.println(
                    "Current execution time lapse: " + df.format(runTime / 1000.0f) + " seconds");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Sleep interrupted", e);
            }
        }
        final long actualTestTime = System.currentTimeMillis() - startTime;

        // show results
        System.out.println("---------------------------------------------------");
        /*
         *  Raw Results 
         */

        System.out.println("<Raw Results>");
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            System.out.printf("  |%s| sc:%d  lt:%d  rt:%d  fl:%d \n", TRANSACTION_NAME[i], success[i],
                    late[i], retry[i], failure[i]);
        }
        System.out.printf(" in %f sec.\n", actualTestTime / 1000.0f);

        /*
        * Raw Results 2
        */
        System.out.println("<Raw Results2(sum ver.)>");
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            success2_sum[i] = 0;
            late2_sum[i] = 0;
            retry2_sum[i] = 0;
            failure2_sum[i] = 0;
            for (int k = 0; k < numConn; k++) {
                success2_sum[i] += success2[i][k];
                late2_sum[i] += late2[i][k];
                retry2_sum[i] += retry2[i][k];
                failure2_sum[i] += failure2[i][k];
            }
        }
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            System.out.printf("  |%s| sc:%d  lt:%d  rt:%d  fl:%d \n", TRANSACTION_NAME[i],
                    success2_sum[i], late2_sum[i], retry2_sum[i], failure2_sum[i]);
        }

        System.out.println("<Constraint Check> (all must be [OK])\n [transaction percentage]");
        int j = 0;
        int i;
        for (i = 0; i < TRANSACTION_COUNT; i++) {
            j += (success[i] + late[i]);
        }

        double f = 100.0 * (success[1] + late[1]) / j;
        System.out.printf("        Payment: %f%% (>=43.0%%)", f);
        if (f >= 43.0) {
            System.out.printf(" [OK]\n");
        } else {
            System.out.printf(" [NG] *\n");
        }
        f = 100.0 * (success[2] + late[2]) / j;
        System.out.printf("   Order-Status: %f%% (>= 4.0%%)", f);
        if (f >= 4.0) {
            System.out.printf(" [OK]\n");
        } else {
            System.out.printf(" [NG] *\n");
        }
        f = 100.0 * (success[3] + late[3]) / j;
        System.out.printf("       Delivery: %f%% (>= 4.0%%)", f);
        if (f >= 4.0) {
            System.out.printf(" [OK]\n");
        } else {
            System.out.printf(" [NG] *\n");
        }
        f = 100.0 * (success[4] + late[4]) / j;
        System.out.printf("    Stock-Level: %f%% (>= 4.0%%)", f);
        if (f >= 4.0) {
            System.out.printf(" [OK]\n");
        } else {
            System.out.printf(" [NG] *\n");
        }

        /*
        * Response Time
        */
        System.out.printf(" [response time (at least 90%% passed)]\n");

        for (int n = 0; n < TRANSACTION_NAME.length; n++) {
            f = 100.0 * success[n] / (success[n] + late[n]);
            if (DEBUG)
                logger.debug("f: " + f + " success[" + n + "]: " + success[n] + " late[" + n + "]: "
                        + late[n]);
            System.out.printf("      %s: %f%% ", TRANSACTION_NAME[n], f);
            if (f >= 90.0) {
                System.out.printf(" [OK]\n");
            } else {
                System.out.printf(" [NG] *\n");
            }
        }

        double total = 0.0;
        for (j = 0; j < TRANSACTION_COUNT; j++) {
            total = total + success[j] + late[j];
            System.out.println(" " + TRANSACTION_NAME[j] + " Total: " + (success[j] + late[j]));
        }

        float tpcm = (success[0] + late[0]) * 60000f / actualTestTime;

        System.out.println();
        System.out.println("<TpmC>");
        System.out.println(tpcm + " TpmC");

        // stop threads
        System.out.printf("\nSTOPPING THREADS\n");
        activate_transaction = 0;

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Timed out waiting for executor to terminate");
        }

        // TODO: To be implemented better later.
        // RtHist.histReport();
    }

    private static void showUsage() {
        System.out.println("The possible arguments are as follows: ");
        System.out.println("-h [database host]");
        System.out.println("-d [database name]");
        System.out.println("-u [database username]");
        System.out.println("-p [database password]");
        System.out.println("-w [number of warehouses]");
        System.out.println("-c [number of connections]");
        System.out.println("-r [ramp up time]");
        System.out.println("-t [duration of the benchmark (sec)]");
        System.out.println("-j [java driver]");
        System.out.println("-l [jdbc url]");
        System.out.println("-h [jdbc fetch size]");
        System.out.println("-J [joins (true|false) default true]");
        System.exit(-1);
    }
}
