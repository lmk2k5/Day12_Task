package in.edu.kristujayanti.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class AuthHandler implements Handler<RoutingContext> {

    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @Override
    public void handle(RoutingContext ctx) {
        String authHeader = ctx.request().getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ctx.response().setStatusCode(401).end("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Pass user info to downstream handlers
            ctx.put("user", claims);
            ctx.next(); // Proceed to next handler
        } catch (Exception e) {
            ctx.response().setStatusCode(401).end("Invalid or expired token");
        }

    }
}
