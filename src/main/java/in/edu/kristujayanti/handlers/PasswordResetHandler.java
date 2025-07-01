package in.edu.kristujayanti.handlers;

import in.edu.kristujayanti.services.PasswordResetService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class PasswordResetHandler {
    private final PasswordResetService service;

    public PasswordResetHandler(Vertx vertx) {
        this.service = new PasswordResetService(vertx);
    }

    public void setupRoutes(Router router) {
        router.post("/password-reset/initiate").handler(this::initiateReset);
        router.post("/password-reset/complete").handler(this::completeReset);
    }

    private void initiateReset(RoutingContext ctx) {
        String email = ctx.body().asJsonObject().getString("email");

        service.initiateReset(email, res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.response().setStatusCode(400).end(res.cause().getMessage());
            }
        });
    }

    private void completeReset(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String token = body.getString("token");
        String newPassword = body.getString("newPassword");

        service.completeReset(token, newPassword, res -> {
            if (res.succeeded()) {
                ctx.response().end(res.result());
            } else {
                ctx.response().setStatusCode(400).end(res.cause().getMessage());
            }
        });
    }
}
