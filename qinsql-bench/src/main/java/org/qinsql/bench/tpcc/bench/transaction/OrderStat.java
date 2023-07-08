/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench.transaction;

import java.sql.ResultSet;

import org.qinsql.bench.tpcc.config.TpccConstants;

public class OrderStat implements TpccConstants {

    private TpccStatements pStmts;

    public OrderStat(TpccStatements pStmts) {
        this.pStmts = pStmts;
    }

    @SuppressWarnings("unused")
    public int ordStat(int w_id, /* warehouse id */
            int d_id, /* district id */
            int byname, /* select by c_id or c_last? */
            int c_id, /* customer id */
            String c_last_arg /* customer last name, format? */
    ) {

        try {

            pStmts.setAutoCommit(false);
            if (DEBUG)
                logger.debug("Transaction: ORDER STAT");
            int c_d_id = d_id;
            int c_w_id = w_id;
            String c_first = null;
            String c_middle = null;
            String c_last = null;
            float c_balance = 0;
            int o_id = 0;
            String o_entry_d = null;
            int o_carrier_id = 0;
            int ol_i_id = 0;
            int ol_supply_w_id = 0;
            int ol_quantity = 0;
            float ol_amount = 0;
            String ol_delivery_d = null;
            int namecnt = 0;

            int n = 0;

            if (byname > 0) {
                c_last = c_last_arg;

                if (TRACE)
                    logger.trace("SELECT count(c_id) FROM customer WHERE c_w_id = " + c_w_id
                            + " AND c_d_id = " + c_d_id + " AND c_last = " + c_last);
                pStmts.getStatement(20).setInt(1, c_w_id);
                pStmts.getStatement(20).setInt(2, c_d_id);
                pStmts.getStatement(20).setString(3, c_last);
                try (ResultSet rs = pStmts.getStatement(20).executeQuery()) {
                    if (rs.next()) {
                        namecnt = rs.getInt(1);
                    }
                }

                if (TRACE)
                    logger.trace("SELECT c_balance, c_first, c_middle, c_last FROM customer WHERE "
                            + "c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id + " AND c_last = "
                            + c_last + " ORDER BY c_first");
                pStmts.getStatement(21).setInt(1, c_w_id);
                pStmts.getStatement(21).setInt(2, c_d_id);
                pStmts.getStatement(21).setString(3, c_last);
                try (ResultSet rs = pStmts.getStatement(21).executeQuery()) {
                    if (namecnt % 2 == 1) { // ?? Check
                        namecnt++;
                    } /* Locate midpoint customer; */

                    // Use a for loop to find midpoint customer based on namecnt.
                    for (n = 0; n < namecnt / 2; n++) {
                        rs.next();
                        c_balance = rs.getFloat(1);
                        c_first = rs.getString(2);
                        c_middle = rs.getString(3);
                        c_last = rs.getString(4);
                    }
                }
            } else { /* by number */
                if (TRACE)
                    logger.trace("SELECT c_balance, c_first, c_middle, c_last FROM customer WHERE "
                            + "c_w_id = " + c_w_id + " AND c_d_id = " + c_d_id + " AND c_id = " + c_id);
                pStmts.getStatement(22).setInt(1, c_w_id);
                pStmts.getStatement(22).setInt(2, c_d_id);
                pStmts.getStatement(22).setInt(3, c_id);
                try (ResultSet rs = pStmts.getStatement(22).executeQuery()) {
                    if (rs.next()) {
                        c_balance = rs.getFloat(1);
                        c_first = rs.getString(2);
                        c_middle = rs.getString(3);
                        c_last = rs.getString(4);
                    }
                }
            }

            /* find the most recent order for this customer */

            if (TRACE)
                logger.trace("SELECT o_id, o_entry_d, COALESCE(o_carrier_id,0) FROM orders "
                        + "WHERE o_w_id = " + c_w_id + " AND o_d_id = " + c_d_id + " AND o_c_id = "
                        + c_id + " AND o_id = " + "(SELECT MAX(o_id) FROM orders WHERE o_w_id = "
                        + c_w_id + " AND o_d_id = " + c_d_id + " AND o_c_id = " + c_id);
            pStmts.getStatement(23).setInt(1, c_w_id);
            pStmts.getStatement(23).setInt(2, c_d_id);
            pStmts.getStatement(23).setInt(3, c_id);
            pStmts.getStatement(23).setInt(4, c_w_id);
            pStmts.getStatement(23).setInt(5, c_d_id);
            pStmts.getStatement(23).setInt(6, c_id);
            try (ResultSet rs = pStmts.getStatement(23).executeQuery()) {
                if (rs.next()) {
                    o_id = rs.getInt(1);
                    o_entry_d = rs.getString(2);
                    o_carrier_id = rs.getInt(3);
                }
            }

            if (TRACE)
                logger.trace("SELECT ol_i_id, ol_supply_w_id, ol_quantity, "
                        + "ol_amount, ol_delivery_d FROM order_line " + "WHERE ol_w_id = " + c_w_id
                        + " AND ol_d_id = " + c_d_id + " AND ol_o_id = " + o_id);
            pStmts.getStatement(24).setInt(1, c_w_id);
            pStmts.getStatement(24).setInt(2, c_d_id);
            pStmts.getStatement(24).setInt(3, o_id);
            try (ResultSet rs = pStmts.getStatement(24).executeQuery()) {
                while (rs.next()) {
                    ol_i_id = rs.getInt(1);
                    ol_supply_w_id = rs.getInt(2);
                    ol_quantity = rs.getInt(3);
                    ol_amount = rs.getFloat(4);
                    ol_delivery_d = rs.getString(5);
                }
            }

            pStmts.commit();
            return 1;
        } catch (Exception e) {
            pStmts.rollback(e, "Order stat");
            return 0;
        }
    }
}
