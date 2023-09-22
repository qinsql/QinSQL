/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class MongodbSyncBTest {

    int threadCount = 48;
    int outerLoop = 30;
    int innerLoop = 200;

    int clientCount = 2; // 超过cpu核数性能会下降
    MongoClient[] mongoClients;

    void beforeBenchTest() {
    }

    void afterBenchTest() {
    }

    void run(int port) {
        createMongoClients(port);
        beforeBenchTest();
        for (int i = 0; i < outerLoop; i++) {
            benchTest();
        }
        afterBenchTest();
        closeMongoClients();
    }

    void createMongoClients(int port) {
        String connectionString = "mongodb://127.0.0.1:" + port;
        connectionString += "/?maxPoolSize=" + threadCount + "&&minPoolSize=" + (threadCount / 2);
        mongoClients = new MongoClient[clientCount];
        for (int i = 0; i < clientCount; i++) {
            mongoClients[i] = MongoClients.create(connectionString);
        }
    }

    void closeMongoClients() {
        for (int i = 0; i < clientCount; i++) {
            mongoClients[i].close();
        }
    }

    MongoCollection<Document> getCollection(int clientIndex) {
        MongoClient mongoClient = mongoClients[clientIndex % clientCount];
        MongoDatabase database = mongoClient.getDatabase("test");
        return database.getCollection(getClass().getSimpleName());
    }

    void benchTest() {
        AtomicLong totalTime = new AtomicLong();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            threads[i] = new Thread(() -> {
                MongoCollection<Document> collection = getCollection(index);
                long t1 = System.nanoTime();
                for (int j = 0; j < innerLoop; j++) {
                    execute(collection);
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
        System.out.println(getClass().getSimpleName() + " thread count: " + (threadCount)
                + ", document count: " + (threadCount * innerLoop) + ", total time: "
                + totalTime.get() / 1000 / 1000 / threadCount + " ms");
    }

    abstract void execute(MongoCollection<Document> collection);
}
