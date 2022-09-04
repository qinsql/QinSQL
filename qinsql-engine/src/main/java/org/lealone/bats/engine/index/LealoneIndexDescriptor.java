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
package org.lealone.bats.engine.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelFieldCollation.NullDirection;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rex.RexNode;
import org.apache.drill.common.expression.LogicalExpression;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.physical.base.GroupScan;
import org.apache.drill.exec.physical.base.IndexGroupScan;
import org.apache.drill.exec.planner.cost.DrillCostBase;
import org.apache.drill.exec.planner.cost.PluginCost;
import org.apache.drill.exec.planner.index.CollationContext;
import org.apache.drill.exec.planner.index.DrillIndexDefinition;
import org.apache.drill.exec.planner.index.DrillIndexDescriptor;
import org.apache.drill.exec.planner.index.FunctionalIndexInfo;
import org.apache.drill.exec.planner.index.IndexDescriptor;
import org.apache.drill.exec.planner.index.IndexProperties;
import org.apache.drill.exec.planner.logical.DrillTable;
import org.lealone.bats.engine.storage.LealoneGroupScan;
import org.lealone.db.table.Table;

public class LealoneIndexDescriptor extends DrillIndexDescriptor {
    static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LealoneIndexDescriptor.class);
    /**
     * The name of Drill's Storage Plugin on which the Index was stored
     */
    private String storage;

    private DrillTable table;
    LealoneGroupScan gs;

    public LealoneIndexDescriptor(Table table, LealoneGroupScan gs, List<LogicalExpression> indexCols,
            CollationContext indexCollationContext, List<LogicalExpression> nonIndexCols,
            List<LogicalExpression> rowKeyColumns, String indexName, String tableName, IndexType type,
            NullDirection nullsDirection) {
        super(indexCols, indexCollationContext, nonIndexCols, rowKeyColumns, indexName, tableName, type,
                nullsDirection);
        this.gs = gs;
        this.table = new LealoneIndexTable(table, gs.getStoragePlugin().getName(), gs.getStoragePlugin(),
                table.getSchema(), gs.getLealoneScanSpec(), indexCols, indexName);
    }

    public LealoneIndexDescriptor(DrillIndexDefinition def) {
        super(def);
    }

    @Override
    public double getRows(RelNode scan, RexNode indexCondition) {
        // TODO: real implementation is to use Drill's stats implementation. for now return fake value 1.0
        return 1.0;
    }

    @Override
    public RelOptCost getCost(IndexProperties indexProps, RelOptPlanner planner, int numProjectedFields,
            GroupScan primaryGroupScan) {
        return new DrillCostBase.DrillCostFactory().makeZeroCost();
    }

    @Override
    public RelCollation getCollation() {
        return null;
    }

    @Override
    public IndexGroupScan getIndexGroupScan() {
        try {
            final DrillTable idxTable = getDrillTable();
            GroupScan scan = idxTable.getGroupScan();

            if (!(scan instanceof IndexGroupScan)) {
                logger.error("The Groupscan from table {} is not an IndexGroupScan", idxTable.toString());
                return null;
            }
            return (IndexGroupScan) scan;
        } catch (IOException e) {
            logger.error("Error in getIndexGroupScan ", e);
        }
        return null;
    }

    /**
     * Set the storage plugin name
     * @param storageName
     */
    @Override
    public void setStorageName(String storageName) {
        storage = storageName;
    }

    /**
     * Get storage plugin name for this index descriptor
     * @return name of the storage plugin
     */
    @Override
    public String getStorageName() {
        return storage;
    }

    /**
     * Set the drill table corresponding to the index
     * @param table
     */
    @Override
    public void setDrillTable(DrillTable table) {
        this.table = table;
    }

    /**
     * Get the drill table corresponding to the index descriptor
     * @return instance of DrillTable
     */
    @Override
    public DrillTable getDrillTable() {
        return this.table;
    }

    @Override
    public FunctionalIndexInfo getFunctionalInfo() {
        return new FunctionalIndexInfo() {

            @Override
            public boolean hasFunctional() {
                return false;
            }

            @Override
            public IndexDescriptor getIndexDesc() {
                return LealoneIndexDescriptor.this;
            }

            @Override
            public SchemaPath getNewPath(SchemaPath path) {
                return SchemaPath.getSimplePath(path.toString());
            }

            @Override
            public SchemaPath getNewPathFromExpr(LogicalExpression expr) {
                return SchemaPath.getSimplePath(expr.toString());
            }

            @Override
            public Map<LogicalExpression, Set<SchemaPath>> getPathsInFunctionExpr() {
                return new HashMap<>();
            }

            @Override
            public Map<LogicalExpression, LogicalExpression> getExprMap() {
                return new HashMap<>();
            }

            @Override
            public Set<SchemaPath> allNewSchemaPaths() {
                return new HashSet<>();
            }

            @Override
            public Set<SchemaPath> allPathsInFunction() {
                return new HashSet<>();
            }

            @Override
            public boolean supportEqualCharConvertToLike() {
                return false;
            }
        };
    }

    @Override
    public PluginCost getPluginCostModel() {
        return null;
    }
}
