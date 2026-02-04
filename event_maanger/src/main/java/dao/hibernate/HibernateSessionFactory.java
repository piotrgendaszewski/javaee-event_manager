package dao.hibernate;

import model.Event;
import model.EventReview;
import model.Location;
import model.Room;
import model.Ticket;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Singleton factory for Hibernate SessionFactory
 * Ensures only one SessionFactory is created for the entire application
 * This prevents database locking issues with SQLite
 */
public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;

    /**
     * Private constructor to prevent instantiation
     */
    private HibernateSessionFactory() {
    }

    /**
     * Get the singleton SessionFactory instance
     * Thread-safe double-checked locking pattern
     *
     * @return SessionFactory instance
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateSessionFactory.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration()
                                .configure("hibernate.cfg.xml")
                                .addAnnotatedClass(User.class)
                                .addAnnotatedClass(Location.class)
                                .addAnnotatedClass(Room.class)
                                .addAnnotatedClass(Event.class)
                                .addAnnotatedClass(Ticket.class)
                                .addAnnotatedClass(EventReview.class)
                                .buildSessionFactory();
                    } catch (Exception e) {
                        System.err.println("Error creating Hibernate SessionFactory: " + e.getMessage());
                        e.printStackTrace();
                        throw new ExceptionInInitializerError(e);
                    }
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Close the SessionFactory
     * Should be called on application shutdown
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}

