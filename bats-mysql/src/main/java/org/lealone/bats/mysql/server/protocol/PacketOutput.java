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
package org.lealone.bats.mysql.server.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lealone.bats.mysql.server.MySQLServerConnection;
import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;

public class PacketOutput {

    private static final Logger logger = LoggerFactory.getLogger(MySQLServerConnection.class);

    private final DataOutputStream out;

    public PacketOutput(DataOutputStream out) {
        this.out = out;
    }

    public ByteBuffer checkWriteBuffer(ByteBuffer buffer, int capacity) {
        if (capacity > buffer.remaining()) {
            write(buffer);
            return allocate();
        } else {
            return buffer;
        }
    }

    public ByteBuffer allocate() {
        return ByteBuffer.allocate(MySQLServerConnection.BUFFER_SIZE);
    }

    public int getPacketHeaderSize() {
        return Packet.PACKET_HEADER_SIZE;
    }

    public ByteBuffer writeToBuffer(byte[] src, ByteBuffer buffer) {
        int offset = 0;
        int length = src.length;
        int remaining = buffer.remaining();
        while (length > 0) {
            if (remaining >= length) {
                buffer.put(src, offset, length);
                break;
            } else {
                buffer.put(src, offset, remaining);
                write(buffer);
                buffer = allocate();
                offset += remaining;
                length -= remaining;
                remaining = buffer.remaining();
                continue;
            }
        }
        return buffer;
    }

    public void write(ByteBuffer buffer) {
        buffer.flip();
        try {
            out.write(buffer.array(), buffer.arrayOffset(), buffer.limit());
            out.flush();
        } catch (IOException e) {
            logger.error("Failed to write", e);
        }
    }
}
