package JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "mySecretKeyForJWTAuthenticationEventManagerApplicationPiotrGendaszewski2026MySecretKeyForJWTAuthenticationEventManagerApplicationPiotrGendaszewski2026";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    public static String generateToken(int userId, String username, boolean isAdmin) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("isAdmin", isAdmin)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage());
        }
    }

    public static String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public static boolean isAdminFromToken(String token) {
        return validateToken(token).get("isAdmin", Boolean.class);
    }

    public static int getUserIdFromToken(String token) {
        return validateToken(token).get("userId", Integer.class);
    }
}

