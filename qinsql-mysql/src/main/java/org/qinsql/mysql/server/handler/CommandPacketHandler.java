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
package org.qinsql.mysql.server.handler;

import org.qinsql.mysql.server.MySQLServerConnection;
import org.qinsql.mysql.server.protocol.ErrorCode;
import org.qinsql.mysql.server.protocol.ExecutePacket;
import org.qinsql.mysql.server.protocol.InitDbPacket;
import org.qinsql.mysql.server.protocol.PacketInput;
import org.qinsql.mysql.server.protocol.PacketType;

public class CommandPacketHandler implements PacketHandler {

    private final MySQLServerConnection conn;

    public CommandPacketHandler(MySQLServerConnection conn) {
        this.conn = conn;
    }

    private String readSql(PacketInput in) {
        in.position(5);
        // 使用指定的编码来读取数据
        return in.readString("utf-8");
    }

    @Override
    public void handle(PacketInput in) {
        switch (in.type()) {
        case PacketType.COM_QUERY: {
            String sql = readSql(in).trim();
            conn.executeStatement(sql);
            break;
        }
        case PacketType.COM_STMT_PREPARE: {
            String sql = readSql(in);
            conn.prepareStatement(sql);
            break;
        }
        case PacketType.COM_STMT_EXECUTE: {
            ExecutePacket packet = new ExecutePacket();
            packet.read(in, "utf-8", conn.getSession());
            conn.executeStatement(packet);
            break;
        }
        case PacketType.COM_STMT_CLOSE:
            in.position(5);
            conn.closeStatement(in.readInt());
            break;
        case PacketType.COM_INIT_DB:
            InitDbPacket packet = new InitDbPacket();
            packet.read(in);
            conn.initDatabase(packet.database);
            conn.writeOkPacket();
            break;
        case PacketType.COM_QUIT:
            conn.close();
            break;
        case PacketType.COM_PROCESS_KILL: // 直接返回OkPacket
        case PacketType.COM_PING:
            conn.writeOkPacket();
            break;
        default:
            conn.sendErrorMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
        }
    }
}
