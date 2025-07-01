package in.edu.kristujayanti.handlers;

import in.edu.kristujayanti.services.BookingService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BookingHandler {
    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void createBooking(RoutingContext ctx) {
        String userId = ctx.user().principal().getString("id");
        JsonObject body = ctx.body().asJsonObject();
        String eventId = body.getString("eventId");

        bookingService.createBooking(userId, eventId)
                .onSuccess(id -> {
                    ctx.response()
                            .setStatusCode(201)
                            .end(new JsonObject().put("id", id).encode());
                })
                .onFailure(err -> {
                    ctx.response()
                            .setStatusCode(400)
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                });
    }
}