package in.edu.kristujayanti.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.vertx.core.json.JsonObject;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class TokenUtil {

    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24 hours
    private static final Key KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));

    // ✅ JWT Token Generator
    public static String generateToken(JsonObject userJson) {
        return Jwts.builder()
                .setSubject(userJson.getString("email"))
                .claim("role", userJson.getString("role", "user"))
                .claim("id", userJson.getString("_id"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Random Password Generator
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
