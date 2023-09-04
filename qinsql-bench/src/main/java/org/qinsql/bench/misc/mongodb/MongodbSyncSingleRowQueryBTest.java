/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongodbSyncSingleRowQueryBTest {

    private static AtomicInteger id = new AtomicInteger();

    public static void main(String[] args) {
        new MongodbSyncSingleRowQueryBTest().run();
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
        if (collection.countDocuments() <= 0)
            insert(collection);
        // query(collection);

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

    static void insert(MongoCollection<Document> collection) {
        for (int i = 0; i < 10000; i++) {
            Document doc1 = new Document("_id", id.incrementAndGet()).append("f1", 1);
            collection.insertOne(doc1);
        }
        System.out.println("total document count: " + collection.countDocuments());
    }

    static void query(MongoCollection<Document> collection) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("_id", 1)).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

    }

    void query(MongoCollection<Document> collection, MongoClient[] mongoClients) {
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
                    MongoCursor<Document> cursor = collection2
                            .find(Filters.eq("_id", random.nextInt(rowCount))).iterator();
                    try {
                        while (cursor.hasNext()) {
                            cursor.next();
                        }
                    } finally {
                        cursor.close();
                    }
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
        System.out.println("MongodbSyncSingleRowQueryBTest thread count: " + (threadCount)
                + ", document count: " + (threadCount * innerLoop) + ", total time: "
                + totalTime.get() / 1000 / 1000 / threadCount + " ms");
    }
}
