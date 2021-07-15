/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.mysql.server.handler;

import java.io.UnsupportedEncodingException;

import org.lealone.bats.mysql.server.MySQLServerConnection;
import org.lealone.bats.mysql.server.protocol.ErrorCode;
import org.lealone.bats.mysql.server.protocol.InitDbPacket;
import org.lealone.bats.mysql.server.protocol.MySQLMessage;
import org.lealone.bats.mysql.server.protocol.MySQLPacket;
import org.lealone.bats.mysql.server.protocol.PacketInput;
import org.lealone.common.exceptions.DbException;

public class CommandPacketHandler implements PacketHandler {

    private final MySQLServerConnection conn;

    public CommandPacketHandler(MySQLServerConnection conn) {
        this.conn = conn;
    }

    @Override
    public void handle(PacketInput in) {
        MySQLMessage mm = new MySQLMessage(in);
        switch (mm.type()) {
        case MySQLPacket.COM_QUERY:
            mm.position(5);
            String sql = null;
            try {
                // 使用指定的编码来读取数据
                sql = mm.readString("utf-8");
            } catch (UnsupportedEncodingException e) {
                throw DbException.convert(e);
            }
            conn.executeStatement(sql);
            break;
        case MySQLPacket.COM_INIT_DB:
            InitDbPacket packet = new InitDbPacket();
            packet.read(in);
            conn.initDatabase(packet.database);
            conn.writeOkPacket();
            break;
        case MySQLPacket.COM_QUIT:
            conn.close();
            break;
        case MySQLPacket.COM_PROCESS_KILL: // 直接返回OkPacket
        case MySQLPacket.COM_PING:
            conn.writeOkPacket();
            break;
        default:
            conn.sendErrorMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
        }
        // case MySQLPacket.COM_STMT_PREPARE:
        // commands.doStmtPrepare();
        // source.stmtPrepare(data);
        // break;
        // case MySQLPacket.COM_STMT_EXECUTE:
        // commands.doStmtExecute();
        // source.stmtExecute(data);
        // break;
        // case MySQLPacket.COM_STMT_CLOSE:
        // commands.doStmtClose();
        // source.stmtClose(data);
        // break;
        // case MySQLPacket.COM_HEARTBEAT:
        // commands.doHeartbeat();
        // source.heartbeat(data);
        // break;
    }
}
