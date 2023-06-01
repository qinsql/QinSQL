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

import org.lealone.db.DataBuffer;
import org.lealone.db.DataBufferFactory;
import org.lealone.net.WritableChannel;

public class PacketOutput {

    private static final int BUFFER_SIZE = 8 * 1024;

    private final WritableChannel writableChannel;
    private final DataBufferFactory dataBufferFactory;
    private DataBuffer dataBuffer;

    public PacketOutput(WritableChannel writableChannel, DataBufferFactory dataBufferFactory) {
        this.writableChannel = writableChannel;
        this.dataBufferFactory = dataBufferFactory;
    }

    public ByteBuffer allocate(int capacity) {
        capacity = Math.min(capacity, BUFFER_SIZE);
        dataBuffer = dataBufferFactory.create(capacity);
        return dataBuffer.getBuffer();
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
                flush();
                buffer = allocate(BUFFER_SIZE);
                offset += remaining;
                length -= remaining;
                remaining = buffer.remaining();
                continue;
            }
        }
        return buffer;
    }

    public void flush() {
        if (dataBuffer != null) {
            dataBuffer.getAndFlipBuffer();
            writableChannel.write(writableChannel.getBufferFactory().createBuffer(dataBuffer));
            dataBuffer = null;
        }
    }
}
