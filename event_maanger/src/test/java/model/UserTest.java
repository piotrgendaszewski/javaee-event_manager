package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertNull(user.getLogin());
        assertNull(user.getEmail());
    }

    @Test
    void testConstructorWithLoginAndEmail() {
        User testUser = new User("john_doe", "john@example.com");
        assertNotNull(testUser);
        assertEquals("john_doe", testUser.getLogin());
        assertEquals("john@example.com", testUser.getEmail());
        assertNull(testUser.getFirstName());
    }

    @Test
    void testConstructorWithAllFields() {
        User testUser = new User("jane_smith", "Jane", "Smith", "jane@example.com", "+48-600-100-100", "ul. Główna 10");
        assertNotNull(testUser);
        assertEquals("jane_smith", testUser.getLogin());
        assertEquals("Jane", testUser.getFirstName());
        assertEquals("Smith", testUser.getLastName());
        assertEquals("jane@example.com", testUser.getEmail());
        assertEquals("+48-600-100-100", testUser.getPhoneNumber());
        assertEquals("ul. Główna 10", testUser.getAddress());
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void setId() {
        user.setId(42);
        assertEquals(42, user.getId());
    }

    @Test
    void setIdMultipleTimes() {
        user.setId(1);
        assertEquals(1, user.getId());
        user.setId(2);
        assertEquals(2, user.getId());
    }

    // ===== LOGIN TESTS =====

    @Test
    void getLogin() {
        user.setLogin("test_user");
        assertEquals("test_user", user.getLogin());
    }

    @Test
    void setLogin() {
        user.setLogin("admin");
        assertEquals("admin", user.getLogin());
    }

    @Test
    void setLoginNull() {
        user.setLogin(null);
        assertNull(user.getLogin());
    }

    @Test
    void setLoginEmpty() {
        user.setLogin("");
        assertEquals("", user.getLogin());
    }

    // ===== EMAIL TESTS =====

    @Test
    void getEmail() {
        user.setEmail("user@example.com");
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void setEmail() {
        user.setEmail("newuser@example.com");
        assertEquals("newuser@example.com", user.getEmail());
    }

    @Test
    void setEmailNull() {
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    // ===== FIRST NAME TESTS =====

    @Test
    void getFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void setFirstName() {
        user.setFirstName("Alice");
        assertEquals("Alice", user.getFirstName());
    }

    @Test
    void setFirstNameNull() {
        user.setFirstName(null);
        assertNull(user.getFirstName());
    }

    // ===== LAST NAME TESTS =====

    @Test
    void getLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void setLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void setLastNameNull() {
        user.setLastName(null);
        assertNull(user.getLastName());
    }

    // ===== PHONE NUMBER TESTS =====

    @Test
    void getPhoneNumber() {
        user.setPhoneNumber("+48-600-100-100");
        assertEquals("+48-600-100-100", user.getPhoneNumber());
    }

    @Test
    void setPhoneNumber() {
        user.setPhoneNumber("+48-700-200-200");
        assertEquals("+48-700-200-200", user.getPhoneNumber());
    }

    @Test
    void setPhoneNumberNull() {
        user.setPhoneNumber(null);
        assertNull(user.getPhoneNumber());
    }

    @Test
    void setPhoneNumberEmpty() {
        user.setPhoneNumber("");
        assertEquals("", user.getPhoneNumber());
    }

    // ===== ADDRESS TESTS =====

    @Test
    void getAddress() {
        user.setAddress("ul. Główna 10, Warszawa");
        assertEquals("ul. Główna 10, Warszawa", user.getAddress());
    }

    @Test
    void setAddress() {
        user.setAddress("ul. Długa 20, Kraków");
        assertEquals("ul. Długa 20, Kraków", user.getAddress());
    }

    @Test
    void setAddressNull() {
        user.setAddress(null);
        assertNull(user.getAddress());
    }

    // ===== PASSWORD HASH TESTS =====

    @Test
    void getPasswordHash() {
        String hash = "$2a$10$testHashValue";
        user.setPasswordHash(hash);
        assertEquals(hash, user.getPasswordHash());
    }

    @Test
    void setPasswordHash() {
        String newHash = "$2a$10$newHashValue";
        user.setPasswordHash(newHash);
        assertEquals(newHash, user.getPasswordHash());
    }

    @Test
    void setPasswordHashNull() {
        user.setPasswordHash(null);
        assertNull(user.getPasswordHash());
    }

    @Test
    void setPasswordHashEmpty() {
        user.setPasswordHash("");
        assertEquals("", user.getPasswordHash());
    }

    // ===== ADMIN TESTS =====

    @Test
    void isAdmin() {
        user.setAdmin(true);
        assertTrue(user.isAdmin());
    }

    @Test
    void setAdmin() {
        user.setAdmin(true);
        assertTrue(user.isAdmin());
        user.setAdmin(false);
        assertFalse(user.isAdmin());
    }

    @Test
    void setAdminDefault() {
        // New user should not be admin by default
        assertFalse(user.isAdmin());
    }

    @Test
    void setAdminMultipleTimes() {
        user.setAdmin(true);
        assertTrue(user.isAdmin());
        user.setAdmin(false);
        assertFalse(user.isAdmin());
        user.setAdmin(true);
        assertTrue(user.isAdmin());
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        user.setId(1);
        user.setLogin("john_doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("+48-600-100-100");
        user.setAddress("ul. Główna 10");
        user.setAdmin(false);

        String result = user.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("login='john_doe'"));
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
        assertTrue(result.contains("email='john@example.com'"));
        assertTrue(result.contains("phoneNumber='+48-600-100-100'"));
        assertTrue(result.contains("address='ul. Główna 10'"));
        assertTrue(result.contains("isAdmin=false"));
    }

    @Test
    void testToStringWithAdmin() {
        user.setId(2);
        user.setLogin("admin");
        user.setEmail("admin@admin.com");
        user.setAdmin(true);

        String result = user.toString();
        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("login='admin'"));
        assertTrue(result.contains("isAdmin=true"));
    }

    @Test
    void testToStringWithNullValues() {
        user.setId(3);
        user.setLogin(null);
        user.setEmail(null);

        String result = user.toString();
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("login='null'"));
        assertTrue(result.contains("email='null'"));
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullUserFlow() {
        // Create user with constructor
        User testUser = new User("alice_brown", "Alice", "Brown", "alice@example.com", "+48-800-300-300", "ul. Spacerowa 15");

        // Set additional fields
        testUser.setId(5);
        testUser.setPasswordHash("$2a$10$aliceHash");
        testUser.setAdmin(false);

        // Verify all fields
        assertEquals(5, testUser.getId());
        assertEquals("alice_brown", testUser.getLogin());
        assertEquals("Alice", testUser.getFirstName());
        assertEquals("Brown", testUser.getLastName());
        assertEquals("alice@example.com", testUser.getEmail());
        assertEquals("+48-800-300-300", testUser.getPhoneNumber());
        assertEquals("ul. Spacerowa 15", testUser.getAddress());
        assertEquals("$2a$10$aliceHash", testUser.getPasswordHash());
        assertFalse(testUser.isAdmin());
    }

    @Test
    void testTwoUsersAreIndependent() {
        User user1 = new User("user1", "user1@example.com");
        User user2 = new User("user2", "user2@example.com");

        user1.setAdmin(true);
        user2.setAdmin(false);

        assertTrue(user1.isAdmin());
        assertFalse(user2.isAdmin());
        assertNotEquals(user1.getLogin(), user2.getLogin());
    }

    @Test
    void testUserEquality() {
        User user1 = new User("test_user", "test@example.com");
        User user2 = new User("test_user", "test@example.com");

        // Different objects but same data
        assertNotSame(user1, user2);
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getEmail(), user2.getEmail());
    }
}