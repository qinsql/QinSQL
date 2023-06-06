/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage;

import org.lealone.db.value.ValueInt;
import org.lealone.db.value.ValueString;
import org.qinsql.storage.memory.MemoryStorage;

public class SkipListBTest extends StorageMapBTest {

    public static void main(String[] args) throws Exception {
        new SkipListBTest().run();
    }

    @Override
    protected void init() {
        if (!inited.compareAndSet(false, true))
            return;
        MemoryStorage ms = new MemoryStorage();
        map = ms.openMap(SkipListBTest.class.getSimpleName(), ValueInt.type, ValueString.type, null);
    }
}
