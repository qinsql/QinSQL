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
package org.apache.calcite.rex.impl;

import java.util.List;

import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexSubQuery;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlOperator;

import com.google.common.collect.ImmutableList;

public class RexSubQueryImpl extends RexCallImpl implements RexSubQuery {

    private final RelNode rel;

    RexSubQueryImpl(RelDataType type, SqlOperator op, ImmutableList<RexNode> operands, RelNode rel) {
        super(type, op, operands);
        this.rel = rel;
        this.digest = computeDigest(false);
    }

    @Override
    public RelNode getRel() {
        return rel;
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitSubQuery(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitSubQuery(this, arg);
    }

    @Override
    protected String computeDigest(boolean withType) {
        final StringBuilder sb = new StringBuilder(op.getName());
        sb.append("(");
        for (RexNode operand : operands) {
            sb.append(operand);
            sb.append(", ");
        }
        sb.append("{\n");
        sb.append(RelOptUtil.toString(rel));
        sb.append("})");
        return sb.toString();
    }

    @Override
    public RexSubQuery clone(RelDataType type, List<RexNode> operands) {
        return new RexSubQueryImpl(type, getOperator(), ImmutableList.copyOf(operands), rel);
    }

    @Override
    public RexSubQuery clone(RelNode rel) {
        return new RexSubQueryImpl(type, getOperator(), operands, rel);
    }
}
