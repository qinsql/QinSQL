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
package org.qinsql.engine.sql;

import java.util.ArrayList;

import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.exec.proto.UserProtos;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.store.SchemaTreeProvider;
import org.apache.drill.exec.store.StoragePluginRegistry;
import org.apache.drill.exec.work.user.UserWorker;
import org.lealone.common.exceptions.DbException;
import org.lealone.db.Constants;
import org.lealone.db.Database;
import org.lealone.db.LealoneDatabase;
import org.lealone.db.PluginManager;
import org.lealone.db.async.AsyncHandler;
import org.lealone.db.async.AsyncResult;
import org.lealone.db.index.Cursor;
import org.lealone.db.result.LocalResult;
import org.lealone.db.result.Result;
import org.lealone.db.result.ResultTarget;
import org.lealone.db.schema.Schema;
import org.lealone.db.session.ServerSession;
import org.lealone.db.session.SessionStatus;
import org.lealone.db.table.Table;
import org.lealone.net.NetNode;
import org.lealone.server.ProtocolServerEngine;
import org.lealone.sql.SQLStatement;
import org.lealone.sql.StatementBase;
import org.lealone.sql.executor.YieldableBase;
import org.lealone.sql.query.YieldableQueryBase;
import org.qinsql.engine.server.QinClientConnection;
import org.qinsql.engine.server.QinServer;
import org.qinsql.engine.storage.LealoneScanSpec;
import org.qinsql.engine.storage.LealoneStoragePlugin;
import org.qinsql.engine.storage.LealoneStoragePluginConfig;
import org.qinsql.engine.storage.LealoneTable;

public class QinQuery extends StatementBase {

    private final String sql;
    private LocalResult localResult;
    private Cursor cursor;
    private boolean stopped;
    private boolean useDefaultSchema;

    public QinQuery(ServerSession session, String sql, boolean useDefaultSchema) {
        super(session);
        this.sql = sql;
        this.useDefaultSchema = useDefaultSchema;
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
        YieldableQinQuery yieldable = new YieldableQinQuery(this, maxRows, false, null, null);
        return syncExecute(yieldable);
    }

    @Override
    public YieldableBase<Result> createYieldableQuery(int maxRows, boolean scrollable,
            AsyncHandler<AsyncResult<Result>> asyncHandler) {
        return new YieldableQinQuery(this, maxRows, scrollable, asyncHandler, null);
    }

    private static class YieldableQinQuery extends YieldableQueryBase {

        private final QinQuery select;
        // private final ResultTarget target;
        private Result result;

        public YieldableQinQuery(QinQuery select, int maxRows, boolean scrollable,
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

            SchemaPlus rootSchema = getRootSchema(session, sql, useDefaultSchema,
                    select.localResult != null);

            Drillbit drillbit = ((QinServer) PluginManager
                    .getPlugin(ProtocolServerEngine.class, QinSQLEngine.NAME).getProtocolServer())
                            .getDrillbit();
            UserProtos.RunQuery runQuery = UserProtos.RunQuery.newBuilder().setPlan(sql)
                    .setType(org.apache.drill.exec.proto.UserBitShared.QueryType.SQL).build();
            UserWorker userWorker = drillbit.getWorkManager().getUserWorker();

            QinClientConnection clientConnection = new QinClientConnection(rootSchema,
                    select.useDefaultSchema, session, userWorker,
                    NetNode.getLocalTcpNode().getInetSocketAddress(), select.getLocalResult(), select,
                    res -> {
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

    public static SchemaPlus getRootSchema(ServerSession session, String sql, boolean useDefaultSchema,
            boolean isOlap) {
        Drillbit drillbit = ((QinServer) PluginManager
                .getPlugin(ProtocolServerEngine.class, QinSQLEngine.NAME).getProtocolServer())
                        .getDrillbit();
        StoragePluginRegistry storageRegistry = drillbit.getContext().getStorage();
        if (isOlap) {
            LealoneStoragePlugin lsp;
            try {
                lsp = (LealoneStoragePlugin) storageRegistry.getPlugin(LealoneStoragePluginConfig.NAME);
            } catch (ExecutionSetupException e) {
                throw DbException.throwInternalError();
            }
            SchemaPlus parent = CalciteSchema.createRootSchema(false, true, "").plus();
            SchemaPlus defaultSchema = CalciteSchema.createRootSchema(false, true, Constants.SCHEMA_MAIN)
                    .plus();
            String dbName = session.getDatabase().getShortName();
            Database db = LealoneDatabase.getInstance().getDatabase(dbName);
            for (Schema schema : db.getAllSchemas()) {
                final String schemaName = schema.getName();
                final SchemaPlus subSchema;
                if (schemaName.equalsIgnoreCase(Constants.SCHEMA_MAIN)) {
                    subSchema = defaultSchema;
                } else {
                    subSchema = CalciteSchema.createRootSchema(false, true, schemaName).plus();
                }
                for (Table table : schema.getAllTablesAndViews()) {
                    LealoneTable t = new LealoneTable(table, lsp,
                            new LealoneScanSpec(dbName, schemaName, table.getName()));
                    subSchema.add(table.getName().toUpperCase(), t);
                    subSchema.add(table.getName().toLowerCase(), t);
                }
                parent.add(schemaName, subSchema);
            }
            if (session.getCurrentSchemaName().equalsIgnoreCase(Constants.SCHEMA_MAIN))
                return defaultSchema;
            return parent;
        }
        SchemaTreeProvider schemaTreeProvider = new SchemaTreeProvider(drillbit.getContext());
        SchemaPlus rootSchema = schemaTreeProvider
                .createRootSchema(drillbit.getContext().getOptionManager());
        if (useDefaultSchema && (isOlap || sql.contains(LealoneStoragePluginConfig.NAME))) {
            LealoneStoragePlugin lsp;
            try {
                lsp = (LealoneStoragePlugin) storageRegistry.getPlugin(LealoneStoragePluginConfig.NAME);
            } catch (ExecutionSetupException e) {
                throw DbException.throwInternalError();
            }

            SchemaPlus defaultSchema = CalciteSchema.createRootSchema(false, true, Constants.SCHEMA_MAIN)
                    .plus();
            String dbName = session.getDatabase().getShortName();
            SchemaPlus schema = CalciteSchema.createRootSchema(defaultSchema, false, true, dbName)
                    .plus();
            lsp.registerSchema(schema, dbName, defaultSchema);
            rootSchema.add(LealoneStoragePluginConfig.NAME, defaultSchema);
            rootSchema.add("", defaultSchema);
        }
        return rootSchema;
    }
}
