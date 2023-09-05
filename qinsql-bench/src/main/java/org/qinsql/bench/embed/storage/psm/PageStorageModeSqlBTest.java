/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage.psm;

import org.lealone.storage.aose.AOStorageEngine;
import org.lealone.storage.aose.btree.page.PageStorageMode;
import org.lealone.test.sql.SqlTestBase;

//把CACHE_SIZE加大后，RowStorage的方式有更多内存就不会重复从硬盘读取page，此时就跟ColumnStorage的性能差不多
public class PageStorageModeSqlBTest extends SqlTestBase {

    public PageStorageModeSqlBTest() {
        super("PageStorageModeSqlPerfTest");
        setEmbedded(true);
        addConnectionParameter("PAGE_SIZE", (2 * 1024 * 1024) + "");
        // addConnectionParameter("COMPRESS", "true");

        printURL();
    }

    int rowCount = 6000;
    int columnCount = 20;
    int pageSize = 1024 * 1024;

    // @Test
    public void run() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println();
            System.out.println("------------------loop " + (i + 1) + " start---------------------");
            testRowStorage();

            System.out.println();
            testColumnStorage();
            System.out.println("------------------loop " + (i + 1) + " end---------------------");
        }
    }

    void createTestTable(String tableName, String pageStorageMode) {
        // executeUpdate("drop table IF EXISTS " + tableName);
        StringBuilder sql = new StringBuilder("create table IF NOT EXISTS ").append(tableName)
                .append("(pk int primary key");
        for (int col = 1; col <= columnCount; col++) {
            sql.append(", f").append(col).append(" varchar");
        }
        sql.append(") Engine ").append(AOStorageEngine.NAME).append(" PARAMETERS(pageStorageMode='")
                .append(pageStorageMode).append("')");
        executeUpdate(sql.toString());
    }

    void putData(String tableName) {
        sql = "select count(*) from " + tableName;
        int count = 0;
        try {
            count = getIntValue(1, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count <= 0) {
            // Random random = new Random();
            for (int row = 1; row <= rowCount; row++) {
                StringBuilder sql = new StringBuilder("insert into ").append(tableName)
                        .append(" values(").append(row);
                for (int col = 1; col <= columnCount; col++) {
                    sql.append(", 'value-row" + row + "-col" + col + "'");
                    // columns[col] = ValueString.get("a string");
                    // int randomIndex = random.nextInt(columnCount);
                    // columns[col] = ValueString.get("value-" + randomIndex);
                    // if (col % 2 == 0) {
                    // columns[col] = ValueString.get("a string");
                    // } else {
                    // columns[col] = ValueString.get("value-row" + row + "-col" + (col + 1));
                    // }
                }
                sql.append(")");
                // System.out.println(Arrays.asList(columns));
                executeUpdate(sql.toString());
            }
            executeUpdate("checkpoint");
            // map.remove();
        }
    }

    void testRowStorage() {
        testCRUD("testRowStorage", PageStorageMode.ROW_STORAGE.name());
    }

    void testColumnStorage() {
        testCRUD("testColumnStorage", PageStorageMode.COLUMN_STORAGE.name());
    }

    void testCRUD(String tableName, String pageStorageMode) {
        executeUpdate("SET OPTIMIZE_REUSE_RESULTS 0");
        long t0 = System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        createTestTable(tableName, pageStorageMode);
        putData(tableName);
        long t2 = System.currentTimeMillis();
        // System.out.println(pageStorageMode + " create table time: " + (t2 - t1) + " ms");

        // t1 = System.currentTimeMillis();
        // sql = "select f2 from " + tableName + " where pk = 2";
        // printResultSet();
        // t2 = System.currentTimeMillis();
        // System.out.println(pageStorageMode + " select time: " + (t2 - t1) + " ms");
        //
        // t1 = System.currentTimeMillis();
        // sql = "select f2 from " + tableName + " where pk = 2000";
        // printResultSet();
        // t2 = System.currentTimeMillis();
        // System.out.println(pageStorageMode + " select time: " + (t2 - t1) + " ms");

        t1 = System.currentTimeMillis();
        sql = "select sum(pk),count(f6) from " + tableName + " where pk >= 2000";
        int sum = 0;
        int count = 0;
        try {
            sum = getIntValue(1);
            count = getIntValue(2, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        t2 = System.currentTimeMillis();
        System.out.println(pageStorageMode + " agg time: " + (t2 - t1) + " ms" + ", count: " + count
                + ", sum: " + sum);

        System.out.println(pageStorageMode + " total time: " + (t2 - t0) + " ms");
    }
}
