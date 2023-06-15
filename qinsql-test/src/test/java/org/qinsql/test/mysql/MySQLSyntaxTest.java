/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.mysql;

import org.junit.Test;

public class MySQLSyntaxTest extends MySQLTestBase {
    @Test
    public void run() throws Exception {
        testTransactionIsolation();
    }

    void testTransactionIsolation() throws Exception {
        executeUpdate("SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED");
        executeQuery("select @@session.tx_isolation");
        assertEquals("READ-COMMITTED", getStringValue(1));
    }
}
