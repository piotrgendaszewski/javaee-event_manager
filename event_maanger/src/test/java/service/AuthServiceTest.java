package service;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthService
 */
class AuthServiceTest {

    private AuthService authService;
    private TestUserDAO testUserDAO;

    @BeforeEach
    void setUp() {
        testUserDAO = new TestUserDAO();
        authService = new AuthService(testUserDAO);
    }

    @Test
    void testRegisterUserSuccess() {
        User result = authService.registerUser("newuser", "new@example.com", "password123", "New", "User", "Address", "123456789");

        assertNotNull(result);
        assertEquals("newuser", result.getLogin());
        assertEquals("new@example.com", result.getEmail());
        assertFalse(result.isAdmin());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        authService.registerUser("existinguser", "existing@example.com", "password123", "Existing", "User", "Address", "123456789");

        assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("existinguser", "other@example.com", "password456", "Other", "User", "Address", "987654321")
        );
    }

    @Test
    void testRegisterUserNullLogin() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser(null, "test@example.com", "password123", "Test", "User", "Address", "123456789")
        );
    }

    @Test
    void testRegisterUserEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("testuser", "test@example.com", "", "Test", "User", "Address", "123456789")
        );
    }

    @Test
    void testLoginSuccess() {
        authService.registerUser("loginuser", "login@example.com", "password123", "Login", "User", "Address", "123456789");

        String token = authService.login("loginuser", "password123");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testLoginInvalidCredentials() {
        authService.registerUser("loginuser2", "login2@example.com", "password123", "Login", "User", "Address", "123456789");

        assertThrows(IllegalArgumentException.class, () ->
                authService.login("loginuser2", "wrongpassword")
        );
    }

    @Test
    void testLoginUserNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("nonexistent", "password123")
        );
    }

    @Test
    void testLoginNullLogin() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login(null, "password123")
        );
    }

    @Test
    void testLoginEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("testuser", "")
        );
    }


    @Test
    void testRegistrationAndLogin() {
        authService.registerUser("fulluser", "full@example.com", "secure123", "Full", "User", "Address", "123456789");

        String token = authService.login("fulluser", "secure123");

        assertNotNull(token);
    }

    private static class TestUserDAO implements UserDAO {
        private List<User> users = new ArrayList<>();

        @Override
        public void rollback() {}
        @Override
        public void commit() {}
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
            return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
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
                        u.setAddress(user.getAddress());
                        u.setPhoneNumber(user.getPhoneNumber());
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

