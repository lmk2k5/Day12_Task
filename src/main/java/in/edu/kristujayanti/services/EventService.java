package in.edu.kristujayanti.services;

import in.edu.kristujayanti.config.MongoConnection;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

public class EventService {
    private final MongoClient mongoClient;

    public EventService(Vertx vertx) {
        this.mongoClient = MongoConnection.getInstance(vertx);
    }

    public Future<String> createEvent(JsonObject event) {
        return mongoClient.insert("events", event);
    }

    public Future<JsonObject> getEvents(int page, int limit, String status) {
        JsonObject query = new JsonObject();
        if (status != null) {
            query.put("status", status);
        }

        FindOptions options = new FindOptions()
                .setSkip((page - 1) * limit)
                .setLimit(limit)
                .setSort(new JsonObject().put("time", 1));

        return mongoClient.findWithOptions("events", query, options)
                .map(events -> new JsonObject().put("events", events));
    }
}