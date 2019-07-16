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
package org.apache.calcite.rex;

import java.util.List;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.fun.SqlQuantifyOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;

import com.google.common.collect.ImmutableList;

/**
 * Scalar expression that represents an IN, EXISTS or scalar sub-query.
 */
public interface RexSubQuery extends RexCall {

    RexSubQuery clone(RelNode rel);

    RelNode getRel();

    /** Creates an EXISTS sub-query. */
    public static RexSubQuery exists(RelNode rel) {
        final RelDataTypeFactory typeFactory = rel.getCluster().getTypeFactory();
        final RelDataType type = typeFactory.createSqlType(SqlTypeName.BOOLEAN);
        return RexBuilder.getRexFactory().makeSubQuery(type, SqlStdOperatorTable.EXISTS, ImmutableList.of(), rel);
    }

    /** Creates a scalar sub-query. */
    public static RexSubQuery scalar(RelNode rel) {
        final List<RelDataTypeField> fieldList = rel.getRowType().getFieldList();
        assert fieldList.size() == 1;
        final RelDataTypeFactory typeFactory = rel.getCluster().getTypeFactory();
        final RelDataType type = typeFactory.createTypeWithNullability(fieldList.get(0).getType(), true);
        return RexBuilder.getRexFactory().makeSubQuery(type, SqlStdOperatorTable.SCALAR_QUERY, ImmutableList.of(), rel);
    }

    /** Creates an IN sub-query. */
    public static RexSubQuery in(RelNode rel, ImmutableList<RexNode> nodes) {
        final RelDataType type = type(rel, nodes);
        return RexBuilder.getRexFactory().makeSubQuery(type, SqlStdOperatorTable.IN, nodes, rel);
    }

    /** Creates a SOME sub-query.
     *
     * <p>There is no ALL. For {@code x comparison ALL (sub-query)} use instead
     * {@code NOT (x inverse-comparison SOME (sub-query))}.
     * If {@code comparison} is {@code >}
     * then {@code negated-comparison} is {@code <=}, and so forth. */
    public static RexSubQuery some(RelNode rel, ImmutableList<RexNode> nodes, SqlQuantifyOperator op) {
        assert op.kind == SqlKind.SOME;
        final RelDataType type = type(rel, nodes);
        return RexBuilder.getRexFactory().makeSubQuery(type, op, nodes, rel);
    }

    static RelDataType type(RelNode rel, ImmutableList<RexNode> nodes) {
        assert rel.getRowType().getFieldCount() == nodes.size();
        final RelDataTypeFactory typeFactory = rel.getCluster().getTypeFactory();
        boolean nullable = false;
        for (RexNode node : nodes) {
            if (node.getType().isNullable()) {
                nullable = true;
            }
        }
        for (RelDataTypeField field : rel.getRowType().getFieldList()) {
            if (field.getType().isNullable()) {
                nullable = true;
            }
        }
        return typeFactory.createTypeWithNullability(typeFactory.createSqlType(SqlTypeName.BOOLEAN), nullable);
    }
}

// End RexSubQuery.java
