/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.qinsql.bench.tpcc.bench.transaction.Delivery;
import org.qinsql.bench.tpcc.bench.transaction.NewOrder;
import org.qinsql.bench.tpcc.bench.transaction.OrderStat;
import org.qinsql.bench.tpcc.bench.transaction.Payment;
import org.qinsql.bench.tpcc.bench.transaction.Slev;
import org.qinsql.bench.tpcc.bench.transaction.TpccStatements;
import org.qinsql.bench.tpcc.config.TpccConstants;
import org.qinsql.bench.tpcc.util.RtHist;
import org.qinsql.bench.tpcc.util.Util;

public class TpccThread extends Thread implements TpccConstants {

    /**
     * For debug use only.
     */
    private static final boolean DETECT_LOCK_WAIT_TIMEOUTS = false;

    /**
     * Can be disabled for debug use only.
     */
    private static final boolean ALLOW_MULTI_WAREHOUSE_TX = true;

    private static final int MAX_RETRY = 2000;
    private static final int RTIME_NEWORD = 5 * 1000;
    private static final int RTIME_PAYMENT = 5 * 1000;
    private static final int RTIME_ORDSTAT = 5 * 1000;
    private static final int RTIME_DELIVERY = 5 * 1000;
    private static final int RTIME_SLEV = 20 * 1000;
    private static final int[] RTIME = new int[TRANSACTION_COUNT];
    static {
        RTIME[0] = RTIME_NEWORD;
        RTIME[1] = RTIME_PAYMENT;
        RTIME[2] = RTIME_ORDSTAT;
        RTIME[3] = RTIME_DELIVERY;
        RTIME[4] = RTIME_SLEV;
    }

    public final int[] success = new int[TRANSACTION_COUNT];
    public final int[] late = new int[TRANSACTION_COUNT];
    public final int[] retry = new int[TRANSACTION_COUNT];
    public final int[] failure = new int[TRANSACTION_COUNT];

    public final long[] min_rt = new long[TRANSACTION_COUNT];
    public final long[] max_rt = new long[TRANSACTION_COUNT];
    public final long[] sum_rt = new long[TRANSACTION_COUNT];

    private final int num_ware;

    private final Connection conn;
    private final TpccStatements pStmts;

    // Transaction instances.
    private final NewOrder newOrder;
    private final Payment payment;
    private final OrderStat orderStat;
    private final Delivery delivery;
    private final Slev slev;

    public long start, stop, sum;

    public TpccThread(Connection conn, int t_num, int num_ware, int fetchSize, boolean joins) {
        super("tcpp-thread-" + (t_num + 1));
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            min_rt[i] = Long.MAX_VALUE;
        }
        for (int i = 0; i < TRANSACTION_COUNT; i++) {
            max_rt[i] = 0;
        }

        this.num_ware = num_ware;
        try {
            this.conn = conn;
            pStmts = new TpccStatements(conn, fetchSize);

            // Initialize the transactions.
            newOrder = new NewOrder(pStmts, joins);
            payment = new Payment(pStmts);
            orderStat = new OrderStat(pStmts);
            delivery = new Delivery(pStmts);
            slev = new Slev(pStmts);
        } catch (Throwable t) {
            throw new RuntimeException("Error initializing TpccThread", t);
        }
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 200; i++) {
                // start = System.currentTimeMillis();
                runTransaction();
                // stop = System.currentTimeMillis();
                // System.out.println("time: " + (stop - start));
            }
            for (int j = 0; j < 100; j++) {
                start = System.currentTimeMillis();
                for (int i = 0; i < 10; i++) {
                    runTransaction();
                }
                stop = System.currentTimeMillis();
                sum += (stop - start);
                System.out.println("time: " + (stop - start));
            }

        } catch (Throwable e) {
            logger.error("Unhandled exception", e);
        }
    }

    private int runTransaction() {
        // while (!TpccBench.stopped) {
        for (int i = 0; i < 1; i++) {
            int sequence = Util.seqGet();
            try {
                if (DETECT_LOCK_WAIT_TIMEOUTS) {
                    detectLockWaitTimeouts(sequence);
                } else {
                    doNextTransaction(sequence);
                }
            } catch (Throwable th) {
                logger.error("FAILED", th);
                TpccBench.stopped = true;
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    logger.error("", e);
                }
                return -1;
            }
        }
        return 0;
    }

    private final Executor exec = Executors.newSingleThreadExecutor();

    private void detectLockWaitTimeouts(final int sequence) throws SQLException {
        FutureTask<Object> t = new FutureTask<>(new Callable<>() {
            @Override
            public Object call() throws Exception {
                doNextTransaction(sequence);
                return null;
            }
        });
        exec.execute(t);

        try {
            t.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
            TpccBench.stopped = true;
        } catch (ExecutionException e) {
            logger.error("Unhandled exception", e);
            TpccBench.stopped = true;
        } catch (TimeoutException e) {
            logger.error("Detected Lock Wait", e);
            TpccBench.stopped = true;
        }
    }

    private void doNextTransaction(int sequence) throws SQLException {
        if (sequence == 0) {
            doNeword();
        } else if (sequence == 1) {
            doPayment();
        } else if (sequence == 2) {
            doOrdstat();
        } else if (sequence == 3) {
            doDelivery();
        } else if (sequence == 4) {
            doSlev();
        } else {
            throw new IllegalStateException("Error - Unknown sequence");
        }
    }

    // prepare data and execute the new order transaction for one order
    // officially, this is supposed to be simulated terminal I/O
    private void doNeword() throws SQLException {
        int all_local = 1;
        int notfound = MAXITEMS + 1;

        int[] itemid = new int[MAX_NUM_ITEMS];
        int[] supware = new int[MAX_NUM_ITEMS];
        int[] qty = new int[MAX_NUM_ITEMS];

        int w_id = Util.randomNumber(1, num_ware);
        if (w_id < 1) {
            throw new IllegalStateException("Invalid warehouse ID " + w_id);
        }

        int d_id = Util.randomNumber(1, DIST_PER_WARE);
        int c_id = Util.nuRand(1023, 1, CUST_PER_DIST);

        int ol_cnt = Util.randomNumber(5, 15);
        int rbk = Util.randomNumber(1, 100);

        for (int i = 0; i < ol_cnt; i++) {
            itemid[i] = Util.nuRand(8191, 1, MAXITEMS);
            if ((i == ol_cnt - 1) && (rbk == 1)) {
                itemid[i] = notfound;
            }
            if (ALLOW_MULTI_WAREHOUSE_TX) {
                if (Util.randomNumber(1, 100) != 1) {
                    supware[i] = w_id;
                } else {
                    supware[i] = otherWare(w_id);
                    all_local = 0;
                }
            } else {
                supware[i] = w_id;
            }
            qty[i] = Util.randomNumber(1, 10);
        }

        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_RETRY; i++) {
            if (DEBUG)
                logger.debug(" w_id: " + w_id + " c_id: " + c_id + " ol_cnt: " + ol_cnt + " all_local: "
                        + all_local + " qty: " + Arrays.toString(qty));
            int result = newOrder.neword(w_id, d_id, c_id, ol_cnt, all_local, itemid, supware, qty);
            if (handleResult(beginTime, result, 0)) {
                return;
            }
        }
        handleResult2(0);
    }

    // produce the id of a valid warehouse other than home_ware (assuming there is one)
    private int otherWare(int home_ware) {
        int tmp;

        if (num_ware == 1)
            return home_ware;
        while ((tmp = Util.randomNumber(1, num_ware)) == home_ware)
            ;
        return tmp;
    }

    // prepare data and execute payment transaction
    private void doPayment() {
        int byname;
        if (Util.randomNumber(1, 100) <= 60) {
            byname = 1; /* select by last name */
        } else {
            byname = 0; /* select by customer id */
        }

        int w_id = Util.randomNumber(1, num_ware);
        int d_id = Util.randomNumber(1, DIST_PER_WARE);
        int c_id = Util.nuRand(1023, 1, CUST_PER_DIST);
        String c_last = Util.lastName(Util.nuRand(255, 0, 999));
        int h_amount = Util.randomNumber(1, 5000);

        int c_w_id;
        int c_d_id;
        if (ALLOW_MULTI_WAREHOUSE_TX) {
            if (Util.randomNumber(1, 100) <= 85) {
                c_w_id = w_id;
                c_d_id = d_id;
            } else {
                c_w_id = otherWare(w_id);
                c_d_id = Util.randomNumber(1, DIST_PER_WARE);
            }
        } else {
            c_w_id = w_id;
            c_d_id = d_id;
        }

        // if (DEBUG)
        // logger.debug("Payment| cnum: " + c_num + " w_id: " + w_id + " d_id: " + d_id + " c_id: "
        // + c_id + "c_last: " + c_last + " h_amount: " + h_amount + " byname: " + byname
        // + " c_w_id: " + c_w_id + " c_d_id: " + c_d_id);

        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_RETRY; i++) {
            int result = payment.payment(w_id, d_id, byname, c_w_id, c_d_id, c_id, c_last, h_amount);
            if (handleResult(beginTime, result, 1)) {
                return;
            }
        }
        handleResult2(1);
    }

    // prepare data and execute order status transaction
    private void doOrdstat() {
        int byname;
        if (Util.randomNumber(1, 100) <= 60) {
            byname = 1; /* select by last name */
        } else {
            byname = 0; /* select by customer id */
        }
        int w_id = Util.randomNumber(1, num_ware);
        int d_id = Util.randomNumber(1, DIST_PER_WARE);
        int c_id = Util.nuRand(1023, 1, CUST_PER_DIST);
        String c_last = Util.lastName(Util.nuRand(255, 0, 999));
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_RETRY; i++) {
            int result = orderStat.ordStat(w_id, d_id, byname, c_id, c_last);
            if (handleResult(beginTime, result, 2)) {
                return;
            }
        }
        handleResult2(2);
    }

    // execute delivery transaction
    private void doDelivery() {
        int w_id = Util.randomNumber(1, num_ware);
        int o_carrier_id = Util.randomNumber(1, 10);
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_RETRY; i++) {
            int result = delivery.delivery(w_id, o_carrier_id);
            if (handleResult(beginTime, result, 3)) {
                return;
            }
        }
        handleResult2(3);
    }

    // prepare data and execute the stock level transaction
    private void doSlev() {
        int w_id = Util.randomNumber(1, num_ware);
        int d_id = Util.randomNumber(1, DIST_PER_WARE);
        int level = Util.randomNumber(10, 20);
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_RETRY; i++) {
            // int result = slev.slev(w_id, d_id, level);
            int result = slev.slev_ps(w_id, d_id, level);
            if (handleResult(beginTime, result, 4)) {
                return;
            }
        }
        handleResult2(4);
    }

    private boolean handleResult(long beginTime, int result, int tIndex) {
        if (result >= 1) {
            if (TpccBench.counting_on) {
                long rt = System.currentTimeMillis() - beginTime;
                sum_rt[tIndex] += rt;
                if (rt < min_rt[tIndex])
                    min_rt[tIndex] = rt;
                if (rt > max_rt[tIndex])
                    max_rt[tIndex] = rt;
                RtHist.histInc(tIndex, rt);
                if (rt < RTIME[tIndex]) {
                    success[tIndex]++;
                } else {
                    late[tIndex]++;
                }
            }
            return true;
        } else {
            if (TpccBench.counting_on) {
                retry[tIndex]++;
            }
            return false;
        }
    }

    private void handleResult2(int tIndex) {
        if (TpccBench.counting_on) {
            retry[tIndex]--;
            failure[tIndex]++;
        }
    }
}
