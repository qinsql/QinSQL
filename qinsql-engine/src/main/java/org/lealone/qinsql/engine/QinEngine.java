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
package org.lealone.qinsql.engine;

import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.exec.server.Drillbit;
import org.apache.drill.exec.store.SchemaTreeProvider;
import org.apache.drill.exec.store.StoragePluginRegistry;
import org.lealone.common.exceptions.DbException;
import org.lealone.db.Constants;
import org.lealone.db.Database;
import org.lealone.db.LealoneDatabase;
import org.lealone.db.PluginManager;
import org.lealone.db.schema.Schema;
import org.lealone.db.session.ServerSession;
import org.lealone.db.table.Table;
import org.lealone.qinsql.engine.server.QinServer;
import org.lealone.qinsql.engine.sql.QinSQLEngine;
import org.lealone.qinsql.engine.storage.LealoneScanSpec;
import org.lealone.qinsql.engine.storage.LealoneStoragePlugin;
import org.lealone.qinsql.engine.storage.LealoneStoragePluginConfig;
import org.lealone.qinsql.engine.storage.LealoneTable;
import org.lealone.server.ProtocolServerEngine;

public class QinEngine {

    public static SqlNode parse(String sql) throws SqlParseException {
        SqlParser.Config config = SqlParser.configBuilder()
                .setUnquotedCasing(org.apache.calcite.util.Casing.TO_LOWER).build();
        return parse(sql, config);
    }

    public static SqlNode parse(String sql, SqlParser.Config config) throws SqlParseException {
        SqlParser sqlParser = SqlParser.create(sql, config);
        return sqlParser.parseQuery();
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
