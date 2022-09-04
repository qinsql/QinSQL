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
package org.lealone.qinsql.test.executor;

import org.apache.drill.exec.expr.fn.impl.GMathFunctions;
import org.apache.drill.exec.expr.fn.interpreter.InterpreterEvaluator;
import org.apache.drill.exec.expr.holders.IntHolder;

public class DrillFuncTest {

    public static void main(String[] args) {
        IntHolder in = new IntHolder();
        in.value = -1;
        Object[] args2 = { in };
        GMathFunctions.AbsInt abs = new GMathFunctions.AbsInt();
        try {
            IntHolder v = (IntHolder) InterpreterEvaluator.evaluateFunction(abs, args2, "abs");
            System.out.println(v.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
