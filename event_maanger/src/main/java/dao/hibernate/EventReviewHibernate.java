package dao.hibernate;

import dao.EventReviewDAO;
import model.Event;
import model.EventReview;
import model.Location;
import model.Room;
import model.Ticket;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EventReviewHibernate implements EventReviewDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public EventReviewHibernate() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(EventReview.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Ticket.class)
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
    public EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate) {
        Event event = session.get(Event.class, eventId);
        User user = session.get(User.class, userId);

        if (event != null && user != null) {
            EventReview review = new EventReview(event, user, rating, reviewText, reviewDate);
            session.persist(review);
            return review;
        }
        return null;
    }

    @Override
    public EventReview getReviewById(int id) {
        return session.get(EventReview.class, id);
    }

    @Override
    public void updateReview(EventReview review) {
        session.merge(review);
    }

    @Override
    public void deleteReview(EventReview review) {
        session.remove(review);
    }

    @Override
    public List<EventReview> getAllReviews() {
        String query = "FROM EventReview";
        return session.createQuery(query, EventReview.class).list();
    }

    @Override
    public List<EventReview> getReviewsByEventId(int eventId) {
        String query = "FROM EventReview WHERE event.id = :eventId";
        return session.createQuery(query, EventReview.class)
                .setParameter("eventId", eventId)
                .list();
    }

    @Override
    public List<EventReview> getReviewsByUserId(int userId) {
        String query = "FROM EventReview WHERE user.id = :userId";
        return session.createQuery(query, EventReview.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public EventReview getReviewByEventAndUser(int eventId, int userId) {
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
        String query = "SELECT AVG(rating) FROM EventReview WHERE event.id = :eventId";
        Double result = session.createQuery(query, Double.class)
                .setParameter("eventId", eventId)
                .getSingleResult();
        return result != null ? result : 0.0;
    }
}

