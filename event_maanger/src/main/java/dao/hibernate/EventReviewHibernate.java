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
    public EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Event event = session.get(Event.class, eventId);
            User user = session.get(User.class, userId);

            if (event != null && user != null) {
                EventReview review = new EventReview(event, user, rating, reviewText, reviewDate);
                session.persist(review);
                transaction.commit();
                return review;
            }
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
            return null;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding review: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public EventReview getReviewById(int id) {
        Session session = getSession();

        try {
            return session.get(EventReview.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateReview(EventReview review) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating review: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteReview(EventReview review) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(session.merge(review));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting review: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<EventReview> getAllReviews() {
        Session session = getSession();

        try {
            String query = "FROM EventReview";
            return session.createQuery(query, EventReview.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EventReview> getReviewsByEventId(int eventId) {
        Session session = getSession();

        try {
            String query = "FROM EventReview WHERE event.id = :eventId";
            return session.createQuery(query, EventReview.class)
                    .setParameter("eventId", eventId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EventReview> getReviewsByUserId(int userId) {
        Session session = getSession();

        try {
            String query = "FROM EventReview WHERE user.id = :userId";
            return session.createQuery(query, EventReview.class)
                    .setParameter("userId", userId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public EventReview getReviewByEventAndUser(int eventId, int userId) {
        Session session = getSession();

        try {
            String query = "FROM EventReview WHERE event.id = :eventId AND user.id = :userId";
            return session.createQuery(query, EventReview.class)
                    .setParameter("eventId", eventId)
                    .setParameter("userId", userId)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean hasUserReviewedEvent(int eventId, int userId) {
        return getReviewByEventAndUser(eventId, userId) != null;
    }

    @Override
    public double getAverageRatingForEvent(int eventId) {
        Session session = getSession();

        try {
            String query = "SELECT AVG(rating) FROM EventReview WHERE event.id = :eventId";
            Double result = session.createQuery(query, Double.class)
                    .setParameter("eventId", eventId)
                    .getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            session.close();
        }
    }
}

