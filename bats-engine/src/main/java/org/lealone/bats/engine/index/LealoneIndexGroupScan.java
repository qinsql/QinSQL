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
package org.lealone.bats.engine.index;

import java.io.IOException;
import java.util.List;

import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.physical.base.IndexGroupScan;
import org.apache.drill.exec.physical.base.ScanStats;
import org.apache.drill.exec.physical.base.ScanStats.GroupScanProperty;
import org.apache.drill.exec.planner.index.Statistics;
import org.apache.drill.exec.store.StoragePluginRegistry;
import org.lealone.bats.engine.storage.LealoneGroupScan;
import org.lealone.bats.engine.storage.LealoneScanSpec;
import org.lealone.bats.engine.storage.LealoneStoragePlugin;
import org.lealone.bats.engine.storage.LealoneStoragePluginConfig;
import org.lealone.bats.engine.storage.LealoneSubScan;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("lealone-index-scan")
public class LealoneIndexGroupScan extends LealoneGroupScan implements IndexGroupScan {
    @JsonCreator
    public LealoneIndexGroupScan(@JsonProperty("scanSpec") LealoneScanSpec scanSpec,
            @JsonProperty("lealoneStoragePluginConfig") LealoneStoragePluginConfig lealoneStoragePluginConfig,
            @JsonProperty("columns") List<SchemaPath> columns, @JacksonInject StoragePluginRegistry pluginRegistry)
            throws IOException, ExecutionSetupException {
        super((LealoneStoragePlugin) pluginRegistry.getPlugin(lealoneStoragePluginConfig), scanSpec, columns);
    }

    public LealoneIndexGroupScan(LealoneStoragePlugin lealoneStoragePlugin, LealoneScanSpec scanSpec,
            List<SchemaPath> columns) {
        super(lealoneStoragePlugin, scanSpec, columns);
    }

    @Override
    public LealoneSubScan getSpecificScan(int minorFragmentId) {
        return super.getSpecificScan(minorFragmentId);
    }

    @Override
    public ScanStats getScanStats() {
        long recordCount = 0;
        return new ScanStats(GroupScanProperty.NO_EXACT_ROW_COUNT, recordCount, 0, recordCount);
    }

    @Override
    public int getRowKeyOrdinal() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setStatistics(Statistics statistics) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColumns(List<SchemaPath> columns) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setParallelizationWidth(int width) {
        // TODO Auto-generated method stub

    }
}
