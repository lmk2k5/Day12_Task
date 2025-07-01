package in.edu.kristujayanti.config;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoConnection {

    private static MongoClient mongoClient;

    public static MongoClient getInstance(Vertx vertx) {
        if (mongoClient == null) {
            JsonObject config = new JsonObject()
                    .put("connection_string", Config.MONGO_URI)
                    .put("db_name", "event-ticket-db");

            mongoClient = MongoClient.create(vertx, config);
        }
        return mongoClient;
    }
}
