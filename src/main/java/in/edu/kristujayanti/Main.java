package in.edu.kristujayanti;

import in.edu.kristujayanti.handlers.UserHandler;
import in.edu.kristujayanti.services.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        // Initialize UserService and Handler
        UserService userService = new UserService(vertx);
        UserHandler userHandler = new UserHandler(userService);

        // Setup router
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // to parse JSON body

        // ✅ Register Route
        router.post("/register").handler(userHandler::register);

        // Start server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(server -> {
                    System.out.println("✅ Server started on port " + server.actualPort());
                })
                .onFailure(err -> {
                    System.err.println("❌ Failed to start server: " + err.getMessage());
                });

    }
}
