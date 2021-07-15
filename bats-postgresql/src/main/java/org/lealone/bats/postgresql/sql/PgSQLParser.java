/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.postgresql.sql;

import org.lealone.db.session.ServerSession;

public class PgSQLParser extends Parser {

    public PgSQLParser(ServerSession session) {
        super(session);
    }
}
