package JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // Fixed secret key - use a strong, fixed key for production
    private static final String SECRET = "mySecretKeyForJWTAuthenticationEventManagerApplicationPiotrGendaszewski2026MySecretKeyForJWTAuthenticationEventManagerApplicationPiotrGendaszewski2026";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    /**
     * Generates a JWT token for a user
     * @param userId user ID
     * @param username username/login
     * @param isAdmin whether user is admin
     * @return JWT token string
     */
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

    /**
     * Validates and parses JWT token
     * @param token JWT token string
     * @return Claims object
     * @throws RuntimeException if token is invalid or expired
     */
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

    /**
     * Extracts username from token
     * @param token JWT token string
     * @return username
     */
    public static String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    /**
     * Checks if user is admin from token
     * @param token JWT token string
     * @return true if admin, false otherwise
     */
    public static boolean isAdminFromToken(String token) {
        return validateToken(token).get("isAdmin", Boolean.class);
    }

    /**
     * Extracts user ID from token
     * @param token JWT token string
     * @return user ID
     */
    public static int getUserIdFromToken(String token) {
        return validateToken(token).get("userId", Integer.class);
    }
}

