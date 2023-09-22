/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

public class LealoneDocdbSyncSingleRowQueryBTest extends MongodbSyncSingleRowQueryBTest {

    public static void main(String[] args) {
        new LealoneDocdbSyncSingleRowQueryBTest().run(9610);
    }

}
