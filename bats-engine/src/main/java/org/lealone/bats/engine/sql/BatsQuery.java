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
package org.lealone.bats.engine.sql;

import java.util.ArrayList;

import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.exec.proto.UserProtos;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.store.SchemaTreeProvider;
import org.apache.drill.exec.work.user.UserWorker;
import org.lealone.bats.engine.server.BatsClientConnection;
import org.lealone.bats.engine.server.BatsServer;
import org.lealone.bats.engine.storage.LealoneStoragePlugin;
import org.lealone.bats.engine.storage.LealoneStoragePluginConfig;
import org.lealone.common.exceptions.DbException;
import org.lealone.db.Constants;
import org.lealone.db.PluginManager;
import org.lealone.db.async.AsyncHandler;
import org.lealone.db.async.AsyncResult;
import org.lealone.db.index.Cursor;
import org.lealone.db.result.LocalResult;
import org.lealone.db.result.Result;
import org.lealone.db.result.ResultTarget;
import org.lealone.db.session.ServerSession;
import org.lealone.db.session.SessionStatus;
import org.lealone.net.NetNode;
import org.lealone.server.ProtocolServerEngine;
import org.lealone.sql.SQLStatement;
import org.lealone.sql.StatementBase;
import org.lealone.sql.executor.YieldableBase;
import org.lealone.sql.query.YieldableQueryBase;

public class BatsQuery extends StatementBase {

    private final String sql;
    private LocalResult localResult;
    private Cursor cursor;
    private boolean stopped;

    public BatsQuery(ServerSession session, String sql) {
        super(session);
        this.sql = sql;
        parameters = new ArrayList<>();
    }

    public LocalResult getLocalResult() {
        return localResult;
    }

    public void setLocalResult(LocalResult localResult) {
        this.localResult = localResult;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
    }

    @Override
    public int getType() {
        return SQLStatement.SELECT;
    }

    @Override
    public boolean isQuery() {
        return true;
    }

    @Override
    public Result query(int maxRows) {
        YieldableBatsQuery yieldable = new YieldableBatsQuery(this, maxRows, false, null, null);
        return syncExecute(yieldable);
    }

    @Override
    public YieldableBase<Result> createYieldableQuery(int maxRows, boolean scrollable,
            AsyncHandler<AsyncResult<Result>> asyncHandler) {
        return new YieldableBatsQuery(this, maxRows, scrollable, asyncHandler, null);
    }

    private static class YieldableBatsQuery extends YieldableQueryBase {

        private final BatsQuery select;
        // private final ResultTarget target;
        private Result result;

        public YieldableBatsQuery(BatsQuery select, int maxRows, boolean scrollable,
                AsyncHandler<AsyncResult<Result>> asyncHandler, ResultTarget target) {
            super(select, maxRows, scrollable, asyncHandler);
            this.select = select;
            // this.target = target;
        }

        @Override
        protected boolean startInternal() {
            return false;
        }

        @Override
        protected void stopInternal() {
        }

        @Override
        protected void executeInternal() {
            if (result == null && this.pendingException == null) {
                session.setStatus(SessionStatus.STATEMENT_RUNNING);
                executeQueryAsync(select.getSession(), select.sql, true);
            }
        }

        private void executeQueryAsync(ServerSession session, String sql, boolean useDefaultSchema) {
            Drillbit drillbit = ((BatsServer) PluginManager
                    .getPlugin(ProtocolServerEngine.class, BatsSQLEngine.NAME).getProtocolServer())
                            .getDrillbit();
            UserProtos.RunQuery runQuery = UserProtos.RunQuery.newBuilder().setPlan(sql)
                    .setType(org.apache.drill.exec.proto.UserBitShared.QueryType.SQL).build();
            UserWorker userWorker = drillbit.getWorkManager().getUserWorker();
            SchemaTreeProvider schemaTreeProvider = new SchemaTreeProvider(
                    drillbit.getWorkManager().getContext());
            SchemaPlus rootSchema = schemaTreeProvider.createRootSchema(userWorker.getSystemOptions());
            if (useDefaultSchema && sql.contains(LealoneStoragePluginConfig.NAME)) {
                LealoneStoragePlugin lsp;
                try {
                    lsp = (LealoneStoragePlugin) drillbit.getStoragePluginRegistry()
                            .getPlugin(LealoneStoragePluginConfig.NAME);
                } catch (ExecutionSetupException e) {
                    throw DbException.throwInternalError();
                }

                SchemaPlus defaultSchema = CalciteSchema
                        .createRootSchema(false, true, Constants.SCHEMA_MAIN).plus();
                String dbName = session.getDatabase().getShortName();
                SchemaPlus schema = CalciteSchema.createRootSchema(defaultSchema, false, true, dbName)
                        .plus();
                lsp.registerSchema(schema, dbName, defaultSchema);
                rootSchema.add(LealoneStoragePluginConfig.NAME, defaultSchema);
            }
            BatsClientConnection clientConnection = new BatsClientConnection(rootSchema, session,
                    userWorker, NetNode.getLocalTcpNode().getInetSocketAddress(),
                    select.getLocalResult(), select, res -> {
                        if (res.isSucceeded()) {
                            result = res.getResult();
                            setResult(result, result.getRowCount());
                        } else {
                            setPendingException(res.getCause());
                        }
                        session.setStatus(SessionStatus.STATEMENT_COMPLETED);
                        session.getTransactionListener().wakeUp();
                    });
            clientConnection.setCursor(select.getCursor());
            userWorker.submitWork(clientConnection, runQuery);
        }
    }
}
