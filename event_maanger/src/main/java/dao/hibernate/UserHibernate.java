package dao.hibernate;

import dao.UserDAO;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserHibernate implements UserDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public UserHibernate() {

        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    public void commit() {
        transaction.commit();
    }

    public void rollback() {
        transaction.rollback();
    }

    @Override
    public User addUser(String login, String email) {
        User user = new User(login, email);
        session.persist(user);
        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        return session.get(User.class, login);
    }

    @Override
    public void updateUser(User user) {
        session.merge(user);
    }

    @Override
    public void deleteUser(User user) {
        session.remove(user);
    }

    @Override
    public List<User> getAllUsers() {
        String query = "FROM User";
        return session.createQuery(query, User.class).list();
    }


}
