/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.embed.transaction;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.lealone.storage.aose.AOStorage;
import org.lealone.storage.aose.AOStorageBuilder;
import org.lealone.storage.page.DefaultPageOperationHandler;
import org.lealone.storage.page.PageOperation;
import org.lealone.storage.page.PageOperationHandlerFactory;
import org.lealone.test.aote.TransactionEngineTest;
import org.lealone.transaction.Transaction;
import org.lealone.transaction.TransactionEngine;
import org.lealone.transaction.TransactionMap;

public class LealoneTransactionBTest extends TransactionBTest {

    public static void main(String[] args) throws Exception {
        printMemoryUsage();
        LealoneTransactionBTest test = new LealoneTransactionBTest();
        run(test);
    }

    protected AOStorage storage;
    protected String storagePath;

    private final HashMap<String, String> config = new HashMap<>();
    private final AtomicInteger index = new AtomicInteger(0);
    private DefaultPageOperationHandler[] handlers;
    private TransactionEngine te;

    @Override
    protected void resetFields() {
        super.resetFields();
        index.set(0);
    }

    @Override
    protected void init() throws Exception {
        String factoryType = "RoundRobin";
        factoryType = "Random";
        // factoryType = "LoadBalance";
        config.put("page_operation_handler_factory_type", factoryType);
        // config.put("page_operation_handler_count", (threadCount + 1) + "");
        createPageOperationHandlers();

        AOStorageBuilder builder = new AOStorageBuilder(config);
        storagePath = joinDirs("lealone", "aose");
        int pageSize = 16 * 1024;
        builder.storagePath(storagePath).compress().pageSize(pageSize).minFillRate(30);
        storage = builder.openStorage();

        initTransactionEngineConfig(config);
        te = TransactionEngineTest.getTransactionEngine(config);

        singleThreadSerialWrite();
    }

    @Override
    protected void destroy() throws Exception {
        te.close();
        storage.close();
    }

    private void createPageOperationHandlers() {
        handlers = new DefaultPageOperationHandler[threadCount];
        for (int i = 0; i < threadCount; i++) {
            handlers[i] = new DefaultPageOperationHandler(i, threadCount, config);
        }
        PageOperationHandlerFactory f = PageOperationHandlerFactory.create(config, handlers);
        f.startHandlers();
    }

    private void singleThreadSerialWrite() {
        Transaction t = te.beginTransaction(false);
        TransactionMap<Integer, String> map = t.openMap(mapName, storage);
        map.clear();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < rowCount; i++) {
            map.put(i, "valueaaa");
        }
        t.commit();
        long t2 = System.currentTimeMillis();

        printResult("single-thread serial write time: " + (t2 - t1) + " ms, row count: " + map.size());
    }

    @Override
    protected void write(int start, int end) throws Exception {
        Transaction t = te.beginTransaction(false);
        TransactionMap<Integer, String> map = t.openMap(mapName, storage);
        for (int i = start; i < end; i++) {
            Integer key;
            if (isRandom())
                key = randomKeys[i];
            else
                key = i;
            String value = "value-";// "value-" + key;
            // map.put(key, value);

            Transaction t2 = te.beginTransaction(false);
            TransactionMap<Integer, String> m = map.getInstance(t2);
            m.tryUpdate(key, value);
            t2.commit();
            // System.out.println(getName() + " key:" + key);
            notifyOperationComplete();
        }
    }

    @Override
    protected BenchTestTask createBenchTestTask(int start, int end) throws Exception {
        return new LealoneTransactionBenchTestTask(start, end);
    }

    class LealoneTransactionBenchTestTask extends TransactionBenchTestTask implements PageOperation {

        LealoneTransactionBenchTestTask(int start, int end) throws Exception {
            super(start, end);
            DefaultPageOperationHandler h = handlers[index.getAndIncrement()];
            h.handlePageOperation(this);
        }

        @Override
        public boolean needCreateThread() {
            return false;
        }
    }
}
