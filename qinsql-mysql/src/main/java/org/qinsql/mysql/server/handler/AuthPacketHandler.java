/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.mysql.server.handler;

import org.qinsql.mysql.server.MySQLServerConnection;
import org.qinsql.mysql.server.protocol.AuthPacket;
import org.qinsql.mysql.server.protocol.PacketInput;

public class AuthPacketHandler implements PacketHandler {

    private final MySQLServerConnection conn;

    public AuthPacketHandler(MySQLServerConnection conn) {
        this.conn = conn;
    }

    @Override
    public void handle(PacketInput in) {
        AuthPacket authPacket = new AuthPacket();
        authPacket.read(in);
        conn.authenticate(authPacket);
    }
}
