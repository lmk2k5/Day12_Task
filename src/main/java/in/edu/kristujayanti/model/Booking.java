package in.edu.kristujayanti.model;

import io.vertx.core.json.JsonObject;
import java.time.LocalDateTime;

public class Booking {
    private String id;
    private String userId;
    private String eventId;
    private String token;
    private LocalDateTime bookingTime;
    private boolean validated;

    // Getters and setters

    public JsonObject toJson() {
        return new JsonObject()
                .put("userId", userId)
                .put("eventId", eventId)
                .put("token", token)
                .put("bookingTime", bookingTime.toString())
                .put("validated", validated);
    }
}