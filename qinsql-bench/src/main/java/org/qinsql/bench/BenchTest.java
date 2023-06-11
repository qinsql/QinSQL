/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

//以单元测试的方式运行会比通过main方法运行得出稍微慢一些的测试结果，
//这可能是因为单元测试额外启动了一个ReaderThread占用了一些资源
//-Xms512M -Xmx512M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

//-XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx800M
public abstract class BenchTest {

    public static final String BENCH_TEST_BASE_DIR = "." + File.separatorChar + "target"
            + File.separatorChar + "bench-test-data";

    protected static final int DEFAULT_ROW_COUNT = 1 * 10000;
    protected int rowCount = DEFAULT_ROW_COUNT; // 总记录数
    protected int threadCount = Runtime.getRuntime().availableProcessors();

    protected void init() throws Exception {
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
