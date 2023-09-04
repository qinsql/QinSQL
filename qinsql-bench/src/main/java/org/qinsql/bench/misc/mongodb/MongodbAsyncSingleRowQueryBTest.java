/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

public class MongodbAsyncSingleRowQueryBTest {

    public static void main(String[] args) {
        new MongodbAsyncSingleRowQueryBTest().run();
    }

    int rowCount = 10000; // 总记录数
    int threadCount = 16;
    int outerLoop = 50;
    int innerLoop = 250;
    boolean multiClients = true; // 改为true也没区别
    Random random = new Random();

    void run() {
        String connectionString = "mongodb://127.0.0.1:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("test");

        MongoClient[] mongoClients = new MongoClient[threadCount];
        if (multiClients) {
            for (int i = 0; i < threadCount; i++) {
                mongoClients[i] = MongoClients.create(connectionString);
            }
        }

        for (int i = 0; i < outerLoop; i++) {
            query(collection, mongoClients);
        }
        mongoClient.close();
        if (multiClients) {
            for (int i = 0; i < threadCount; i++) {
                mongoClients[i].close();
            }
        }
    }

    void query(MongoCollection<Document> collection, MongoClient[] mongoClients) {
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
                    query(collection2, latch, counter);
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
        System.out.println("MongodbAsyncSingleRowQueryBTest thread count: " + (threadCount)
                + ", document count: " + (threadCount * innerLoop) + ", total time: "
                + totalTime.get() / 1000 / 1000 / threadCount + " ms");
    }

    void query(MongoCollection<Document> collection, CountDownLatch latch, AtomicInteger counter) {
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
