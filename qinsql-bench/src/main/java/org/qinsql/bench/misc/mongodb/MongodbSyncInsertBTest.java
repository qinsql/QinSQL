/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongodbSyncInsertBTest {

    private static AtomicInteger id = new AtomicInteger();

    public static void main(String[] args) {
        new MongodbSyncInsertBTest().run();
    }

    int threadCount = 48;
    int outerLoop = 30;
    int innerLoop = 200;
    boolean multiClients = true; // 改为true也没区别

    void run() {
        String connectionString = "mongodb://127.0.0.1:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("test");
        collection.drop();

        MongoClient[] mongoClients = new MongoClient[threadCount];
        if (multiClients) {
            for (int i = 0; i < threadCount; i++) {
                mongoClients[i] = MongoClients.create(connectionString);
            }
        }

        for (int i = 0; i < outerLoop; i++) {
            insert(collection, mongoClients);
        }
        System.out.println("total document count: " + collection.countDocuments());

        int count = 5;
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                if (--count == 0)
                    break;
            }
        } finally {
            cursor.close();
        }

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
                MongoCollection<Document> collection2 = collection;
                if (multiClients) {
                    MongoClient mongoClient = mongoClients[index];
                    MongoDatabase database = mongoClient.getDatabase("test");
                    collection2 = database.getCollection("test");
                }
                long t1 = System.nanoTime();
                for (int j = 0; j < innerLoop; j++) {
                    Document doc = new Document("_id", id.incrementAndGet()).append("f1", 1);
                    collection2.insertOne(doc);
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
        System.out.println("MongodbSyncInsertBTest thread count: " + (threadCount) + ", document count: "
                + (threadCount * innerLoop) + ", total time: "
                + totalTime.get() / 1000 / 1000 / threadCount + " ms");
    }
}
