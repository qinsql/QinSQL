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
import java.util.List;

import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.physical.EndpointAffinity;
import org.apache.drill.exec.physical.base.AbstractGroupScan;
import org.apache.drill.exec.physical.base.GroupScan;
import org.apache.drill.exec.physical.base.PhysicalOperator;
import org.apache.drill.exec.physical.base.ScanStats;
import org.apache.drill.exec.physical.base.ScanStats.GroupScanProperty;
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
import org.lealone.bats.engine.h2.H2SubScan.H2SubScanSpec;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("h2-scan")
public class H2GroupScan extends AbstractGroupScan {
    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(H2GroupScan.class);
    private static final long DEFAULT_TABLET_SIZE = 1000;

    private H2StoragePlugin h2StoragePlugin;
    private List<SchemaPath> columns;
    private H2ScanSpec scanSpec;

    private boolean filterPushedDown = false;
    private List<H2Work> h2WorkList = Lists.newArrayList();
    private ListMultimap<Integer, H2Work> assignments;
    private List<EndpointAffinity> affinities;

    @JsonCreator
    public H2GroupScan(@JsonProperty("scanSpec") H2ScanSpec scanSpec,
            @JsonProperty("h2StoragePluginConfig") H2StoragePluginConfig h2StoragePluginConfig,
            @JsonProperty("columns") List<SchemaPath> columns, @JacksonInject StoragePluginRegistry pluginRegistry)
            throws IOException, ExecutionSetupException {
        this((H2StoragePlugin) pluginRegistry.getPlugin(h2StoragePluginConfig), scanSpec, columns);
    }

    public H2GroupScan(H2StoragePlugin h2StoragePlugin, H2ScanSpec scanSpec, List<SchemaPath> columns) {
        super((String) null);
        this.h2StoragePlugin = h2StoragePlugin;
        this.scanSpec = scanSpec;
        this.columns = columns == null || columns.size() == 0 ? ALL_COLUMNS : columns;
    }

    private static class H2Work implements CompleteWork {

        private final EndpointByteMapImpl byteMap = new EndpointByteMapImpl();
        private byte[] partitionKeyStart;
        private byte[] partitionKeyEnd;

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
     * @param that The H2GroupScan to clone
     */
    private H2GroupScan(H2GroupScan that) {
        super(that);
        this.h2StoragePlugin = that.h2StoragePlugin;
        this.columns = that.columns;
        this.scanSpec = that.scanSpec;
        this.filterPushedDown = that.filterPushedDown;
        this.h2WorkList = that.h2WorkList;
        this.assignments = that.assignments;
    }

    @Override
    public GroupScan clone(List<SchemaPath> columns) {
        H2GroupScan newScan = new H2GroupScan(this);
        newScan.columns = columns;
        return newScan;
    }

    @Override
    public List<EndpointAffinity> getOperatorAffinity() {
        if (affinities == null) {
            affinities = AffinityCreator.getAffinityMap(h2WorkList);
        }
        return affinities;
    }

    @Override
    public int getMaxParallelizationWidth() {
        return h2WorkList.size();
    }

    /**
     *
     * @param incomingEndpoints
     */
    @Override
    public void applyAssignments(List<DrillbitEndpoint> incomingEndpoints) {
        assignments = AssignmentCreator.getMappings(incomingEndpoints, h2WorkList);
    }

    @Override
    public H2SubScan getSpecificScan(int minorFragmentId) {
        List<H2Work> workList = assignments.get(minorFragmentId);

        List<H2SubScanSpec> scanSpecList = Lists.newArrayList();

        for (H2Work work : workList) {
            scanSpecList.add(new H2SubScanSpec(scanSpec, getTableName(), work.getPartitionKeyStart(),
                    work.getPartitionKeyEnd()));
        }
        scanSpecList.add(new H2SubScanSpec(scanSpec, getTableName(), null, null));
        return new H2SubScan(h2StoragePlugin, scanSpecList, this.columns);
    }

    @Override
    public ScanStats getScanStats() {
        long recordCount = 100000 * h2WorkList.size();
        return new ScanStats(GroupScanProperty.NO_EXACT_ROW_COUNT, recordCount, 1, recordCount);
    }

    @Override
    @JsonIgnore
    public PhysicalOperator getNewWithChildren(List<PhysicalOperator> children) {
        Preconditions.checkArgument(children.isEmpty());
        return new H2GroupScan(this);
    }

    @JsonIgnore
    public H2StoragePlugin getStoragePlugin() {
        return h2StoragePlugin;
    }

    @JsonIgnore
    public String getTableName() {
        return getH2ScanSpec().getTableName();
    }

    @Override
    public String getDigest() {
        return toString();
    }

    @Override
    public String toString() {
        return "H2GroupScan [H2ScanSpec=" + scanSpec + ", columns=" + columns + "]";
    }

    @JsonProperty
    public H2StoragePluginConfig getH2StoragePluginConfig() {
        return h2StoragePlugin.getConfig();
    }

    @Override
    @JsonProperty
    public List<SchemaPath> getColumns() {
        return columns;
    }

    @JsonProperty
    public H2ScanSpec getH2ScanSpec() {
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

    @JsonIgnore
    public boolean isFilterPushedDown() {
        return filterPushedDown;
    }

    /**
     * Empty constructor, do not use, only for testing.
     */
    @VisibleForTesting
    public H2GroupScan() {
        super((String) null);
    }

    /**
     * Do not use, only for testing.
     */
    @VisibleForTesting
    public void setH2ScanSpec(H2ScanSpec scanSpec) {
        this.scanSpec = scanSpec;
    }

}
