package dao.hibernate;

import filter.HibernateSessionFilter;
import org.hibernate.Session;

/**
 * Helper class for DAO layer to access Hibernate session
 */
public class HibernateSessionHelper {

    /**
     * Get current session - either from ThreadLocal (HTTP requests) or create new one (tests)
     */
    public static Session getCurrentSession() {
        Session session = HibernateSessionFilter.getSession();
        if (session != null) {
            return session;
        }
        // Fallback for tests or non-HTTP contexts
        return HibernateSessionFactory.getSessionFactory().openSession();
    }

    /**
     * Check if we're in an HTTP request context
     */
    public static boolean isInHttpContext() {
        return HibernateSessionFilter.getSession() != null;
    }
}

