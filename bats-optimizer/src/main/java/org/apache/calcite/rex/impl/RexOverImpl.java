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
import java.util.Objects;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexFieldAccess;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexOver;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.rex.RexWindow;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlWindow;

import com.google.common.base.Preconditions;

public class RexOverImpl extends RexCallImpl implements RexOver {

    // ~ Instance fields --------------------------------------------------------

    private final RexWindow window;
    private final boolean distinct;

    // ~ Constructors -----------------------------------------------------------

    /**
     * Creates a RexOver.
     *
     * <p>For example, "SUM(DISTINCT x) OVER (ROWS 3 PRECEDING)" is represented
     * as:
     *
     * <ul>
     * <li>type = Integer,
     * <li>op = {@link org.apache.calcite.sql.fun.SqlStdOperatorTable#SUM},
     * <li>operands = { {@link RexFieldAccess}("x") }
     * <li>window = {@link SqlWindow}(ROWS 3 PRECEDING)
     * </ul>
     *
     * @param type     Result type
     * @param op       Aggregate operator
     * @param operands Operands list
     * @param window   Window specification
     * @param distinct Aggregate operator is applied on distinct elements
     */
    RexOverImpl(RelDataType type, SqlAggFunction op, List<RexNode> operands, RexWindow window, boolean distinct) {
        super(type, op, operands);
        Preconditions.checkArgument(op.isAggregator());
        this.window = Objects.requireNonNull(window);
        this.distinct = distinct;
    }

    // ~ Methods ----------------------------------------------------------------

    /**
     * Returns the aggregate operator for this expression.
     */
    @Override
    public SqlAggFunction getAggOperator() {
        return (SqlAggFunction) getOperator();
    }

    @Override
    public RexWindow getWindow() {
        return window;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    protected String computeDigest(boolean withType) {
        final StringBuilder sb = new StringBuilder(op.getName());
        sb.append("(");
        if (distinct) {
            sb.append("DISTINCT ");
        }
        appendOperands(sb);
        sb.append(")");
        if (withType) {
            sb.append(":");
            sb.append(type.getFullTypeString());
        }
        sb.append(" OVER (").append(window).append(")");
        return sb.toString();
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitOver(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitOver(this, arg);
    }

    @Override
    public RexCall clone(RelDataType type, List<RexNode> operands) {
        throw new UnsupportedOperationException();
    }
}
