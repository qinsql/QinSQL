/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc;

import java.io.File;
import java.io.IOException;

import org.lealone.storage.fs.FileUtils;
import org.qinsql.bench.BenchTest;

public class DeleteBTestData {

    public static void main(String[] args) throws IOException {
        FileUtils.deleteRecursive(BenchTest.BENCH_TEST_BASE_DIR, true);
        if (!FileUtils.exists(BenchTest.BENCH_TEST_BASE_DIR)) {
            System.out.println(
                    "dir '" + new File(BenchTest.BENCH_TEST_BASE_DIR).getCanonicalPath() + "' deleted");
        }
    }
}
