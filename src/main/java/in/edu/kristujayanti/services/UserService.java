package in.edu.kristujayanti.services;

import in.edu.kristujayanti.config.MongoConnection;
import in.edu.kristujayanti.model.User;
import in.edu.kristujayanti.util.TokenUtil;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final MongoClient mongoClient;
    private final MailService mailService;

    public UserService(Vertx vertx) {
        this.mongoClient = MongoConnection.getInstance(vertx);
        this.mailService = new MailService(vertx);
    }
    public Future<String> loginUser(String email, String password) {
        Promise<String> promise = Promise.promise();

        String normalizedEmail = email.trim().toLowerCase();
        JsonObject query = new JsonObject().put("email", normalizedEmail);

        mongoClient.findOne("users", query, null).onComplete(res -> {
            if (res.succeeded()) {
                JsonObject userJson = res.result();
                if (userJson == null) {
                    promise.fail("User not found.");
                } else {
                    String hashedPassword = userJson.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        // Generate JWT token
                        String token = TokenUtil.generateToken(userJson);
                        promise.complete(token);
                    } else {
                        promise.fail("Invalid password.");
                    }
                }
            } else {
                promise.fail("Failed to retrieve user.");
            }
        });

        return promise.future();
    }

    public Future<String> registerUser(String name, String email) {
        Promise<String> promise = Promise.promise();

        String normalizedEmail = email.trim().toLowerCase();
        JsonObject query = new JsonObject().put("email", normalizedEmail);

        mongoClient.findOne("users", query, null).onComplete(res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    promise.fail("Email already registered.");
                } else {
                    String password = TokenUtil.generateRandomPassword(12);
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    User user = new User();
                    user.setName(name);
                    user.setEmail(normalizedEmail);
                    user.setHashedPassword(hashedPassword);
                    user.setRole("user");

                    mongoClient.insert("users", user.toJson()).onComplete(insertRes -> {
                        if (insertRes.succeeded()) {
                            mailService.sendPasswordEmail(normalizedEmail, password).onComplete(mailRes -> {
                                if (mailRes.succeeded()) {
                                    System.out.println("✅ Email sent to " + normalizedEmail);
                                } else {
                                    System.out.println("❌ Failed to send email to " + normalizedEmail);
                                    mailRes.cause().printStackTrace();
                                }
                            });
                            promise.complete("User registered successfully. Password sent to email.");
                        } else {
                            promise.fail("Failed to save user.");
                        }
                    });
                }
            } else {
                promise.fail("Failed to check existing user.");
            }
        });

        return promise.future();
    }
}
