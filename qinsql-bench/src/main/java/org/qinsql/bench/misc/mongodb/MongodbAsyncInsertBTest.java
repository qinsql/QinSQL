/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

public class MongodbAsyncInsertBTest {

    private static AtomicInteger id = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        new MongodbAsyncInsertBTest().run();
    }

    int threadCount = 48;
    int outerLoop = 30;
    int innerLoop = 200;
    boolean multiClients = true; // 改为true也没区别

    void run() throws Exception {
        String connectionString = "mongodb://127.0.0.1:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("test");
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
        latch0.await();

        MongoClient[] mongoClients = new MongoClient[threadCount];
        if (multiClients) {
            for (int i = 0; i < threadCount; i++) {
                mongoClients[i] = MongoClients.create(connectionString);
            }
        }

        for (int i = 0; i < outerLoop; i++) {
            insert(collection, mongoClients);
        }

        CountDownLatch latch = new CountDownLatch(1);
        collection.countDocuments().subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Long t) {
                System.out.println("total document count: " + t);
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });
        latch.await();
        mongoClient.close();
        if (multiClients) {
            for (int i = 0; i < threadCount; i++) {
                mongoClients[i].close();
            }
        }
    }

    void insert(MongoCollection<Document> collection, MongoClient[] mongoClients) {
        AtomicLong totalTime = new AtomicLong();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            threads[i] = new Thread(() -> {
                CountDownLatch latch = new CountDownLatch(1);
                AtomicInteger counter = new AtomicInteger(innerLoop);
                MongoCollection<Document> collection2 = collection;
                if (multiClients) {
                    MongoClient mongoClient = mongoClients[index];
                    MongoDatabase database = mongoClient.getDatabase("test");
                    collection2 = database.getCollection("test");
                }
                long t1 = System.nanoTime();
                for (int j = 0; j < innerLoop; j++) {
                    insert(collection2, latch, counter);
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long t2 = System.nanoTime();
                totalTime.addAndGet(t2 - t1);
            });
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("MongodbAsyncInsertBTest thread count: " + (threadCount)
                + ", document count: " + (threadCount * innerLoop) + ", total time: "
                + totalTime.get() / 1000 / 1000 / threadCount + " ms");
    }

    void insert(MongoCollection<Document> collection, CountDownLatch latch, AtomicInteger counter) {
        Document doc = new Document("_id", id.incrementAndGet()).append("f1", 1);
        Publisher<InsertOneResult> publisher = collection.insertOne(doc);
        publisher.subscribe(new Subscriber<InsertOneResult>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(InsertOneResult t) {
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
