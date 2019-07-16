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

import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexFieldAccess;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlKind;

public class RexFieldAccessImpl extends RexNodeBase implements RexFieldAccess {
    // ~ Instance fields --------------------------------------------------------

    private final RexNode expr;
    private final RelDataTypeField field;

    // ~ Constructors -----------------------------------------------------------

    RexFieldAccessImpl(RexNode expr, RelDataTypeField field) {
        super(field.getType());
        this.expr = expr;
        this.field = field;
        this.digest = expr + "." + field.getName();
        assert expr.getType().getFieldList().get(field.getIndex()) == field;
    }

    // ~ Methods ----------------------------------------------------------------

    @Override
    public SqlKind getKind() {
        return SqlKind.FIELD_ACCESS;
    }

    @Override
    public RelDataTypeField getField() {
        return field;
    }

    @Override
    public RexNode getReferenceExpr() {
        return expr;
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitFieldAccess(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitFieldAccess(this, arg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RexFieldAccessImpl that = (RexFieldAccessImpl) o;

        return field.equals(that.field) && expr.equals(that.expr);
    }

    @Override
    public int hashCode() {
        int result = expr.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }
}
