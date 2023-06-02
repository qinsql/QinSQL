/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.mysql.sql.expression;

import org.lealone.db.session.ServerSession;
import org.lealone.db.value.Value;
import org.lealone.db.value.ValueInt;
import org.lealone.sql.expression.Variable;

public class MySQLVariable extends Variable {

    public MySQLVariable(ServerSession session, String name) {
        super(session, name);
    }

    @Override
    public Value getValue(ServerSession session) {
        switch (getName().toLowerCase()) {
        case "max_allowed_packet":
        case "net_buffer_length":
            return ValueInt.get(-1);
        case "auto_increment_increment":
            return ValueInt.get(1);
        }
        return super.getValue(session);
    }
}
