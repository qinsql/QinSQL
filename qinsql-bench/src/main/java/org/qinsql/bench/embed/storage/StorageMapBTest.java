/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.storage;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;

import org.lealone.storage.StorageMap;
import org.lealone.storage.aose.AOStorage;
import org.lealone.storage.aose.AOStorageBuilder;
import org.lealone.storage.aose.btree.BTreeMap;
import org.lealone.storage.aose.btree.page.Page;
import org.lealone.storage.page.PageOperationHandlerFactory;
import org.qinsql.bench.embed.EmbeddedBTest;

public abstract class StorageMapBTest extends EmbeddedBTest {

    protected AOStorage storage;
    protected String storagePath;
    protected StorageMap<Integer, String> map;
    final HashMap<String, String> config = new HashMap<>();

    static int conflictKeyCount = 10000 * 5; // 冲突key个数
    int[] conflictKeys = getConflictKeys();
    boolean testConflictOnly;

    PageOperationHandlerFactory pohFactory;

    protected StorageMapBTest() {
        this(200 * 10000);
    }

    protected StorageMapBTest(int rowCount) {
        super(rowCount);
    }

    protected void testWrite(int loop) {
        singleThreadRandomWrite();
        singleThreadSerialWrite();
        // multiThreadsRandomWrite(loop);
        // multiThreadsSerialWrite(loop);
    }

    protected void testRead(int loop) {
        singleThreadRandomRead();
        singleThreadSerialRead();
        // multiThreadsRandomRead(loop);
        // multiThreadsSerialRead(loop);
    }

    protected void testConflict(int loop) {
        testConflict(loop, false);
    }

    @Override
    public void run() {
        init();
        createData();

        loopCount = 10;
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        threadCount = availableProcessors;
        run0();

        threadCount = availableProcessors * 2;
        run0();

        threadCount = availableProcessors * 4;
        run0();

        threadCount = 100;
        run0();

        // 同样是完成500万次更新操作，
        // 对于高并发高冲突的场景，只开availableProcessors个线程多循环几次效果更好
        threadCount = availableProcessors;
        loopCount = 100 / threadCount;
        testConflictOnly = true;
        // run0();
    }

    private void run0() {
        beforeRun();
        loop();
    }

    protected void createData() {
        // map.clear();
        if (map.isEmpty())
            singleThreadSerialWrite();// 先生成初始数据
    }

    protected void beforeRun() {
        // System.out.println("map size: " + map.size());
    }

    private void loop() {
        long t1 = System.currentTimeMillis();
        for (int i = 1; i <= loopCount; i++) {
            // map.clear();
            if (!testConflictOnly) {
                testWrite(i);
                testRead(i);
            }
            // testConflict(i);

            System.out.println();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("total time: " + (t2 - t1) + " ms");
        System.out.println("map size: " + size());
        System.out.println();

        // testSystemArraycopy();

        // for (int i = 0; i < 40; i++) {
        // testConcurrentLinkedQueue();
        // testLinkedTransferQueue();
        // }

        // for (int i = 0; i < 40; i++) {
        // // testCurrentThread();
        // testCountDownLatch();
        // }

        // testConcurrentSkipListMap();
    }

    protected int size() {
        return (int) map.size();
    }

    @Override
    protected abstract void init();

    protected void initConfig() {
        String factoryType = "RoundRobin";
        // factoryType = "Random";
        // factoryType = "LoadBalance";
        config.put("page_operation_handler_factory_type", factoryType);
        config.put("page_operation_handler_count", threadCount + "");
    }

    protected void openStorage() {
        AOStorageBuilder builder = new AOStorageBuilder(config, pohFactory);
        storagePath = joinDirs("lealone", "aose");
        int pageSize = 16 * 1024;
        pageSize = 2 * 1024;
        pageSize = 4 * 1024; // 最优
        // pageSize = 6 * 1024;
        // pageSize = 8 * 1024;
        // pageSize = 1 * 1024;
        // pageSize = 1024 / 2 / 2;
        // pageSize = 32 * 1024;
        // pageSize = 512 * 1024;
        builder.storagePath(storagePath).compress().pageSize(pageSize).minFillRate(30);
        storage = builder.openStorage();
    }

    void testSystemArraycopy() {
        Object[] src = new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int len = src.length;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            Object[] dest = new Object[len];
            System.arraycopy(src, 0, dest, 0, len);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("SystemArraycopy time: " + (t2 - t1) + " ms, count: " + rowCount);
    }

    void testConcurrentLinkedQueue() {
        int count = 50000;
        long t1 = System.currentTimeMillis();
        ConcurrentLinkedQueue<String> tasks = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < count; i++) {
            tasks.add("abc");
        }
        long t2 = System.currentTimeMillis();
        System.out.println("ConcurrentLinkedQueue add time: " + (t2 - t1) + " ms, count: " + count);
    }

    void testLinkedTransferQueue() {
        int count = 50000;
        long t1 = System.currentTimeMillis();
        LinkedTransferQueue<String> tasks = new LinkedTransferQueue<>();
        for (int i = 0; i < count; i++) {
            tasks.add("abc");
        }
        long t2 = System.currentTimeMillis();
        System.out.println("LinkedTransferQueue add time: " + (t2 - t1) + " ms, count: " + count);
    }

    void testCurrentThread() {
        int count = 50000;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Thread.currentThread();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("invoke Thread.currentThread time: " + (t2 - t1) + " ms, count: " + count);
    }

    void testCountDownLatch() {
        int count = 50000;
        CountDownLatch latch = new CountDownLatch(count);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            latch.countDown();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("CountDownLatch time: " + (t2 - t1) + " ms, count: " + count);
    }

    void testCopy() {
        int count = 50000;
        Page root = ((BTreeMap<Integer, String>) map).getRootPage();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            root.copy();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("write time: " + (t2 - t1) + " ms, count: " + count);
    }

    void testConcurrentSkipListMap() {
        ConcurrentSkipListMap<Integer, Integer> skipListMap = new ConcurrentSkipListMap<>();

        skipListMap.put(1, 10);
        skipListMap.put(2, 20);
        skipListMap.put(2, 200);
        skipListMap.replace(2, 20, 300);
        Thread t = new Thread(() -> {
            skipListMap.put(1, 20);
        });
        t.start();

        Thread t3 = new Thread(() -> {
            skipListMap.remove(1);
        });
        t3.start();
        try {
            t.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int loop = 0; loop < 20; loop++) {
            // skipListMap.clear();
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < rowCount; i++) {
                skipListMap.put(i, i * 100);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("ConcurrentSkipListMap serial write time: " + (t2 - t1) + " ms, count: " + rowCount);
        }
        System.out.println();
        int[] keys = getRandomKeys();
        for (int loop = 0; loop < 20; loop++) {
            // skipListMap.clear(); //不clear时更快一些
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < rowCount; i++) {
                skipListMap.put(keys[i], i * 100);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("ConcurrentSkipListMap random write time: " + (t2 - t1) + " ms, count: " + rowCount);
        }
    }

    int[] getConflictKeys() {
        Random random = new Random();
        int[] keys = new int[conflictKeyCount];
        for (int i = 0; i < conflictKeyCount; i++) {
            keys[i] = random.nextInt(rowCount);
        }
        return keys;
    }

    void singleThreadSerialWrite() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            put(i, "valueaaa");
        }
        long t2 = System.currentTimeMillis();
        printResult("single-thread serial write time: " + (t2 - t1) + " ms, count: " + rowCount);
    }

    void singleThreadRandomWrite() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            put(randomKeys[i], "valueaaa");
        }
        long t2 = System.currentTimeMillis();
        printResult("single-thread random write time: " + (t2 - t1) + " ms, count: " + rowCount);
    }

    void singleThreadSerialRead() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            get(i);
        }
        long t2 = System.currentTimeMillis();
        printResult("single-thread serial read time: " + (t2 - t1) + " ms, count: " + rowCount);
    }

    void singleThreadRandomRead() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            get(randomKeys[i]);
        }
        long t2 = System.currentTimeMillis();
        printResult("single-thread random read time: " + (t2 - t1) + " ms, count: " + rowCount);
    }

    @Override
    protected BenchTestTask createBenchTestTask(int start, int end) throws Exception {
        if (testConflictOnly)
            return new StorageMapConflictBenchTestTask();
        else
            return new StorageMapBenchTestTask(start, end);
    }

    protected class StorageMapBenchTestTask extends BenchTestTask {

        StorageMapBenchTestTask(int start, int end) throws Exception {
            super(start, end);
        }

        @Override
        public void startBenchTest() throws Exception {
            if (isWrite()) {
                write();
            } else {
                read();
            }
        }

        protected void read() throws Exception {
            for (int i = start; i < end; i++) {
                int key;
                if (isRandom())
                    key = randomKeys[i];
                else
                    key = i;
                get(key);
                notifyOperationComplete();
            }
        }

        protected void write() throws Exception {
            for (int i = start; i < end; i++) {
                int key;
                if (isRandom())
                    key = randomKeys[i];
                else
                    key = i;
                String value = "value-";// "value-" + key;

                put(key, value);
                notifyOperationComplete();
            }
        }
    }

    class StorageMapConflictBenchTestTask extends StorageMapBenchTestTask {

        StorageMapConflictBenchTestTask() throws Exception {
            super(0, conflictKeyCount);
        }

        @Override
        protected void write() throws Exception {
            for (int i = 0; i < conflictKeyCount; i++) {
                int key = conflictKeys[i];
                String value = "value-conflict";

                put(key, value);
                notifyOperationComplete();
            }
        }
    }

    protected void put(Integer key, String value) {
        map.put(key, value);
    }

    protected String get(Integer key) {
        return map.get(key);
    }

    void multiThreadsSerialRead(int loop) {
        multiThreads(loop, true, false, false);
    }

    void multiThreadsRandomRead(int loop) {
        multiThreads(loop, true, true, false);
    }

    void multiThreadsSerialWrite(int loop) {
        multiThreads(loop, false, false, false);
    }

    void multiThreadsRandomWrite(int loop) {
        multiThreads(loop, false, true, false);
    }

    void multiThreadsSerialWriteAsync(int loop) {
        multiThreads(loop, false, false, true);
    }

    void multiThreadsRandomWriteAsync(int loop) {
        multiThreads(loop, false, true, true);
    }

    void multiThreads(int loop, boolean read, boolean random, boolean async) {
        write = !read;
        isRandom = random;
        try {
            run(loop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printRunResult(int loop, long totalTime, long avgTime, String str) {
        if (testConflictOnly)
            printResult(loop,
                    ", row count: " + rowCount + ", thread count: " + threadCount + ", conflict keys: "
                            + conflictKeyCount + ", sync write conflict, total time: " + totalTime + " ms, avg time: "
                            + avgTime + " ms");
        else
            printResult(loop, ", row count: " + rowCount + ", thread count: " + threadCount + ", sync " + str
                    + ", total time: " + totalTime + " ms, avg time: " + avgTime + " ms");
    }

    void testConflict(int loop, boolean async) {
        write = true;
        isRandom = true;
        try {
            run(loop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void resetFields() {
        super.resetFields();
        if (testConflictOnly)
            pendingOperations.set(conflictKeyCount * threadCount);
    }
}
