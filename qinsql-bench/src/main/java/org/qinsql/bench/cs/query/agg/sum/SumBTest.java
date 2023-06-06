/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.query.agg.sum;

import org.qinsql.bench.cs.query.agg.AggBTest;

public abstract class SumBTest extends AggBTest {
    @Override
    protected String nextAggSql() {
        return "select sum(pk) from AggBTest where pk<9999";
    }
}
