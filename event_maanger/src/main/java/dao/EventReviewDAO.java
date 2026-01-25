package dao;

import model.EventReview;
import java.util.List;

public interface EventReviewDAO {
    void rollback();
    void commit();

    EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate);
    EventReview getReviewById(int id);
    void updateReview(EventReview review);
    void deleteReview(EventReview review);
    List<EventReview> getAllReviews();
    List<EventReview> getReviewsByEventId(int eventId);
    List<EventReview> getReviewsByUserId(int userId);
    EventReview getReviewByEventAndUser(int eventId, int userId);
    boolean hasUserReviewedEvent(int eventId, int userId);
    double getAverageRatingForEvent(int eventId);
}

