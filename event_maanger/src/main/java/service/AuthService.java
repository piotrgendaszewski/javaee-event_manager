package service;

import dao.UserDAO;
import model.User;
import JWT.JwtUtil;
import JWT.PasswordUtil;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user
     * @param login username/login
     * @param email user email
     * @param password plain text password
     * @param firstName first name
     * @param lastName last name
     * @param address user address
     * @param phoneNumber phone number
     * @return User object if successful
     * @throws IllegalArgumentException if user already exists or invalid input
     */
    public User registerUser(String login, String email, String password, String firstName,
                            String lastName, String address, String phoneNumber) throws IllegalArgumentException {
        if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Login and password cannot be empty");
        }

        // Check if user already exists
        User existingUser = userDAO.getUserByLogin(login);
        if (existingUser != null) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists");
        }

        // Hash password
        String passwordHash = PasswordUtil.hashPassword(password);

        // Create new user
        User newUser = new User(login, firstName, lastName, email, phoneNumber, address);
        newUser.setPasswordHash(passwordHash);
        newUser.setAdmin(false); // New users are not admins by default

        // Add user to database
        userDAO.addUser(login, email);
        User savedUser = userDAO.getUserByLogin(login);

        if (savedUser != null) {
            savedUser.setPasswordHash(passwordHash);
            savedUser.setFirstName(firstName);
            savedUser.setLastName(lastName);
            savedUser.setPhoneNumber(phoneNumber);
            savedUser.setAddress(address);
            userDAO.updateUser(savedUser);
            // No need to call commit - updateUser handles transaction
        }

        return savedUser;
    }

    /**
     * Authenticates user and returns JWT token
     * @param login username/login
     * @param password plain text password
     * @return JWT token if authentication successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public String login(String login, String password) throws IllegalArgumentException {
        if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Login and password are required");
        }

        User user = userDAO.getUserByLogin(login);
        if (user == null) {
            throw new IllegalArgumentException("Invalid login or password");
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid login or password");
        }

        // Generate JWT token
        return JwtUtil.generateToken(user.getId(), user.getLogin(), user.isAdmin());
    }

}

