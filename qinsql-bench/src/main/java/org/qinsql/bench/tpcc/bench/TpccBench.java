/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.sql.Connection;
import java.text.DecimalFormat;

import org.qinsql.bench.tpcc.config.TpccConfig;
import org.qinsql.bench.tpcc.util.RtHist;
import org.qinsql.bench.tpcc.util.Util;

public class TpccBench extends TpccConfig {

    public static void main(String[] args) {
        System.setProperty("lealone.server.cached.objects", "10000000");
        dumpInformation(args);
        TpccBench tpccBench = new TpccBench();
        tpccBench.parseArgs(args);
        tpccBench.runBenchmark();
        System.out.println("Terminating process now");
    }

    private static final String RAMPUP_TIME = "rampup_time";
    private static final String MEASURE_TIME = "measure_time";
    private static final String JOINS = "joins";

    private static final String TRANSACTION_NAME[] = {
            "NewOrder",
            "Payment",
            "Order Stat",
            "Delivery",
            "Slev" };

    public static volatile boolean stopped = false;
    public static volatile boolean counting_on = false;

    private int rampupTime;
    private int measureTime;
    private boolean joins;

    private TpccThread[] threads;

    public TpccBench() {
        // Empty.
    }

    private void parseArgs(String[] args) {
        if (args.length == 0) {
            loadConfig();
            rampupTime = getIntProperty(RAMPUP_TIME);
            measureTime = getIntProperty(MEASURE_TIME);
            joins = Boolean.parseBoolean(properties.getProperty(JOINS));
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
                } else if (args[i].equals("-J")) {
                    joins = Boolean.parseBoolean(args[i + 1]);
                } else {
                    showUsage();
                }
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
    }

    private void init() {
        RtHist.histInit();
        stopped = false;

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
    }

    Connection[] connections;

    private void runBenchmark() {

        System.out.println("***************************************");
        System.out.println("********* Java TPC-C Benchmark ********");
        System.out.println("***************************************");

        init();
        connections = new Connection[numConn];
        for (int i = 0; i < numConn; i++) {
            connections[i] = getConnection();
        }
        // start threads
        startThreads();

        if (rampupTime > 0) {
            // rampup time
            System.out.println();
            System.out.println("RAMPUP START.");
            // timeLapse(System.currentTimeMillis(), rampupTime);
            System.out.println("RAMPUP END.");
        }
        // startThreads();

        // start counting
        counting_on = true;

        // measure time
        System.out.println();
        System.out.println("MEASURING START.");
        // loop for the measure_time
        long startTime = System.currentTimeMillis();
        // timeLapse(startTime, measureTime);
        System.out.println("MEASURING END.");

        // stop threads
        stopThreads();

        showResults(startTime);

        // TODO: To be implemented better later.
        // RtHist.histReport();
    }

    private TpccThread[] startThreads() {
        System.out.println("STARTING TPCC THREADS");
        threads = new TpccThread[numConn];
        for (int i = 0; i < numConn; i++) {
            threads[i] = new TpccThread(connections[i], i, numWare, fetchSize, joins);
        }
        for (int i = 0; i < numConn; i++) {
            threads[i].start();
        }
        return threads;
    }

    private void stopThreads() {
        // stopped = true;
        try {
            for (int i = 0; i < numConn; i++) {
                threads[i].join(30 * 1000);
            }
            System.out.println("TPCC THREADS STOPPED");
        } catch (InterruptedException e) {
            System.out.println("Timed out waiting for threads to terminate");
        }
    }

    private void showResults(long startTime) {
        final long actualTestTime = System.currentTimeMillis() - startTime;
        // show results
        System.out.println("---------------------------------------------------");

        int[] success = new int[TRANSACTION_COUNT];
        int[] late = new int[TRANSACTION_COUNT];
        int[] retry = new int[TRANSACTION_COUNT];
        int[] failure = new int[TRANSACTION_COUNT];
        long[] sum_rt = new long[TRANSACTION_COUNT];
        long[] min_rt = new long[TRANSACTION_COUNT];
        long[] max_rt = new long[TRANSACTION_COUNT];
        long[] avg_rt = new long[TRANSACTION_COUNT];
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            min_rt[i] = Long.MAX_VALUE;
        }
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            max_rt[i] = 0;
        }

        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            for (int j = 0; j < numConn; j++) {
                TpccThread t = threads[j];
                success[i] += t.success[i];
                late[i] += t.late[i];
                retry[i] += t.retry[i];
                failure[i] += t.failure[i];

                sum_rt[i] += t.sum_rt[i];
                long rt = t.min_rt[i];
                if (rt < min_rt[i])
                    min_rt[i] = rt;
                rt = t.max_rt[i];
                if (rt > max_rt[i])
                    max_rt[i] = rt;
            }
        }
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            int count = success[i] + late[i];
            if (count > 0)
                avg_rt[i] = sum_rt[i] / count;
        }

        /*
         * Raw Results 
         */
        System.out.println("<Raw Results>");
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            System.out.printf("  |%s| success:%d  late:%d  retry:%d  failure:%d \n", TRANSACTION_NAME[i],
                    success[i], late[i], retry[i], failure[i]);
        }
        System.out.printf(" in %f sec.\n", actualTestTime / 1000.0f);

        System.out.println("<Constraint Check> (all must be [OK])\n [transaction percentage]");
        int j = 0;
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
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
                System.out.printf(" [OK]");
            } else {
                System.out.printf(" [NG] *");
            }
            System.out.printf("  min:%d  max:%d  avg:%d [ms]\n", min_rt[n], max_rt[n], avg_rt[n]);
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

        long time = 0;
        for (int t = 0; t < numConn; t++) {
            TpccThread tt = threads[t];
            time += tt.sum;
        }
        System.out.println("time: " + time / numConn / 100);
    }

    @SuppressWarnings("unused")
    private static void timeLapse(long startTime, int endTime) {
        DecimalFormat df = new DecimalFormat("#,##0.0");
        long runTime = 0;
        while ((runTime = System.currentTimeMillis() - startTime) < endTime * 1000) {
            System.out.println(
                    "Current execution time lapse: " + df.format(runTime / 1000.0f) + " seconds");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Sleep interrupted", e);
            }
        }
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
