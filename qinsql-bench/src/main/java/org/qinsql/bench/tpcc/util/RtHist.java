/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.util;

public class RtHist {

    private static final int MAXREC = 20;
    private static final int REC_PER_SEC = 1000;
    private static int[][] total_hist = new int[5][MAXREC * REC_PER_SEC];
    private static int[][] cur_hist = new int[5][MAXREC * REC_PER_SEC];

    // CHECK: These are externs so should they be public????
    private static double[] max_rt = new double[10];
    private static double[] cur_max_rt = new double[10];

    /* initialize */
    public static void histInit() {
        int i = 0;
        int j = 0;

        for (i = 0; i < 5; i++) {
            for (j = 0; j < (MAXREC * REC_PER_SEC); j++) {
                total_hist[i][j] = cur_hist[i][j] = 0;
            }
        }
    }

    /* incliment matched one */
    public static void histInc(int transaction, double rtclk) {
        int i = 0;
        i = (int) (rtclk * REC_PER_SEC);
        if (i >= (MAXREC * REC_PER_SEC)) {
            i = (MAXREC * REC_PER_SEC) - 1;
        }
        if (rtclk > cur_max_rt[transaction]) {
            cur_max_rt[transaction] = rtclk;
        }
        ++cur_hist[transaction][i];
        // System.out.print("In: %.3f, trx: %d, Added %d\n", rtclk, transaction, i);
    }

    /* check point, add on total histgram, return 90% line */
    public static double histCkp(int transaction) {
        int i;
        int total, tmp, line, line_set;

        total = tmp = line_set = 0;
        line = MAXREC * REC_PER_SEC;
        for (i = 0; i < (MAXREC * REC_PER_SEC); i++) {
            total += cur_hist[transaction][i];
            // total += i;
        }
        for (i = 0; i < (MAXREC * REC_PER_SEC); i++) {
            tmp += cur_hist[transaction][i];
            // tmp += i;
            total_hist[transaction][i] += cur_hist[transaction][i];
            cur_hist[transaction][i] = 0;
            if ((tmp >= (total * 99 / 100)) && (line_set == 0)) {
                line = i;
                line_set = 1;
            }
        }
        // System.out.print("CKP: trx: %d line: %d total: %d tmp: %d ret: %.3f\n", transaction, line, total,
        // tmp,(double)(line)/(double)(REC_PER_SEC));
        return ((double) (line) / (double) (REC_PER_SEC));
    }

    public static void histReport() {
        int i = 0;
        int j = 0;
        int[] total = new int[5];
        int[] tmp = new int[5];
        int[] line = new int[5];

        for (j = 0; j < 5; j++) {
            total[j] = tmp[j] = 0;
            line[j] = MAXREC * REC_PER_SEC;
            for (i = 0; i < (MAXREC * REC_PER_SEC); i++) {
                total[j] += total_hist[j][i];
            }
            for (i = (MAXREC * REC_PER_SEC) - 1; i >= 0; i--) {
                tmp[j] += total_hist[j][i];
                if ((tmp[j] * 10) <= total[j]) {
                    line[j] = i;
                }
            }
        }
        System.out.print("\n<RT Histogram>\n");

        for (j = 0; j < 5; j++) {
            switch (j) {
            case 0:
                System.out.print("\n1.New-Order\n\n");
                break;
            case 1:
                System.out.print("\n2.Payment\n\n");
                break;
            case 2:
                System.out.print("\n3.Order-Status\n\n");
                break;
            case 3:
                System.out.print("\n4.Delivery\n\n");
                break;
            case 4:
                System.out.print("\n5.Stock-Level\n\n");
            }
            for (i = 0; (i < (MAXREC * REC_PER_SEC)) && (i <= line[j] * 4); i++) {
                System.out.printf("%3.2f, %6d\n", (double) (i + 1) / (double) (REC_PER_SEC),
                        total_hist[j][i]);
            }
            System.out.print("\n");
        }

        System.out.print("\n<90th Percentile RT (MaxRT)>\n");
        for (j = 0; j < 5; j++) {
            switch (j) {
            case 0:
                System.out.print("   New-Order : ");
                break;
            case 1:
                System.out.print("     Payment : ");
                break;
            case 2:
                System.out.print("Order-Status : ");
                break;
            case 3:
                System.out.print("    Delivery : ");
                break;
            case 4:
                System.out.print(" Stock-Level : ");
            }
            System.out.printf("%3.2f  (%.2f)\n", (double) (line[j]) / (double) (REC_PER_SEC), max_rt[j]);
        }
    }
}
