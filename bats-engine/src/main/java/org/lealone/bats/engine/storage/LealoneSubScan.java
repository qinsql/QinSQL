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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.physical.base.AbstractBase;
import org.apache.drill.exec.physical.base.PhysicalOperator;
import org.apache.drill.exec.physical.base.PhysicalVisitor;
import org.apache.drill.exec.physical.base.SubScan;
import org.apache.drill.exec.proto.UserBitShared.CoreOperatorType;
import org.apache.drill.exec.store.StoragePluginRegistry;
import org.apache.drill.shaded.guava.com.google.common.base.Preconditions;
import org.apache.drill.shaded.guava.com.google.common.collect.ImmutableSet;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

// Class containing information for reading a single Lealone tablet
@JsonTypeName("lealone-sub-scan")
public class LealoneSubScan extends AbstractBase implements SubScan {
    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LealoneSubScan.class);

    private final LealoneStoragePlugin lealoneStoragePlugin;
    private final List<LealoneSubScanSpec> tabletScanSpecList;
    private final List<SchemaPath> columns;

    @JsonCreator
    public LealoneSubScan(@JacksonInject StoragePluginRegistry registry,
            @JsonProperty("lealoneStoragePluginConfig") LealoneStoragePluginConfig lealoneStoragePluginConfig,
            @JsonProperty("tabletScanSpecList") LinkedList<LealoneSubScanSpec> tabletScanSpecList,
            @JsonProperty("columns") List<SchemaPath> columns) throws ExecutionSetupException {
        super((String) null);
        lealoneStoragePlugin = (LealoneStoragePlugin) registry.getPlugin(lealoneStoragePluginConfig);
        this.tabletScanSpecList = tabletScanSpecList;
        this.columns = columns;
    }

    public LealoneSubScan(LealoneStoragePlugin plugin, List<LealoneSubScanSpec> tabletInfoList,
            List<SchemaPath> columns) {
        super((String) null);
        this.lealoneStoragePlugin = plugin;
        this.tabletScanSpecList = tabletInfoList;
        this.columns = columns;
    }

    public LealoneStoragePluginConfig getLealoneStoragePluginConfig() {
        return lealoneStoragePlugin.getConfig();
    }

    public List<LealoneSubScanSpec> getTabletScanSpecList() {
        return tabletScanSpecList;
    }

    public List<SchemaPath> getColumns() {
        return columns;
    }

    @Override
    public boolean isExecutable() {
        return false;
    }

    @JsonIgnore
    public LealoneStoragePlugin getStorageEngine() {
        return lealoneStoragePlugin;
    }

    @Override
    public <T, X, E extends Throwable> T accept(PhysicalVisitor<T, X, E> physicalVisitor, X value) throws E {
        return physicalVisitor.visitSubScan(this, value);
    }

    @Override
    public PhysicalOperator getNewWithChildren(List<PhysicalOperator> children) {
        Preconditions.checkArgument(children.isEmpty());
        return new LealoneSubScan(lealoneStoragePlugin, tabletScanSpecList, columns);
    }

    @Override
    public Iterator<PhysicalOperator> iterator() {
        return ImmutableSet.<PhysicalOperator> of().iterator();
    }

    public static class LealoneSubScanSpec {

        private final LealoneScanSpec scanSpec;

        private final String tableName;
        private final byte[] startKey;
        private final byte[] endKey;

        @JsonCreator
        public LealoneSubScanSpec(@JsonProperty("scanSpec") LealoneScanSpec scanSpec,
                @JsonProperty("tableName") String tableName, @JsonProperty("startKey") byte[] startKey,
                @JsonProperty("endKey") byte[] endKey) {
            this.scanSpec = scanSpec;
            this.tableName = tableName;
            this.startKey = startKey;
            this.endKey = endKey;
        }

        public LealoneScanSpec getScanSpec() {
            return scanSpec;
        }

        public String getTableName() {
            return tableName;
        }

        public byte[] getStartKey() {
            return startKey;
        }

        public byte[] getEndKey() {
            return endKey;
        }
    }

    @Override
    public int getOperatorType() {
        return CoreOperatorType.KUDU_SUB_SCAN_VALUE;
    }
}
