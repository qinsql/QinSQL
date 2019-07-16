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
package org.apache.calcite.materialize;

import java.util.List;

import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.CalciteSchema;
import org.apache.calcite.schema.impl.StarTable;

/** Describes that a given SQL query is materialized by a given table.
   * The materialization is currently valid, and can be used in the planning
   * process. */
public class Materialization {
    /** The table that holds the materialized data. */
    final CalciteSchema.TableEntry materializedTable;
    /** The query that derives the data. */
    final String sql;
    /** The schema path for the query. */
    final List<String> viewSchemaPath;
    /** Relational expression for the table. Usually a
     * {@link org.apache.calcite.rel.logical.LogicalTableScan}. */
    RelNode tableRel;
    /** Relational expression for the query to populate the table. */
    RelNode queryRel;
    /** Star table identified. */
    //private RelOptTable starRelOptTable;

    public Materialization(CalciteSchema.TableEntry materializedTable, String sql, List<String> viewSchemaPath) {
        assert materializedTable != null;
        assert sql != null;
        this.materializedTable = materializedTable;
        this.sql = sql;
        this.viewSchemaPath = viewSchemaPath;
    }

    public void materialize(RelNode queryRel, RelOptTable starRelOptTable) {
        this.queryRel = queryRel;
        //this.starRelOptTable = starRelOptTable;
        assert starRelOptTable.unwrap(StarTable.class) != null;
    }
}