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

import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.util.ControlFlowException;
import org.apache.calcite.util.Util;

/**
 * Call to an aggregate function over a window.
 */
public interface RexOver extends RexCall {

    // ~ Methods ----------------------------------------------------------------

    /**
     * Returns the aggregate operator for this expression.
     */
    public SqlAggFunction getAggOperator();

    public RexWindow getWindow();

    public boolean isDistinct();

    /**
     * Returns whether an expression contains an OVER clause.
     */
    public static boolean containsOver(RexNode expr) {
        try {
            expr.accept(FINDER);
            return false;
        } catch (OverFound e) {
            Util.swallow(e, null);
            return true;
        }
    }

    /**
     * Returns whether a program contains an OVER clause.
     */
    public static boolean containsOver(RexProgram program) {
        try {
            RexUtil.apply(FINDER, program.getExprList(), null);
            return false;
        } catch (OverFound e) {
            Util.swallow(e, null);
            return true;
        }
    }

    /**
     * Returns whether an expression list contains an OVER clause.
     */
    public static boolean containsOver(List<RexNode> exprs, RexNode condition) {
        try {
            RexUtil.apply(FINDER, exprs, condition);
            return false;
        } catch (OverFound e) {
            Util.swallow(e, null);
            return true;
        }
    }

    static final Finder FINDER = new Finder();

    // ~ Inner Classes ----------------------------------------------------------

    /** Exception thrown when an OVER is found. */
    static class OverFound extends ControlFlowException {
        public static final OverFound INSTANCE = new OverFound();
    }

    /**
     * Visitor which detects a {@link RexOver} inside a {@link RexNode}
     * expression.
     *
     * <p>It is re-entrant (two threads can use an instance at the same time)
     * and it can be re-used for multiple visits.
     */
    static class Finder extends RexVisitorImpl<Void> {
        Finder() {
            super(true);
        }

        @Override
        public Void visitOver(RexOver over) {
            throw OverFound.INSTANCE;
        }
    }
}

// End RexOver.java
