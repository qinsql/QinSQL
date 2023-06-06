/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.storage.mvstore;

import org.junit.Test;
import org.lealone.test.TestBase;
import org.lealone.test.misc.CRUDExample;
import org.qinsql.storage.mvstore.MVStorageEngine;

public class MVStoreTest extends TestBase {
    @Test
    public void run() throws Exception {
        TestBase test = new TestBase();
        test.setInMemory(true);
        test.setEmbedded(true);
        test.printURL();
        CRUDExample.crud(test.getConnection(), MVStorageEngine.NAME);
    }
}
