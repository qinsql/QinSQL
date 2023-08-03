/*
 * Copyright Lealone Database Group. CodeFutures Corporation
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh, CodeFutures Corporation
 */
package org.qinsql.bench.tpcc.bench.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.qinsql.bench.tpcc.config.TpccConstants;

public class TpccStatements implements TpccConstants {

    public static final int STMT_COUNT = 37;

    private final Connection conn;

    private final PreparedStatement[] pStmts = new PreparedStatement[STMT_COUNT];

    public Statement stmt;

    public TpccStatements(Connection conn, int fetchSize) throws Exception {
        this.conn = conn;
        stmt = conn.createStatement();
        stmt.setFetchSize(fetchSize);

        // NewOrder statements.
        pStmts[0] = prepareStatement("SELECT c.c_discount, c.c_last, c.c_credit, w.w_tax "
                + "FROM customer AS c JOIN warehouse AS w "
                + "ON c.c_w_id = w_id AND w.w_id = ? AND c.c_w_id = ? AND c.c_d_id = ? AND c.c_id = ?");
        pStmts[1] = prepareStatement(
                "SELECT d_next_o_id, d_tax FROM district WHERE d_id = ? AND d_w_id = ? FOR UPDATE");
        pStmts[2] = prepareStatement(
                "UPDATE district SET d_next_o_id = ? + 1 WHERE d_id = ? AND d_w_id = ?");
        pStmts[3] = prepareStatement(
                "INSERT INTO orders (o_id, o_d_id, o_w_id, o_c_id, o_entry_d, o_ol_cnt, o_all_local) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?)");
        pStmts[4] = prepareStatement(
                "INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (?,?,?)");
        pStmts[5] = prepareStatement("SELECT i_price, i_name, i_data FROM item WHERE i_id = ?");
        pStmts[6] = prepareStatement("SELECT s_quantity, s_data, s_dist_01, s_dist_02, s_dist_03, "
                + "s_dist_04, s_dist_05, s_dist_06, s_dist_07, s_dist_08, s_dist_09, s_dist_10 "
                + "FROM stock WHERE s_i_id = ? AND s_w_id = ? FOR UPDATE");
        pStmts[7] = prepareStatement("UPDATE stock SET s_quantity = ? WHERE s_i_id = ? AND s_w_id = ?");
        pStmts[8] = prepareStatement(
                "INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id, ol_number, ol_i_id, ol_supply_w_id, "
                        + "ol_quantity, ol_amount, ol_dist_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Payment statements.
        pStmts[9] = prepareStatement("UPDATE warehouse SET w_ytd = w_ytd + ? WHERE w_id = ?");
        pStmts[10] = prepareStatement("SELECT w_street_1, w_street_2, w_city, w_state, w_zip, w_name "
                + "FROM warehouse WHERE w_id = ?");
        pStmts[11] = prepareStatement(
                "UPDATE district SET d_ytd = d_ytd + ? WHERE d_w_id = ? AND d_id = ?");
        pStmts[12] = prepareStatement("SELECT d_street_1, d_street_2, d_city, d_state, d_zip, d_name "
                + "FROM district WHERE d_w_id = ? AND d_id = ?");
        pStmts[13] = prepareStatement(
                "SELECT count(c_id) FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_last = ?");
        pStmts[14] = prepareStatement(
                "SELECT c_id FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_last = ? ORDER BY c_first");
        pStmts[15] = prepareStatement(
                "SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city, "
                        + "c_state, c_zip, c_phone, c_credit, c_credit_lim, c_discount, c_balance, c_since "
                        + "FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_id = ? FOR UPDATE");
        pStmts[16] = prepareStatement(
                "SELECT c_data FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");
        pStmts[17] = prepareStatement("UPDATE customer SET c_balance = ?, c_data = ? "
                + "WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");
        pStmts[18] = prepareStatement(
                "UPDATE customer SET c_balance = ? WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");
        pStmts[19] = prepareStatement(
                "INSERT INTO history(h_c_d_id, h_c_w_id, h_c_id, h_d_id, h_w_id, h_date, h_amount, h_data) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

        // OrderStat statements.
        pStmts[20] = prepareStatement(
                "SELECT count(c_id) FROM customer WHERE c_w_id = ? AND c_d_id = ? AND c_last = ?");
        pStmts[21] = prepareStatement("SELECT c_balance, c_first, c_middle, c_last FROM customer "
                + "WHERE c_w_id = ? AND c_d_id = ? AND c_last = ? ORDER BY c_first");
        pStmts[22] = prepareStatement("SELECT c_balance, c_first, c_middle, c_last FROM customer "
                + "WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");
        pStmts[23] = prepareStatement("SELECT o_id, o_entry_d, COALESCE(o_carrier_id,0) FROM orders "
                + "WHERE o_w_id = ? AND o_d_id = ? AND o_c_id = ? AND "
                + "o_id = (SELECT MAX(o_id) FROM orders WHERE o_w_id = ? AND o_d_id = ? AND o_c_id = ?)");
        pStmts[24] = prepareStatement(
                "SELECT ol_i_id, ol_supply_w_id, ol_quantity, ol_amount, ol_delivery_d "
                        + "FROM order_line WHERE ol_w_id = ? AND ol_d_id = ? AND ol_o_id = ?");

        // Delivery statements.
        pStmts[25] = prepareStatement(
                "SELECT COALESCE(MIN(no_o_id),0) FROM new_orders WHERE no_d_id = ? AND no_w_id = ?");
        pStmts[26] = prepareStatement(
                "DELETE FROM new_orders WHERE no_o_id = ? AND no_d_id = ? AND no_w_id = ?");
        pStmts[27] = prepareStatement(
                "SELECT o_c_id FROM orders WHERE o_id = ? AND o_d_id = ? AND o_w_id = ?");
        pStmts[28] = prepareStatement(
                "UPDATE orders SET o_carrier_id = ? WHERE o_id = ? AND o_d_id = ? AND o_w_id = ?");
        pStmts[29] = prepareStatement(
                "UPDATE order_line SET ol_delivery_d = ? WHERE ol_o_id = ? AND ol_d_id = ? AND ol_w_id = ?");
        pStmts[30] = prepareStatement(
                "SELECT SUM(ol_amount) FROM order_line WHERE ol_o_id = ? AND ol_d_id = ? AND ol_w_id = ?");
        pStmts[31] = prepareStatement(
                "UPDATE customer SET c_balance = c_balance + ? , c_delivery_cnt = c_delivery_cnt + 1 "
                        + "WHERE c_id = ? AND c_d_id = ? AND c_w_id = ?");

        // Slev statements.
        pStmts[32] = prepareStatement("SELECT d_next_o_id FROM district WHERE d_id = ? AND d_w_id = ?");
        pStmts[33] = prepareStatement("SELECT DISTINCT ol_i_id FROM order_line "
                + "WHERE ol_w_id = ? AND ol_d_id = ? AND ol_o_id < ? AND ol_o_id >= (? - 20)");
        pStmts[34] = prepareStatement(
                "SELECT count(*) FROM stock WHERE s_w_id = ? AND s_i_id = ? AND s_quantity < ?");

        // These are used in place of pStmts[0] in order to avoid joins
        pStmts[35] = prepareStatement("SELECT c_discount, c_last, c_credit FROM customer "
                + "WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");
        pStmts[36] = prepareStatement("SELECT w_tax FROM warehouse WHERE w_id = ?");

        for (int i = 0; i < pStmts.length; i++) {
            pStmts[i].setFetchSize(fetchSize);
        }
    }

    private PreparedStatement prepareStatement(String sql) throws SQLException {
        if (sql.startsWith("SELECT")) {
            return conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } else {
            return conn.prepareStatement(sql, PreparedStatement.NO_GENERATED_KEYS);
        }
    }

    public PreparedStatement getStatement(int idx) {
        return pStmts[idx];
    }

    public void setAutoCommit(boolean b) throws SQLException {
        conn.setAutoCommit(b);
    }

    /**
     * Commit a transaction.
     */
    public void commit() throws SQLException {
        if (TRACE)
            logger.trace("COMMIT");
        conn.commit();
    }

    /**
     * Rollback a transaction.
     */
    public void rollback() throws SQLException {
        if (TRACE)
            logger.trace("ROLLBACK");
        conn.rollback();
    }

    public void rollback(Exception e, String prefix) {
        logger.error(prefix + " error", new RuntimeException(e));
        try {
            // Rollback if an aborted transaction, they are intentional in some percentage of cases.
            rollback();
        } catch (Throwable t) {
            throw new RuntimeException(prefix + " error", t);
        }
    }
}
