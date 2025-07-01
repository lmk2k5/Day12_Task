package in.edu.kristujayanti.handlers;

import in.edu.kristujayanti.services.EventService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.List;

public class EventHandler {
    private final EventService eventService;

    public EventHandler(EventService eventService) {
        this.eventService = eventService;
    }

    public void createEvent(RoutingContext ctx) {
        JsonObject event = ctx.body().asJsonObject();
        eventService.createEvent(event)
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

    public void getEvents(RoutingContext ctx) {
        try {
            // Get page with default value 1
            List<String> pageParams = ctx.queryParam("page");
            int page = pageParams.isEmpty() ? 1 : Integer.parseInt(pageParams.get(0));

            // Get limit with default value 10
            List<String> limitParams = ctx.queryParam("limit");
            int limit = limitParams.isEmpty() ? 10 : Integer.parseInt(limitParams.get(0));

            // Get status (optional)
            List<String> statusParams = ctx.queryParam("status");
            String status = statusParams.isEmpty() ? null : statusParams.get(0);

            eventService.getEvents(page, limit, status)
                    .onSuccess(events -> {
                        ctx.response()
                                .end(events.encode());
                    })
                    .onFailure(err -> {
                        ctx.response()
                                .setStatusCode(400)
                                .end(new JsonObject().put("error", err.getMessage()).encode());
                    });
        } catch (NumberFormatException e) {
            ctx.response()
                    .setStatusCode(400)
                    .end(new JsonObject().put("error", "Invalid page or limit parameter").encode());
        }
    }
}