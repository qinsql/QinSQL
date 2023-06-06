/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.MVStore.Builder;

public class H2MVMapBTest extends StorageMapBTest {

    public static void main(String[] args) throws Exception {
        new H2MVMapBTest().run();
    }

    private MVMap<Integer, String> map;

    @Override
    protected void init() {
        if (!inited.compareAndSet(false, true))
            return;
        Builder builder = new Builder();
        // builder.pageSplitSize(4 * 1024);
        // builder.keysPerPage(128);
        // builder.keysPerPage(24);
        // builder.keysPerPage(60); // 默认48，也是效果最优的
        MVStore store = builder.open();
        // MVStore store = MVStore.open(null);
        map = store.openMap(H2MVMapBTest.class.getSimpleName());
    }

    @Override
    protected void createData() {
        // map.clear();
        if (map.isEmpty())
            singleThreadSerialWrite();
    }

    @Override
    protected int size() {
        return map.size();
    }

    @Override
    protected void put(Integer key, String value) {
        map.put(key, value);
    }

    @Override
    protected String get(Integer key) {
        return map.get(key);
    }
}
