package service;

import dao.UserDAO;
import model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void commit() {
        userDAO.commit();
    }

    public void rollback() {
        userDAO.rollback();
    }

    public User getUser(String login) {
        return userDAO.getUserByLogin(login);
    }

    public User addUser(String login, String email) {
        return userDAO.addUser(login, email);
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public void deleteUser(User user) {
        userDAO.deleteUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
