package JWT;

/**
 * Utility class for password hashing and validation
 * Uses SHA-256 algorithm for consistent password hashing across the application
 */
public class PasswordUtil {

    /**
     * Hashes a password using SHA-256
     * @param password plain text password
     * @return hashed password as hex string
     */
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }

    /**
     * Validates a plain text password against a hashed password
     * @param plainPassword plain text password
     * @param hashedPassword hashed password to compare
     * @return true if passwords match, false otherwise
     */
    public static boolean validatePassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}

