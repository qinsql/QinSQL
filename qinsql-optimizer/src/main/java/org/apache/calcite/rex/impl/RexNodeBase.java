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

import java.util.Collection;
import java.util.Objects;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;

public abstract class RexNodeBase implements RexNode {
    // ~ Instance fields --------------------------------------------------------

    protected final RelDataType type;

    // Effectively final. Set in each sub-class constructor, and never re-set.
    protected String digest;

    protected RexNodeBase(RelDataType type) {
        this.type = Objects.requireNonNull(type);
    }

    // ~ Methods ----------------------------------------------------------------

    @Override
    public RelDataType getType() {
        return type;
    }

    /**
     * Returns the kind of node this is.
     *
     * @return Node kind, never null
     */
    @Override
    public SqlKind getKind() {
        return SqlKind.OTHER;
    }

    @Override
    public boolean isA(SqlKind kind) {
        return getKind() == kind;
    }

    @Override
    public boolean isA(Collection<SqlKind> kinds) {
        return getKind().belongsTo(kinds);
    }

    /**
     * Returns whether this expression always returns true. (Such as if this
     * expression is equal to the literal <code>TRUE</code>.)
     */
    @Override
    public boolean isAlwaysTrue() {
        return false;
    }

    /**
     * Returns whether this expression always returns false. (Such as if this
     * expression is equal to the literal <code>FALSE</code>.)
     */
    @Override
    public boolean isAlwaysFalse() {
        return false;
    }

    @Override
    public String toString() {
        return digest;
    }
}
