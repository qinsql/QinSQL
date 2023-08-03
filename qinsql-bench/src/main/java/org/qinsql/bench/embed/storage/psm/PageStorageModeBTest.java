/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage.psm;

import org.lealone.db.index.standard.ValueDataType;
import org.lealone.db.index.standard.VersionedValue;
import org.lealone.db.index.standard.VersionedValueType;
import org.lealone.db.value.Value;
import org.lealone.db.value.ValueLong;
import org.lealone.db.value.ValueString;
import org.lealone.storage.CursorParameters;
import org.lealone.storage.StorageMap;
import org.lealone.storage.StorageMapCursor;
import org.lealone.storage.aose.AOStorage;
import org.lealone.storage.aose.btree.BTreeMap;
import org.lealone.storage.aose.btree.page.PageStorageMode;
import org.lealone.test.TestBase;
import org.lealone.test.aose.AOStorageTest;
import org.lealone.transaction.aote.TransactionalValue;
import org.lealone.transaction.aote.TransactionalValueType;

//把CACHE_SIZE加大后，RowStorage的方式有更多内存就不会重复从硬盘读取page，此时就跟ColumnStorage的性能差不多
public class PageStorageModeBTest extends TestBase {

    public static void main(String[] args) throws Exception {
        new PageStorageModeBTest().run();
    }

    int rowCount = 6000;
    int columnCount = 10;
    int pageSplitSize = 1024 * 1024;
    int cacheSize = 100 * 1024 * 1024; // 100M

    public void run() {
        ValueDataType keyType = new ValueDataType(null, null, null);
        VersionedValueType vvType = new VersionedValueType(null, null, null, columnCount);
        TransactionalValueType tvType = new TransactionalValueType(vvType);

        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.println("------------------loop " + (i + 1) + " start---------------------");
            testRowStorage(keyType, tvType);

            System.out.println();
            testColumnStorage(keyType, tvType);
            System.out.println("------------------loop " + (i + 1) + " end---------------------");
        }
    }

    void putData(StorageMap<ValueLong, TransactionalValue> map) {
        if (map.isEmpty()) {
            // Random random = new Random();
            for (int row = 1; row <= rowCount; row++) {
                ValueLong key = ValueLong.get(row);
                Value[] columns = new Value[columnCount];
                for (int col = 0; col < columnCount; col++) {
                    // columns[col] = ValueString.get("a string");
                    // int randomIndex = random.nextInt(columnCount);
                    // columns[col] = ValueString.get("value-" + randomIndex);
                    // if (col % 2 == 0) {
                    // columns[col] = ValueString.get("a string");
                    // } else {
                    columns[col] = ValueString.get("value-row" + row + "-col" + (col + 1));
                    // }
                }
                // System.out.println(Arrays.asList(columns));
                VersionedValue vv = new VersionedValue(row, columns);
                TransactionalValue tv = TransactionalValue.createCommitted(vv);
                map.put(key, tv);
            }
            map.save();
            // map.remove();
        }
    }

    void testRowStorage(ValueDataType keyType, TransactionalValueType tvType) {
        long t0 = System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        AOStorage storage = AOStorageTest.openStorage(pageSplitSize, cacheSize);
        BTreeMap<ValueLong, TransactionalValue> map = storage.openBTreeMap("testRowStorage", keyType,
                tvType, null);
        map.setPageStorageMode(PageStorageMode.ROW_STORAGE);
        putData(map);
        long t2 = System.currentTimeMillis();
        System.out.println("RowStorage openBTreeMap time: " + (t2 - t1) + " ms");

        System.out.println("firstKey: " + map.firstKey());
        ValueLong key = ValueLong.get(2);
        int columnIndex = 2; // 索引要从0开始算
        TransactionalValue tv = map.get(key);
        VersionedValue vv = (VersionedValue) tv.getValue();
        System.out.println(vv.columns[columnIndex]);
        t1 = System.currentTimeMillis();
        key = ValueLong.get(2999);
        tv = map.get(key);
        vv = (VersionedValue) tv.getValue();
        t2 = System.currentTimeMillis();
        System.out.println("RowStorage get time: " + (t2 - t1) + " ms");
        System.out.println(vv.columns[columnIndex]);

        int rows = 0;
        ValueLong from = ValueLong.get(2000);
        t1 = System.currentTimeMillis();
        StorageMapCursor<ValueLong, TransactionalValue> cursor = map.cursor(from);
        while (cursor.next()) {
            rows++;
        }
        t2 = System.currentTimeMillis();
        System.out.println("RowStorage cursor time: " + (t2 - t1) + " ms" + ", rows: " + rows);

        System.out.println("RowStorage total time: " + (t2 - t0) + " ms");
        // map.close(); //关闭之后就把缓存也关了
    }

    void testColumnStorage(ValueDataType keyType, TransactionalValueType tvType) {
        long t0 = System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        AOStorage storage = AOStorageTest.openStorage(pageSplitSize);
        BTreeMap<ValueLong, TransactionalValue> map = storage.openBTreeMap("testColumnStorage", keyType,
                tvType, null);
        map.setPageStorageMode(PageStorageMode.COLUMN_STORAGE);
        putData(map);
        long t2 = System.currentTimeMillis();
        System.out.println("ColumnStorage openBTreeMap time: " + (t2 - t1) + " ms");
        System.out.println("firstKey: " + map.firstKey());

        // t1 = System.currentTimeMillis();
        // map.get(ValueLong.get(4000));
        // t2 = System.currentTimeMillis();
        // System.out.println("hrcse get all columns time: " + (t2 - t1) + " ms");

        ValueLong key = ValueLong.get(2);
        int columnIndex = 2; // 索引要从0开始算
        TransactionalValue tv = map.get(key, columnIndex);
        VersionedValue vv = (VersionedValue) tv.getValue();
        System.out.println(vv.columns[columnIndex]);
        t1 = System.currentTimeMillis();
        key = ValueLong.get(2999);
        tv = map.get(key, columnIndex);
        vv = (VersionedValue) tv.getValue();
        t2 = System.currentTimeMillis();
        System.out.println("ColumnStorage get time: " + (t2 - t1) + " ms");
        System.out.println(vv.columns[columnIndex]);

        // key = ValueLong.get(2000);
        // tv = map.get(key, columnIndex);
        // vv = (VersionedValue) tv.value;
        // System.out.println(vv.value.getList()[columnIndex]);

        int rows = 0;
        ValueLong from = ValueLong.get(2000);
        t1 = System.currentTimeMillis();
        StorageMapCursor<ValueLong, TransactionalValue> cursor = map
                .cursor(CursorParameters.create(from, columnIndex));
        while (cursor.next()) {
            rows++;
        }
        t2 = System.currentTimeMillis();
        System.out.println("ColumnStorage cursor time: " + (t2 - t1) + " ms" + ", rows: " + rows);

        System.out.println("ColumnStorage total time: " + (t2 - t0) + " ms");
        // map.close();
    }
}
