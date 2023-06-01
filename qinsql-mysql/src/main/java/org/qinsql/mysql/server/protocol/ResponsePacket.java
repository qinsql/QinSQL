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
package org.qinsql.mysql.server.protocol;

import java.nio.ByteBuffer;

import org.qinsql.mysql.server.util.BufferUtil;

public abstract class ResponsePacket extends Packet {

    /**
     * 计算数据包大小，不包含包头长度。
     */
    public abstract int calcPacketSize();

    public abstract void writeBody(ByteBuffer buffer, PacketOutput out);

    @Override
    public void write(PacketOutput out) {
        int size = calcPacketSize();
        ByteBuffer buffer = out.allocate(4 + size); // PacketHeader占4个字节

        // write header
        BufferUtil.writeUB3(buffer, size);
        buffer.put(packetId);

        writeBody(buffer, out);
        out.flush();
    }
}
