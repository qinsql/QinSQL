/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage;

import org.lealone.db.value.ValueInt;
import org.lealone.db.value.ValueString;

public class BTreeSyncBTest extends StorageMapBTest {

    public static void main(String[] args) throws Exception {
        new BTreeSyncBTest().run();
    }

    @Override
    protected void init() {
        if (!inited.compareAndSet(false, true))
            return;
        initConfig();
        openStorage();
        map = storage.openBTreeMap(BTreeSyncBTest.class.getSimpleName(), ValueInt.type, ValueString.type, null);
    }
}
