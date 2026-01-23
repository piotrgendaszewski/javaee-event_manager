package dao;

import model.User;

import java.util.List;

public interface UserDAO {
    void rollback();
    void commit();

    // Define user-related data access methods here
    User addUser(String username, String email);
    User getUserByLogin(String login);
    void updateUser(User user);
    void deleteUser(User user);
    List<User> getAllUsers();

}
