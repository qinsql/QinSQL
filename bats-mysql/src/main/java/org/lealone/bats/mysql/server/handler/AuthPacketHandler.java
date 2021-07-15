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

import org.lealone.bats.mysql.server.MySQLServerConnection;
import org.lealone.bats.mysql.server.protocol.AuthPacket;
import org.lealone.bats.mysql.server.protocol.PacketInput;

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
