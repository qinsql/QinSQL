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
package org.apache.calcite.rex.impl;

import java.util.List;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.core.Match.RexMRAggCall;
import org.apache.calcite.rel.core.Window.RexWinAggCall;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexCorrelVariable;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.rex.RexFactory;
import org.apache.calcite.rex.RexFieldAccess;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexLocalRef;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexOver;
import org.apache.calcite.rex.RexRangeRef;
import org.apache.calcite.rex.RexSubQuery;
import org.apache.calcite.rex.RexWindow;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.type.SqlTypeName;

import com.google.common.collect.ImmutableList;

@SuppressWarnings("rawtypes")
public class RexFactoryImpl implements RexFactory {

    @Override
    public RexCall makeCall(RelDataType type, SqlOperator op, List<? extends RexNode> exprs) {
        return new RexCallImpl(type, op, exprs);
    }

    @Override
    public RexInputRef makeInputRef(int index, RelDataType type) {
        return new RexInputRefImpl(index, type);
    }

    @Override
    public RexFieldAccess makeFieldAccess(RexNode expr, RelDataTypeField field) {
        return new RexFieldAccessImpl(expr, field);
    }

    @Override
    public RexOver makeOver(RelDataType type, SqlAggFunction op, List<RexNode> operands, RexWindow window,
            boolean distinct) {
        return new RexOverImpl(type, op, operands, window, distinct);
    }

    @Override
    public RexLiteral makeLiteral(Comparable value, RelDataType type, SqlTypeName typeName) {
        return new RexLiteralImpl(value, type, typeName);
    }

    @Override
    public RexRangeRef makeRangeReference(RelDataType rangeType, int offset) {
        return new RexRangeRefImpl(rangeType, offset);
    }

    @Override
    public RexDynamicParam makeDynamicParam(RelDataType type, int index) {
        return new RexDynamicParamImpl(type, index);
    }

    @Override
    public RexCorrelVariable makeCorrel(CorrelationId id, RelDataType type) {
        return new RexCorrelVariableImpl(id, type);
    }

    @Override
    public RexWinAggCall makeWinAggCall(SqlAggFunction aggFun, RelDataType type, List<RexNode> operands, int ordinal,
            boolean distinct) {
        return new RexWinAggCallImpl(aggFun, type, operands, ordinal, distinct);
    }

    @Override
    public RexMRAggCall makeMRAggCall(SqlAggFunction aggFun, RelDataType type, List<RexNode> operands, int ordinal) {
        return new RexMRAggCallImpl(aggFun, type, operands, ordinal);
    }

    @Override
    public RexSubQuery makeSubQuery(RelDataType type, SqlOperator op, ImmutableList<RexNode> operands, RelNode rel) {
        return new RexSubQueryImpl(type, op, operands, rel);
    }

    @Override
    public RexLocalRef makeLocalRef(int index, RelDataType type) {
        return new RexLocalRefImpl(index, type);
    }
}
