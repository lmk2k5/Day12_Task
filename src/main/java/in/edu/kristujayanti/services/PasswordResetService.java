package in.edu.kristujayanti.services;

import in.edu.kristujayanti.config.MongoConnection;
import in.edu.kristujayanti.config.RedisConnection;
import in.edu.kristujayanti.util.TokenUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.redis.client.RedisAPI;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class PasswordResetService {
    private final MongoClient mongo;
    private final MailService mailService;
    private final RedisAPI redis;

    public PasswordResetService(Vertx vertx) {
        this.mongo = MongoConnection.getInstance(vertx);
        this.mailService = new MailService(vertx);
        this.redis = RedisConnection.getInstance(vertx);
    }

    public void initiateReset(String email, io.vertx.core.Handler<io.vertx.core.AsyncResult<String>> handler) {
        mongo.findOne("users", new JsonObject().put("email", email), null, res -> {
            if (res.succeeded() && res.result() != null) {
                String token = TokenUtil.generateRandomToken();

                // Store token in Redis (TTL: 10 minutes)
                redis.setex(List.of("reset:" + token, "600", email), redisRes -> {
                    if (redisRes.succeeded()) {
                        mailService.sendMail(email, "Password Reset Link", "Use this token: " + token);
                        handler.handle(io.vertx.core.Future.succeededFuture("Reset token sent"));
                    } else {
                        handler.handle(io.vertx.core.Future.failedFuture("Failed to store token"));
                    }
                });
            } else {
                handler.handle(io.vertx.core.Future.failedFuture("Email not registered"));
            }
        });
    }

    public void completeReset(String token, String newPassword, io.vertx.core.Handler<io.vertx.core.AsyncResult<String>> handler) {
        redis.get("reset:" + token, redisRes -> {
            if (redisRes.succeeded() && redisRes.result() != null) {
                String email = redisRes.result().toString();
                String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                mongo.updateCollection("users",
                        new JsonObject().put("email", email),
                        new JsonObject().put("$set", new JsonObject().put("passwordHash", hashed)),
                        updateRes -> {
                            if (updateRes.succeeded()) {
                                redis.del(List.of("reset:" + token), delRes -> {});
                                handler.handle(io.vertx.core.Future.succeededFuture("Password reset successful"));
                            } else {
                                handler.handle(io.vertx.core.Future.failedFuture("Update failed"));
                            }
                        });
            } else {
                handler.handle(io.vertx.core.Future.failedFuture("Invalid or expired token"));
            }
        });
    }
}
