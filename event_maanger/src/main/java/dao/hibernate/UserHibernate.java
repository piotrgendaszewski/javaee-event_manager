package dao.hibernate;

import dao.UserDAO;
import model.Event;
import model.EventReview;
import model.Location;
import model.Room;
import model.Ticket;
import model.User;
import JWT.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserHibernate implements UserDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public UserHibernate() {

        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(EventReview.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        // Initialize default admin user if database is empty
        initializeDefaultAdmin();
    }

    /**
     * Creates default admin user if no users exist in database
     */
    private void initializeDefaultAdmin() {
        try {
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

                // Hash the password "admin" using SHA-256
                adminUser.setPasswordHash(PasswordUtil.hashPassword("admin"));

                session.persist(adminUser);
                transaction.commit();

                // Start new transaction for subsequent operations
                transaction = session.beginTransaction();

                System.out.println("Default admin user created: login=admin, password=admin");
            }
        } catch (Exception e) {
            System.err.println("Error initializing default admin user: " + e.getMessage());
        }
    }

    /**
     * Hashes password using SHA-256
     */
    // Method moved to PasswordUtil class

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
        String query = "FROM User WHERE login = :login";
        return session.createQuery(query, User.class)
                .setParameter("login", login)
                .uniqueResult();
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
