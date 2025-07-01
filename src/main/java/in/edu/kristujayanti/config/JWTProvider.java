package in.edu.kristujayanti.config;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.JWTOptions;

public class JWTProvider {
    private static JWTAuth jwtAuth;

    public static JWTAuth getInstance(Vertx vertx) {
        if (jwtAuth == null) {
            jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                    .setAlgorithm("HS256")
                    .setBuffer("your_jwt_secret")
                ));
        }
        return jwtAuth;
    }

    public static String generateToken(JWTAuth jwt, String userId, String role) {
        JsonObject claims = new JsonObject()
            .put("userId", userId)
            .put("role", role);

        return jwt.generateToken(claims, new JWTOptions().setExpiresInMinutes(60));
    }
}