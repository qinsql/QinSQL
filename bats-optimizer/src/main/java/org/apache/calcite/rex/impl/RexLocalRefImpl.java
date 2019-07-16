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
import org.apache.calcite.rex.RexLocalRef;
import org.apache.calcite.rex.RexVisitor;
import org.apache.calcite.sql.SqlKind;

public class RexLocalRefImpl extends RexSlotBase implements RexLocalRef {
    // ~ Static fields/initializers ---------------------------------------------

    // array of common names, to reduce memory allocations
    // @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final List<String> NAMES = new SelfPopulatingList("$t", 30);

    // ~ Constructors -----------------------------------------------------------

    /**
     * Creates a local variable.
     *
     * @param index Index of the field in the underlying row type
     * @param type  Type of the column
     */
    public RexLocalRefImpl(int index, RelDataType type) {
        super(createName(index), index, type);
    }

    // ~ Methods ----------------------------------------------------------------

    @Override
    public SqlKind getKind() {
        return SqlKind.LOCAL_REF;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof RexLocalRefImpl && this.type == ((RexLocalRefImpl) obj).type
                && this.index == ((RexLocalRefImpl) obj).index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, index);
    }

    @Override
    public <R> R accept(RexVisitor<R> visitor) {
        return visitor.visitLocalRef(this);
    }

    @Override
    public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
        return visitor.visitLocalRef(this, arg);
    }

    private static String createName(int index) {
        return NAMES.get(index);
    }
}

// End RexLocalRef.java
