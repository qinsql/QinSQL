/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench.transaction;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.qinsql.bench.tpcc.config.TpccConstants;

public class Delivery implements TpccConstants {

    private TpccStatements pStmts;

    public Delivery(TpccStatements pStmts) {
        this.pStmts = pStmts;
    }

    public int delivery(int w_id, int o_carrier_id) {
        try {
            // Start a transaction.
            pStmts.setAutoCommit(false);
            if (DEBUG)
                logger.debug("Transaction:	Delivery");
            int c_id = 0;
            int no_o_id = 0;
            float ol_total = 0;

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            Timestamp currentTimeStamp = new Timestamp(now.getTime());

            for (int d_id = 1; d_id <= DIST_PER_WARE; d_id++) {
                if (TRACE)
                    logger.trace("SELECT COALESCE(MIN(no_o_id),0) FROM new_orders WHERE no_d_id = "
                            + d_id + " AND no_w_id = " + w_id);
                pStmts.getStatement(25).setInt(1, d_id);
                pStmts.getStatement(25).setInt(2, w_id);
                try (ResultSet rs = pStmts.getStatement(25).executeQuery()) {
                    if (rs.next()) {
                        no_o_id = rs.getInt(1);
                    }
                }

                if (no_o_id == 0) {
                    continue;
                } else {
                    if (DEBUG)
                        logger.debug("No_o_id did not equal 0 -> " + no_o_id);
                }

                if (TRACE)
                    logger.trace("DELETE FROM new_orders WHERE no_o_id = " + no_o_id + " AND no_d_id = "
                            + d_id + " AND no_w_id = " + w_id);
                pStmts.getStatement(26).setInt(1, no_o_id);
                pStmts.getStatement(26).setInt(2, d_id);
                pStmts.getStatement(26).setInt(3, w_id);
                pStmts.getStatement(26).executeUpdate();

                if (TRACE)
                    logger.trace("SELECT o_c_id FROM orders WHERE o_id = " + no_o_id + " AND o_d_id = "
                            + d_id + " AND o_w_id = " + w_id);
                pStmts.getStatement(27).setInt(1, no_o_id);
                pStmts.getStatement(27).setInt(2, d_id);
                pStmts.getStatement(27).setInt(3, w_id);
                try (ResultSet rs = pStmts.getStatement(27).executeQuery()) {
                    if (rs.next()) {
                        c_id = rs.getInt(1);
                    }
                }

                if (TRACE)
                    logger.trace("UPDATE orders SET o_carrier_id = " + o_carrier_id + " WHERE o_id = "
                            + no_o_id + " AND o_d_id = " + d_id + " AND o_w_id = " + w_id);
                pStmts.getStatement(28).setInt(1, o_carrier_id);
                pStmts.getStatement(28).setInt(2, no_o_id);
                pStmts.getStatement(28).setInt(3, d_id);
                pStmts.getStatement(28).setInt(4, w_id);
                pStmts.getStatement(28).executeUpdate();

                if (TRACE)
                    logger.trace("UPDATE order_line SET ol_delivery_d = " + currentTimeStamp.toString()
                            + " WHERE ol_o_id = " + no_o_id + " AND ol_d_id = " + d_id
                            + " AND ol_w_id = " + w_id);
                // pStmts.getStatement(29).setString(1, currentTimeStamp.toString());
                pStmts.getStatement(29).setTimestamp(1, currentTimeStamp);
                pStmts.getStatement(29).setInt(2, no_o_id);
                pStmts.getStatement(29).setInt(3, d_id);
                pStmts.getStatement(29).setInt(4, w_id);
                pStmts.getStatement(29).executeUpdate();

                if (TRACE)
                    logger.trace("SELECT SUM(ol_amount) FROM order_line WHERE ol_o_id = " + no_o_id
                            + " AND ol_d_id = " + d_id + " AND ol_w_id = " + w_id);
                pStmts.getStatement(30).setInt(1, no_o_id);
                pStmts.getStatement(30).setInt(2, d_id);
                pStmts.getStatement(30).setInt(3, w_id);
                try (ResultSet rs = pStmts.getStatement(30).executeQuery()) {
                    if (rs.next()) {
                        ol_total = rs.getFloat(1);
                    }
                }

                if (TRACE)
                    logger.trace("UPDATE customer SET c_balance = c_balance + " + ol_total
                            + ", c_delivery_cnt = c_delivery_cnt + 1 WHERE c_id = " + c_id
                            + " AND c_d_id = " + d_id + " AND c_w_id = " + w_id);
                pStmts.getStatement(31).setFloat(1, ol_total);
                pStmts.getStatement(31).setInt(2, c_id);
                pStmts.getStatement(31).setInt(3, d_id);
                pStmts.getStatement(31).setInt(4, w_id);
                pStmts.getStatement(31).executeUpdate();
            }

            pStmts.commit();
            return 1;
        } catch (Exception e) {
            pStmts.rollback(e, "Delivery");
            return 0;
        }
    }
}
