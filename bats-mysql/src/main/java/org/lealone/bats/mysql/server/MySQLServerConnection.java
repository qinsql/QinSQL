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
package org.lealone.bats.mysql.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.lealone.bats.mysql.server.handler.AuthPacketHandler;
import org.lealone.bats.mysql.server.handler.CommandPacketHandler;
import org.lealone.bats.mysql.server.handler.PacketHandler;
import org.lealone.bats.mysql.server.protocol.AuthPacket;
import org.lealone.bats.mysql.server.protocol.EOFPacket;
import org.lealone.bats.mysql.server.protocol.ErrorPacket;
import org.lealone.bats.mysql.server.protocol.FieldPacket;
import org.lealone.bats.mysql.server.protocol.HandshakePacket;
import org.lealone.bats.mysql.server.protocol.OkPacket;
import org.lealone.bats.mysql.server.protocol.PacketInput;
import org.lealone.bats.mysql.server.protocol.PacketOutput;
import org.lealone.bats.mysql.server.protocol.ResultSetHeaderPacket;
import org.lealone.bats.mysql.server.protocol.RowDataPacket;
import org.lealone.bats.mysql.util.PacketUtil;
import org.lealone.common.exceptions.DbException;
import org.lealone.common.logging.Logger;
import org.lealone.common.logging.LoggerFactory;
import org.lealone.db.ConnectionInfo;
import org.lealone.db.Constants;
import org.lealone.db.result.Result;
import org.lealone.db.session.Session;
import org.lealone.db.value.Value;
import org.lealone.db.value.ValueNull;
import org.lealone.net.AsyncConnection;
import org.lealone.net.NetBuffer;
import org.lealone.net.NetBufferInputStream;
import org.lealone.net.NetBufferOutputStream;
import org.lealone.net.WritableChannel;
import org.lealone.sql.PreparedSQLStatement;

public class MySQLServerConnection extends AsyncConnection {

    private static final Logger logger = LoggerFactory.getLogger(MySQLServerConnection.class);
    public static final int BUFFER_SIZE = 16 * 1024;
    private static final byte[] AUTH_OK = new byte[] { 7, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0 };

    private final MySQLServer server;
    private Session session;
    private PacketHandler packetHandler;
    private AuthPacket authPacket;

    protected MySQLServerConnection(MySQLServer server, WritableChannel writableChannel, boolean isServer) {
        super(writableChannel, isServer);
        this.server = server;
    }

    // 客户端连上来后，数据库先发回一个握手包
    void handshake() {
        int threadId = 0;
        PacketOutput output = getPacketOutput();
        HandshakePacket.create(threadId).write(output);
        // 接着创建一个AuthPacketHandler用来鉴别是否是合法的用户
        packetHandler = new AuthPacketHandler(this);
    }

    public void authenticate(AuthPacket authPacket) {
        this.authPacket = authPacket;
        try {
            session = createSession(authPacket, MySQLServer.DATABASE_NAME);
        } catch (Throwable e) {
            logger.error("Failed to create session", e);
            sendErrorMessage(e);
            close();
            return;
        }
        server.addConnection(this);
        // 鉴别成功后创建CommandPacketHandler用来处理各种命令(包括SQL)
        packetHandler = new CommandPacketHandler(this);
        sendMessage(AUTH_OK);
    }

    private static Session createSession(AuthPacket authPacket, String dbName) {
        Properties info = new Properties();
        info.put("MODE", "MySQL");
        info.put("USER", authPacket.user);
        info.put("PASSWORD", getPassword(authPacket));
        String url = Constants.URL_PREFIX + Constants.URL_EMBED + dbName;
        ConnectionInfo ci = new ConnectionInfo(url, info);
        return ci.createSession();
    }

    private static String getPassword(AuthPacket authPacket) {
        if (authPacket.password == null || authPacket.password.length == 0)
            return "";
        // TODO MySQL的密码跟Lealone不一样
        return "";
    }

    public void initDatabase(String dbName) {
        session = createSession(authPacket, dbName);
    }

    public void executeStatement(String sql) {
        logger.info("sql: " + sql);
        try {
            PreparedSQLStatement ps = (PreparedSQLStatement) session.prepareSQLCommand(sql, -1);

            if (ps.isQuery()) {
                Result result = ps.executeQuery(-1).get();
                writeQueryResult(result);
            } else {
                int updateCount = ps.executeUpdate().get();
                writeUpdateResult(updateCount);
            }
        } catch (Throwable e) {
            logger.error("Failed to execute statement: " + sql, e);
            sendErrorMessage(e);
        }
    }

    private void writeQueryResult(Result result) {
        PacketOutput out = getPacketOutput();
        int fieldCount = result.getVisibleColumnCount();
        ResultSetHeaderPacket header = PacketUtil.getHeader(fieldCount);
        FieldPacket[] fields = new FieldPacket[fieldCount];
        EOFPacket eof = new EOFPacket();
        byte packetId = 0;
        header.packetId = ++packetId;
        // packetId++;
        for (int i = 0; i < fieldCount; i++) {
            fields[i] = PacketUtil.getField(result.getColumnName(i), result.getColumnType(i));
            fields[i].packetId = ++packetId;
        }
        eof.packetId = ++packetId;

        ByteBuffer buffer = out.allocate();

        // write header
        buffer = header.write(buffer, out);

        // write fields
        for (FieldPacket field : fields) {
            buffer = field.write(buffer, out);
        }

        // write eof
        buffer = eof.write(buffer, out);

        // write rows
        packetId = eof.packetId;
        for (int i = 0; i < result.getRowCount(); i++) {
            RowDataPacket row = new RowDataPacket(fieldCount);
            if (result.next()) {
                Value[] values = result.currentRow();
                for (int j = 0; j < fieldCount; j++) {
                    if (values[j] == ValueNull.INSTANCE) {
                        row.add(new byte[0]);
                    } else {
                        row.add(values[j].toString().getBytes());
                    }
                }
                row.packetId = ++packetId;
                buffer = row.write(buffer, out);
            }
        }

        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        buffer = lastEof.write(buffer, out);

        // post write
        out.write(buffer);
    }

    private void writeUpdateResult(int updateCount) {
        writeOkPacket(updateCount);
    }

    public void writeOkPacket() {
        writeOkPacket(0);
    }

    private void writeOkPacket(int updateCount) {
        PacketOutput out = getPacketOutput();
        OkPacket packet = new OkPacket();
        packet.packetId = 1;
        packet.affectedRows = updateCount;
        packet.serverStatus = 2;
        packet.write(out);
    }

    private final static byte[] encodeString(String src, String charset) {
        if (src == null) {
            return null;
        }
        if (charset == null) {
            return src.getBytes();
        }
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return src.getBytes();
        }
    }

    private void sendErrorMessage(Throwable e) {
        if (e instanceof DbException) {
            DbException dbe = (DbException) e;
            sendErrorMessage(dbe.getErrorCode(), dbe.getMessage());
        } else {
            sendErrorMessage(DbException.convert(e));
        }
    }

    public void sendErrorMessage(int errno, String msg) {
        ErrorPacket err = new ErrorPacket();
        err.packetId = 0;
        err.errno = errno;
        err.message = encodeString(msg, "utf-8");
        err.write(getPacketOutput());
    }

    private PacketOutput getPacketOutput() {
        DataOutputStream out = new DataOutputStream(createNetBufferOutputStream());
        PacketOutput output = new PacketOutput(out);
        return output;
    }

    private NetBufferOutputStream createNetBufferOutputStream() {
        return new NetBufferOutputStream(writableChannel, BUFFER_SIZE);
    }

    private void sendMessage(byte[] data) {
        NetBufferOutputStream out = createNetBufferOutputStream();
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            logger.error("Failed to send message", e);
        }
    }

    private final ByteBuffer packetLengthByteBuffer = ByteBuffer.allocateDirect(4);

    @Override
    public ByteBuffer getPacketLengthByteBuffer() {
        return packetLengthByteBuffer;
    }

    @Override
    public int getPacketLength() {
        int length = (packetLengthByteBuffer.get(0) & 0xff);
        length |= (packetLengthByteBuffer.get(1) & 0xff) << 8;
        length |= (packetLengthByteBuffer.get(2) & 0xff) << 16;
        return length;
    }

    @Override
    public void handle(NetBuffer buffer) {
        if (!buffer.isOnlyOnePacket()) {
            DbException.throwInternalError("NetBuffer must be OnlyOnePacket");
        }
        try {
            int length = buffer.length();
            byte[] packet = new byte[length + 4];
            packetLengthByteBuffer.get(packet, 0, 4);
            packetLengthByteBuffer.clear();
            DataInputStream in = new DataInputStream(new NetBufferInputStream(buffer));
            in.read(packet, 4, length);
            in.close();
            PacketInput input = new PacketInput(packet);
            packetHandler.handle(input);
        } catch (Throwable e) {
            logger.error("Handle packet", e);
            sendErrorMessage(e);
        }
    }
}
