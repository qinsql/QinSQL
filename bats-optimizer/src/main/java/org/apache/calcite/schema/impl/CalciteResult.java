/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.schema.impl;

import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.util.ImmutableIntList;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public interface CalciteResult {

    /** The result of parsing and validating a SQL query. */
    class ParseResult {
        public final String sql; // for debug
        public final SqlNode sqlNode;
        public final RelDataType rowType;
        public final RelDataTypeFactory typeFactory;

        public ParseResult(SqlValidator validator, String sql, SqlNode sqlNode, RelDataType rowType) {
            super();
            this.sql = sql;
            this.sqlNode = sqlNode;
            this.rowType = rowType;
            this.typeFactory = validator.getTypeFactory();
        }

        /** Returns the kind of statement.
         *
         * <p>Possibilities include:
         *
         * <ul>
         *   <li>Queries: usually {@link SqlKind#SELECT}, but
         *   other query operators such as {@link SqlKind#UNION} and
         *   {@link SqlKind#ORDER_BY} are possible
         *   <li>DML statements: {@link SqlKind#INSERT}, {@link SqlKind#UPDATE} etc.
         *   <li>Session control statements: {@link SqlKind#COMMIT}
         *   <li>DDL statements: {@link SqlKind#CREATE_TABLE},
         *   {@link SqlKind#DROP_INDEX}
         * </ul>
         *
         * @return Kind of statement, never null
         */
        public SqlKind kind() {
            return sqlNode.getKind();
        }
    }

    /** The result of parsing and validating a SQL query and converting it to
     * relational algebra. */
    class ConvertResult extends ParseResult {
        public final RelRoot root;

        public ConvertResult(SqlValidator validator, String sql, SqlNode sqlNode, RelDataType rowType, RelRoot root) {
            super(validator, sql, sqlNode, rowType);
            this.root = root;
        }
    }

    /** The result of analyzing a view. */
    class AnalyzeViewResult extends ConvertResult {
        /** Not null if and only if the view is modifiable. */
        public final Table table;
        public final ImmutableList<String> tablePath;
        public final RexNode constraint;
        public final ImmutableIntList columnMapping;
        public final boolean modifiable;

        public AnalyzeViewResult(SqlValidator validator, String sql, SqlNode sqlNode, RelDataType rowType, RelRoot root,
                Table table, ImmutableList<String> tablePath, RexNode constraint, ImmutableIntList columnMapping,
                boolean modifiable) {
            super(validator, sql, sqlNode, rowType, root);
            this.table = table;
            this.tablePath = tablePath;
            this.constraint = constraint;
            this.columnMapping = columnMapping;
            this.modifiable = modifiable;
            Preconditions.checkArgument(modifiable == (table != null));
        }
    }
}

// End CalcitePrepare.java
