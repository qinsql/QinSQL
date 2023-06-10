/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.lealone.db.LealoneDatabase;
import org.lealone.db.SysProperties;
import org.lealone.test.TestBase;
import org.lealone.transaction.aote.log.LogSyncService;

//以单元测试的方式运行会比通过main方法运行得出稍微慢一些的测试结果，
//这可能是因为单元测试额外启动了一个ReaderThread占用了一些资源
//-Xms512M -Xmx512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

//-XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx800M
public abstract class BenchTest {

    public static final int PG_PORT = 9510;

    public static final String BENCH_TEST_BASE_DIR = "." + File.separatorChar + "target"
            + File.separatorChar + "bench-test-data";

    public static Connection getConnection(int port, String user, String password) throws Exception {
        String url = "jdbc:postgresql://localhost:" + port + "/test";
        return getConnection(url, user, password);
    }

    public static Connection getConnection(String url, String user, String password) throws Exception {
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);
        return DriverManager.getConnection(url, info);
    }

    public static String joinDirs(String... dirs) {
        StringBuilder s = new StringBuilder(BENCH_TEST_BASE_DIR);
        for (String dir : dirs)
            s.append(File.separatorChar).append(dir);
        return s.toString();
    }

    public static Connection getH2Connection() throws Exception {
        return getH2Connection(false);
    }

    public static Connection getH2Connection(boolean isEmbedded) throws Exception {
        String url;
        if (isEmbedded) {
            url = "jdbc:h2:file:./EmbeddedBenchTestDB";
            // url = "jdbc:h2:mem:mydb";
        } else {
            url = "jdbc:h2:tcp://localhost:9092/CSBenchTestDB";
        }
        // url += ";OPEN_NEW=true;FORBID_CREATION=false";
        url += ";FORBID_CREATION=false";
        return DriverManager.getConnection(url, "sa", "");
    }

    public static Connection getLealoneConnection() throws Exception {
        return getLealoneConnection(false);
    }

    public static Connection getLealoneConnection(boolean isEmbedded) throws Exception {
        TestBase test = new TestBase();
        test.setEmbedded(isEmbedded);
        String url = test.getURL(LealoneDatabase.NAME);
        SysProperties.setBaseDir(joinDirs("lealone"));
        return DriverManager.getConnection(url);
    }

    protected static final int DEFAULT_ROW_COUNT = 1 * 10000;
    protected int loopCount = 30; // 重复测试次数
    protected int rowCount = DEFAULT_ROW_COUNT; // 总记录数
    protected int threadCount = Runtime.getRuntime().availableProcessors();
    protected final AtomicLong pendingOperations = new AtomicLong(0);
    protected final AtomicLong startTime = new AtomicLong(0);
    protected final AtomicLong endTime = new AtomicLong(0);
    protected final AtomicBoolean inited = new AtomicBoolean(false);
    protected final int[] randomKeys;
    protected Boolean isRandom;
    protected Boolean write;
    protected CountDownLatch latch;

    protected BenchTest() {
        this(DEFAULT_ROW_COUNT);
    }

    protected BenchTest(int rowCount) {
        this.rowCount = rowCount;
        randomKeys = getRandomKeys();
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

    protected void initTransactionEngineConfig(HashMap<String, String> config) {
        config.put("base_dir", joinDirs("lealone", "amte"));
        config.put("redo_log_dir", "redo_log");
        config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_INSTANT);
        // config.put("checkpoint_service_loop_interval", "10"); // 10ms
        config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_PERIODIC);
        // config.put("log_sync_type", LogSyncService.LOG_SYNC_TYPE_NO_SYNC);
        config.put("log_sync_period", "50"); // 500ms
    }

    protected boolean isRandom() {
        return isRandom != null && isRandom;
    }

    protected boolean isWrite() {
        return write != null && write;
    }

    public static void println() {
        System.out.println();
    }

    public void printResult(String str) {
        System.out.println(this.getClass().getSimpleName() + ": " + str);
    }

    public void printResult(int loop, String str) {
        System.out.println(this.getClass().getSimpleName() + ": loop: " + loop + str);
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

    protected void init() throws Exception {
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

    public static void printMemoryUsage() {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;

        System.out.println("Heap size:");
        System.out.println("-------------------");
        System.out.println("TotalMemory: " + formatSize(total));
        System.out.println("UsedMemory:  " + formatSize(used));
        System.out.println("FreeMemory:  " + formatSize(free));

        MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nmu = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        System.out.println("HeapMemoryUsage: " + mu);
        System.out.println("NonHeapMemoryUsage: " + nmu);
    }

    public static String formatSize(long size) {
        if (size < 1014)
            return size + "Bytes";
        if (size >= 1 << 30)
            return String.format("%.1fG", size / (double) (1 << 30));
        if (size >= 1 << 20)
            return String.format("%.1fM", size / (double) (1 << 20));
        return String.format("%.1fK", size / (double) (1 << 10));
    }
}
