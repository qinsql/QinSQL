/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.cs.write.singleRowUpdate;

public class AsyncLealoneSingleRowUpdateBTest extends SingleRowUpdateBTest {

    public static void main(String[] args) {
        new AsyncLealoneSingleRowUpdateBTest().start();
    }
}
