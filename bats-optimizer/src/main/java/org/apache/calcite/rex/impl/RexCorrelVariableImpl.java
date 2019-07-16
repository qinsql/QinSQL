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

import java.util.Objects;

import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBiVisitor;
import org.apache.calcite.rex.RexCorrelVariable;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlKind;

public class RexCorrelVariableImpl extends RexVariableBase implements RexCorrelVariable {

    private final CorrelationId id;

    RexCorrelVariableImpl(CorrelationId id, RelDataType type) {
        super(Objects.requireNonNull(id).getName(), type);
        this.id = id;
    }

    @Override
    public SqlKind getKind() {
        return SqlKind.CORREL_VARIABLE;
    }

    @Override
    public CorrelationId getCorrelationId() {
        return id;
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitCorrelVariable(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitCorrelVariable(this, arg);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof RexCorrelVariableImpl
                && digest.equals(((RexCorrelVariableImpl) obj).digest)
                && type.equals(((RexCorrelVariableImpl) obj).type) && id.equals(((RexCorrelVariableImpl) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digest, type, id);
    }
}
