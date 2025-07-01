package in.edu.kristujayanti.services;

import in.edu.kristujayanti.config.MongoConnection;
import in.edu.kristujayanti.util.TokenUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class BookingService {
    private final MongoClient mongoClient;
    private final MailService mailService;

    public BookingService(Vertx vertx) {
        this.mongoClient = MongoConnection.getInstance(vertx);
        this.mailService = new MailService(vertx);
    }

    public Future<String> createBooking(String userId, String eventId) {
        String token = TokenUtil.generateRandomPassword(12);
        JsonObject booking = new JsonObject()
                .put("userId", userId)
                .put("eventId", eventId)
                .put("token", token)
                .put("bookingTime", new java.util.Date().toString())
                .put("validated", false);

        return mongoClient.insert("bookings", booking)
                .compose(id -> {
                    return mailService.sendBookingConfirmation(
                            userId,
                            eventId,
                            token
                    ).map(id);
                });
    }
}