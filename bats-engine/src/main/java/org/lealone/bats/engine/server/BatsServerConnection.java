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
package org.lealone.bats.engine.server;

import java.io.IOException;

import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.exec.proto.UserProtos;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.work.user.UserWorker;
import org.lealone.bats.engine.storage.LealoneStoragePlugin;
import org.lealone.bats.engine.storage.LealoneStoragePluginConfig;
import org.lealone.common.exceptions.DbException;
import org.lealone.common.exceptions.UnsupportedSchemaException;
import org.lealone.db.Constants;
import org.lealone.db.result.Result;
import org.lealone.db.session.ServerSession;
import org.lealone.db.session.Session;
import org.lealone.net.TransferInputStream;
import org.lealone.net.TransferOutputStream;
import org.lealone.net.WritableChannel;
import org.lealone.server.TcpServerConnection;
import org.lealone.server.protocol.PacketType;
import org.lealone.server.protocol.statement.StatementQueryAck;
import org.lealone.sql.PreparedSQLStatement;
import org.lealone.sql.dml.Query;

public class BatsServerConnection extends TcpServerConnection {

    // private static final Logger logger = LoggerFactory.getLogger(BatsServerConnection.class);
    private final boolean useBatsEngineForQuery = Boolean
            .parseBoolean(System.getProperty("use.bats.engine.for.query", "true"));

    private final BatsServer server;

    protected BatsServerConnection(BatsServer server, WritableChannel writableChannel, boolean isServer) {
        super(server, writableChannel, isServer);
        this.server = server;
    }

    @Override
    protected void handleRequest(TransferInputStream in, int packetId, int packetType) throws IOException {
        try {
            super.handleRequest(in, packetId, packetType);
        } catch (UnsupportedSchemaException e) {
            ServerSession session = (ServerSession) e.getSession();
            if (packetType == PacketType.PREPARED_STATEMENT_PREPARE.value
                    || packetType == PacketType.PREPARED_STATEMENT_PREPARE_READ_PARAMS.value) {
                TransferOutputStream out = createTransferOutputStream(session);
                out.writeResponseHeader(packetId, Session.STATUS_OK);
                out.writeBoolean(true);
                if (packetType == PacketType.PREPARED_STATEMENT_PREPARE_READ_PARAMS.value) {
                    out.writeInt(0);
                }
                out.flush();
                return;
            }
            String sql = e.getSql();
            int fetchSize = Integer.MAX_VALUE;
            PreparedSQLStatement stmt = session.prepareStatement(sql, fetchSize);
            if (useBatsEngineForQuery && stmt instanceof Query) {
                executeQueryAsync(session, sql, packetId, fetchSize, true);
            }
        }
    }

    private void executeQueryAsync(ServerSession session, String sql, int packetId, int fetchSize,
            boolean useDefaultSchema) throws IOException {
        Drillbit drillbit = server.getDrillbit();
        UserProtos.RunQuery runQuery = UserProtos.RunQuery.newBuilder().setPlan(sql)
                .setType(org.apache.drill.exec.proto.UserBitShared.QueryType.SQL).build();
        UserWorker userWorker = drillbit.getWorkManager().getUserWorker();
        SchemaPlus schema = null;
        if (useDefaultSchema) {
            LealoneStoragePlugin lsp;
            try {
                lsp = (LealoneStoragePlugin) drillbit.getStoragePluginRegistry()
                        .getPlugin(LealoneStoragePluginConfig.NAME);
            } catch (ExecutionSetupException e) {
                throw DbException.throwInternalError();
            }

            SchemaPlus defaultSchema = CalciteSchema.createRootSchema(false, true, Constants.SCHEMA_MAIN).plus();
            String dbName = session.getDatabase().getShortName();
            schema = CalciteSchema.createRootSchema(defaultSchema, false, true, dbName).plus();
            lsp.registerSchema(schema, dbName, defaultSchema);
        }
        BatsClientConnection clientConnection = new BatsClientConnection(schema, session.getUserName(), userWorker,
                getWritableChannel().getSocketChannel().getRemoteAddress(), res -> {
                    if (res.isSucceeded()) {
                        Result result = res.getResult();
                        try {
                            int rowCount = result.getRowCount();
                            int fetch = fetchSize;
                            if (rowCount != -1)
                                fetch = Math.min(rowCount, fetchSize);
                            StatementQueryAck ack = new StatementQueryAck(result, rowCount, fetch);
                            TransferOutputStream out = createTransferOutputStream(session);
                            out.writeResponseHeader(packetId, Session.STATUS_OK);
                            ack.encode(out, session.getProtocolVersion());
                            out.flush();
                        } catch (Exception e) {
                            sendError(session, packetId, e);
                        }
                    }
                });
        userWorker.submitWork(clientConnection, runQuery);
    }
}
