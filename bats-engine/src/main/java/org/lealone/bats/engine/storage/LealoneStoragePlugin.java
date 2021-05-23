/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.bats.engine.storage;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.JSONOptions;
import org.apache.drill.exec.server.DrillbitContext;
import org.apache.drill.exec.store.AbstractSchema;
import org.apache.drill.exec.store.AbstractStoragePlugin;
import org.apache.drill.exec.store.SchemaConfig;
import org.apache.drill.exec.store.SystemPlugin;
import org.apache.drill.shaded.guava.com.google.common.base.Joiner;
import org.lealone.db.Constants;
import org.lealone.db.Database;
import org.lealone.db.LealoneDatabase;
import org.lealone.db.schema.Schema;
import org.lealone.db.table.Table;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SystemPlugin
public class LealoneStoragePlugin extends AbstractStoragePlugin {
    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LealoneStoragePlugin.class);

    private final LealoneStoragePluginConfig engineConfig;
    // private final LealoneSchemaFactory schemaFactory;

    public LealoneStoragePlugin(DrillbitContext context) throws IOException {
        this(new LealoneStoragePluginConfig(), context, LealoneStoragePluginConfig.NAME);
    }

    public LealoneStoragePlugin(LealoneStoragePluginConfig configuration, DrillbitContext context, String name)
            throws IOException {
        super(context, name);
        this.engineConfig = configuration;
        // this.schemaFactory = new LealoneSchemaFactory(name);
    }

    @Override
    public void start() throws IOException {

    }

    @Override
    public boolean supportsRead() {
        return true;
    }

    @Override
    public LealoneGroupScan getPhysicalScan(String userName, JSONOptions selection) throws IOException {
        LealoneScanSpec scanSpec = selection.getListWith(new ObjectMapper(), new TypeReference<LealoneScanSpec>() {
        });
        return new LealoneGroupScan(this, scanSpec, null);
    }

    @Override
    public boolean supportsWrite() {
        return true;
    }

    @Override
    public LealoneStoragePluginConfig getConfig() {
        return engineConfig;
    }

    private class CapitalizingJdbcSchema extends AbstractSchema {

        private final Map<String, CapitalizingJdbcSchema> schemaMap = new HashMap<>();
        private final Schema inner;

        public CapitalizingJdbcSchema(List<String> parentSchemaPath, String name, String catalog, Schema schema) {
            super(parentSchemaPath, name);
            inner = schema;
        }

        @Override
        public String getTypeName() {
            return LealoneStoragePluginConfig.NAME;
        }

        // @Override
        // public Collection<Function> getFunctions(String name) {
        // return inner.getFunctions(name);
        // }
        //
        // @Override
        // public Set<String> getFunctionNames() {
        // return inner.getFunctionNames();
        // }

        @Override
        public CapitalizingJdbcSchema getSubSchema(String name) {
            return schemaMap.get(name);
        }

        void setHolder(SchemaPlus plusOfThis) {
            for (String s : getSubSchemaNames()) {
                CapitalizingJdbcSchema inner = getSubSchema(s);
                SchemaPlus holder = plusOfThis.add(s, inner);
                inner.setHolder(holder);
            }
        }

        @Override
        public Set<String> getSubSchemaNames() {
            return schemaMap.keySet();
        }

        @Override
        public Set<String> getTableNames() {
            if (inner == null)
                return Collections.emptySet();
            Set<String> names = new HashSet<>();
            for (Table t : inner.getAllTablesAndViews()) {
                names.add(t.getName());
            }
            return names;
        }

        @Override
        public String toString() {
            return Joiner.on(".").join(getSchemaPath());
        }

        @Override
        public org.apache.calcite.schema.Table getTable(String name) {
            Table table = inner.findTableOrView(null, name);
            if (table == null) {
                if (!areTableNamesCaseSensitive()) {
                    // Oracle and H2 changes unquoted identifiers to uppercase.
                    table = inner.findTableOrView(null, name.toUpperCase());
                    if (table == null) {
                        // Postgres changes unquoted identifiers to lowercase.
                        table = inner.findTableOrView(null, name.toLowerCase());
                    }
                }
            }

            // no table was found.
            if (table != null) {
                return new LealoneTable(table, LealoneStoragePlugin.this.getName(), LealoneStoragePlugin.this, inner,
                        new LealoneScanSpec(table.getDatabase().getName(), table.getSchema().getName(),
                                table.getName()));
            } else {
                return null;
            }
        }

        // @Override
        // public boolean areTableNamesCaseSensitive() {
        // return !config.areTableNamesCaseInsensitive();
        // }
    }

    private class JdbcCatalogSchema extends AbstractSchema {

        private final Map<String, CapitalizingJdbcSchema> schemaMap = new HashMap<>();
        private final CapitalizingJdbcSchema defaultSchema;

        public JdbcCatalogSchema(String name) {
            super(Collections.emptyList(), name);
            for (Database db : LealoneDatabase.getInstance().getDatabases()) {
                final String catalogName = db.getName().toLowerCase();
                CapitalizingJdbcSchema schema = new CapitalizingJdbcSchema(getSchemaPath(), catalogName, catalogName,
                        null);
                schemaMap.put(schema.getName(), schema);

                addSchemas(catalogName, db);
            }
            defaultSchema = schemaMap.values().iterator().next();
        }

        void setHolder(SchemaPlus plusOfThis) {
            for (String s : getSubSchemaNames()) {
                CapitalizingJdbcSchema inner = getSubSchema(s);
                SchemaPlus holder = plusOfThis.add(s, inner);
                inner.setHolder(holder);
            }
        }

        private boolean addSchemas(String catalogName, Database db) {
            boolean added = false;
            for (Schema schema : db.getAllSchemas()) {
                final String schemaName = schema.getName();

                CapitalizingJdbcSchema parentSchema = schemaMap.get(catalogName);
                CapitalizingJdbcSchema subSchema = new CapitalizingJdbcSchema(parentSchema.getSchemaPath(), schemaName,
                        catalogName, schema);
                parentSchema.schemaMap.put(schemaName, subSchema);

                added = true;
            }
            return added;
        }

        @Override
        public String getTypeName() {
            return LealoneStoragePluginConfig.NAME;
        }

        @Override
        public org.apache.calcite.schema.Schema getDefaultSchema() {
            return defaultSchema;
        }

        @Override
        public CapitalizingJdbcSchema getSubSchema(String name) {
            return schemaMap.get(name);
        }

        @Override
        public Set<String> getSubSchemaNames() {
            return schemaMap.keySet();
        }

        @Override
        public org.apache.calcite.schema.Table getTable(String name) {
            org.apache.calcite.schema.Schema schema = getDefaultSchema();

            if (schema != null) {
                try {
                    return schema.getTable(name);
                } catch (RuntimeException e) {
                    logger.warn("Failure while attempting to read table '{}' from JDBC source.", name, e);
                }
            }
            // no table was found.
            return null;
        }

        @Override
        public Set<String> getTableNames() {
            return defaultSchema.getTableNames();
        }

        @Override
        public boolean areTableNamesCaseSensitive() {
            return defaultSchema.areTableNamesCaseSensitive();
        }
    }

    @Override
    public void registerSchemas(SchemaConfig config, SchemaPlus parent) throws IOException {
        JdbcCatalogSchema schema = new JdbcCatalogSchema(getName());
        SchemaPlus holder = parent.add(getName(), schema);
        schema.setHolder(holder);
        // schemaFactory.registerSchemas(config, parent);
    }

    public void registerSchema(SchemaPlus parent, String dbName, SchemaPlus defaultSchema) {
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
                LealoneTable t = new LealoneTable(table, getName(), this, schema,
                        new LealoneScanSpec(dbName, schemaName, table.getName()));
                subSchema.add(table.getName().toUpperCase(), t);
                subSchema.add(table.getName().toLowerCase(), t);
            }
            parent.add(schemaName, subSchema);
        }
    }
}
