/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.sphere.mysql.jdbc;

import org.apache.shardingsphere.db.protocol.codec.DatabasePacketCodecEngine;
import org.apache.shardingsphere.db.protocol.mysql.codec.MySQLPacketCodecEngine;
import org.apache.shardingsphere.proxy.frontend.mysql.auth.MySQLAuthenticationEngine;
import org.apache.shardingsphere.proxy.frontend.mysql.command.MySQLCommandExecuteEngine;
import org.lealone.bats.sphere.jdbc.LealoneFrontendEngine;

public class LealoneMySQLFrontendEngine extends LealoneFrontendEngine {

    public static LealoneMySQLFrontendEngine INSTANCE = new LealoneMySQLFrontendEngine();

    private final MySQLAuthenticationEngine authEngine = new MySQLAuthenticationEngine();

    private final MySQLCommandExecuteEngine commandExecuteEngine = new MySQLCommandExecuteEngine();

    private final DatabasePacketCodecEngine<?> codecEngine = new MySQLPacketCodecEngine();

    @Override
    public String getDatabaseType() {
        return "LealoneMySQL";
    }

    @Override
    public MySQLAuthenticationEngine getAuthEngine() {
        return authEngine;
    }

    @Override
    public MySQLCommandExecuteEngine getCommandExecuteEngine() {
        return commandExecuteEngine;
    }

    @Override
    public DatabasePacketCodecEngine<?> getCodecEngine() {
        return codecEngine;
    }
}
