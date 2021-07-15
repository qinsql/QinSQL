/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.postgresql.jdbc;

import org.lealone.bats.sphere.jdbc.LealoneJDBCDriverURLRecognizer;

public class LealonePostgreSQLJDBCDriverURLRecognizer extends LealoneJDBCDriverURLRecognizer {

    @Override
    public String getDatabaseType() {
        return "LealonePostgreSQL";
    }
}
