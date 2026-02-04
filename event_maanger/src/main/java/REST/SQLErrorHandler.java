package REST;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to handle SQL errors and convert them to appropriate HTTP responses
 * Handles:
 * - Duplicate key violations (UNIQUE constraints)
 * - Foreign key violations
 * - Check constraint violations
 * - General SQL exceptions
 */
public class SQLErrorHandler {

    /**
     * Handle SQL exception and throw appropriate WebApplicationException
     * Detects duplicate keys, foreign key violations, check constraint violations, etc.
     */
    public static void handleSQLException(Exception e, String operation) {
        String message = e.getMessage() != null ? e.getMessage().toUpperCase() : "";

        // Duplicate key / Unique constraint violation
        if (isDuplicateKeyError(message, e)) {
            String detail = extractDuplicateFieldName(message);
            throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity(buildErrorResponse("Duplicate " + detail + " already exists. Please use a unique value."))
                            .build()
            );
        }

        // Foreign key violation
        if (isForeignKeyError(message, e)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity(buildErrorResponse("Cannot " + operation + ". This item is referenced by other records."))
                            .build()
            );
        }

        // Check constraint violation
        if (isCheckConstraintError(message)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(buildErrorResponse("Data validation failed. Please check the values you're providing."))
                            .build()
            );
        }

        // Not null violation
        if (isNotNullError(message)) {
            String field = extractNotNullFieldName(message);
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(buildErrorResponse("Field '" + field + "' is required and cannot be empty."))
                            .build()
            );
        }

        // Generic SQL error
        if (e instanceof SQLException || message.contains("SQL")) {
            throw new WebApplicationException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(buildErrorResponse("Database error during " + operation + ". Please try again."))
                            .build()
            );
        }

        // Unknown error
        if (e instanceof IllegalArgumentException) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(buildErrorResponse(e.getMessage()))
                            .build()
            );
        }

        throw new WebApplicationException(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(buildErrorResponse("An unexpected error occurred during " + operation + "."))
                        .build()
        );
    }

    /**
     * Check if error is a duplicate key/unique constraint violation
     */
    private static boolean isDuplicateKeyError(String message, Exception e) {
        return message.contains("UNIQUE") ||
               message.contains("DUPLICATE") ||
               message.contains("CONSTRAINT") ||
               message.contains("KEY") ||
               (e.getCause() != null && e.getCause().getMessage() != null &&
                e.getCause().getMessage().toUpperCase().contains("UNIQUE"));
    }

    /**
     * Check if error is a foreign key violation
     */
    private static boolean isForeignKeyError(String message, Exception e) {
        return message.contains("FOREIGN") ||
               message.contains("REFERENCE") ||
               message.contains("PARENT") ||
               message.contains("CHILD") ||
               (e.getCause() != null && e.getCause().getMessage() != null &&
                e.getCause().getMessage().toUpperCase().contains("FOREIGN"));
    }

    /**
     * Check if error is a check constraint violation
     */
    private static boolean isCheckConstraintError(String message) {
        return message.contains("CHECK") ||
               message.contains("CONSTRAINT");
    }

    /**
     * Check if error is a not null violation
     */
    private static boolean isNotNullError(String message) {
        return message.contains("NOT NULL") ||
               message.contains("CANNOT BE NULL");
    }

    /**
     * Extract field name from duplicate key error message
     */
    private static String extractDuplicateFieldName(String message) {
        // Try to extract from common patterns
        if (message.contains("LOGIN")) return "login";
        if (message.contains("EMAIL")) return "email";
        if (message.contains("NAME")) return "name";
        if (message.contains("PHONE")) return "phone number";
        return "value";
    }

    /**
     * Extract field name from not null error message
     */
    private static String extractNotNullFieldName(String message) {
        if (message.contains("LOGIN")) return "login";
        if (message.contains("EMAIL")) return "email";
        if (message.contains("NAME")) return "name";
        if (message.contains("PASSWORD")) return "password";
        return "required field";
    }

    /**
     * Build error response map for JAX-RS
     */
    private static Object buildErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
}

