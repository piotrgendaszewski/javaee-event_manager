package dao.hibernate;

import dao.UserDAO;
import model.User;
import JWT.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserHibernate implements UserDAO {

    private static boolean adminInitialized = false;
    private static final Object initLock = new Object();

    public UserHibernate() {
        // Ensure default admin is initialized only once
        if (!adminInitialized) {
            synchronized (initLock) {
                if (!adminInitialized) {
                    initializeDefaultAdmin();
                    adminInitialized = true;
                }
            }
        }
    }

    /**
     * Creates default admin user if no users exist in database
     * Executed only once during application startup
     */
    private void initializeDefaultAdmin() {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Check if any users exist
            String query = "SELECT COUNT(*) FROM User";
            Long userCount = session.createQuery(query, Long.class).getSingleResult();

            if (userCount == null || userCount == 0) {
                // Create default admin user
                User adminUser = new User("admin", "admin@admin.com");
                adminUser.setFirstName("Admin");
                adminUser.setLastName("Administrator");
                adminUser.setAddress("System");
                adminUser.setPhoneNumber("000-000-0000");
                adminUser.setAdmin(true);
                adminUser.setPasswordHash(PasswordUtil.hashPassword("admin"));

                session.persist(adminUser);
                transaction.commit();
                System.out.println("Default admin user created: login=admin, password=admin");
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error initializing default admin user: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /**
     * Get a new session from the shared SessionFactory
     */
    private Session getSession() {
        return HibernateSessionFactory.getSessionFactory().openSession();
    }

    @Override
    public void commit() {
        // No-op: transactions are handled per-operation
    }

    @Override
    public void rollback() {
        // No-op: transactions are handled per-operation
    }

    @Override
    public User addUser(String login, String email) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            User user = new User(login, email);
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding user: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        Session session = getSession();

        try {
            String query = "FROM User WHERE login = :login";
            return session.createQuery(query, User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateUser(User user) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteUser(User user) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = getSession();

        try {
            String query = "FROM User";
            return session.createQuery(query, User.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public User getUserById(int id) {
        Session session = getSession();

        try {
            return session.get(User.class, id);
        } finally {
            session.close();
        }
    }
}
