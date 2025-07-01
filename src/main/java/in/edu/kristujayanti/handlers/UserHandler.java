package in.edu.kristujayanti.handlers;

import in.edu.kristujayanti.services.UserService;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;

public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();

        String email = body.getString("email");
        String name = body.getString("name");

        if (email == null || name == null) {
            ctx.response()
                    .setStatusCode(400)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("error", "Missing email or name").encodePrettily());
            return;
        }

        userService.registerUser(name, email).onSuccess(result -> {
            ctx.response()
                    .setStatusCode(201)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("message", result).encodePrettily());
        }).onFailure(err -> {
            ctx.response()
                    .setStatusCode(400)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("error", err.getMessage()).encodePrettily());
        });
    }


}
