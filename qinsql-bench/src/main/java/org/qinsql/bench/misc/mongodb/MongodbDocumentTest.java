/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.bench.misc.mongodb;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class MongodbDocumentTest {

    public static void main(String[] args) {
        Document doc = Document.parse("{ status: { $in: [ \"A\", \"D\" ] } }");
        System.out.println(doc.toJson());

        Bson bson = Filters.and(Filters.eq("_id", 1), Filters.eq("_id", 2));
        System.out.println(bson.toBsonDocument().toJson());

        bson = Filters.in("_id", 1, 2, 2);
        System.out.println(bson.toBsonDocument().toJson());
    }

}
