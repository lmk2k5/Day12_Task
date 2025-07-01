package in.edu.kristujayanti.model;

import io.vertx.core.json.JsonObject;
import java.time.LocalDateTime;

public class Event {
    private String id;
    private String name;
    private String description;
    private LocalDateTime time;
    private String location;
    private int tokenLimit;
    private String status; // "active", "cancelled", "completed"

    // Getters and setters

    public JsonObject toJson() {
        return new JsonObject()
                .put("name", name)
                .put("description", description)
                .put("time", time.toString())
                .put("location", location)
                .put("tokenLimit", tokenLimit)
                .put("status", status);
    }
}