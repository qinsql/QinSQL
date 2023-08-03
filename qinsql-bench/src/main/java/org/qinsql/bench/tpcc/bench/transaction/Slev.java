/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench.transaction;

import java.sql.ResultSet;
import java.sql.Statement;

import org.qinsql.bench.tpcc.config.TpccConstants;

public class Slev implements TpccConstants {

    private TpccStatements pStmts;

    public Slev(TpccStatements pStms) {
        this.pStmts = pStms;
    }

    public int slev_ps(int w_id, int d_id, int level) {
        if (DEBUG)
            logger.debug("Transaction:  SLEV");
        try {
            pStmts.setAutoCommit(false);

            int d_next_o_id = 0;
            int ol_i_id = 0;

            if (TRACE)
                logger.trace("SELECT d_next_o_id FROM district WHERE d_id = " + d_id + " AND d_w_id = "
                        + w_id);
            pStmts.getStatement(32).setInt(1, d_id);
            pStmts.getStatement(32).setInt(2, w_id);
            try (ResultSet rs = pStmts.getStatement(32).executeQuery()) {
                if (rs.next()) {
                    d_next_o_id = rs.getInt(1);
                }
            }

            if (TRACE)
                logger.trace("SELECT DISTINCT ol_i_id FROM order_line WHERE ol_w_id = " + w_id
                        + " AND ol_d_id = " + d_id + " AND ol_o_id < " + d_next_o_id
                        + " AND ol_o_id >= (" + d_next_o_id + " - 20)");
            pStmts.getStatement(33).setInt(1, w_id);
            pStmts.getStatement(33).setInt(2, d_id);
            pStmts.getStatement(33).setInt(3, d_next_o_id);
            pStmts.getStatement(33).setInt(4, d_next_o_id);
            try (ResultSet rs = pStmts.getStatement(33).executeQuery()) {
                while (rs.next()) {
                    ol_i_id = rs.getInt(1);
                }
            }

            if (TRACE)
                logger.trace("SELECT count(*) FROM stock WHERE s_w_id = " + w_id + " AND s_i_id = "
                        + ol_i_id + " AND s_quantity < " + level);
            pStmts.getStatement(34).setInt(1, w_id);
            pStmts.getStatement(34).setInt(2, ol_i_id);
            pStmts.getStatement(34).setInt(3, level);
            try (ResultSet rs = pStmts.getStatement(34).executeQuery()) {
                if (rs.next()) {
                    rs.getInt(1);
                }
            }

            pStmts.commit();
            return 1;
        } catch (Exception e) {
            pStmts.rollback(e, "Slev");
            return 0;
        }
    }

    public int slev(int w_id, int d_id, int level) {
        if (DEBUG)
            logger.debug("Transaction:  SLEV");
        try {
            pStmts.setAutoCommit(false);
            Statement stmt = pStmts.stmt;
            int d_next_o_id = 0;
            int ol_i_id = 0;

            if (TRACE)
                logger.trace("SELECT d_next_o_id FROM district WHERE d_id = " + d_id + " AND d_w_id = "
                        + w_id);
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT d_next_o_id FROM district WHERE d_id = " + d_id + " AND d_w_id = " + w_id)) {
                if (rs.next()) {
                    d_next_o_id = rs.getInt(1);
                }
            }

            if (TRACE)
                logger.trace("SELECT DISTINCT ol_i_id FROM order_line WHERE ol_w_id = " + w_id
                        + " AND ol_d_id = " + d_id + " AND ol_o_id < " + d_next_o_id
                        + " AND ol_o_id >= (" + d_next_o_id + " - 20)");
            try (ResultSet rs = stmt
                    .executeQuery("SELECT DISTINCT ol_i_id FROM order_line WHERE ol_w_id = " + w_id
                            + " AND ol_d_id = " + d_id + " AND ol_o_id < " + d_next_o_id
                            + " AND ol_o_id >= (" + d_next_o_id + " - 20)")) {
                while (rs.next()) {
                    ol_i_id = rs.getInt(1);
                }
            }

            if (TRACE)
                logger.trace("SELECT count(*) FROM stock WHERE s_w_id = " + w_id + " AND s_i_id = "
                        + ol_i_id + " AND s_quantity < " + level);
            try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM stock WHERE s_w_id = " + w_id
                    + " AND s_i_id = " + ol_i_id + " AND s_quantity < " + level)) {
                if (rs.next()) {
                    rs.getInt(1);
                }
            }

            pStmts.commit();
            return 1;
        } catch (Exception e) {
            pStmts.rollback(e, "Slev");
            return 0;
        }
    }
}
