/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.config;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.qinsql.bench.tpcc.bench.TpccBench;

public interface TpccConstants {
    /*
     * correct values
     */
    public static int TRANSACTION_COUNT = 5;
    public static int MAXITEMS = 100000;
    public static int CUST_PER_DIST = 3000;
    public static int DIST_PER_WARE = 10;
    public static int ORD_PER_DIST = 3000;

    public static int[] nums = new int[CUST_PER_DIST];

    /* definitions for new order transaction */
    public static int MAX_NUM_ITEMS = 15;
    public static int MAX_ITEM_LEN = 24;

    public static final Logger logger = LoggerFactory.getLogger(TpccBench.class);
    public static final boolean DEBUG = logger.isDebugEnabled();
    public static final boolean TRACE = logger.isTraceEnabled();
}
