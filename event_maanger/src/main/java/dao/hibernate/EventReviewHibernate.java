package dao.hibernate;

import dao.EventReviewDAO;
import model.Event;
import model.EventReview;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EventReviewHibernate implements EventReviewDAO {

    public EventReviewHibernate() {
        // No-arg constructor - SessionFactory is shared via singleton
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
    public EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Event event = session.get(Event.class, eventId);
            User user = session.get(User.class, userId);

            if (event != null && user != null) {
                EventReview review = new EventReview(event, user, rating, reviewText, reviewDate);
                session.persist(review);
                if (!isManaged && transaction.isActive()) {
                    transaction.commit();
                }
                return review;
            }
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
            return null;
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding review: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public EventReview getReviewById(int id) {
        Session session = HibernateSessionHelper.getCurrentSession();
        return session.get(EventReview.class, id);
    }

    @Override
    public void updateReview(EventReview review) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.merge(review);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating review: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteReview(EventReview review) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.remove(session.merge(review));
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting review: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<EventReview> getAllReviews() {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM EventReview";
        return session.createQuery(query, EventReview.class).list();
    }

    @Override
    public List<EventReview> getReviewsByEventId(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM EventReview WHERE event.id = :eventId";
        return session.createQuery(query, EventReview.class)
                .setParameter("eventId", eventId)
                .list();
    }

    @Override
    public List<EventReview> getReviewsByUserId(int userId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM EventReview WHERE user.id = :userId";
        return session.createQuery(query, EventReview.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public EventReview getReviewByEventAndUser(int eventId, int userId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM EventReview WHERE event.id = :eventId AND user.id = :userId";
        return session.createQuery(query, EventReview.class)
                .setParameter("eventId", eventId)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    @Override
    public boolean hasUserReviewedEvent(int eventId, int userId) {
        return getReviewByEventAndUser(eventId, userId) != null;
    }

    @Override
    public double getAverageRatingForEvent(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT AVG(rating) FROM EventReview WHERE event.id = :eventId";
        Double result = session.createQuery(query, Double.class)
                .setParameter("eventId", eventId)
                .getSingleResult();
        return result != null ? result : 0.0;
    }
}

