/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class MongodbSyncSingleRowQueryBTest extends MongodbSyncBTest {

    public static void main(String[] args) {
        new MongodbSyncSingleRowQueryBTest().run(27017);
    }

    private final static AtomicInteger id = new AtomicInteger();
    private final int rowCount = 10000; // 总记录数
    private final Random random = new Random();

    @Override
    void beforeBenchTest() {
        threadCount = 16;
        innerLoop = 250;
        MongoCollection<Document> collection = getCollection(0);
        // collection.drop();
        if (collection.countDocuments() <= 0)
            insert(collection);
        // query(collection);
    }

    void insert(MongoCollection<Document> collection) {
        for (int i = 0; i < rowCount; i++) {
            Document doc1 = new Document("_id", id.incrementAndGet()).append("f1", i);
            collection.insertOne(doc1);
        }
        System.out.println("total document count: " + collection.countDocuments());
    }

    void query(MongoCollection<Document> collection) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("_id", 1)).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    void execute(MongoCollection<Document> collection) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("f1", random.nextInt(rowCount)))
                .iterator();
        try {
            while (cursor.hasNext()) {
                cursor.next();
                break;
            }
        } finally {
            cursor.close();
        }
    }
}
