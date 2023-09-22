/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MongodbSyncInsertBTest extends MongodbSyncBTest {

    private final static AtomicInteger id = new AtomicInteger();

    public static void main(String[] args) {
        new MongodbSyncInsertBTest().run(27017);
    }

    @Override
    void beforeBenchTest() {
        MongoCollection<Document> collection = getCollection(0);
        collection.drop();
    }

    @Override
    void afterBenchTest() {
        MongoCollection<Document> collection = getCollection(0);
        System.out.println("total document count: " + collection.countDocuments());
        // query(collection);
    }

    void query(MongoCollection<Document> collection) {
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
    }

    @Override
    void execute(MongoCollection<Document> collection) {
        Document doc = new Document("_id", id.incrementAndGet()).append("f1", 1);
        collection.insertOne(doc);
    }
}
