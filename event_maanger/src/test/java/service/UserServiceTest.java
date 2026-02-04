package service;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserService
 * Tests business logic without mocking DAO (specification-based)
 */
class UserServiceTest {

    private UserService userService;
    private TestUserDAO testUserDAO;

    @BeforeEach
    void setUp() {
        testUserDAO = new TestUserDAO();
        userService = new UserService(testUserDAO);
    }

    // ===== GET USER TESTS =====

    @Test
    void testGetUserByLogin() {
        User user = new User("john_doe", "john@example.com");
        user.setId(1);
        testUserDAO.addTestUser(user);

        User result = userService.getUser("john_doe");

        assertNotNull(result);
        assertEquals("john_doe", result.getLogin());
    }

    @Test
    void testGetUserByLoginNotFound() {
        User result = userService.getUser("nonexistent");

        assertNull(result);
    }

    // ===== ADD USER TESTS =====

    @Test
    void testAddUser() {
        User result = userService.addUser("alice_smith", "alice@example.com");

        assertNotNull(result);
        assertEquals("alice_smith", result.getLogin());
        assertEquals("alice@example.com", result.getEmail());
    }

    @Test
    void testAddUserWithNullLogin() {
        User result = userService.addUser(null, "test@example.com");

        // UserService doesn't validate input - it delegates to DAO
        // DAO creates User with null login
        assertNotNull(result);
        assertNull(result.getLogin());
        assertEquals("test@example.com", result.getEmail());
    }

    // ===== UPDATE USER TESTS =====

    @Test
    void testUpdateUser() {
        User user = new User("bob_jones", "bob@example.com");
        user.setId(2);
        user.setFirstName("Bob");

        assertDoesNotThrow(() -> userService.updateUser(user));
        assertEquals("Bob", user.getFirstName());
    }

    // ===== DELETE USER TESTS =====

    @Test
    void testDeleteUser() {
        User user = new User("charlie_brown", "charlie@example.com");
        user.setId(3);

        assertDoesNotThrow(() -> userService.deleteUser(user));
    }

    // ===== GET ALL USERS TESTS =====

    @Test
    void testGetAllUsers() {
        testUserDAO.addTestUser(new User("user1", "user1@example.com"));
        testUserDAO.addTestUser(new User("user2", "user2@example.com"));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllUsersEmpty() {
        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET USER BY ID TESTS =====

    @Test
    void testGetUserById() {
        User user = new User("diana_prince", "diana@example.com");
        user.setId(4);
        testUserDAO.addTestUser(user);

        User result = userService.getUserById(4);

        assertNotNull(result);
        assertEquals(4, result.getId());
    }

    // ===== CREATE ADMIN USER TESTS =====

    @Test
    void testCreateAdminUser() {
        User result = userService.createAdminUser("admin_user", "admin@example.com", "password123", "Admin", "User");

        assertNotNull(result);
        assertEquals("admin_user", result.getLogin());
        assertEquals("Admin", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertTrue(result.isAdmin());
        assertNotNull(result.getPasswordHash());
    }

    @Test
    void testCreateAdminUserWithNullPassword() {
        User result = userService.createAdminUser("admin2", "admin2@example.com", null, "Admin", "Two");

        assertNotNull(result);
        assertTrue(result.isAdmin());
    }

    // ===== TRANSACTION TESTS =====

    @Test
    void testCommit() {
        assertDoesNotThrow(() -> userService.commit());
    }

    @Test
    void testRollback() {
        assertDoesNotThrow(() -> userService.rollback());
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testUserLifecycle() {
        User newUser = userService.addUser("eve_taylor", "eve@example.com");
        assertNotNull(newUser);

        newUser.setFirstName("Eve");
        userService.updateUser(newUser);
        assertEquals("Eve", newUser.getFirstName());

        User retrieved = userService.getUser("eve_taylor");
        assertNotNull(retrieved);
        assertEquals("Eve", retrieved.getFirstName());

        userService.deleteUser(newUser);
        userService.commit();
    }

    @Test
    void testMultipleUsers() {
        userService.addUser("user1", "user1@example.com");
        userService.addUser("user2", "user2@example.com");
        userService.addUser("user3", "user3@example.com");

        List<User> allUsers = userService.getAllUsers();
        assertEquals(3, allUsers.size());
    }

    // ===== TEST DAO IMPLEMENTATION =====

    /**
     * Test DAO implementation for in-memory testing
     */
    private static class TestUserDAO implements UserDAO {
        private List<User> users = new ArrayList<>();

        void addTestUser(User user) {
            users.add(user);
        }

        @Override
        public void rollback() {
        }

        @Override
        public void commit() {
        }

        @Override
        public User addUser(String username, String email) {
            User user = new User(username, email);
            user.setId(users.size() + 1);
            users.add(user);
            return user;
        }

        @Override
        public User getUserByLogin(String login) {
            return users.stream()
                    .filter(u -> u.getLogin() != null && u.getLogin().equals(login))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public User getUserById(int id) {
            return users.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void updateUser(User user) {
            users.stream()
                    .filter(u -> u.getId() == user.getId())
                    .findFirst()
                    .ifPresent(u -> {
                        u.setLogin(user.getLogin());
                        u.setEmail(user.getEmail());
                        u.setFirstName(user.getFirstName());
                        u.setLastName(user.getLastName());
                        u.setPasswordHash(user.getPasswordHash());
                        u.setAdmin(user.isAdmin());
                    });
        }

        @Override
        public void deleteUser(User user) {
            users.removeIf(u -> u.getId() == user.getId());
        }

        @Override
        public List<User> getAllUsers() {
            return new ArrayList<>(users);
        }
    }
}

