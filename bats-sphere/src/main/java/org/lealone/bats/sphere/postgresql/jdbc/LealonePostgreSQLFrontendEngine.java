/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.postgresql.jdbc;

import org.apache.shardingsphere.db.protocol.codec.DatabasePacketCodecEngine;
import org.apache.shardingsphere.db.protocol.postgresql.codec.PostgreSQLPacketCodecEngine;
import org.apache.shardingsphere.proxy.frontend.postgresql.auth.PostgreSQLAuthenticationEngine;
import org.apache.shardingsphere.proxy.frontend.postgresql.command.PostgreSQLCommandExecuteEngine;
import org.lealone.bats.sphere.jdbc.LealoneFrontendEngine;

public class LealonePostgreSQLFrontendEngine extends LealoneFrontendEngine {

    public static LealonePostgreSQLFrontendEngine INSTANCE = new LealonePostgreSQLFrontendEngine();

    private final PostgreSQLAuthenticationEngine authEngine = new PostgreSQLAuthenticationEngine();

    private final PostgreSQLCommandExecuteEngine commandExecuteEngine = new PostgreSQLCommandExecuteEngine();

    private final DatabasePacketCodecEngine<?> codecEngine = new PostgreSQLPacketCodecEngine();

    @Override
    public String getDatabaseType() {
        return "LealonePostgreSQL";
    }

    @Override
    public PostgreSQLAuthenticationEngine getAuthEngine() {
        return authEngine;
    }

    @Override
    public PostgreSQLCommandExecuteEngine getCommandExecuteEngine() {
        return commandExecuteEngine;
    }

    @Override
    public DatabasePacketCodecEngine<?> getCodecEngine() {
        return codecEngine;
    }
}
