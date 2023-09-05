/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;

public class MongodbAsyncSingleRowQueryBTest extends MongodbAsyncBTest {

    public static void main(String[] args) {
        new MongodbAsyncSingleRowQueryBTest().run();
    }

    private final static AtomicInteger id = new AtomicInteger();
    private final int rowCount = 10000; // 总记录数
    private final Random random = new Random();

    @Override
    void beforeBenchTest() {
        threadCount = 16;
        innerLoop = 250;
        MongoCollection<Document> collection = getCollection(0);
        if (countDocuments() <= 0)
            insert(collection);
    }

    void insert(MongoCollection<Document> collection) {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(rowCount);
        for (int i = 0; i < rowCount; i++) {
            Document doc = new Document("_id", id.incrementAndGet()).append("f1", 1);
            insertOne(collection, latch, counter, doc);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("total document count: " + countDocuments());
    }

    @Override
    void execute(MongoCollection<Document> collection, CountDownLatch latch, AtomicInteger counter) {
        FindPublisher<Document> findPublisher = collection
                .find(Filters.eq("_id", random.nextInt(rowCount)));
        findPublisher.subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Document t) {
                // System.out.println(t.toJson());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                if (counter.decrementAndGet() == 0) {
                    latch.countDown();
                }
            }
        });
    }
}
