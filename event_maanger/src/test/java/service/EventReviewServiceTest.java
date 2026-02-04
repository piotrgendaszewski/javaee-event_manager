package service;

import dao.EventReviewDAO;
import model.EventReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EventReviewService
 */
class EventReviewServiceTest {

    private EventReviewService eventReviewService;
    private TestEventReviewDAO testEventReviewDAO;

    @BeforeEach
    void setUp() {
        testEventReviewDAO = new TestEventReviewDAO();
        eventReviewService = new EventReviewService(testEventReviewDAO);
    }

    @Test
    void testAddReview() {
        EventReview result = eventReviewService.addReview(1, 1, 5, "Excellent!", "2026-05-15");

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Excellent!", result.getReviewText());
    }

    @Test
    void testGetReviewById() {
        EventReview review = eventReviewService.addReview(1, 1, 4, "Good", "2026-05-15");
        EventReview result = eventReviewService.getReview(review.getId());

        assertNotNull(result);
        assertEquals(4, result.getRating());
    }

    @Test
    void testUpdateReview() {
        EventReview review = eventReviewService.addReview(1, 1, 3, "Average", "2026-05-15");
        review.setRating(4);

        assertDoesNotThrow(() -> eventReviewService.updateReview(review));
    }

    @Test
    void testDeleteReview() {
        EventReview review = eventReviewService.addReview(1, 1, 2, "Bad", "2026-05-15");

        assertDoesNotThrow(() -> eventReviewService.deleteReview(review));
    }

    @Test
    void testGetAllReviews() {
        eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");
        eventReviewService.addReview(1, 2, 4, "Good", "2026-05-16");

        List<EventReview> result = eventReviewService.getAllReviews();

        assertEquals(2, result.size());
    }

    @Test
    void testGetReviewsByEventId() {
        eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");
        eventReviewService.addReview(1, 2, 4, "Good", "2026-05-16");

        List<EventReview> result = eventReviewService.getReviewsByEvent(1);

        assertNotNull(result);
    }

    @Test
    void testGetReviewsByUserId() {
        eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");
        eventReviewService.addReview(2, 1, 4, "Good", "2026-05-16");

        List<EventReview> result = eventReviewService.getReviewsByUser(1);

        assertNotNull(result);
    }

    @Test
    void testGetReviewByEventAndUser() {
        EventReview review = eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");
        EventReview result = eventReviewService.getReviewByEventAndUser(1, 1);

        assertNotNull(result);
        assertEquals(5, result.getRating());
    }

    @Test
    void testHasUserReviewedEvent() {
        eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");

        boolean result = eventReviewService.hasUserReviewedEvent(1, 1);

        assertTrue(result);
    }

    @Test
    void testHasUserReviewedEventFalse() {
        boolean result = eventReviewService.hasUserReviewedEvent(1, 1);

        assertFalse(result);
    }

    @Test
    void testGetAverageRatingForEvent() {
        eventReviewService.addReview(1, 1, 5, "Great", "2026-05-15");
        eventReviewService.addReview(1, 2, 3, "Average", "2026-05-16");

        double result = eventReviewService.getAverageRatingForEvent(1);

        assertEquals(4.0, result);
    }

    @Test
    void testCommit() {
        assertDoesNotThrow(() -> eventReviewService.commit());
    }

    @Test
    void testRollback() {
        assertDoesNotThrow(() -> eventReviewService.rollback());
    }

    private static class TestEventReviewDAO implements EventReviewDAO {
        private List<EventReview> reviews = new ArrayList<>();

        @Override
        public void rollback() {}
        @Override
        public void commit() {}
        @Override
        public EventReview addReview(int eventId, int userId, int rating, String reviewText, String reviewDate) {
            EventReview review = new EventReview();
            review.setId(reviews.size() + 1);
            review.setRating(rating);
            review.setReviewText(reviewText);
            review.setReviewDate(reviewDate);
            reviews.add(review);
            return review;
        }
        @Override
        public EventReview getReviewById(int id) {
            return reviews.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
        }
        @Override
        public void updateReview(EventReview review) {}
        @Override
        public void deleteReview(EventReview review) {
            reviews.removeIf(r -> r.getId() == review.getId());
        }
        @Override
        public List<EventReview> getAllReviews() {
            return new ArrayList<>(reviews);
        }
        @Override
        public List<EventReview> getReviewsByEventId(int eventId) {
            return new ArrayList<>();
        }
        @Override
        public List<EventReview> getReviewsByUserId(int userId) {
            return new ArrayList<>();
        }
        @Override
        public EventReview getReviewByEventAndUser(int eventId, int userId) {
            return reviews.stream().findFirst().orElse(null);
        }
        @Override
        public boolean hasUserReviewedEvent(int eventId, int userId) {
            return !reviews.isEmpty();
        }
        @Override
        public double getAverageRatingForEvent(int eventId) {
            return reviews.stream().mapToInt(EventReview::getRating).average().orElse(0.0);
        }
    }
}

