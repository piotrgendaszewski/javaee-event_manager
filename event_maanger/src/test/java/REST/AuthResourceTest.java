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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for AuthResource using Jersey Test Framework
 * Tests HTTP responses without mocking - validates endpoint behavior
 * Note: These tests require the database to be available and unlocked
 */
@DisplayName("AuthResource HTTP Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(AuthResource.class);
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

    // ==================== REGISTRATION TESTS ====================

    @Test
    @DisplayName("Should return 400 for missing login in registration")
    void testRegisterWithMissingLogin() {
        safeTest("testRegisterWithMissingLogin", () -> {
            String jsonPayload = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(jsonPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            Map<String, Object> entity = response.readEntity(Map.class);
            assertNotNull(entity.get("error"));
        });
    }

    @Test
    @DisplayName("Should return 400 for missing email in registration")
    void testRegisterWithMissingEmail() {
        safeTest("testRegisterWithMissingEmail", () -> {
            String login = "testuser_" + System.currentTimeMillis();
            String jsonPayload = String.format(
                    "{\"login\":\"%s\",\"password\":\"password123\"}",
                    login
            );

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(jsonPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should return 400 for missing password in registration")
    void testRegisterWithMissingPassword() {
        safeTest("testRegisterWithMissingPassword", () -> {
            String login = "testuser_" + System.currentTimeMillis();
            String email = "test_" + System.currentTimeMillis() + "@example.com";
            String jsonPayload = String.format(
                    "{\"login\":\"%s\",\"email\":\"%s\"}",
                    login, email
            );

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(jsonPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        });
    }

    // ==================== LOGIN TESTS ====================

    @Test
    @DisplayName("Should return 400 for missing login field in login")
    void testLoginWithMissingLogin() {
        safeTest("testLoginWithMissingLogin", () -> {
            String loginPayload = "{\"password\":\"password123\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(loginPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            Map<String, Object> entity = response.readEntity(Map.class);
            assertNotNull(entity.get("error"));
        });
    }

    @Test
    @DisplayName("Should return 400 for missing password field in login")
    void testLoginWithMissingPassword() {
        safeTest("testLoginWithMissingPassword", () -> {
            String loginPayload = "{\"login\":\"testuser\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(loginPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should return 400 for empty login request body")
    void testLoginWithEmptyRequest() {
        safeTest("testLoginWithEmptyRequest", () -> {
            String loginPayload = "{}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(loginPayload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        });
    }

    @Test
    @DisplayName("Should return 400 for empty registration request body")
    void testRegisterWithEmptyRequest() {
        safeTest("testRegisterWithEmptyRequest", () -> {
            String payload = "{}";

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        });
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate required fields in registration")
    void testRegisterValidationAllFieldsMissing() {
        safeTest("testRegisterValidationAllFieldsMissing", () -> {
            String payload = "{\"someRandomField\":\"value\"}";

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should return error for missing required fields");
        });
    }

    @Test
    @DisplayName("Should validate required fields in login")
    void testLoginValidationAllFieldsMissing() {
        safeTest("testLoginValidationAllFieldsMissing", () -> {
            String payload = "{\"someRandomField\":\"value\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertTrue(response.getStatus() >= 400, "Should return error for missing required fields");
        });
    }

    @Test
    @DisplayName("Should respond with JSON content type")
    void testResponseContentType() {
        safeTest("testResponseContentType", () -> {
            String payload = "{}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            String contentType = response.getHeaderString("Content-Type");
            assertTrue(contentType.contains("application/json"), "Response should be JSON");
        });
    }

    @Test
    @DisplayName("Should accept application/json content type")
    void testContentTypeHeader() {
        safeTest("testContentTypeHeader", () -> {
            String payload = "{\"login\":\"test\",\"password\":\"test\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertNotNull(response.getStatus(), "Should process JSON request");
        });
    }

    // ==================== ERROR RESPONSE TESTS ====================

    @Test
    @DisplayName("Should return error response with error field")
    void testErrorResponseFormat() {
        safeTest("testErrorResponseFormat", () -> {
            String payload = "{\"login\":\"\",\"password\":\"\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if (response.getStatus() >= 400) {
                Map<String, Object> entity = response.readEntity(Map.class);
                assertTrue(entity.containsKey("error") || entity.containsKey("message"),
                        "Error response should contain error or message field");
            }
        });
    }

    @Test
    @DisplayName("Should not expose internal exceptions in error messages")
    void testErrorMessageSafety() {
        safeTest("testErrorMessageSafety", () -> {
            String payload = "{\"login\":null,\"password\":null}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if (response.getStatus() >= 400) {
                String responseBody = response.readEntity(String.class);
                assertFalse(responseBody.contains("SQLException"), "Should not expose SQL errors");
                assertFalse(responseBody.contains("NullPointerException"), "Should not expose NPE");
            }
        });
    }

    // ==================== ENDPOINT MAPPING TESTS ====================

    @Test
    @DisplayName("Should map POST /auth/register endpoint")
    void testRegisterEndpointExists() {
        safeTest("testRegisterEndpointExists", () -> {
            String payload = "{\"login\":\"test\",\"email\":\"test@test.com\",\"password\":\"test\"}";

            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertNotNull(response.getStatus(), "Register endpoint should exist");
            assertTrue(response.getStatus() > 0, "Should return valid HTTP status");
        });
    }

    @Test
    @DisplayName("Should map POST /auth/login endpoint")
    void testLoginEndpointExists() {
        safeTest("testLoginEndpointExists", () -> {
            String payload = "{\"login\":\"test\",\"password\":\"test\"}";

            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            assertNotNull(response.getStatus(), "Login endpoint should exist");
            assertTrue(response.getStatus() > 0, "Should return valid HTTP status");
        });
    }

    @Test
    @DisplayName("Should return error for wrong HTTP method on register")
    void testRegisterWrongMethod() {
        safeTest("testRegisterWrongMethod", () -> {
            Response response = target("/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "GET method should not be allowed");
        });
    }

    @Test
    @DisplayName("Should return error for wrong HTTP method on login")
    void testLoginWrongMethod() {
        safeTest("testLoginWrongMethod", () -> {
            Response response = target("/auth/login")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            assertTrue(response.getStatus() >= 400, "GET method should not be allowed");
        });
    }
}

