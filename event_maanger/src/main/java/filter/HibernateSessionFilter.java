package filter;

import dao.hibernate.HibernateSessionFactory;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

/**
 * Servlet Filter that manages Hibernate Session and Transaction for each HTTP request
 * Implements OpenSessionInView pattern to allow lazy loading throughout request processing
 * Uses ThreadLocal to store session/transaction for access from DAO layer
 */
public class HibernateSessionFilter implements Filter {

    private static final ThreadLocal<Session> SESSION_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> TRANSACTION_HOLDER = new ThreadLocal<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        SESSION_HOLDER.set(session);
        TRANSACTION_HOLDER.set(transaction);

        try {
            chain.doFilter(request, response);
            // Auto-commit on success
            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            // Auto-rollback on exception
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            try {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            } finally {
                SESSION_HOLDER.remove();
                TRANSACTION_HOLDER.remove();
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }

    /**
     * Get current session from ThreadLocal
     */
    public static Session getSession() {
        return SESSION_HOLDER.get();
    }

    /**
     * Get current transaction from ThreadLocal
     */
    public static Transaction getTransaction() {
        return TRANSACTION_HOLDER.get();
    }
}

