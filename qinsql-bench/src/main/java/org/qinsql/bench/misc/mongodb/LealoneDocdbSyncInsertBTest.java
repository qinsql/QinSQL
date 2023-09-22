/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

public class LealoneDocdbSyncInsertBTest extends MongodbSyncInsertBTest {

    public static void main(String[] args) {
        new LealoneDocdbSyncInsertBTest().run(9610);
    }
}
