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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.util.Pair;

/**
 * Variable which references a field of an input relational expression.
 *
 * <p>Fields of the input are 0-based. If there is more than one input, they are
 * numbered consecutively. For example, if the inputs to a join are</p>
 *
 * <ul>
 * <li>Input #0: EMP(EMPNO, ENAME, DEPTNO) and</li>
 * <li>Input #1: DEPT(DEPTNO AS DEPTNO2, DNAME)</li>
 * </ul>
 *
 * <p>then the fields are:</p>
 *
 * <ul>
 * <li>Field #0: EMPNO</li>
 * <li>Field #1: ENAME</li>
 * <li>Field #2: DEPTNO (from EMP)</li>
 * <li>Field #3: DEPTNO2 (from DEPT)</li>
 * <li>Field #4: DNAME</li>
 * </ul>
 *
 * <p>So <code>RexInputRef(3, Integer)</code> is the correct reference for the
 * field DEPTNO2.</p>
 */
public interface RexInputRef extends RexSlot {

    /**
     * Creates a reference to a given field in a row type.
     */
    public static RexInputRef of(int index, RelDataType rowType) {
        return of(index, rowType.getFieldList());
    }

    /**
     * Creates a reference to a given field in a list of fields.
     */
    public static RexInputRef of(int index, List<RelDataTypeField> fields) {
        return RexBuilder.getRexFactory().makeInputRef(index, fields.get(index).getType());
    }

    /**
     * Creates a reference to a given field in a list of fields.
     */
    public static Pair<RexNode, String> of2(int index, List<RelDataTypeField> fields) {
        final RelDataTypeField field = fields.get(index);
        return Pair.of((RexNode) RexBuilder.getRexFactory().makeInputRef(index, field.getType()), field.getName());
    }
}
