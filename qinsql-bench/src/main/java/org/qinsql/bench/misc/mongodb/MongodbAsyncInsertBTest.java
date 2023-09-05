/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.reactivestreams.client.MongoCollection;

public class MongodbAsyncInsertBTest extends MongodbAsyncBTest {

    private final static AtomicInteger id = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        new MongodbAsyncInsertBTest().run();
    }

    @Override
    void beforeBenchTest() {
        MongoCollection<Document> collection = getCollection(0);
        CountDownLatch latch0 = new CountDownLatch(1);
        collection.drop().subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Void t) {
            }

            @Override
            public void onError(Throwable t) {
                latch0.countDown();
            }

            @Override
            public void onComplete() {
                latch0.countDown();
            }
        });
        try {
            latch0.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void afterBenchTest() {
        System.out.println("total document count: " + countDocuments());
    }

    @Override
    void execute(MongoCollection<Document> collection, CountDownLatch latch, AtomicInteger counter) {
        Document doc = new Document("_id", id.incrementAndGet()).append("f1", 1);
        insertOne(collection, latch, counter, doc);
    }
}
