/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.util.Random;

public class Util implements TpccConstants {

    // Weight Variables
    static int no;
    static int py;
    static int os;
    static int dl;
    static int sl;
    static int total = 0;

    private static int[] seq;
    private static int nextNum = 0;
    static Boolean first = true;

    static int permCount;
    private static Random generate = new Random();

    private static final int C_255 = randomNumber(0, 255);
    private static final int C_1023 = randomNumber(0, 1023);
    private static final int C_8191 = randomNumber(0, 8191);

    // Member Functions
    public static void shuffle() {
        // Counters
        int i;
        int j;
        int tmp;
        int rmd;

        Random rnd = new Random();

        for (i = 0, j = 0; i < no; i++, j++) {
            seq[j] = 0;
        }
        for (i = 0; i < py; i++, j++) {
            seq[j] = 1;
        }
        for (i = 0; i < os; i++, j++) {
            seq[j] = 2;
        }
        for (i = 0; i < dl; i++, j++) {
            seq[j] = 3;
        }
        for (i = 0; i < sl; i++, j++) {
            seq[j] = 4;
        }
        for (i = 0, j = total - 1; j > 0; i++, j--) {

            rmd = rnd.nextInt() % (j + 1);
            if (rmd < 0) {
                rmd = rmd * -1;
            }
            tmp = seq[rmd + i];
            seq[rmd + i] = seq[i];
            seq[i] = tmp;
        }

    }

    public static void seqInit(int n, int p, int o, int d, int s) {
        no = n;
        py = p;
        os = o;
        dl = d;
        sl = s;
        total = n + p + o + d + s;
        System.out.printf("TOTAL:%d\n", total);
        seq = new int[total];
        shuffle();
        nextNum = 0;
    }

    public synchronized static int seqGet() {
        if (nextNum >= total) {
            shuffle();
            nextNum = 0;
        }
        return seq[nextNum++];
    }

    public static void setSeed(int seed) {
        generate.setSeed(seed);
    }

    /*
      * return number uniformly distributed b/w min and max, inclusive
      */
    public static int randomNumber(int min, int max) {
        int next = generate.nextInt();
        int div = next % ((max - min) + 1);
        if (div < 0) {
            div = div * -1;
        }
        int value = min + div;

        /*
        if (value < min || value > max) {
           throw new IllegalStateException("next=" + next + ", div=" + div + ", min=" + min + ", max="
                   + max + ", value=" + value);
        }
        */

        return value;
    }

    /*
      *
      *
      * the constant C depends on which value of A is passed, but the same
      * value of C should be used for all calls with the same value of
      * A.  however, we know in advance which values of A will be used.
      */
    public static int nuRand(int A, int x, int y) {
        int C = 0;

        switch (A) {
        case 255:
            C = C_255;
            break;
        case 1023:
            C = C_1023;
            break;
        case 8191:
            C = C_8191;
            break;
        default:
            throw new RuntimeException("NURand: unexpected value (%d) of A used\n" + A);
        }

        return ((((randomNumber(0, A) | randomNumber(x, y)) + C) % (y - x + 1)) + x);

    }

    /*
       *
       * make a ``random a-string'': a string of random alphanumeric
       * characters of a random length of minimum x, maximum y, and
       * mean (y+x)/2
       */
    public static String makeAlphaString(int x, int y) {
        String str = null;
        String temp = "0123456789" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
        char[] alphanum = temp.toCharArray();
        int arrmax = 61; /* index of last array element */
        int i;
        int len;
        len = randomNumber(x, y);

        for (i = 0; i < len; i++) {
            if (str != null) {
                str = str + alphanum[randomNumber(0, arrmax)];
            } else {
                str = "" + alphanum[randomNumber(0, arrmax)];
            }
        }
        return str;
    }

    /*
       * like MakeAlphaString, only numeric characters only
       */
    public static String makeNumberString(int x, int y) {
        String str = null;
        char[] numeric = "0123456789".toCharArray();
        int arrmax = 9;
        int i;
        int len;

        len = randomNumber(x, y);

        for (i = 0; i < len; i++) {
            if (str != null) {
                str = str + numeric[randomNumber(0, arrmax)];
            } else {
                str = "0";
            }
        }
        return str;
    }

    public static void initPermutation() {
        int i, j = 0;
        int[] tempNums = new int[CUST_PER_DIST];

        permCount = 0;

        /* initialize with consecutive values [1..ORD_PER_DIST] */
        // for (i = 0, cur = nums; i < ORD_PER_DIST; i++, cur++) {
        // *cur = i + 1;
        // }

        for (i = 0; i < ORD_PER_DIST; i++) {
            nums[i] = i + 1;
            tempNums[i] = i + 1;
        }

        /* now, shuffle */
        // for (i = 0; i < ORD_PER_DIST-1; i++) {
        // j = (int)RandomNumber(i+1, ORD_PER_DIST-1);
        // swap_int(nums[i], nums[j]);
        // }
        // TODO: Why is it ORD_PER_DIST-1. Not shuffling the last one?
        for (i = 0; i < ORD_PER_DIST - 1; i++) {
            j = randomNumber(i + 1, ORD_PER_DIST - 1);
            nums[j] = tempNums[i];
        }
    }

    public static int getPermutation() {
        if (permCount >= ORD_PER_DIST) {
            throw new RuntimeException("GetPermutation: past end of list!\n");
        }
        return nums[permCount++];
    }

    public static String lastName(int num) {
        String name = null;
        String[] n = { "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE", "ANTI", "CALLY", "ATION", "EING" };

        name = n[num / 100];
        name = name + n[(num / 10) % 10];
        name = name + n[num % 10];

        return name;
    }
}
