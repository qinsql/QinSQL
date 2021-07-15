/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.postgresql.jdbc;

import org.lealone.bats.sphere.jdbc.LealoneStatementMemoryStrictlyFetchSizeSetter;

public class LealonePostgreSQLStatementMemoryStrictlyFetchSizeSetter
        extends LealoneStatementMemoryStrictlyFetchSizeSetter {

    @Override
    public String getType() {
        return "LealonePostgreSQL";
    }
}
