/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.lealone.db.LealoneDatabase;
import org.lealone.db.SysProperties;
import org.lealone.test.TestBase;
import org.lealone.transaction.aote.log.LogSyncService;
import org.qinsql.bench.BenchTest;

public abstract class EmbeddedBTest extends BenchTest {

    public static String joinDirs(String... dirs) {
        StringBuilder s = new StringBuilder(BENCH_TEST_BASE_DIR);
        for (String dir : dirs)
            s.append(File.separatorChar).append(dir);
        return s.toString();
    }

    protected final AtomicLong pendingOperations = new AtomicLong(0);
    protected final AtomicLong startTime = new AtomicLong(0);
    protected final AtomicLong endTime = new AtomicLong(0);
    protected final AtomicBoolean inited = new AtomicBoolean(false);
    protected final int[] randomKeys;
    protected int loopCount = 30; // 重复测试次数
    protected Boolean isRandom;
    protected Boolean write;
    protected CountDownLatch latch;

    protected EmbeddedBTest() {
        this(DEFAULT_ROW_COUNT);
    }

    protected EmbeddedBTest(int rowCount) {
        this.rowCount = rowCount;
        randomKeys = getRandomKeys();
    }

    public static Connection getEmbeddedH2Connection() throws Exception {
        String url;
        url = "jdbc:h2:file:./EmbeddedBenchTestDB";
        // url = "jdbc:h2:mem:mydb";
        // url += ";OPEN_NEW=true;FORBID_CREATION=false";
        url += ";FORBID_CREATION=false";
        return DriverManager.getConnection(url, "sa", "");
    }

    public static Connection getEmbeddedLealoneConnection() throws Exception {
        TestBase test = new TestBase();
        test.setEmbedded(true);
        String url = test.getURL(LealoneDatabase.NAME);
        SysProperties.setBaseDir(joinDirs("lealone"));
        return DriverManager.getConnection(url);
    }

    protected void initTransactionEngineConfig(HashMap<String, String> config) {
        config.put("base_dir", joinDirs("lealone", "amte"));
        config.put("redo_log_dir", "redo_log");
        config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_INSTANT);
        // config.put("checkpoint_service_loop_interval", "10"); // 10ms
        config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_PERIODIC);
        // config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_NO_SYNC);
        config.put("log_sync_period", "50"); // 500ms
    }

    protected int[] getRandomKeys() {
        ArrayList<Integer> list = new ArrayList<>(rowCount);
        for (int i = 0; i < rowCount; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] keys = new int[rowCount];
        for (int i = 0; i < rowCount; i++) {
            keys[i] = list.get(i);
        }
        return keys;
    }

    protected boolean isRandom() {
        return isRandom != null && isRandom;
    }

    protected boolean isWrite() {
        return write != null && write;
    }

    public void run(String[] args) throws Exception {
        run();
    }

    public void run() throws Exception {
        init();
        try {
            runLoop();
        } finally {
            destroy();
        }
    }

    protected void runLoop() throws Exception {
        for (int i = 1; i <= loopCount; i++) {
            run(i);
        }
    }

    protected void run(int loop) throws Exception {
        resetFields();
        runBenchTestTasks();

        long totalTime = endTime.get() - startTime.get();
        long avgTime = totalTime / threadCount;

        String str = "";
        if (isRandom != null) {
            if (isRandom)
                str += " random ";
            else
                str += " serial ";

            if (write != null) {
                if (write)
                    str += "write";
                else
                    str += "read";
            } else {
                str += "write";
            }
        }
        printRunResult(loop, totalTime, avgTime, str);
    }

    protected void printRunResult(int loop, long totalTime, long avgTime, String str) {
        printResult(loop, ", row count: " + rowCount + ", thread count: " + threadCount + str
                + ", total time: " + totalTime + " ms, avg time: " + avgTime + " ms");
    }

    protected void resetFields() {
        startTime.set(0);
        endTime.set(0);
        pendingOperations.set(rowCount);
        latch = new CountDownLatch(1);
    }

    protected void notifyOperationComplete() {
        if (latch == null)
            return;
        if (pendingOperations.decrementAndGet() <= 0) {
            endTime.set(System.currentTimeMillis());
            latch.countDown();
        }
        // System.out.println(pendingOperations.get());
    }

    protected void destroy() throws Exception {
    }

    protected BenchTestTask createBenchTestTask(int start, int end) throws Exception {
        return null;
    }

    private void runBenchTestTasks() throws Exception {
        int avg = rowCount / threadCount;
        BenchTestTask[] tasks = new BenchTestTask[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int start = i * avg;
            int end = (i + 1) * avg;
            if (i == threadCount - 1)
                end = rowCount;
            tasks[i] = createBenchTestTask(start, end);
        }

        for (int i = 0; i < threadCount; i++) {
            if (tasks[i].needCreateThread()) {
                new Thread(tasks[i], tasks[i].name).start();
            } else {
                // 什么都不做，后台线程会自己运行
            }
        }
        latch.await();
        for (int i = 0; i < threadCount; i++) {
            try {
                tasks[i].stopBenchTest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract class BenchTestTask implements Runnable {
        protected final int start;
        protected final int end;
        protected final String name;

        public BenchTestTask(int start, int end) throws Exception {
            this.start = start;
            this.end = end;
            name = getClass().getSimpleName() + "-" + start;
        }

        @Override
        public void run() {
            // 取最早启动的那个线程的时间
            startTime.compareAndSet(0, System.currentTimeMillis());
            try {
                startBenchTest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public abstract void startBenchTest() throws Exception;

        public void stopBenchTest() throws Exception {
        }

        public boolean needCreateThread() {
            return true;
        }
    }
}
