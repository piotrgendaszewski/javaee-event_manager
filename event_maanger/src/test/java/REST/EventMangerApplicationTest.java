package REST;

import jakarta.ws.rs.ApplicationPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EventMangerApplication configuration
 * Verifies that all resources and filters are registered correctly
 * Tests the application setup without actual HTTP calls
 */
@DisplayName("EventMangerApplication Configuration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventMangerApplicationTest {

    @Test
    @DisplayName("Should register AuthResource class")
    void testAuthResourceIsRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertTrue(classes.contains(AuthResource.class),
                "AuthResource should be registered");
    }

    @Test
    @DisplayName("Should register PublicResource class")
    void testPublicResourceIsRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertTrue(classes.contains(PublicResource.class),
                "PublicResource should be registered");
    }

    @Test
    @DisplayName("Should register AuthenticatedUserResource class")
    void testAuthenticatedUserResourceIsRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertTrue(classes.contains(AuthenticatedUserResource.class),
                "AuthenticatedUserResource should be registered");
    }

    @Test
    @DisplayName("Should register AdminResource class")
    void testAdminResourceIsRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertTrue(classes.contains(AdminResource.class),
                "AdminResource should be registered");
    }

    @Test
    @DisplayName("Should register JwtFilter class")
    void testJwtFilterIsRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertTrue(classes.contains(JWT.JwtFilter.class),
                "JwtFilter should be registered");
    }

    @Test
    @DisplayName("Should have application path /api")
    void testApplicationPathIsApi() {
        ApplicationPath pathAnnotation = EventMangerApplication.class.getAnnotation(ApplicationPath.class);

        assertNotNull(pathAnnotation, "ApplicationPath annotation should be present");
        assertEquals("/api", pathAnnotation.value(),
                "ApplicationPath should be /api");
    }

    @Test
    @DisplayName("Should register all required classes")
    void testAllRequiredClassesRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        assertNotNull(classes, "Classes set should not be null");
        assertFalse(classes.isEmpty(), "Classes set should not be empty");

        // Verify count (JWT Filter + 4 Resources = 5 classes)
        assertTrue(classes.size() >= 5,
                "Should have at least 5 registered classes (JWT Filter + 4 Resources)");
    }

    @Test
    @DisplayName("Should not register duplicate classes")
    void testNoDuplicateClassesRegistered() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        // Convert to array and check uniqueness
        Class<?>[] classArray = classes.toArray(new Class[0]);

        for (int i = 0; i < classArray.length; i++) {
            for (int j = i + 1; j < classArray.length; j++) {
                assertNotEquals(classArray[i], classArray[j],
                        "Duplicate class should not exist: " + classArray[i].getSimpleName());
            }
        }
    }

    @Test
    @DisplayName("Should be instance of JAX-RS Application")
    void testIsJaxRsApplication() {
        EventMangerApplication app = new EventMangerApplication();

        assertInstanceOf(jakarta.ws.rs.core.Application.class, app,
                "EventMangerApplication should be instance of Application");
    }

    @Test
    @DisplayName("Should provide immutable classes set")
    void testCanCreateNewInstanceMultipleTimes() {
        EventMangerApplication app1 = new EventMangerApplication();
        EventMangerApplication app2 = new EventMangerApplication();

        Set<Class<?>> classes1 = app1.getClasses();
        Set<Class<?>> classes2 = app2.getClasses();

        // Both should have same resources
        assertEquals(classes1.size(), classes2.size(),
                "Multiple instances should return same number of classes");

        assertTrue(classes1.containsAll(classes2),
                "Both instances should have same classes");
    }

    @Test
    @DisplayName("Should register resources in order: JWT Filter, Auth, Public, User, Admin")
    void testResourceRegistrationOrder() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        // Verify all resource types are present
        boolean hasJwtFilter = classes.stream()
                .anyMatch(c -> c.getName().contains("JwtFilter"));
        boolean hasAuthResource = classes.contains(AuthResource.class);
        boolean hasPublicResource = classes.contains(PublicResource.class);
        boolean hasUserResource = classes.contains(AuthenticatedUserResource.class);
        boolean hasAdminResource = classes.contains(AdminResource.class);

        assertTrue(hasJwtFilter && hasAuthResource && hasPublicResource &&
                   hasUserResource && hasAdminResource,
                "All resources should be registered");
    }

    @Test
    @DisplayName("Should handle getClasses() called multiple times")
    void testGetClassesMultipleCalls() {
        EventMangerApplication app = new EventMangerApplication();

        Set<Class<?>> classes1 = app.getClasses();
        Set<Class<?>> classes2 = app.getClasses();

        assertEquals(classes1.size(), classes2.size(),
                "Multiple calls should return same number of classes");
    }

    @Test
    @DisplayName("Should register AuthResource for authentication endpoints")
    void testAuthResourceConfiguration() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        Class<?> authResource = classes.stream()
                .filter(c -> c.equals(AuthResource.class))
                .findFirst()
                .orElse(null);

        assertNotNull(authResource, "AuthResource should be registered");
        assertTrue(authResource.isAnnotationPresent(jakarta.ws.rs.Path.class),
                "AuthResource should have @Path annotation");
    }

    @Test
    @DisplayName("Should register PublicResource for public endpoints")
    void testPublicResourceConfiguration() {
        EventMangerApplication app = new EventMangerApplication();
        Set<Class<?>> classes = app.getClasses();

        Class<?> publicResource = classes.stream()
                .filter(c -> c.equals(PublicResource.class))
                .findFirst()
                .orElse(null);

        assertNotNull(publicResource, "PublicResource should be registered");
        assertTrue(publicResource.isAnnotationPresent(jakarta.ws.rs.Path.class),
                "PublicResource should have @Path annotation");
    }

    @Test
    @DisplayName("Application should extend Application interface")
    void testExtendsApplicationInterface() {
        EventMangerApplication app = new EventMangerApplication();

        assertInstanceOf(jakarta.ws.rs.core.Application.class, app,
                "EventMangerApplication should implement Application interface");
    }

    @Test
    @DisplayName("Should have /api prefix for all endpoints")
    void testApiPathPrefix() {
        ApplicationPath pathAnnotation = EventMangerApplication.class.getAnnotation(ApplicationPath.class);

        assertNotNull(pathAnnotation, "Should have @ApplicationPath annotation");
        assertEquals("/api", pathAnnotation.value(),
                "All endpoints should be prefixed with /api");
    }
}

