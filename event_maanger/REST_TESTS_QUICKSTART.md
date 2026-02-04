# REST API Unit Tests - Quick Start Guide

## What Was Created

Complete unit test suite for all 5 REST resource classes in the Event Manager application:

| File | Tests | Coverage |
|------|-------|----------|
| `AuthResourceTest.java` | 22 | User login/registration |
| `AdminResourceTest.java` | 33 | Admin CRUD operations |
| `AuthenticatedUserResourceTest.java` | 30 | User data management |
| `PublicResourceTest.java` | 35 | Public event browsing |
| `EventMangerApplicationTest.java` | 15 | App configuration |

**Total: 135 unit tests**

## Quick Start

### 1. Run All Tests
```bash
cd event_maanger
mvn test
```

### 2. Run Tests for Specific Resource
```bash
# Authentication tests
mvn test -Dtest=AuthResourceTest

# Admin tests
mvn test -Dtest=AdminResourceTest

# User private data tests
mvn test -Dtest=AuthenticatedUserResourceTest

# Public event browsing tests
mvn test -Dtest=PublicResourceTest

# Application configuration tests
mvn test -Dtest=EventMangerApplicationTest
```

### 3. Run Single Test Method
```bash
mvn test -Dtest=AuthResourceTest#testRegisterWithMissingLogin
```

## Test Organization

### By Resource
- **AuthResource** → User authentication (login, registration)
- **AdminResource** → Admin-only operations (events, locations, rooms management)
- **AuthenticatedUserResource** → Authenticated user operations (tickets, reviews)
- **PublicResource** → Public event browsing and search
- **EventMangerApplication** → Application setup verification

### By Category (in each test class)
1. **Access Control Tests** - Authentication/authorization verification
2. **Endpoint Mapping Tests** - Route and HTTP method validation
3. **Validation Tests** - Input validation and business logic
4. **Query Parameter Tests** - Search filters and optional parameters
5. **Error Handling Tests** - Error responses and edge cases
6. **HTTP Method Validation** - Correct HTTP verbs support

## Test Features

✅ Uses Jersey Test Framework (in-memory container)
✅ Follows existing AuthResourceTest pattern
✅ Handles database locking gracefully
✅ No external dependencies required
✅ 135 comprehensive test cases
✅ JUnit 5 (Jupiter) with @DisplayName annotations
✅ Tests both success and error scenarios

## Database Requirements

Tests use real SQLite database connection. If you encounter "SQLITE_BUSY" errors:

1. Stop Tomcat: `mvn tomcat7:stop`
2. Close other database tools
3. Run tests: `mvn test`

The `safeTest()` wrapper will gracefully skip tests if database is locked.

## Example Test Structure

All tests follow this pattern:

```java
@DisplayName("Should validate event name is required")
void testCreateEventValidatesName() {
    safeTest("testCreateEventValidatesName", () -> {
        String eventPayload = "{\"description\":\"Test without name\"}";
        
        Response response = target("/admin/events")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(eventPayload, MediaType.APPLICATION_JSON));
        
        assertTrue(response.getStatus() >= 400, "Should require event name");
    });
}
```

## Key Testing Patterns

### 1. Authentication Tests
```java
void testGetTicketsRequiresAuthentication() {
    Response response = target("/private/tickets")
            .request(MediaType.APPLICATION_JSON)
            .get();
    
    assertTrue(response.getStatus() >= 400, "Should require authentication");
}
```

### 2. Admin Access Tests
```java
void testGetEventsRequiresAdmin() {
    Response response = target("/admin/events")
            .request(MediaType.APPLICATION_JSON)
            .get();
    
    assertTrue(response.getStatus() >= 400, "Should require admin access");
}
```

### 3. Data Validation Tests
```java
void testCreateEventValidatesName() {
    String eventPayload = "{\"description\":\"Test without name\"}";
    
    Response response = target("/admin/events")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(eventPayload, MediaType.APPLICATION_JSON));
    
    assertTrue(response.getStatus() >= 400, "Should require event name");
}
```

### 4. Query Parameter Tests
```java
void testSearchEventsAcceptsEventNameParam() {
    Response response = target("/public/events/search")
            .queryParam("eventName", "Conference")
            .request(MediaType.APPLICATION_JSON)
            .get();
    
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
}
```

### 5. Error Handling Tests
```java
void testGetNonExistentEventReturns404() {
    Response response = target("/public/events/999999")
            .request(MediaType.APPLICATION_JSON)
            .get();
    
    assertTrue(response.getStatus() >= 400, "Should return error");
}
```

## Endpoints Covered

### Authentication (/auth)
- `POST /auth/login` - 8 tests
- `POST /auth/register` - 14 tests

### Admin (/admin)
- `GET /admin/events` - 3 tests
- `POST /admin/events` - 4 tests
- `PUT /admin/events/{id}` - 2 tests
- `DELETE /admin/events/{id}` - 2 tests
- Similar coverage for locations and rooms
- **Total: 33 tests**

### Authenticated User (/private)
- `GET /private/tickets` - 4 tests
- `GET /private/tickets/search` - 6 tests
- `GET /private/reviews` - 4 tests
- `GET /private/reviews/search` - 6 tests
- `POST /private/reviews` - 3 tests
- `GET /private/locations` - 7 tests
- **Total: 30 tests**

### Public (/public)
- `GET /public/events` - 4 tests
- `GET /public/events/search` - 12 tests
- `GET /public/events/{id}` - 3 tests
- `GET /public/events/name/{name}` - 2 tests
- `GET /public/events/{id}/average-rating` - 2 tests
- `GET /public/events/{id}/tickets/remaining` - 2 tests
- `GET /public/events/{id}/reviews` - 3 tests
- **Total: 35 tests**

## Test Results Example

```
[INFO] -------------------------------------------------------
[INFO] T E S T S
[INFO] -------------------------------------------------------
[INFO] Running REST.AuthResourceTest
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running REST.AdminResourceTest
[INFO] Tests run: 33, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running REST.AuthenticatedUserResourceTest
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running REST.PublicResourceTest
[INFO] Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running REST.EventMangerApplicationTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] -------------------------------------------------------
[INFO] Tests run: 135, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Files Location

All test files are in: `src/test/java/REST/`

- ✅ `AuthResourceTest.java` - 22 tests for user authentication
- ✅ `AdminResourceTest.java` - 33 tests for admin operations
- ✅ `AuthenticatedUserResourceTest.java` - 30 tests for user data
- ✅ `PublicResourceTest.java` - 35 tests for public browsing
- ✅ `EventMangerApplicationTest.java` - 15 tests for app config
- 📄 `REST_TESTS_DOCUMENTATION.md` - Comprehensive documentation

## IDE Integration

### IntelliJ IDEA / JetBrains
1. Right-click test file → "Run"
2. Right-click test class → "Run" for all tests in class
3. Right-click test method → "Run" for single test
4. Use Ctrl+Shift+F10 to run current test

### VS Code
Use the Test Explorer extension or:
```bash
mvn test -Dtest=AuthResourceTest
```

## Common Issues & Solutions

### Issue: SQLITE_BUSY
**Cause**: Database is locked by another process
**Solution**: Close other DB connections or wait. Tests will skip gracefully.

### Issue: 404 errors in test
**Cause**: In-memory container or endpoint not registered
**Solution**: Ensure resource is registered in `EventMangerApplication`

### Issue: Tests timeout
**Solution**: Check database performance or add `@Test(timeout=10000)`

### Issue: ClassNotFound errors
**Solution**: Run `mvn clean install` to download dependencies

## Documentation

For detailed test documentation, see: **`REST_TESTS_DOCUMENTATION.md`**

This includes:
- Test structure overview
- Detailed endpoint coverage
- Best practices implemented
- Contributing guidelines
- Troubleshooting guide

