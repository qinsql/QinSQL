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
package org.qinsql.engine.storage;

import java.io.IOException;
import java.util.List;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rex.RexNode;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.common.logical.StoragePluginConfig;
import org.apache.drill.exec.physical.EndpointAffinity;
import org.apache.drill.exec.physical.base.AbstractDbGroupScan;
import org.apache.drill.exec.physical.base.GroupScan;
import org.apache.drill.exec.physical.base.PhysicalOperator;
import org.apache.drill.exec.physical.base.ScanStats;
import org.apache.drill.exec.physical.base.ScanStats.GroupScanProperty;
import org.apache.drill.exec.planner.common.DrillScanRelBase;
import org.apache.drill.exec.planner.index.IndexCallContext;
import org.apache.drill.exec.planner.index.IndexCollection;
import org.apache.drill.exec.planner.index.IndexDescriptor;
import org.apache.drill.exec.planner.index.IndexDiscover;
import org.apache.drill.exec.planner.index.IndexDiscoverFactory;
import org.apache.drill.exec.planner.index.Statistics;
import org.apache.drill.exec.proto.CoordinationProtos.DrillbitEndpoint;
import org.apache.drill.exec.store.StoragePluginRegistry;
import org.apache.drill.exec.store.schedule.AffinityCreator;
import org.apache.drill.exec.store.schedule.AssignmentCreator;
import org.apache.drill.exec.store.schedule.CompleteWork;
import org.apache.drill.exec.store.schedule.EndpointByteMap;
import org.apache.drill.exec.store.schedule.EndpointByteMapImpl;
import org.apache.drill.shaded.guava.com.google.common.annotations.VisibleForTesting;
import org.apache.drill.shaded.guava.com.google.common.base.Preconditions;
import org.apache.drill.shaded.guava.com.google.common.collect.ListMultimap;
import org.apache.drill.shaded.guava.com.google.common.collect.Lists;
import org.lealone.db.Database;
import org.lealone.db.LealoneDatabase;
import org.lealone.db.schema.Schema;
import org.lealone.db.table.Table;
import org.qinsql.engine.index.LealoneIndexDiscover;
import org.qinsql.engine.storage.LealoneSubScan.LealoneSubScanSpec;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("lealone-scan")
public class LealoneGroupScan extends AbstractDbGroupScan {
    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LealoneGroupScan.class);
    private static final long DEFAULT_TABLET_SIZE = 1000;

    private LealoneStoragePlugin lealoneStoragePlugin;
    private List<SchemaPath> columns;
    private LealoneScanSpec scanSpec;

    private boolean filterPushedDown = false;
    private List<LealoneWork> lealoneWorkList = Lists.newArrayList();
    private ListMultimap<Integer, LealoneWork> assignments;
    private List<EndpointAffinity> affinities;

    private double rowCountApproximation;

    @JsonCreator
    public LealoneGroupScan(@JsonProperty("scanSpec") LealoneScanSpec scanSpec,
            @JsonProperty("lealoneStoragePluginConfig") LealoneStoragePluginConfig lealoneStoragePluginConfig,
            @JsonProperty("columns") List<SchemaPath> columns, @JacksonInject StoragePluginRegistry pluginRegistry)
            throws IOException, ExecutionSetupException {
        this((LealoneStoragePlugin) pluginRegistry.getPlugin(lealoneStoragePluginConfig), scanSpec, columns);
    }

    public LealoneGroupScan(LealoneStoragePlugin lealoneStoragePlugin, LealoneScanSpec scanSpec,
            List<SchemaPath> columns) {
        super((String) null);
        this.lealoneStoragePlugin = lealoneStoragePlugin;
        this.scanSpec = scanSpec;
        this.columns = columns == null || columns.size() == 0 ? ALL_COLUMNS : columns;
        init();
        rowCountApproximation = getRowCountApproximation();
    }

    private void init() {
        // String tableName = scanSpec.getTableName();
        // Collection<DrillbitEndpoint> endpoints = lealoneStoragePlugin.getContext().getBits();
        // Map<String,DrillbitEndpoint> endpointMap = Maps.newHashMap();
        // for (DrillbitEndpoint endpoint : endpoints) {
        // endpointMap.put(endpoint.getAddress(), endpoint);
        // }
        // try {
        // List<LocatedTablet> locations =
        // lealoneStoragePlugin.getClient().openTable(tableName).getTabletsLocations(10000);
        // for (LocatedTablet tablet : locations) {
        // LealoneWork work = new LealoneWork(tablet.getPartition().getPartitionKeyStart(),
        // tablet.getPartition().getPartitionKeyEnd());
        // for (Replica replica : tablet.getReplicas()) {
        // String host = replica.getRpcHost();
        // DrillbitEndpoint ep = endpointMap.get(host);
        // if (ep != null) {
        // work.getByteMap().add(ep, DEFAULT_TABLET_SIZE);
        // }
        // }
        // lealoneWorkList.add(work);
        // }
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
    }

    private static class LealoneWork implements CompleteWork {

        private final EndpointByteMapImpl byteMap = new EndpointByteMapImpl();
        private byte[] partitionKeyStart;
        private byte[] partitionKeyEnd;

        // public LealoneWork(byte[] partitionKeyStart, byte[] partitionKeyEnd) {
        // this.partitionKeyStart = partitionKeyStart;
        // this.partitionKeyEnd = partitionKeyEnd;
        // }

        public byte[] getPartitionKeyStart() {
            return partitionKeyStart;
        }

        public byte[] getPartitionKeyEnd() {
            return partitionKeyEnd;
        }

        @Override
        public long getTotalBytes() {
            return DEFAULT_TABLET_SIZE;
        }

        @Override
        public EndpointByteMap getByteMap() {
            return byteMap;
        }

        @Override
        public int compareTo(CompleteWork o) {
            return 0;
        }
    }

    /**
     * Private constructor, used for cloning.
     * @param that The LealoneGroupScan to clone
     */
    private LealoneGroupScan(LealoneGroupScan that) {
        super(that);
        this.lealoneStoragePlugin = that.lealoneStoragePlugin;
        this.columns = that.columns;
        this.scanSpec = that.scanSpec;
        this.filterPushedDown = that.filterPushedDown;
        this.lealoneWorkList = that.lealoneWorkList;
        this.assignments = that.assignments;
        this.rowCountApproximation = that.rowCountApproximation;
    }

    @Override
    public GroupScan clone(List<SchemaPath> columns) {
        LealoneGroupScan newScan = new LealoneGroupScan(this);
        newScan.columns = columns;
        return newScan;
    }

    @Override
    public List<EndpointAffinity> getOperatorAffinity() {
        if (affinities == null) {
            affinities = AffinityCreator.getAffinityMap(lealoneWorkList);
        }
        return affinities;
    }

    @Override
    public int getMaxParallelizationWidth() {
        return lealoneWorkList.size();
    }

    /**
     *
     * @param incomingEndpoints
     */
    @Override
    public void applyAssignments(List<DrillbitEndpoint> incomingEndpoints) {
        assignments = AssignmentCreator.getMappings(incomingEndpoints, lealoneWorkList);
    }

    @Override
    public LealoneSubScan getSpecificScan(int minorFragmentId) {
        List<LealoneWork> workList = assignments.get(minorFragmentId);

        List<LealoneSubScanSpec> scanSpecList = Lists.newArrayList();

        for (LealoneWork work : workList) {
            scanSpecList.add(new LealoneSubScanSpec(scanSpec, getTableName(), work.getPartitionKeyStart(),
                    work.getPartitionKeyEnd()));
        }
        scanSpecList.add(new LealoneSubScanSpec(scanSpec, getTableName(), null, null));
        return new LealoneSubScan(lealoneStoragePlugin, scanSpecList, this.columns, null);
    }

    // LealoneStoragePlugin plugin, LealoneStoragePluginConfig config,
    // List<LealoneSubScanSpec> tabletInfoList, List<SchemaPath> columns
    @Override
    public ScanStats getScanStats() {
        // long recordCount = 100000 * 1;// lealoneWorkList.size();
        double recordCount = rowCountApproximation * 100000;
        return new ScanStats(GroupScanProperty.NO_EXACT_ROW_COUNT, recordCount, 1, recordCount);
    }

    @Override
    @JsonIgnore
    public PhysicalOperator getNewWithChildren(List<PhysicalOperator> children) {
        Preconditions.checkArgument(children.isEmpty());
        return new LealoneGroupScan(this);
    }

    @Override
    @JsonIgnore
    public LealoneStoragePlugin getStoragePlugin() {
        return lealoneStoragePlugin;
    }

    @JsonIgnore
    public String getTableName() {
        return getLealoneScanSpec().getTableName();
    }

    @Override
    public String getDigest() {
        return toString();
    }

    @Override
    public String toString() {
        return "LealoneGroupScan [LealoneScanSpec=" + scanSpec + ", columns=" + columns + "]";
    }

    @JsonProperty
    public LealoneStoragePluginConfig getLealoneStoragePluginConfig() {
        return lealoneStoragePlugin.getConfig();
    }

    @Override
    public boolean supportsRestrictedScan() {
        return true;
    }

    @Override
    @JsonProperty
    public List<SchemaPath> getColumns() {
        return columns;
    }

    @JsonProperty
    public LealoneScanSpec getLealoneScanSpec() {
        return scanSpec;
    }

    @Override
    @JsonIgnore
    public boolean canPushdownProjects(List<SchemaPath> columns) {
        return true;
    }

    @JsonIgnore
    public void setFilterPushedDown(boolean b) {
        this.filterPushedDown = true;
    }

    @Override
    @JsonIgnore
    public boolean isFilterPushedDown() {
        return filterPushedDown;
    }

    /**
     * Empty constructor, do not use, only for testing.
     */
    @VisibleForTesting
    public LealoneGroupScan() {
        super((String) null);
    }

    /**
     * Do not use, only for testing.
     */
    @VisibleForTesting
    public void setLealoneScanSpec(LealoneScanSpec scanSpec) {
        this.scanSpec = scanSpec;
    }

    @Override
    public IndexCollection getSecondaryIndexCollection(RelNode scanRel) {
        IndexDiscover discover = IndexDiscoverFactory.getIndexDiscover(getStorageConfig(), this, scanRel,
                LealoneIndexDiscover.class);

        if (discover == null) {
            logger.error("Null IndexDiscover was found for {}!", scanRel);
        }
        return discover.getTableIndex(getTableName());
    }

    @Override
    public boolean supportsSecondaryIndex() {
        return true;
    }

    @Override
    public void setRowCount(RexNode condition, double count, double capRowCount) {
    }

    @Override
    public double getRowCount(RexNode condition, RelNode scanRel) {
        return getRowCountApproximation();
    }

    private double getRowCountApproximation() {
        Database db = LealoneDatabase.getInstance().getDatabase(scanSpec.getDbName());
        Schema schema = db.getSchema(null, scanSpec.getSchemaName());
        Table table = schema.getTableOrView(null, scanSpec.getTableName());
        return table.getRowCountApproximation();
    }

    @Override
    public Statistics getStatistics() {
        return new Statistics() {

            @Override
            public boolean isStatsAvailable() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String buildUniqueIndexIdentifier(IndexDescriptor idx) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public double getRowCount(RexNode condition, String tabIdxName, RelNode scanRel) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public double getLeadingRowCount(RexNode condition, String tabIdxName, DrillScanRelBase scanRel) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public double getAvgRowSize(String tabIdxName, boolean isIndexScan) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean initialize(RexNode condition, DrillScanRelBase scanRel, IndexCallContext context) {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }

    @Override
    public void setCostFactor(double sel) {

    }

    @Override
    public boolean isIndexScan() {
        return false;
    }

    @Override
    public String getIndexHint() {
        return "";
    }

    @Override
    public StoragePluginConfig getStorageConfig() {
        return lealoneStoragePlugin.getConfig();
    }
}
