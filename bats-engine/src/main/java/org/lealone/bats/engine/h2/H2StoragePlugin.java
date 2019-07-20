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
package org.lealone.bats.engine.h2;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.JSONOptions;
import org.apache.drill.exec.server.DrillbitContext;
import org.apache.drill.exec.store.AbstractSchema;
import org.apache.drill.exec.store.AbstractStoragePlugin;
import org.apache.drill.exec.store.SchemaConfig;
import org.apache.drill.exec.store.SystemPlugin;
import org.apache.drill.shaded.guava.com.google.common.base.Joiner;
import org.h2.engine.ConnectionInfo;
import org.h2.engine.Constants;
import org.h2.engine.Database;
import org.h2.engine.SysProperties;
import org.h2.schema.Schema;
import org.h2.store.fs.FileUtils;
import org.h2.table.Table;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SystemPlugin
public class H2StoragePlugin extends AbstractStoragePlugin {

    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(H2StoragePlugin.class);
    private static final Map<String, Database> DATABASES = new HashMap<>();
    private final H2StoragePluginConfig engineConfig;

    static Database getDatabase(String dbName) {
        return DATABASES.get(dbName);
    }

    public H2StoragePlugin(DrillbitContext context) throws IOException {
        this(new H2StoragePluginConfig(), context, H2StoragePluginConfig.NAME);
    }

    public H2StoragePlugin(H2StoragePluginConfig configuration, DrillbitContext context, String name)
            throws IOException {
        super(context, name);
        this.engineConfig = configuration;
    }

    @Override
    public void start() throws IOException {
    }

    @Override
    public boolean supportsRead() {
        return true;
    }

    @Override
    public H2GroupScan getPhysicalScan(String userName, JSONOptions selection) throws IOException {
        H2ScanSpec scanSpec = selection.getListWith(new ObjectMapper(), new TypeReference<H2ScanSpec>() {
        });
        return new H2GroupScan(this, scanSpec, null);
    }

    @Override
    public boolean supportsWrite() {
        return true;
    }

    @Override
    public H2StoragePluginConfig getConfig() {
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
            return H2StoragePluginConfig.NAME;
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
                return new H2Table(table, H2StoragePlugin.this.getName(), H2StoragePlugin.this, inner,
                        new H2ScanSpec(table.getDatabase().getName(), table.getSchema().getName(), table.getName()));
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

            String baseDir = SysProperties.getBaseDir();
            if (baseDir == null) {
                baseDir = "./";
            }
            try {
                baseDir = new java.io.File(baseDir).getCanonicalPath();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            // if (!(baseDir.endsWith("/") && baseDir.endsWith("\\"))) {
            // baseDir += "/";
            // }
            logger.info("baseDir: " + baseDir);
            for (String fileName : FileUtils.newDirectoryStream(baseDir)) {
                String catalogName = null;
                if (fileName.endsWith(Constants.SUFFIX_MV_FILE)) {
                    catalogName = fileName.substring(0, fileName.length() - Constants.SUFFIX_MV_FILE.length());
                } else if (fileName.endsWith(Constants.SUFFIX_PAGE_FILE)) {
                    catalogName = fileName.substring(0, fileName.length() - Constants.SUFFIX_PAGE_FILE.length());
                }

                if (catalogName == null)
                    continue;

                try {
                    Database db = DATABASES.get(catalogName);
                    if (db == null) {
                        // String url = "jdbc:h2:file:" + catalogName;
                        ConnectionInfo ci = new ConnectionInfo(catalogName);
                        // db = Engine.getInstance().createSession(ci).getDatabase();
                        db = new Database(ci, null);
                        DATABASES.put(catalogName, db);
                    }

                    catalogName = db.getShortName();
                    CapitalizingJdbcSchema schema = new CapitalizingJdbcSchema(getSchemaPath(), catalogName,
                            catalogName, null);
                    schemaMap.put(catalogName, schema);
                    addSchemas(catalogName, db);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            // 这种方式行不通，因为不知道数据库的用户名和密码
            // String url = "jdbc:h2:mem:" + name;
            // Properties prop = new Properties();
            // prop.setProperty("user", "sa");
            // prop.setProperty("password", "");
            // JdbcConnection conn;
            // try {
            // conn = new JdbcConnection(url, prop);
            // ResultSet rs = conn.getMetaData().getCatalogs();
            // while (rs.next()) {
            // String catalogName = rs.getString(1).toLowerCase();
            // if (catalogName.equalsIgnoreCase(name))
            // continue;
            // catalogName = baseDir + catalogName;
            // ConnectionInfo ci = new ConnectionInfo(catalogName);
            // Database db = Engine.getInstance().createSession(ci).getDatabase();
            //
            // CapitalizingJdbcSchema schema = new CapitalizingJdbcSchema(getSchemaPath(), catalogName,
            // catalogName, null);
            // schemaMap.put(schema.getName(), schema);
            // addSchemas(catalogName, db);
            // }
            // } catch (Exception e) {
            // logger.debug(e.getMessage());
            // }

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
            return H2StoragePluginConfig.NAME;
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
    }
}
