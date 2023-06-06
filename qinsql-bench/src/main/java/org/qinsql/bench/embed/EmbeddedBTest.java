/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed;

import org.qinsql.bench.BenchTest;

public abstract class EmbeddedBTest extends BenchTest {

    protected EmbeddedBTest() {
    }

    protected EmbeddedBTest(int rowCount) {
        super(rowCount);
    }
}
