/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.transaction;

import java.nio.ByteBuffer;

import org.qinsql.bench.embed.EmbeddedBTest;

public abstract class TransactionBTest extends EmbeddedBTest {

    public static void run(TransactionBTest test) throws Exception {
        test.run();
    }

    protected final String mapName = getClass().getSimpleName();

    protected TransactionBTest() {
        super(10000);
    }

    @Override
    public void run() throws Exception {
        init();
        try {
            isRandom = true;
            runLoop();

            println();

            isRandom = false;
            runLoop();
        } finally {
            destroy();
        }
    }

    void testByteBufferAllocate() {
        for (int j = 0; j < 20; j++) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 25000; i++) {
                ByteBuffer.allocate(1024 * 1024);
                // DataBuffer writeBuffer = DataBuffer.create(1024 * 1024);
                // try (DataBuffer writeBuffer = DataBuffer.create()) {
                // ByteBuffer buffer = writeBuffer.getAndFlipBuffer();
                // ByteBuffer operations = ByteBuffer.allocateDirect(buffer.limit());
                // operations.put(buffer);
                // operations.flip();
                // }
            }
            long t2 = System.currentTimeMillis();
            printResult("ByteBufferAllocate time: " + (t2 - t1) + " ms");
        }
    }

    protected abstract void write(int start, int end) throws Exception;

    @Override
    protected BenchTestTask createBenchTestTask(int start, int end) throws Exception {
        return new TransactionBenchTestTask(start, end);
    }

    protected class TransactionBenchTestTask extends BenchTestTask {

        TransactionBenchTestTask(int start, int end) throws Exception {
            super(start, end);
        }

        @Override
        public void startBenchTest() throws Exception {
            write(start, end);
        }
    }
}
