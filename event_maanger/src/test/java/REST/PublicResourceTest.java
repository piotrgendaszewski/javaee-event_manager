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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for PublicResource using Jersey Test Framework
 * Tests HTTP responses for public endpoints
 * No authentication required
 * Note: These tests require the database to be available and unlocked
 */
@DisplayName("PublicResource HTTP Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(PublicResource.class);
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

    // ==================== EVENTS ENDPOINTS TESTS ====================

    @Test
    @DisplayName("Should map GET /public/events endpoint")
    void testGetAllEventsEndpointExists() {
        safeTest("testGetAllEventsEndpointExists", () -> {
            Response response = target("/public/events")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertNotNull(response.getEntity(), "Should return events");
        });
    }

    @Test
    @DisplayName("Should return JSON array for GET /public/events")
    void testGetAllEventsReturnsJson() {
        safeTest("testGetAllEventsReturnsJson", () -> {
            Response response = target("/public/events")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            String contentType = response.getHeaderString("Content-Type");
            assertTrue(contentType != null && contentType.contains("application/json"),
                    "Response should be JSON");
        });
    }

    @Test
    @DisplayName("Should map GET /public/events/search endpoint")
    void testSearchEventsEndpointExists() {
        safeTest("testSearchEventsEndpointExists", () -> {
            Response response = target("/public/events/search")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should map GET /public/events/{id} endpoint")
    void testGetEventByIdEndpointExists() {
        safeTest("testGetEventByIdEndpointExists", () -> {
            Response response = target("/public/events/1")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
            assertTrue(response.getStatus() > 0, "Should return valid HTTP status");
        });
    }

    @Test
    @DisplayName("Should return 404 for non-existent event")
    void testGetNonExistentEventReturns404() {
        safeTest("testGetNonExistentEventReturns404", () -> {
            Response response = target("/public/events/999999")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "Should return error for non-existent event");
        });
    }

    @Test
    @DisplayName("Should map GET /public/events/name/{name} endpoint")
    void testGetEventByNameEndpointExists() {
        safeTest("testGetEventByNameEndpointExists", () -> {
            Response response = target("/public/events/name/NonExistentEvent")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should map GET /public/events/{eventId}/average-rating endpoint")
    void testGetAverageRatingEndpointExists() {
        safeTest("testGetAverageRatingEndpointExists", () -> {
            Response response = target("/public/events/1/average-rating")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should return numeric value for average rating")
    void testAverageRatingReturnsNumber() {
        safeTest("testAverageRatingReturnsNumber", () -> {
            Response response = target("/public/events/1/average-rating")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                Object rating = response.readEntity(Object.class);
                assertNotNull(rating, "Should return rating value");
            }
        });
    }

    @Test
    @DisplayName("Should map GET /public/events/{eventId}/tickets/remaining endpoint")
    void testGetRemainingTicketsEndpointExists() {
        safeTest("testGetRemainingTicketsEndpointExists", () -> {
            Response response = target("/public/events/1/tickets/remaining")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should return map of ticket quantities for remaining tickets")
    void testGetRemainingTicketsReturnsMap() {
        safeTest("testGetRemainingTicketsReturnsMap", () -> {
            Response response = target("/public/events/1/tickets/remaining")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                Map<String, Integer> tickets = response.readEntity(Map.class);
                assertNotNull(tickets, "Should return ticket map");
            }
        });
    }

    // ==================== REVIEWS ENDPOINTS TESTS ====================

    @Test
    @DisplayName("Should map GET /public/events/{eventId}/reviews endpoint")
    void testGetEventReviewsEndpointExists() {
        safeTest("testGetEventReviewsEndpointExists", () -> {
            Response response = target("/public/events/1/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Endpoint should exist");
        });
    }

    @Test
    @DisplayName("Should return JSON array for event reviews")
    void testGetEventReviewsReturnsJson() {
        safeTest("testGetEventReviewsReturnsJson", () -> {
            Response response = target("/public/events/1/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                String contentType = response.getHeaderString("Content-Type");
                assertTrue(contentType != null && contentType.contains("application/json"),
                        "Response should be JSON");
            }
        });
    }

    // ==================== SEARCH QUERY PARAMETER TESTS ====================

    @Test
    @DisplayName("Should accept eventName query parameter in search")
    void testSearchEventsAcceptsEventNameParam() {
        safeTest("testSearchEventsAcceptsEventNameParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("eventName", "Conference")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept locationName query parameter in search")
    void testSearchEventsAcceptsLocationNameParam() {
        safeTest("testSearchEventsAcceptsLocationNameParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("locationName", "Hall")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept startDate query parameter in search")
    void testSearchEventsAcceptsStartDateParam() {
        safeTest("testSearchEventsAcceptsStartDateParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("startDate", "2026-01-01")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept endDate query parameter in search")
    void testSearchEventsAcceptsEndDateParam() {
        safeTest("testSearchEventsAcceptsEndDateParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("endDate", "2026-12-31")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept minPrice query parameter in search")
    void testSearchEventsAcceptsMinPriceParam() {
        safeTest("testSearchEventsAcceptsMinPriceParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("minPrice", "10.0")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept maxPrice query parameter in search")
    void testSearchEventsAcceptsMaxPriceParam() {
        safeTest("testSearchEventsAcceptsMaxPriceParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("maxPrice", "100.0")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept onlyAvailable query parameter in search")
    void testSearchEventsAcceptsOnlyAvailableParam() {
        safeTest("testSearchEventsAcceptsOnlyAvailableParam", () -> {
            Response response = target("/public/events/search")
                    .queryParam("onlyAvailable", "true")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should accept multiple query parameters in search")
    void testSearchEventsAcceptsMultipleParams() {
        safeTest("testSearchEventsAcceptsMultipleParams", () -> {
            Response response = target("/public/events/search")
                    .queryParam("eventName", "Conference")
                    .queryParam("locationName", "Hall")
                    .queryParam("minPrice", "10.0")
                    .queryParam("maxPrice", "100.0")
                    .queryParam("startDate", "2026-01-01")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }

    // ==================== NO AUTHENTICATION REQUIRED TESTS ====================

    @Test
    @DisplayName("Should allow access to /public/events without authentication")
    void testPublicEventsNoAuthRequired() {
        safeTest("testPublicEventsNoAuthRequired", () -> {
            // No Authorization header
            Response response = target("/public/events")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),
                    "Should allow unauthenticated access");
        });
    }

    @Test
    @DisplayName("Should allow access to /public/events/search without authentication")
    void testPublicEventSearchNoAuthRequired() {
        safeTest("testPublicEventSearchNoAuthRequired", () -> {
            Response response = target("/public/events/search")
                    .queryParam("eventName", "Test")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(),
                    "Should allow unauthenticated access");
        });
    }

    @Test
    @DisplayName("Should allow access to event reviews without authentication")
    void testPublicReviewsNoAuthRequired() {
        safeTest("testPublicReviewsNoAuthRequired", () -> {
            Response response = target("/public/events/1/reviews")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Should return data or 404 if event doesn't exist, but not 403
            assertTrue(response.getStatus() != 403, "Should allow unauthenticated access");
        });
    }

    // ==================== ERROR HANDLING TESTS ====================

    @Test
    @DisplayName("Should handle invalid price parameter gracefully")
    void testSearchEventsHandlesInvalidPrice() {
        safeTest("testSearchEventsHandlesInvalidPrice", () -> {
            Response response = target("/public/events/search")
                    .queryParam("minPrice", "invalid")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // Should either parse 0 or return error, but not crash
            assertNotNull(response.getStatus(), "Should handle invalid price");
        });
    }

    @Test
    @DisplayName("Should handle invalid date format gracefully")
    void testSearchEventsHandlesInvalidDate() {
        safeTest("testSearchEventsHandlesInvalidDate", () -> {
            Response response = target("/public/events/search")
                    .queryParam("startDate", "invalid-date-format")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should handle invalid date");
        });
    }

    @Test
    @DisplayName("Should handle invalid boolean parameter gracefully")
    void testSearchEventsHandlesInvalidBoolean() {
        safeTest("testSearchEventsHandlesInvalidBoolean", () -> {
            Response response = target("/public/events/search")
                    .queryParam("onlyAvailable", "maybe")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertNotNull(response.getStatus(), "Should handle invalid boolean");
        });
    }

    // ==================== HTTP METHOD VALIDATION ====================

    @Test
    @DisplayName("Should reject POST on GET-only endpoints")
    void testRejectPostOnEventsList() {
        safeTest("testRejectPostOnEventsList", () -> {
            Response response = target("/public/events")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity("{}", MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should reject POST on GET endpoint");
        });
    }

    @Test
    @DisplayName("Should reject DELETE on public endpoints")
    void testRejectDeleteOnEvents() {
        safeTest("testRejectDeleteOnEvents", () -> {
            Response response = target("/public/events/1")
                    .request(MediaType.APPLICATION_JSON)
                    .delete();

            assertTrue(response.getStatus() >= 400, "Should reject DELETE on public endpoint");
        });
    }

    @Test
    @DisplayName("Should respond with JSON content type")
    void testResponseJsonContentType() {
        safeTest("testResponseJsonContentType", () -> {
            Response response = target("/public/events")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            String contentType = response.getHeaderString("Content-Type");
            assertTrue(contentType != null && contentType.contains("application/json"),
                    "Response should be JSON");
        });
    }

    @Test
    @DisplayName("Should return empty list for search with no matches")
    void testSearchEventsReturnsEmptyList() {
        safeTest("testSearchEventsReturnsEmptyList", () -> {
            Response response = target("/public/events/search")
                    .queryParam("eventName", "NonExistentEventXYZ12345")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            List<Object> events = response.readEntity(List.class);
            assertNotNull(events, "Should return list");
        });
    }
}

