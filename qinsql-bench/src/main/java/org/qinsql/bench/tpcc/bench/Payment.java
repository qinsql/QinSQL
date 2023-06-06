/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;

public class Payment implements TpccConstants {
    private static final Logger logger = LoggerFactory.getLogger(Driver.class);
    private static final boolean DEBUG = logger.isDebugEnabled();
    private static final boolean TRACE = logger.isTraceEnabled();

    private TpccStatements pStmts;

    public Payment(TpccStatements pStmts) {
        this.pStmts = pStmts;
    }

    @SuppressWarnings("unused")
    public int payment(int t_num, int w_id_arg, /* warehouse id */
            int d_id_arg, /* district id */
            int byname, /* select by c_id or c_last? */
            int c_w_id_arg, int c_d_id_arg, int c_id_arg, /* customer id */
            String c_last_arg, /* customer last name */
            float h_amount_arg /* payment amount */
    ) {
        try {
            // Start a transaction.
            pStmts.setAutoCommit(false);
            if (DEBUG)
                logger.debug("Transaction:	PAYMENT");
            int w_id = w_id_arg;
            int d_id = d_id_arg;
            int c_id = c_id_arg;
            String w_name = null;
            String w_street_1 = null;
            String w_street_2 = null;
            String w_city = null;
            String w_state = null;
            String w_zip = null;

            int c_d_id = c_d_id_arg;
            int c_w_id = c_w_id_arg;
            String c_first = null;
            String c_middle = null;
            String c_last = null;
            String c_street_1 = null;
            String c_street_2 = null;
            String c_city = null;
            String c_state = null;
            String c_zip = null;
            String c_phone = null;
            String c_since = null;
            String c_credit = null;

            int c_credit_lim = 0;
            float c_discount = 0;
            float c_balance = 0;
            String c_data = null;
            String c_new_data = null;

            float h_amount = h_amount_arg;
            String h_data = null;
            String d_name = null;
            String d_street_1 = null;
            String d_street_2 = null;
            String d_city = null;
            String d_state = null;
            String d_zip = null;

            int namecnt = 0;
            int n;
            int proceed = 0;

            // Time Stamp
            final Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

            proceed = 1;

            // Get prepared statement
            // "UPDATE warehouse SET w_ytd = w_ytd + ? WHERE w_id = ?"
            try {

                pStmts.getStatement(9).setFloat(1, h_amount);
                pStmts.getStatement(9).setInt(2, w_id);
                if (TRACE)
                    logger.trace("UPDATE warehouse SET w_ytd = w_ytd + " + h_amount + " WHERE w_id = "
                            + w_id);
                pStmts.getStatement(9).executeUpdate();

            } catch (SQLException e) {
                logger.error(
                        "UPDATE warehouse SET w_ytd = w_ytd + " + h_amount + " WHERE w_id = " + w_id, e);
                throw new Exception("Payment Update transaction error", e);
            }

            proceed = 2;
            // Get prepared statement
            // "SELECT w_street_1, w_street_2, w_city, w_state, w_zip, w_name FROM warehouse WHERE w_id = ?"

            try {
                pStmts.getStatement(10).setInt(1, w_id);
                if (TRACE)
                    logger.trace(
                            "SELECT w_street_1, w_street_2, w_city, w_state, w_zip, w_name FROM warehouse WHERE w_id = "
                                    + w_id);
                ResultSet rs = pStmts.getStatement(10).executeQuery();
                if (rs.next()) {
                    w_street_1 = rs.getString(1);
                    w_street_2 = rs.getString(2);
                    w_city = rs.getString(3);
                    w_state = rs.getString(4);
                    w_zip = rs.getString(5);
                    w_name = rs.getString(6);
                }

                rs.close();

            } catch (SQLException e) {
                logger.error(
                        "SELECT w_street_1, w_street_2, w_city, w_state, w_zip, w_name FROM warehouse WHERE w_id = "
                                + w_id,
                        e);
                throw new Exception("Payment select transaction error", e);
            }

            proceed = 3;
            // Get prepared statement
            // "UPDATE district SET d_ytd = d_ytd + ? WHERE d_w_id = ? AND d_id = ?"
            try {
                pStmts.getStatement(11).setFloat(1, h_amount);
                pStmts.getStatement(11).setInt(2, w_id);
                pStmts.getStatement(11).setInt(3, d_id);
                if (TRACE)
                    logger.trace("UPDATE district SET d_ytd = d_ytd + " + h_amount + " WHERE d_w_id = "
                            + w_id + " AND d_id = " + d_id);
                pStmts.getStatement(11).executeUpdate();

            } catch (SQLException e) {
                logger.error("UPDATE district SET d_ytd = d_ytd + " + h_amount + " WHERE d_w_id = "
                        + w_id + " AND d_id = " + d_id, e);
                throw new Exception("Payment update transaction error", e);
            }

            proceed = 4;
            // Get prepared statement
            // "SELECT d_street_1, d_street_2, d_city, d_state, d_zip, d_name FROM district WHERE d_w_id = ? AND d_id =
            // ?"

            try {
                pStmts.getStatement(12).setInt(1, w_id);
                pStmts.getStatement(12).setInt(2, d_id);
                if (TRACE)
                    logger.trace(
                            "SELECT d_street_1, d_street_2, d_city, d_state, d_zip, d_name FROM district WHERE d_w_id = "
                                    + w_id + " AND d_id = " + d_id);
                ResultSet rs = pStmts.getStatement(12).executeQuery();
                if (rs.next()) {
                    d_street_1 = rs.getString(1);
                    d_street_2 = rs.getString(2);
                    d_city = rs.getString(3);
                    d_state = rs.getString(4);
                    d_zip = rs.getString(5);
                    d_name = rs.getString(6);
                }

                rs.close();
            } catch (SQLException e) {
                logger.error(
                        "SELECT d_street_1, d_street_2, d_city, d_state, d_zip, d_name FROM district WHERE d_w_id = "
                                + w_id + " AND d_id = " + d_id,
                        e);
                throw new Exception("Payment select transaction error", e);
            }

            if (byname >= 1) {

                c_last = c_last_arg;

                proceed = 5;
                // Get prepared statement
                // "SELECT count(c_id) FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_last = ?"

                try {
                    pStmts.getStatement(13).setInt(1, c_w_id);
                    pStmts.getStatement(13).setInt(2, c_d_id);
                    pStmts.getStatement(13).setString(3, c_last);
                    if (TRACE)
                        logger.trace("SELECT count(c_id) FROM customer WHERE c_w_id = " + c_w_id
                                + " AND c_d_id = " + c_d_id + " AND c_last = " + c_last);
                    ResultSet rs = pStmts.getStatement(13).executeQuery();
                    if (rs.next()) {
                        namecnt = rs.getInt(1);
                    }

                    rs.close();
                } catch (SQLException e) {
                    logger.error("SELECT count(c_id) FROM customer WHERE c_w_id = " + c_w_id
                            + " AND c_d_id = " + c_d_id + " AND c_last = " + c_last, e);
                    throw new Exception("Payment select transaction error", e);
                }

                // Get prepared Transaction
                // "SELECT c_id FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_last = ? ORDER BY c_first"

                try {
                    pStmts.getStatement(14).setInt(1, c_w_id);
                    pStmts.getStatement(14).setInt(2, c_d_id);
                    pStmts.getStatement(14).setString(3, c_last);
                    if (TRACE)
                        logger.trace(
                                "SELECT c_id FROM customer WHERE c_w_id = " + c_w_id + " AND c_d_id = "
                                        + c_d_id + " AND c_last = " + c_last + " ORDER BY c_first");

                    if (namecnt % 2 == 1) {
                        namecnt++; /* Locate midpoint customer; */
                    }

                    ResultSet rs = pStmts.getStatement(14).executeQuery();
                    for (n = 0; n < namecnt / 2; n++) {
                        if (rs.next()) {
                            // SUCCESS
                            c_id = rs.getInt(1);
                        } else {
                            throw new IllegalStateException();
                        }
                    }
                    rs.close();

                } catch (SQLException e) {
                    logger.error("SELECT c_id FROM customer WHERE c_w_id = " + c_w_id + " AND c_d_id = "
                            + c_d_id + " AND c_last = " + c_last + " ORDER BY c_first", e);
                    throw new Exception("Payment select transaction error", e);
                }

            }

            proceed = 6;

            // Get the prepared statement
            // "SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city, c_state, c_zip, c_phone, c_credit,
            // c_credit_lim, c_discount, c_balance, c_since FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?
            // FOR UPDATE"
            try {
                pStmts.getStatement(15).setInt(1, c_w_id);
                pStmts.getStatement(15).setInt(2, c_d_id);
                pStmts.getStatement(15).setInt(3, c_id);
                if (TRACE)
                    logger.trace(
                            "SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city, c_state, c_zip, c_phone, c_credit, c_credit_lim, c_discount, c_balance, c_since FROM customer "
                                    + "WHERE c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id
                                    + " AND c_id = " + c_id + " FOR UPDATE");
                ResultSet rs = pStmts.getStatement(15).executeQuery();
                if (rs.next()) {
                    c_first = rs.getString(1);
                    c_middle = rs.getString(2);
                    c_last = rs.getString(3);
                    c_street_1 = rs.getString(4);
                    c_street_2 = rs.getString(5);
                    c_city = rs.getString(6);
                    c_state = rs.getString(7);
                    c_zip = rs.getString(8);
                    c_phone = rs.getString(9);
                    c_credit = rs.getString(10);
                    c_credit_lim = rs.getInt(11);
                    c_discount = rs.getFloat(12);
                    c_balance = rs.getFloat(13);
                    c_since = rs.getString(14);
                }

                rs.close();

            } catch (SQLException e) {
                logger.error(
                        "SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city, c_state, c_zip, c_phone, c_credit, c_credit_lim, c_discount, c_balance, c_since FROM customer "
                                + "WHERE c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id + " AND c_id = "
                                + c_id + " FOR UPDATE",
                        e);
                throw new Exception("Payment select transaction error", e);
            }

            c_balance += h_amount;

            if (c_credit != null) {
                if (c_credit.contains("BC")) {
                    proceed = 7;
                    // Get Prepared Statement
                    // "SELECT c_data FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?"
                    try {
                        pStmts.getStatement(16).setInt(1, c_w_id);
                        pStmts.getStatement(16).setInt(2, c_d_id);
                        pStmts.getStatement(16).setInt(3, c_id);
                        if (TRACE)
                            logger.trace("SELECT c_data FROM customer WHERE c_w_id = " + c_w_id
                                    + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id);
                        ResultSet rs = pStmts.getStatement(16).executeQuery();
                        if (rs.next()) {
                            c_data = rs.getString(1);
                        }

                        rs.close();
                    } catch (SQLException e) {
                        logger.error("SELECT c_data FROM customer WHERE c_w_id = " + c_w_id
                                + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id, e);
                        throw new Exception("Payment select transaction error", e);
                    }

                    // TODO: c_new_data is never used - this is a bug ported exactly from the original code
                    c_new_data = String.format("| %d %d %d %d %d $%f %s %s", c_id, c_d_id, c_w_id, d_id,
                            w_id, h_amount, currentTimeStamp.toString(), c_data);

                    // TODO: fix this - causes index out of bounds exceptions
                    // c_new_data = ( c_new_data + c_data.substring(0, (500 - c_new_data.length()) ) );

                    proceed = 8;
                    // Get prepared statement
                    // "UPDATE customer SET c_balance = ?, c_data = ? WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?"
                    try {
                        // System.out.print("Executed UPDATE.\n");
                        pStmts.getStatement(17).setFloat(1, c_balance);
                        pStmts.getStatement(17).setString(2, c_data);
                        pStmts.getStatement(17).setInt(3, c_w_id);
                        pStmts.getStatement(17).setInt(4, c_d_id);
                        pStmts.getStatement(17).setInt(5, c_id);
                        if (TRACE)
                            logger.trace("UPDATE customer SET c_balance = " + c_balance + ", c_data = "
                                    + c_data + " WHERE c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id
                                    + " AND c_id = " + c_id);
                        pStmts.getStatement(17).executeUpdate();

                    } catch (SQLException e) {
                        logger.error("UPDATE customer SET c_balance = " + c_balance + ", c_data = "
                                + c_data + " WHERE c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id
                                + " AND c_id = " + c_id, e);
                        throw new Exception("Payment update transaction error", e);
                    }

                } else {
                    proceed = 9;
                    // Get prepared statement
                    // "UPDATE customer SET c_balance = ? WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?"

                    try {
                        pStmts.getStatement(18).setFloat(1, c_balance);
                        pStmts.getStatement(18).setInt(2, c_w_id);
                        pStmts.getStatement(18).setInt(3, c_d_id);
                        pStmts.getStatement(18).setInt(4, c_id);
                        if (TRACE)
                            logger.trace("UPDATE customer SET c_balance = " + c_balance
                                    + " WHERE c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id
                                    + " AND c_id = " + c_id);
                        pStmts.getStatement(18).executeUpdate();

                    } catch (SQLException e) {
                        logger.error("UPDATE customer SET c_balance = " + c_balance + " WHERE c_w_id = "
                                + c_w_id + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id, e);
                        throw new Exception("Payment update transaction error", e);
                    }

                }
            } else {
                proceed = 9;
                // Get prepared statement
                // "UPDATE customer SET c_balance = ? WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?"

                try {
                    pStmts.getStatement(18).setFloat(1, c_balance);
                    pStmts.getStatement(18).setInt(2, c_w_id);
                    pStmts.getStatement(18).setInt(3, c_d_id);
                    pStmts.getStatement(18).setInt(4, c_id);
                    if (TRACE)
                        logger.trace("UPDATE customer SET c_balance = " + c_balance + " WHERE c_w_id = "
                                + c_w_id + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id);
                    pStmts.getStatement(18).executeUpdate();

                } catch (SQLException e) {
                    logger.error("UPDATE customer SET c_balance = " + c_balance + " WHERE c_w_id = "
                            + c_w_id + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id, e);
                    throw new Exception("Payment update transaction error", e);
                }

            }

            // PostgreSQL不支持ERROR: invalid byte sequence for encoding "UTF8": 0x00
            // h_data = h_data + '\0' + d_name + ' ' + ' ' + ' ' + ' ' + '\0';
            h_data = h_data + '|' + d_name + ' ' + ' ' + ' ' + ' ' + '|';

            proceed = 10;
            // Get prepared statement
            // "INSERT INTO history(h_c_d_id, h_c_w_id, h_c_id, h_d_id, h_w_id, h_date, h_amount, h_data) VALUES(?, ?,
            // ?, ?, ?, ?, ?, ?)"
            try {
                pStmts.getStatement(19).setInt(1, c_d_id);
                pStmts.getStatement(19).setInt(2, c_w_id);
                pStmts.getStatement(19).setInt(3, c_id);
                pStmts.getStatement(19).setInt(4, d_id);
                pStmts.getStatement(19).setInt(5, w_id);
                // pStmts.getStatement(19).setString(6, currentTimeStamp.toString());
                pStmts.getStatement(19).setTimestamp(6, currentTimeStamp);
                pStmts.getStatement(19).setFloat(7, h_amount);
                pStmts.getStatement(19).setString(8, h_data);
                if (TRACE)
                    logger.trace(
                            "INSERT INTO history(h_c_d_id, h_c_w_id, h_c_id, h_d_id, h_w_id, h_date, h_amount, h_data)"
                                    + " VALUES( " + c_d_id + "," + c_w_id + "," + c_id + "," + d_id + ","
                                    + w_id + "," + currentTimeStamp.toString() + "," + h_amount
                                    + "," /*+ h_data*/);
                pStmts.getStatement(19).executeUpdate();

            } catch (SQLException e) {
                logger.error(
                        "INSERT INTO history(h_c_d_id, h_c_w_id, h_c_id, h_d_id, h_w_id, h_date, h_amount, h_data)"
                                + " VALUES( " + c_d_id + "," + c_w_id + "," + c_id + "," + d_id + ","
                                + w_id + "," + currentTimeStamp.toString() + "," + h_amount
                                + "," /*+ h_data*/,
                        e);
                throw new Exception("Payment insert transaction error", e);
            }

            // Commit.
            pStmts.commit();

            return 1;

        } catch (Exception e) {
            try {
                // Rollback if an aborted transaction, they are intentional in some percentage of cases.
                pStmts.rollback();
                return 0;
            } catch (Throwable th) {
                throw new RuntimeException("Payment error", th);
            } finally {
                logger.error("Payment error", e);
            }
        }
    }
}
