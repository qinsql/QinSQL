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

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.tpcc.TpccBench;

public class Driver implements TpccConstants {

    private static final Logger logger = LoggerFactory.getLogger(Driver.class);
    private static final boolean DEBUG = logger.isDebugEnabled();

    /**
     * For debug use only.
     */
    private static final boolean DETECT_LOCK_WAIT_TIMEOUTS = false;

    /**
     * Can be disabled for debug use only.
     */
    private static final boolean ALLOW_MULTI_WAREHOUSE_TX = true;

    // CHECK: The following variables are externs??
    // public int counting_on;
    public int num_ware;
    public int num_conn;

    public int num_node;
    // public int time_count;
    // public PrintWriter freport_file;

    // total count for all threads
    private int[] success;
    private int[] late;
    private int[] retry;
    private int[] failure;

    // per thread counts
    private int[][] success2;
    private int[][] late2;
    private int[][] retry2;
    private int[][] failure2;

    public double[] max_rt = new double[TRANSACTION_COUNT];

    // Private variables
    private final int MAX_RETRY = 2000;
    private final int RTIME_NEWORD = 5 * 1000;
    private final int RTIME_PAYMENT = 5 * 1000;
    private final int RTIME_ORDSTAT = 5 * 1000;
    private final int RTIME_DELIVERY = 5 * 1000;
    private final int RTIME_SLEV = 20 * 1000;

    private Connection conn;
    private TpccStatements pStmts;

    // Transaction instances.
    private NewOrder newOrder;
    private Payment payment;
    private OrderStat orderStat;
    private Slev slev;
    private Delivery delivery;

    /**
     * Constructor.
     *
     * @param conn
     */
    public Driver(Connection conn, int fetchSize, int[] success, int[] late, int[] retry, int[] failure,
            int[][] success2, int[][] late2, int[][] retry2, int[][] failure2, boolean joins) {
        try {
            this.conn = conn;

            pStmts = new TpccStatements(conn, fetchSize);

            // Initialize the transactions.
            newOrder = new NewOrder(pStmts, joins);
            payment = new Payment(pStmts);
            orderStat = new OrderStat(pStmts);
            slev = new Slev(pStmts);
            delivery = new Delivery(pStmts);

            this.success = success;
            this.late = late;
            this.retry = retry;
            this.failure = failure;

            this.success2 = success2;
            this.late2 = late2;
            this.retry2 = retry2;
            this.failure2 = failure2;

            for (int i = 0; i < TRANSACTION_COUNT; i++) {
                max_rt[i] = 0.0;
            }

        } catch (Throwable th) {
            throw new RuntimeException("Error initializing Driver", th);
        }
    }

    private final Executor exec = Executors.newSingleThreadExecutor();

    public int runTransaction(final int t_num, final int numWare, final int numConn) {

        num_ware = numWare;
        num_conn = numConn;

        int count = 0;

        /* Actually, WaitTimes are needed... */
        // CHECK: Is activate_transaction handled correctly?
        int sequence = Util.seqGet();
        while (TpccBench.activate_transaction == 1) {

            try {
                if (DEBUG)
                    logger.debug("BEFORE runTransaction: sequence: " + sequence);

                if (DETECT_LOCK_WAIT_TIMEOUTS) {
                    final int _sequence = sequence;
                    FutureTask<Object> t = new FutureTask<Object>(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            doNextTransaction(t_num, _sequence);
                            return null;
                        }
                    });
                    exec.execute(t);

                    try {
                        t.get(15, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        logger.error("InterruptedException", e);
                        TpccBench.activate_transaction = 0;
                    } catch (ExecutionException e) {
                        logger.error("Unhandled exception", e);
                        TpccBench.activate_transaction = 0;
                    } catch (TimeoutException e) {
                        logger.error("Detected Lock Wait", e);
                        TpccBench.activate_transaction = 0;
                    }

                } else {
                    doNextTransaction(t_num, sequence);
                }

                count++;
            } catch (Throwable th) {
                logger.error("FAILED", th);
                TpccBench.activate_transaction = 0;
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    logger.error("", e);
                }
                return -1;
            } finally {
                if (DEBUG)
                    logger.debug("AFTER runTransaction: sequence: " + sequence);
            }

            sequence = Util.seqGet();

        }

        logger.debug("Driver terminated after {} transactions", count);

        return (0);

    }

    private void doNextTransaction(int t_num, int sequence) throws SQLException {
        if (sequence == 0) {
            doNeword(t_num);
        } else if (sequence == 1) {
            doPayment(t_num);
        } else if (sequence == 2) {
            doOrdstat(t_num);
        } else if (sequence == 3) {
            doDelivery(t_num);
        } else if (sequence == 4) {
            doSlev(t_num);
        } else {
            throw new IllegalStateException("Error - Unknown sequence");
        }
    }

    /*
      * prepare data and execute the new order transaction for one order
      * officially, this is supposed to be simulated terminal I/O
      */
    private int doNeword(int t_num) throws SQLException {
        int c_num = 0;
        int i = 0;
        int ret = 0;
        double rt = 0.0;

        long beginTime = 0;
        long endTime = 0;

        int w_id = 0;
        int d_id = 0;
        int c_id = 0;
        int ol_cnt = 0;
        int all_local = 1;
        int notfound = MAXITEMS + 1;
        int rbk = 0;

        int[] itemid = new int[MAX_NUM_ITEMS];
        int[] supware = new int[MAX_NUM_ITEMS];
        int[] qty = new int[MAX_NUM_ITEMS];

        if (num_node == 0) {
            w_id = Util.randomNumber(1, num_ware);
        } else {
            c_num = ((num_node * t_num) / num_conn); /* drop moduls */
            w_id = Util.randomNumber(1 + (num_ware * c_num) / num_node,
                    (num_ware * (c_num + 1)) / num_node);
        }
        if (w_id < 1) {
            throw new IllegalStateException("Invalid warehouse ID " + w_id);
        }

        d_id = Util.randomNumber(1, DIST_PER_WARE);
        c_id = Util.nuRand(1023, 1, CUST_PER_DIST);

        ol_cnt = Util.randomNumber(5, 15);
        rbk = Util.randomNumber(1, 100);

        for (i = 0; i < ol_cnt; i++) {
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

        beginTime = System.currentTimeMillis();
        for (i = 0; i < MAX_RETRY; i++) {
            if (DEBUG)
                logger.debug("t_num: " + t_num + " w_id: " + w_id + " c_id: " + c_id + " ol_cnt: "
                        + ol_cnt + " all_local: " + all_local + " qty: " + Arrays.toString(qty));
            ret = newOrder.neword(t_num, w_id, d_id, c_id, ol_cnt, all_local, itemid, supware, qty);
            endTime = System.currentTimeMillis();

            if (ret == 1) {

                rt = endTime - beginTime;
                if (DEBUG)
                    logger.debug("BEFORE rt value: " + rt + " max_rt[0] value: " + max_rt[0]);

                if (rt > max_rt[0])
                    max_rt[0] = rt;

                if (DEBUG)
                    logger.debug("AFTER rt value: " + rt + " max_rt[0] value: " + max_rt[0]);

                RtHist.histInc(0, rt);

                if (TpccBench.counting_on) {
                    if (DEBUG)
                        logger.debug(" rt: " + rt + " RTIME_NEWORD " + RTIME_NEWORD);
                    if (rt < RTIME_NEWORD) {
                        if (DEBUG)
                            logger.debug("Rt < RTIME_NEWORD");
                        success[0]++;
                        success2[0][t_num]++;
                    } else {
                        if (DEBUG)
                            logger.debug("Rt > RTIME_NEWORD");
                        late[0]++;
                        late2[0][t_num]++;
                    }
                }

                return (1); /* end */
            } else {

                if (TpccBench.counting_on) {

                    retry[0]++;
                    retry2[0][t_num]++;
                }

            }
        }

        if (TpccBench.counting_on) {
            retry[0]--;
            retry2[0][t_num]--;
            failure[0]++;
            failure2[0][t_num]++;
        }

        return (0);
    }

    /*
      * produce the id of a valid warehouse other than home_ware
      * (assuming there is one)
      */
    private int otherWare(int home_ware) {
        int tmp;

        if (num_ware == 1)
            return home_ware;
        while ((tmp = Util.randomNumber(1, num_ware)) == home_ware)
            ;
        return tmp;
    }

    /*
      * prepare data and execute payment transaction
      */
    private int doPayment(int t_num) {
        int c_num = 0;
        int byname = 0;
        int i = 0;
        int ret = 0;
        double rt = 0.0;

        // clock_t clk1,clk2;
        // struct timespec tbuf1;
        // struct timespec tbuf2;

        long beginTime = 0;
        long endTime = 0;
        int w_id = 0;
        int d_id = 0;
        int c_w_id = 0;
        int c_d_id = 0;
        int c_id = 0;
        int h_amount = 0;
        String c_last = null;

        if (num_node == 0) {
            w_id = Util.randomNumber(1, num_ware);
        } else {
            c_num = ((num_node * t_num) / num_conn); /* drop moduls */
            w_id = Util.randomNumber(1 + (num_ware * c_num) / num_node,
                    (num_ware * (c_num + 1)) / num_node);
        }
        d_id = Util.randomNumber(1, DIST_PER_WARE);
        c_id = Util.nuRand(1023, 1, CUST_PER_DIST);
        c_last = Util.lastName(Util.nuRand(255, 0, 999));
        h_amount = Util.randomNumber(1, 5000);
        if (Util.randomNumber(1, 100) <= 60) {
            byname = 1; /* select by last name */
        } else {
            byname = 0; /* select by customer id */
        }
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

        // clk1 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf1 );
        beginTime = System.currentTimeMillis();
        for (i = 0; i < MAX_RETRY; i++) {
            ret = payment.payment(t_num, w_id, d_id, byname, c_w_id, c_d_id, c_id, c_last, h_amount);
            // clk2 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf2 );
            endTime = System.currentTimeMillis();

            if (ret >= 1) {

                // rt = (double)(tbuf2.tv_sec * 1000.0 + tbuf2.tv_nsec/1000000.0-tbuf1.tv_sec * 1000.0 -
                // tbuf1.tv_nsec/1000000.0);
                rt = endTime - beginTime;
                if (rt > max_rt[1])
                    max_rt[1] = rt;
                RtHist.histInc(1, rt);
                if (TpccBench.counting_on) {
                    if (rt < RTIME_PAYMENT) {
                        success[1]++;
                        success2[1][t_num]++;
                    } else {
                        late[1]++;
                        late2[1][t_num]++;
                    }
                }

                return (1); /* end */
            } else {

                if (TpccBench.counting_on) {
                    retry[1]++;
                    retry2[1][t_num]++;
                }

            }
        }

        if (TpccBench.counting_on) {
            retry[1]--;
            retry2[1][t_num]--;
            failure[1]++;
            failure2[1][t_num]++;
        }

        return (0);
    }

    /*
      * prepare data and execute order status transaction
      */
    private int doOrdstat(int t_num) {
        int c_num = 0;
        int byname = 0;
        int i = 0;
        int ret = 0;
        double rt = 0.0;
        /*clock_t clk1,clk2;
          struct timespec tbuf1;
          struct timespec tbuf2;*/
        long beginTime = 0;
        long endTime = 0;

        int w_id = 0;
        int d_id = 0;
        int c_id = 0;
        String c_last = null;

        if (num_node == 0) {
            w_id = Util.randomNumber(1, num_ware);
        } else {
            c_num = ((num_node * t_num) / num_conn); /* drop moduls */
            w_id = Util.randomNumber(1 + (num_ware * c_num) / num_node,
                    (num_ware * (c_num + 1)) / num_node);
        }
        d_id = Util.randomNumber(1, DIST_PER_WARE);
        c_id = Util.nuRand(1023, 1, CUST_PER_DIST);
        c_last = Util.lastName(Util.nuRand(255, 0, 999));
        if (Util.randomNumber(1, 100) <= 60) {
            byname = 1; /* select by last name */
        } else {
            byname = 0; /* select by customer id */
        }

        // clk1 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf1 );
        beginTime = System.currentTimeMillis();
        for (i = 0; i < MAX_RETRY; i++) {
            ret = orderStat.ordStat(t_num, w_id, d_id, byname, c_id, c_last);
            // clk2 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf2 );
            endTime = System.currentTimeMillis();

            if (ret >= 1) {

                // rt = (double)(tbuf2.tv_sec * 1000.0 + tbuf2.tv_nsec/1000000.0-tbuf1.tv_sec * 1000.0 -
                // tbuf1.tv_nsec/1000000.0)
                rt = endTime - beginTime;
                if (rt > max_rt[2])
                    max_rt[2] = rt;
                RtHist.histInc(2, rt);
                if (TpccBench.counting_on) {
                    if (rt < RTIME_ORDSTAT) {
                        success[2]++;
                        success2[2][t_num]++;
                    } else {
                        late[2]++;
                        late2[2][t_num]++;
                    }
                }

                return (1); /* end */
            } else {

                if (TpccBench.counting_on) {
                    retry[2]++;
                    retry2[2][t_num]++;
                }

            }
        }

        if (TpccBench.counting_on) {
            retry[2]--;
            retry2[2][t_num]--;
            failure[2]++;
            failure2[2][t_num]++;
        }

        return (0);
    }

    /*
      * execute delivery transaction
      */
    private int doDelivery(int t_num) {
        int c_num = 0;
        int i = 0;
        int ret = 0;
        double rt = 0.0;
        long beginTime = 0;
        long endTime = 0;
        int w_id = 0;
        int o_carrier_id = 0;

        if (num_node == 0) {
            w_id = Util.randomNumber(1, num_ware);
        } else {
            c_num = ((num_node * t_num) / num_conn); /* drop moduls */
            w_id = Util.randomNumber(1 + (num_ware * c_num) / num_node,
                    (num_ware * (c_num + 1)) / num_node);
        }
        o_carrier_id = Util.randomNumber(1, 10);

        beginTime = System.currentTimeMillis();
        for (i = 0; i < MAX_RETRY; i++) {
            ret = delivery.delivery(w_id, o_carrier_id);
            endTime = System.currentTimeMillis();
            if (ret >= 1) {

                rt = endTime - beginTime;
                if (rt > max_rt[3])
                    max_rt[3] = rt;
                RtHist.histInc(3, rt);
                if (TpccBench.counting_on) {
                    if (rt < RTIME_DELIVERY) {
                        success[3]++;
                        success2[3][t_num]++;
                    } else {
                        late[3]++;
                        late2[3][t_num]++;
                    }
                }

                return (1); /* end */
            } else {

                if (TpccBench.counting_on) {
                    retry[3]++;
                    retry2[3][t_num]++;
                }

            }
        }

        if (TpccBench.counting_on) {
            retry[3]--;
            retry2[3][t_num]--;
            failure[3]++;
            failure2[3][t_num]++;
        }

        return (0);
    }

    /*
      * prepare data and execute the stock level transaction
      */
    private int doSlev(int t_num) {
        int c_num = 0;
        int i = 0;
        int ret = 0;
        double rt = 0.0;
        long beginTime = 0;
        long endTime = 0;
        int w_id = 0;
        int d_id = 0;
        int level = 0;

        if (num_node == 0) {
            w_id = Util.randomNumber(1, num_ware);
        } else {
            c_num = ((num_node * t_num) / num_conn); /* drop moduls */
            w_id = Util.randomNumber(1 + (num_ware * c_num) / num_node,
                    (num_ware * (c_num + 1)) / num_node);
        }
        d_id = Util.randomNumber(1, DIST_PER_WARE);
        level = Util.randomNumber(10, 20);

        // clk1 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf1 );
        beginTime = System.currentTimeMillis();
        for (i = 0; i < MAX_RETRY; i++) {
            ret = slev.slev(t_num, w_id, d_id, level);
            // clk2 = clock_gettime(CLOCK_THREAD_CPUTIME_ID, &tbuf2 );
            endTime = System.currentTimeMillis();

            if (ret >= 1) {

                rt = endTime - beginTime;
                if (rt > max_rt[4])
                    max_rt[4] = rt;
                RtHist.histInc(4, rt);
                if (TpccBench.counting_on) {
                    if (rt < RTIME_SLEV) {
                        success[4]++;
                        success2[4][t_num]++;
                    } else {
                        late[4]++;
                        late2[4][t_num]++;
                    }
                }

                return (1); /* end */
            } else {

                if (TpccBench.counting_on) {
                    retry[4]++;
                    retry2[4][t_num]++;
                }

            }
        }

        if (TpccBench.counting_on) {
            retry[4]--;
            retry2[4][t_num]--;
            failure[4]++;
            failure2[4][t_num]++;
        }

        return (0);
    }
}
