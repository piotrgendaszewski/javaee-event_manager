package service;

import dao.UserDAO;
import model.User;

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

}
