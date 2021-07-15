/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.mysql.jdbc;

import org.lealone.bats.sphere.jdbc.LealoneJDBCDriverURLRecognizer;

public class LealoneMySQLJDBCDriverURLRecognizer extends LealoneJDBCDriverURLRecognizer {

    @Override
    public String getDatabaseType() {
        return "LealoneMySQL";
    }
}
