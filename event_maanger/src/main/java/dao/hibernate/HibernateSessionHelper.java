package dao.hibernate;

import filter.HibernateSessionFilter;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Helper class for DAO layer to access Hibernate session
 * Manages both HTTP-managed sessions and standalone sessions
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

    /**
     * Get current transaction from HTTP filter or create new one if standalone
     */
    public static Transaction getCurrentTransaction(Session session) {
        if (isInHttpContext()) {
            Transaction tx = HibernateSessionFilter.getTransaction();
            if (tx != null && tx.isActive()) {
                return tx;
            }
        }
        // For standalone operations
        if (!session.getTransaction().isActive()) {
            return session.beginTransaction();
        }
        return session.getTransaction();
    }

    /**
     * Check if transaction is managed by HTTP filter
     */
    public static boolean isTransactionManagedByFilter() {
        return isInHttpContext() && HibernateSessionFilter.getTransaction() != null;
    }
}

