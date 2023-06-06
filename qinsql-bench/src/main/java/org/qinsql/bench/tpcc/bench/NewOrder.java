/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;

public class NewOrder implements TpccConstants {

    private static final Logger logger = LoggerFactory.getLogger(Driver.class);
    private static final boolean DEBUG = logger.isDebugEnabled();
    private static final boolean TRACE = logger.isTraceEnabled();
    private TpccStatements pStmts;

    private String s_dist_01 = null;
    private String s_dist_02 = null;
    private String s_dist_03 = null;
    private String s_dist_04 = null;
    private String s_dist_05 = null;
    private String s_dist_06 = null;
    private String s_dist_07 = null;
    private String s_dist_08 = null;
    private String s_dist_09 = null;
    private String s_dist_10 = null;

    String[] iname = new String[MAX_NUM_ITEMS];
    String[] bg = new String[MAX_NUM_ITEMS];
    float[] amt = new float[MAX_NUM_ITEMS];
    float[] price = new float[MAX_NUM_ITEMS];
    int[] stock = new int[MAX_NUM_ITEMS];
    int[] ol_num_seq = new int[MAX_NUM_ITEMS];
    boolean joins;

    /**
     * Constructor.
     *
     * @param pStmts
     */
    public NewOrder(TpccStatements pStmts, boolean joins) {
        this.pStmts = pStmts;
        this.joins = joins;
    }

    private String pickDistInfo(String ol_dist_info, int ol_supply_w_id) {
        switch (ol_supply_w_id) {
        case 1:
            ol_dist_info = s_dist_01;
            break;
        case 2:
            ol_dist_info = s_dist_02;
            break;
        case 3:
            ol_dist_info = s_dist_03;
            break;
        case 4:
            ol_dist_info = s_dist_04;
            break;
        case 5:
            ol_dist_info = s_dist_05;
            break;
        case 6:
            ol_dist_info = s_dist_06;
            break;
        case 7:
            ol_dist_info = s_dist_07;
            break;
        case 8:
            ol_dist_info = s_dist_08;
            break;
        case 9:
            ol_dist_info = s_dist_09;
            break;
        case 10:
            ol_dist_info = s_dist_10;
            break;
        }

        return ol_dist_info;
    }

    @SuppressWarnings("unused")
    public int neword(int t_num, /* thread number (not used) */
            int w_id_arg, /* warehouse id */
            int d_id_arg, /* district id */
            int c_id_arg, /* customer id */
            int o_ol_cnt_arg, /* number of items */
            int o_all_local_arg, /* are all order lines local */
            int itemid[], /* ids of items to be ordered */
            int supware[], /* warehouses supplying items */
            int qty[]) throws SQLException {

        try {

            // Start a transaction.
            pStmts.setAutoCommit(false);
            if (DEBUG)
                logger.debug("Transaction:	New Order");
            int w_id = w_id_arg;
            int d_id = d_id_arg;
            int c_id = c_id_arg;
            int o_ol_cnt = o_ol_cnt_arg;
            int o_all_local = o_all_local_arg;
            float c_discount = 0;
            String c_last = null;
            String c_credit = null;
            float w_tax = 0;
            int d_next_o_id = 0;
            float d_tax = 0;
            int o_id = 0;
            String i_name = null;
            float i_price = 0;
            String i_data = null;
            int ol_i_id = 0;
            int s_quantity = 0;
            String s_data = null;

            String ol_dist_info = null;
            int ol_supply_w_id = 0;
            float ol_amount = 0;
            int ol_number = 0;
            int ol_quantity = 0;

            int min_num = 0;
            int i = 0, j = 0, tmp = 0, swp = 0;

            // struct timespec tbuf1,tbuf_start;
            // clock_t clk1,clk_start;

            // Timestamp
            java.sql.Timestamp time = new Timestamp(System.currentTimeMillis());
            // String currentTimeStamp = "'" + time.toString() + "'";
            String currentTimeStamp = time.toString();

            // Changing how this works if joins = false
            if (joins) {
                // Get prepared statement
                // "SELECT c_discount, c_last, c_credit, w_tax FROM customer, warehouse WHERE w_id = ? AND c_w_id = w_id
                // AND c_d_id = ? AND c_id = ?"
                logger.debug("joins = true");
                try {
                    int column = 1;
                    final PreparedStatement pstmt0 = pStmts.getStatement(0);
                    pstmt0.setInt(column++, w_id);
                    pstmt0.setInt(column++, w_id);
                    pstmt0.setInt(column++, d_id);
                    pstmt0.setInt(column++, c_id);
                    if (TRACE)
                        logger.trace(
                                "SELECT c_discount, c_last, c_credit, w_tax FROM customer, warehouse WHERE w_id = "
                                        + w_id + " AND c_w_id = " + w_id + " AND c_d_id = " + d_id
                                        + " AND c_id = " + c_id);
                    try (ResultSet rs = pstmt0.executeQuery()) {
                        if (rs.next()) {
                            c_discount = rs.getFloat(1);
                            c_last = rs.getString(2);
                            c_credit = rs.getString(3);
                            w_tax = rs.getFloat(4);
                        }
                    }
                } catch (SQLException e) {
                    logger.error(
                            "SELECT c_discount, c_last, c_credit, w_tax FROM customer, warehouse WHERE w_id = "
                                    + w_id + " AND c_w_id = " + w_id + " AND c_d_id = " + d_id
                                    + " AND c_id = " + c_id,
                            e);
                    throw new Exception("NewOrder select transaction error", e);
                }
            } else {
                logger.debug("joins = false");
                // Running 2 seperate queries here
                try {
                    int column = 1;
                    // SELECT c_discount, c_last, c_credit FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?
                    final PreparedStatement pstmt35 = pStmts.getStatement(35);
                    pstmt35.setInt(column++, w_id);
                    pstmt35.setInt(column++, d_id);
                    pstmt35.setInt(column++, c_id);

                    // SELECT w_tax FROM warehouse WHERE w_id = ?
                    final PreparedStatement pstmt36 = pStmts.getStatement(36);
                    pstmt36.setInt(1, w_id);

                    if (TRACE)
                        logger.trace("SELECT c_discount, c_last, c_credit FROM customer WHERE c_w_id = "
                                + w_id + " AND c_d_id = " + d_id + " AND c_id = " + c_id);
                    if (TRACE)
                        logger.trace("SELECT w_tax FROM warehouse WHERE w_id = " + w_id);
                    try (ResultSet rs0 = pstmt35.executeQuery()) {
                        if (rs0.next()) {
                            c_discount = rs0.getFloat(1);
                            c_last = rs0.getString(2);
                            c_credit = rs0.getString(3);
                        }
                    }
                    try (ResultSet rs1 = pstmt36.executeQuery()) {
                        if (rs1.next()) {
                            w_tax = rs1.getFloat(1);
                        }
                    }
                } catch (SQLException e) {
                    logger.error("SELECT c_discount, c_last, c_credit FROM customer WHERE c_w_id = "
                            + w_id + " AND c_d_id = " + d_id + " AND c_id = " + c_id, e);
                    throw new Exception("NewOrder (join = false) select transaction error", e);
                }
            }
            // Get prepared statement
            // "SELECT d_next_o_id, d_tax FROM district WHERE d_id = ? AND d_w_id = ? FOR UPDATE"

            try {
                final PreparedStatement pstmt1 = pStmts.getStatement(1);
                pstmt1.setInt(1, d_id);
                pstmt1.setInt(2, w_id);
                if (TRACE)
                    logger.trace("SELECT d_next_o_id, d_tax FROM district WHERE d_id = " + d_id
                            + "  AND d_w_id = " + w_id + " FOR UPDATE");
                try (ResultSet rs = pstmt1.executeQuery()) {
                    if (rs.next()) {
                        d_next_o_id = rs.getInt(1);
                        d_tax = rs.getFloat(2);
                    } else {
                        logger.error("Failed to obtain d_next_o_id. No results to query: "
                                + "SELECT d_next_o_id, d_tax FROM district WHERE d_id = " + d_id
                                + "  AND d_w_id = " + w_id + " FOR UPDATE");
                    }
                }
            } catch (SQLException e) {
                logger.error("SELECT d_next_o_id, d_tax FROM district WHERE d_id = " + d_id
                        + "  AND d_w_id = " + w_id + " FOR UPDATE", e);
                throw new Exception("Neworder select transaction error", e);
            }

            // Get prepared statement
            // "UPDATE district SET d_next_o_id = ? + 1 WHERE d_id = ? AND d_w_id = ?"

            try {
                final PreparedStatement pstmt2 = pStmts.getStatement(2);
                pstmt2.setInt(1, d_next_o_id);
                pstmt2.setInt(2, d_id);
                pstmt2.setInt(3, w_id);
                if (TRACE)
                    logger.trace("UPDATE district SET d_next_o_id = " + d_next_o_id
                            + " + 1 WHERE d_id = " + d_id + " AND d_w_id = " + w_id);
                pstmt2.executeUpdate();

            } catch (SQLException e) {
                logger.error("UPDATE district SET d_next_o_id = " + d_next_o_id + " + 1 WHERE d_id = "
                        + d_id + " AND d_w_id = " + w_id, e);
                throw new Exception("NewOrder update transaction error", e);
            }

            o_id = d_next_o_id;

            // Get prepared statement
            // "INSERT INTO orders (o_id, o_d_id, o_w_id, o_c_id, o_entry_d, o_ol_cnt, o_all_local) VALUES(?, ?, ?, ?,
            // ?, ?, ?)"

            try {
                final PreparedStatement pstmt3 = pStmts.getStatement(3);
                pstmt3.setInt(1, o_id);
                pstmt3.setInt(2, d_id);
                pstmt3.setInt(3, w_id);
                pstmt3.setInt(4, c_id);
                // pstmt3.setString(5, currentTimeStamp);
                pstmt3.setTimestamp(5, time);
                pstmt3.setInt(6, o_ol_cnt);
                pstmt3.setInt(7, o_all_local);
                if (TRACE)
                    logger.trace(
                            "INSERT INTO orders (o_id, o_d_id, o_w_id, o_c_id, o_entry_d, o_ol_cnt, o_all_local) "
                                    + "VALUES(" + o_id + "," + d_id + "," + w_id + "," + c_id + ","
                                    + currentTimeStamp + "," + o_ol_cnt + "," + o_all_local + ")");
                pstmt3.executeUpdate();

            } catch (SQLException e) {
                logger.error(
                        "INSERT INTO orders (o_id, o_d_id, o_w_id, o_c_id, o_entry_d, o_ol_cnt, o_all_local) "
                                + "VALUES(" + o_id + "," + d_id + "," + w_id + "," + c_id + ","
                                + currentTimeStamp + "," + o_ol_cnt + "," + o_all_local + ")",
                        e);
                throw new Exception("NewOrder insert transaction error", e);
            }

            // Get prepared statement
            // "INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (?,?,?)
            try {
                final PreparedStatement pstmt4 = pStmts.getStatement(4);
                pstmt4.setInt(1, o_id);
                pstmt4.setInt(2, d_id);
                pstmt4.setInt(3, w_id);
                if (TRACE)
                    logger.trace("INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (" + o_id
                            + "," + d_id + "," + w_id + ")");
                pstmt4.executeUpdate();

            } catch (SQLException e) {
                logger.error("INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (" + o_id + ","
                        + d_id + "," + w_id + ")", e);
                throw new Exception("NewOrder insert transaction error", e);
            }

            /* sort orders to avoid DeadLock */
            for (i = 0; i < o_ol_cnt; i++) {
                ol_num_seq[i] = i;
            }

            for (i = 0; i < (o_ol_cnt - 1); i++) {
                tmp = (MAXITEMS + 1) * supware[ol_num_seq[i]] + itemid[ol_num_seq[i]];
                min_num = i;
                for (j = i + 1; j < o_ol_cnt; j++) {
                    if ((MAXITEMS + 1) * supware[ol_num_seq[j]] + itemid[ol_num_seq[j]] < tmp) {
                        tmp = (MAXITEMS + 1) * supware[ol_num_seq[j]] + itemid[ol_num_seq[j]];
                        min_num = j;
                    }
                }
                if (min_num != i) {
                    swp = ol_num_seq[min_num];
                    ol_num_seq[min_num] = ol_num_seq[i];
                    ol_num_seq[i] = swp;
                }
            }

            for (ol_number = 1; ol_number <= o_ol_cnt; ol_number++) {
                ol_supply_w_id = supware[ol_num_seq[ol_number - 1]];
                ol_i_id = itemid[ol_num_seq[ol_number - 1]];
                ol_quantity = qty[ol_num_seq[ol_number - 1]];

                /* EXEC SQL WHENEVER NOT FOUND GOTO invaliditem; */
                // Get prepared statement
                // "SELECT i_price, i_name, i_data FROM item WHERE i_id = ?"

                try {
                    final PreparedStatement pstmt5 = pStmts.getStatement(5);
                    pstmt5.setInt(1, ol_i_id);
                    if (TRACE)
                        logger.trace("SELECT i_price, i_name, i_data FROM item WHERE i_id =" + ol_i_id);
                    try (ResultSet rs = pstmt5.executeQuery()) {
                        if (rs.next()) {
                            i_price = rs.getFloat(1);
                            i_name = rs.getString(2);
                            i_data = rs.getString(3);
                        } else {
                            if (DEBUG) {
                                logger.debug("No item found for item id " + ol_i_id);
                            }
                            throw new AbortedTransactionException();
                        }
                    }
                } catch (SQLException e) {
                    logger.error("SELECT i_price, i_name, i_data FROM item WHERE i_id =" + ol_i_id, e);
                    throw new Exception("NewOrder select transaction error", e);
                }

                price[ol_num_seq[ol_number - 1]] = i_price;
                iname[ol_num_seq[ol_number - 1]] = i_name;

                // Get prepared statement
                // "SELECT s_quantity, s_data, s_dist_01, s_dist_02, s_dist_03, s_dist_04, s_dist_05, s_dist_06,
                // s_dist_07, s_dist_08, s_dist_09, s_dist_10 FROM stock WHERE s_i_id = ? AND s_w_id = ? FOR UPDATE"
                try {
                    final PreparedStatement pstmt6 = pStmts.getStatement(6);
                    pstmt6.setInt(1, ol_i_id);
                    pstmt6.setInt(2, ol_supply_w_id);
                    if (TRACE)
                        logger.trace(
                                "SELECT s_quantity, s_data, s_dist_01, s_dist_02, s_dist_03, s_dist_04, s_dist_05, s_dist_06, s_dist_07, s_dist_08, s_dist_09, s_dist_10 FROM "
                                        + "stock WHERE s_i_id = " + ol_i_id + " AND s_w_id = "
                                        + ol_supply_w_id + " FOR UPDATE");

                    try (ResultSet rs = pstmt6.executeQuery()) {
                        if (rs.next()) {
                            s_quantity = rs.getInt(1);
                            s_data = rs.getString(2);
                            s_dist_01 = rs.getString(3);
                            s_dist_02 = rs.getString(4);
                            s_dist_03 = rs.getString(5);
                            s_dist_04 = rs.getString(6);
                            s_dist_05 = rs.getString(7);
                            s_dist_06 = rs.getString(8);
                            s_dist_07 = rs.getString(9);
                            s_dist_08 = rs.getString(10);
                            s_dist_09 = rs.getString(11);
                            s_dist_10 = rs.getString(12);
                        }
                    }

                } catch (SQLException e) {
                    logger.error(
                            "SELECT s_quantity, s_data, s_dist_01, s_dist_02, s_dist_03, s_dist_04, s_dist_05, s_dist_06, s_dist_07, s_dist_08, s_dist_09, s_dist_10 FROM "
                                    + "stock WHERE s_i_id = " + ol_i_id + " AND s_w_id = "
                                    + ol_supply_w_id + " FOR UPDATE",
                            e);
                    throw new Exception("NewOrder select transaction error", e);
                }

                ol_dist_info = pickDistInfo(ol_dist_info, d_id); /* pick correct * s_dist_xx */

                stock[ol_num_seq[ol_number - 1]] = s_quantity;

                if ((i_data.contains("original")) && (s_data.contains("original"))) {
                    bg[ol_num_seq[ol_number - 1]] = "B";

                } else {
                    bg[ol_num_seq[ol_number - 1]] = "G";

                }

                if (s_quantity > ol_quantity) {
                    s_quantity = s_quantity - ol_quantity;
                } else {
                    s_quantity = s_quantity - ol_quantity + 91;
                }

                // Get the prepared statement
                // "UPDATE stock SET s_quantity = ? WHERE s_i_id = ? AND s_w_id = ?"
                try {
                    final PreparedStatement pstmt7 = pStmts.getStatement(7);
                    pstmt7.setInt(1, s_quantity);
                    pstmt7.setInt(2, ol_i_id);
                    pstmt7.setInt(3, ol_supply_w_id);
                    if (TRACE)
                        logger.trace("UPDATE stock SET s_quantity = " + s_quantity + " WHERE s_i_id = "
                                + ol_i_id + " AND s_w_id = " + ol_supply_w_id);
                    pstmt7.executeUpdate();

                } catch (SQLException e) {
                    logger.error("UPDATE stock SET s_quantity = " + s_quantity + " WHERE s_i_id = "
                            + ol_i_id + " AND s_w_id = " + ol_supply_w_id, e);
                    throw new Exception("NewOrder update transaction error", e);
                }

                ol_amount = ol_quantity * i_price * (1 + w_tax + d_tax) * (1 - c_discount);
                amt[ol_num_seq[ol_number - 1]] = ol_amount;

                // Get prepared statement
                // "INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id, ol_number, ol_i_id, ol_supply_w_id, ol_quantity,
                // ol_amount, ol_dist_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

                try {
                    final PreparedStatement pstmt8 = pStmts.getStatement(8);
                    pstmt8.setInt(1, o_id);
                    pstmt8.setInt(2, d_id);
                    pstmt8.setInt(3, w_id);
                    pstmt8.setInt(4, ol_number);
                    pstmt8.setInt(5, ol_i_id);
                    pstmt8.setInt(6, ol_supply_w_id);
                    pstmt8.setInt(7, ol_quantity);
                    pstmt8.setFloat(8, ol_amount);
                    pstmt8.setString(9, ol_dist_info);
                    if (TRACE)
                        logger.trace(
                                "INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id, ol_number, ol_i_id, ol_supply_w_id, ol_quantity, ol_amount, ol_dist_info) "
                                        + "VALUES (" + o_id + "," + d_id + "," + w_id + "," + ol_number
                                        + "," + ol_i_id + "," + ol_supply_w_id + "," + ol_quantity + ","
                                        + ol_amount + "," + ol_dist_info + ")");
                    pstmt8.executeUpdate();

                } catch (SQLException e) {
                    logger.error(
                            "INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id, ol_number, ol_i_id, ol_supply_w_id, ol_quantity, ol_amount, ol_dist_info) "
                                    + "VALUES (" + o_id + "," + d_id + "," + w_id + "," + ol_number + ","
                                    + ol_i_id + "," + ol_supply_w_id + "," + ol_quantity + ","
                                    + ol_amount + "," + ol_dist_info + ")",
                            e);
                    throw new Exception("NewOrder insert transaction error", e);
                }

            }
            // Commit.
            pStmts.commit();

            return 1;
        } catch (AbortedTransactionException ate) {
            // Rollback if an aborted transaction, they are intentional in some percentage of cases.
            if (logger.isDebugEnabled()) {
                logger.debug("Caught AbortedTransactionException");
            }
            pStmts.rollback();
            return 1; // this is not an error!
        } catch (Exception e) {
            logger.error("New Order error", e);
            pStmts.rollback();
            return 0;
        }
    }
}
