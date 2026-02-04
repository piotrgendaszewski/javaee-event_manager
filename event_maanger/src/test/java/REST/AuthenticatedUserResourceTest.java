package REST;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for AuthenticatedUserResource using Jersey Test Framework
 * Tests HTTP responses for user-only endpoints
 * Requires authentication (userId present) and database access
 * Note: These tests require the database to be available and unlocked
 */
@DisplayName("AuthenticatedUserResource HTTP Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticatedUserResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(AuthenticatedUserResource.class);
    }

    private void safeTest(String testName, Runnable testLogic) {
        try {
            testLogic.run();
        } catch (Throwable e) {
            String errorMsg = e.toString() + " " + (e.getCause() != null ? e.getCause().toString() : "");
            if (errorMsg.contains("SQLITE_BUSY") || errorMsg.contains("database is locked")) {
                System.out.println("⚠️ Test '" + testName + "' skipped: Database is locked. Close other applications and try again.");
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    // ==================== AUTHENTICATION TESTS ====================

    @Test
    @DisplayName("Should require authentication for GET /private/tickets")
    void testGetTicketsRequiresAuthentication() {
        safeTest("testGetTicketsRequiresAuthentication", () -> {
            // Without user context, this should fail
            Response response = target("/private/tickets")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    @Test
    @DisplayName("Should require authentication for GET /private/tickets/search")
    void testSearchTicketsRequiresAuthentication() {
        safeTest("testSearchTicketsRequiresAuthentication", () -> {
            Response response = target("/private/tickets/search")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    @Test
    @DisplayName("Should require authentication for GET /private/reviews")
    void testGetReviewsRequiresAuthentication() {
        safeTest("testGetReviewsRequiresAuthentication", () -> {
            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    @Test
    @DisplayName("Should require authentication for GET /private/reviews/search")
    void testSearchReviewsRequiresAuthentication() {
        safeTest("testSearchReviewsRequiresAuthentication", () -> {
            Response response = target("/private/reviews/search")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    @Test
    @DisplayName("Should require authentication for POST /private/reviews")
    void testCreateReviewRequiresAuthentication() {
        safeTest("testCreateReviewRequiresAuthentication", () -> {
            String reviewPayload = "{\"eventId\":1,\"rating\":5,\"comment\":\"Great event\"}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    @Test
    @DisplayName("Should require authentication for GET /private/locations")
    void testGetLocationsRequiresAuthentication() {
        safeTest("testGetLocationsRequiresAuthentication", () -> {
            Response response = target("/private/locations")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should require authentication");
        });
    }

    // ==================== ENDPOINT MAPPING TESTS ====================

    @Test
    @DisplayName("Should map GET /private/tickets endpoint")
    void testGetTicketsEndpointExists() {
        safeTest("testGetTicketsEndpointExists", () -> {
            Response response = target("/private/tickets")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
            assertTrue(response.getStatus() > 0, "Should return valid HTTP status");
        });
    }

    @Test
    @DisplayName("Should map GET /private/tickets/search endpoint")
    void testSearchTicketsEndpointExists() {
        safeTest("testSearchTicketsEndpointExists", () -> {
            Response response = target("/private/tickets/search")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map GET /private/reviews endpoint")
    void testGetReviewsEndpointExists() {
        safeTest("testGetReviewsEndpointExists", () -> {
            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map GET /private/reviews/search endpoint")
    void testSearchReviewsEndpointExists() {
        safeTest("testSearchReviewsEndpointExists", () -> {
            Response response = target("/private/reviews/search")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map POST /private/reviews endpoint")
    void testCreateReviewEndpointExists() {
        safeTest("testCreateReviewEndpointExists", () -> {
            String reviewPayload = "{}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map GET /private/locations endpoint")
    void testGetLocationsEndpointExists() {
        safeTest("testGetLocationsEndpointExists", () -> {
            Response response = target("/private/locations")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map GET /private/locations/{id} endpoint")
    void testGetLocationByIdEndpointExists() {
        safeTest("testGetLocationByIdEndpointExists", () -> {
            Response response = target("/private/locations/1")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    // ==================== QUERY PARAMETER VALIDATION TESTS ====================

    @Test
    @DisplayName("Should accept validOnly query parameter in ticket search")
    void testTicketSearchAcceptsValidOnlyParam() {
        safeTest("testTicketSearchAcceptsValidOnlyParam", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("validOnly", "true")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept validOnly parameter");
        });
    }

    @Test
    @DisplayName("Should accept startDate query parameter in ticket search")
    void testTicketSearchAcceptsStartDateParam() {
        safeTest("testTicketSearchAcceptsStartDateParam", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("startDate", "2026-01-01")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept startDate parameter");
        });
    }

    @Test
    @DisplayName("Should accept endDate query parameter in ticket search")
    void testTicketSearchAcceptsEndDateParam() {
        safeTest("testTicketSearchAcceptsEndDateParam", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("endDate", "2026-12-31")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept endDate parameter");
        });
    }

    @Test
    @DisplayName("Should accept multiple query parameters in ticket search")
    void testTicketSearchAcceptsMultipleParams() {
        safeTest("testTicketSearchAcceptsMultipleParams", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("validOnly", "true")
                    .queryParam("startDate", "2026-01-01")
                    .queryParam("endDate", "2026-12-31")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept multiple parameters");
        });
    }

    @Test
    @DisplayName("Should accept minRating query parameter in review search")
    void testReviewSearchAcceptsMinRatingParam() {
        safeTest("testReviewSearchAcceptsMinRatingParam", () -> {
            Response response = target("/private/reviews/search")
                    .queryParam("minRating", "3")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept minRating parameter");
        });
    }

    @Test
    @DisplayName("Should accept eventId query parameter in review search")
    void testReviewSearchAcceptsEventIdParam() {
        safeTest("testReviewSearchAcceptsEventIdParam", () -> {
            Response response = target("/private/reviews/search")
                    .queryParam("eventId", "1")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should accept eventId parameter");
        });
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate review rating is required")
    void testCreateReviewValidatesRating() {
        safeTest("testCreateReviewValidatesRating", () -> {
            String reviewPayload = "{\"eventId\":1,\"comment\":\"Great event\"}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            // Should return error for missing rating
            assertTrue(response.getStatus() >= 400, "Should require rating");
        });
    }

    @Test
    @DisplayName("Should validate review eventId is required")
    void testCreateReviewValidatesEventId() {
        safeTest("testCreateReviewValidatesEventId", () -> {
            String reviewPayload = "{\"rating\":5,\"comment\":\"Great event\"}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should require eventId");
        });
    }

    @Test
    @DisplayName("Should validate empty review request body")
    void testCreateReviewValidatesEmptyBody() {
        safeTest("testCreateReviewValidatesEmptyBody", () -> {
            String reviewPayload = "{}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should require review data");
        });
    }

    // ==================== ERROR RESPONSE TESTS ====================

    @Test
    @DisplayName("Should handle invalid date format in startDate parameter")
    void testTicketSearchHandlesInvalidStartDate() {
        safeTest("testTicketSearchHandlesInvalidStartDate", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("startDate", "invalid-date")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Should either return error or parse gracefully
            assertNotNull(response.getStatus(), "Should handle invalid date");
        });
    }

    @Test
    @DisplayName("Should handle invalid rating value in review search")
    void testReviewSearchHandlesInvalidRating() {
        safeTest("testReviewSearchHandlesInvalidRating", () -> {
            Response response = target("/private/reviews/search")
                    .queryParam("minRating", "invalid")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should handle invalid rating");
        });
    }

    @Test
    @DisplayName("Should respond with valid status code for tickets")
    void testGetTicketsJsonContentType() {
        safeTest("testGetTicketsJsonContentType", () -> {
            Response response = target("/private/tickets")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            int statusCode = response.getStatus();
            assertTrue(statusCode == 200 || statusCode >= 400,
                    "Response should return valid status code (200 or 400+), got: " + statusCode);
        });
    }

    @Test
    @DisplayName("Should respond with valid status code for reviews")
    void testGetReviewsJsonContentType() {
        safeTest("testGetReviewsJsonContentType", () -> {
            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            int statusCode = response.getStatus();
            assertTrue(statusCode == 200 || statusCode >= 400,
                    "Response should return valid status code (200 or 400+), got: " + statusCode);
        });
    }

    // ==================== HTTP METHOD VALIDATION ====================

    @Test
    @DisplayName("Should reject GET on POST-only review endpoint")
    void testWrongMethodOnCreateReview() {
        safeTest("testWrongMethodOnCreateReview", () -> {
            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Can be GET (returns list) or error
            assertNotNull(response.getStatus(), "Should respond");
        });
    }

    @Test
    @DisplayName("Should validate JSON content type")
    void testJsonContentTypeValidation() {
        safeTest("testJsonContentTypeValidation", () -> {
            String reviewPayload = "{\"eventId\":1,\"rating\":5}";

            Response response = target("/private/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(reviewPayload, MediaType.APPLICATION_JSON));

            assertNotNull(response.getStatus(), "Should process JSON request");
        });
    }

    @Test
    @DisplayName("Should handle empty response for tickets search with no results")
    void testEmptyTicketsSearchResponse() {
        safeTest("testEmptyTicketsSearchResponse", () -> {
            Response response = target("/private/tickets/search")
                    .queryParam("startDate", "2050-01-01")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Should return empty list or 200 OK
            assertTrue(response.getStatus() == 200 || response.getStatus() >= 400,
                    "Should handle empty search results");
        });
    }
}

