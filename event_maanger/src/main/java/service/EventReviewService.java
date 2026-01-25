package service;

import dao.EventReviewDAO;
import model.EventReview;

import java.util.List;

public class EventReviewService {
    private final EventReviewDAO eventReviewDAO;

    public EventReviewService(EventReviewDAO eventReviewDAO) {
        this.eventReviewDAO = eventReviewDAO;
    }

    public void commit() {
        eventReviewDAO.commit();
    }

    public void rollback() {
        eventReviewDAO.rollback();
    }

    public EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate) {
        return eventReviewDAO.addReview(eventId, userId, rating, reviewText, reviewDate);
    }

    public EventReview getReview(int id) {
        return eventReviewDAO.getReviewById(id);
    }

    public void updateReview(EventReview review) {
        eventReviewDAO.updateReview(review);
    }

    public void deleteReview(EventReview review) {
        eventReviewDAO.deleteReview(review);
    }

    public List<EventReview> getAllReviews() {
        return eventReviewDAO.getAllReviews();
    }

    public List<EventReview> getReviewsByEvent(int eventId) {
        return eventReviewDAO.getReviewsByEventId(eventId);
    }

    public List<EventReview> getReviewsByUser(int userId) {
        return eventReviewDAO.getReviewsByUserId(userId);
    }

    public EventReview getReviewByEventAndUser(int eventId, int userId) {
        return eventReviewDAO.getReviewByEventAndUser(eventId, userId);
    }

    public boolean hasUserReviewedEvent(int eventId, int userId) {
        return eventReviewDAO.hasUserReviewedEvent(eventId, userId);
    }

    public double getAverageRatingForEvent(int eventId) {
        return eventReviewDAO.getAverageRatingForEvent(eventId);
    }
}

