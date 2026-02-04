package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventReviewTest {

    private EventReview review;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        review = new EventReview();
        event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", false);
        user = new User("john_doe", "john@example.com");
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(review);
        assertEquals(0, review.getId());
        assertNull(review.getEvent());
        assertNull(review.getUser());
    }

    @Test
    void testConstructorWithAllFields() {
        EventReview testReview = new EventReview(event, user, 5, "Excellent concert!", "2026-05-15");
        assertNotNull(testReview);
        assertEquals(event, testReview.getEvent());
        assertEquals(user, testReview.getUser());
        assertEquals(5, testReview.getRating());
        assertEquals("Excellent concert!", testReview.getReviewText());
        assertEquals("2026-05-15", testReview.getReviewDate());
    }

    @Test
    void testConstructorWithRatingAndDate() {
        EventReview testReview = new EventReview(event, user, 4, "2026-05-15");
        assertNotNull(testReview);
        assertEquals(event, testReview.getEvent());
        assertEquals(user, testReview.getUser());
        assertEquals(4, testReview.getRating());
        assertNull(testReview.getReviewText());
        assertEquals("2026-05-15", testReview.getReviewDate());
    }

    @Test
    void testConstructorInvalidRatingTooLow() {
        assertThrows(IllegalArgumentException.class, () ->
            new EventReview(event, user, 0, "Bad", "2026-05-15")
        );
    }

    @Test
    void testConstructorInvalidRatingTooHigh() {
        assertThrows(IllegalArgumentException.class, () ->
            new EventReview(event, user, 6, "Too good", "2026-05-15")
        );
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        review.setId(1);
        assertEquals(1, review.getId());
    }

    @Test
    void setId() {
        review.setId(42);
        assertEquals(42, review.getId());
    }

    // ===== EVENT TESTS =====

    @Test
    void getEvent() {
        review.setEvent(event);
        assertEquals(event, review.getEvent());
        assertEquals("Concert", review.getEvent().getName());
    }

    @Test
    void setEvent() {
        Event newEvent = new Event("Festival", "Music festival", "2026-06-01", "10:00", "2026-06-01", "2026-06-01", true);
        review.setEvent(newEvent);
        assertEquals(newEvent, review.getEvent());
    }

    @Test
    void setEventNull() {
        review.setEvent(null);
        assertNull(review.getEvent());
    }

    // ===== USER TESTS =====

    @Test
    void getUser() {
        review.setUser(user);
        assertEquals(user, review.getUser());
        assertEquals("john_doe", review.getUser().getLogin());
    }

    @Test
    void setUser() {
        User newUser = new User("jane_smith", "jane@example.com");
        review.setUser(newUser);
        assertEquals(newUser, review.getUser());
    }

    @Test
    void setUserNull() {
        review.setUser(null);
        assertNull(review.getUser());
    }

    // ===== RATING TESTS =====

    @Test
    void getRating() {
        review.setRating(3);
        assertEquals(3, review.getRating());
    }

    @Test
    void setRating() {
        review.setRating(5);
        assertEquals(5, review.getRating());
    }

    @Test
    void setRatingMin() {
        review.setRating(1);
        assertEquals(1, review.getRating());
    }

    @Test
    void setRatingMax() {
        review.setRating(5);
        assertEquals(5, review.getRating());
    }

    @Test
    void setRatingInvalidZero() {
        assertThrows(IllegalArgumentException.class, () -> review.setRating(0));
    }

    @Test
    void setRatingInvalidNegative() {
        assertThrows(IllegalArgumentException.class, () -> review.setRating(-1));
    }

    @Test
    void setRatingInvalidTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> review.setRating(6));
    }

    @Test
    void setRatingInvalidWayTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> review.setRating(100));
    }

    // ===== REVIEW TEXT TESTS =====

    @Test
    void getReviewText() {
        review.setReviewText("Great experience!");
        assertEquals("Great experience!", review.getReviewText());
    }

    @Test
    void setReviewText() {
        review.setReviewText("Amazing event");
        assertEquals("Amazing event", review.getReviewText());
    }

    @Test
    void setReviewTextNull() {
        review.setReviewText(null);
        assertNull(review.getReviewText());
    }

    @Test
    void setReviewTextEmpty() {
        review.setReviewText("");
        assertEquals("", review.getReviewText());
    }

    @Test
    void setReviewTextLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String longText = sb.toString();
        review.setReviewText(longText);
        assertEquals(longText, review.getReviewText());
    }

    // ===== REVIEW DATE TESTS =====

    @Test
    void getReviewDate() {
        review.setReviewDate("2026-05-15");
        assertEquals("2026-05-15", review.getReviewDate());
    }

    @Test
    void setReviewDate() {
        review.setReviewDate("2026-05-20");
        assertEquals("2026-05-20", review.getReviewDate());
    }

    @Test
    void setReviewDateNull() {
        review.setReviewDate(null);
        assertNull(review.getReviewDate());
    }

    // ===== VALIDATION TESTS =====

    @Test
    void testIsValidComplete() {
        review.setEvent(event);
        review.setUser(user);
        review.setRating(5);
        review.setReviewDate("2026-05-15");
        assertTrue(review.isValid());
    }

    @Test
    void testIsValidNoEvent() {
        review.setEvent(null);
        review.setUser(user);
        review.setRating(4);
        review.setReviewDate("2026-05-15");
        assertFalse(review.isValid());
    }

    @Test
    void testIsValidNoUser() {
        review.setEvent(event);
        review.setUser(null);
        review.setRating(3);
        review.setReviewDate("2026-05-15");
        assertFalse(review.isValid());
    }

    @Test
    void testIsValidNoDate() {
        review.setEvent(event);
        review.setUser(user);
        review.setRating(5);
        review.setReviewDate(null);
        assertFalse(review.isValid());
    }

    @Test
    void testIsValidInvalidRating() {
        review.setEvent(event);
        review.setUser(user);
        // Don't set rating - should default to 0
        review.setReviewDate("2026-05-15");
        // Rating of 0 is invalid
        assertFalse(review.isValid());
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        event.setId(1);
        user.setId(5);
        review.setId(10);
        review.setEvent(event);
        review.setUser(user);
        review.setRating(5);
        review.setReviewText("Excellent!");
        review.setReviewDate("2026-05-15");

        String result = review.toString();
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("eventId=1"));
        assertTrue(result.contains("userId=5"));
        assertTrue(result.contains("rating=5"));
        assertTrue(result.contains("reviewText='Excellent!'"));
        assertTrue(result.contains("reviewDate='2026-05-15'"));
    }

    @Test
    void testToStringNullReferences() {
        review.setId(20);
        review.setEvent(null);
        review.setUser(null);
        review.setRating(3);
        review.setReviewText(null);
        review.setReviewDate("2026-05-15");

        String result = review.toString();
        assertTrue(result.contains("id=20"));
        assertTrue(result.contains("eventId=0"));  // Null event returns 0
        assertTrue(result.contains("userId=0"));   // Null user returns 0
        assertTrue(result.contains("rating=3"));
        assertTrue(result.contains("reviewText='null'"));
    }

    @Test
    void testToStringAllRatings() {
        review.setId(1);
        review.setEvent(event);
        review.setUser(user);

        for (int rating = 1; rating <= 5; rating++) {
            review.setRating(rating);
            String result = review.toString();
            assertTrue(result.contains("rating=" + rating));
        }
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullReviewFlow() {
        event.setId(1);
        user.setId(1);

        EventReview testReview = new EventReview(event, user, 5, "Outstanding performance!", "2026-05-16");
        testReview.setId(1);

        assertEquals(1, testReview.getId());
        assertEquals(event, testReview.getEvent());
        assertEquals(user, testReview.getUser());
        assertEquals(5, testReview.getRating());
        assertEquals("Outstanding performance!", testReview.getReviewText());
        assertEquals("2026-05-16", testReview.getReviewDate());
        assertTrue(testReview.isValid());
    }

    @Test
    void testMultipleReviewsForSameEvent() {
        event.setId(1);

        User user1 = new User("user1", "user1@example.com");
        User user2 = new User("user2", "user2@example.com");
        User user3 = new User("user3", "user3@example.com");

        user1.setId(1);
        user2.setId(2);
        user3.setId(3);

        EventReview review1 = new EventReview(event, user1, 5, "Excellent!", "2026-05-15");
        EventReview review2 = new EventReview(event, user2, 4, "Good", "2026-05-16");
        EventReview review3 = new EventReview(event, user3, 3, "Average", "2026-05-17");

        assertEquals(5, review1.getRating());
        assertEquals(4, review2.getRating());
        assertEquals(3, review3.getRating());
        assertTrue(review1.isValid());
        assertTrue(review2.isValid());
        assertTrue(review3.isValid());
    }

    @Test
    void testReviewAllRatings() {
        review.setEvent(event);
        review.setUser(user);
        review.setReviewDate("2026-05-15");

        // Test all valid ratings
        for (int rating = 1; rating <= 5; rating++) {
            review.setRating(rating);
            assertEquals(rating, review.getRating());
            assertTrue(review.isValid());
        }
    }

    @Test
    void testTwoReviewsAreIndependent() {
        EventReview review1 = new EventReview(event, user, 5, "Great!", "2026-05-15");
        EventReview review2 = new EventReview(event, user, 2, "Poor", "2026-05-16");

        assertEquals(5, review1.getRating());
        assertEquals(2, review2.getRating());
        assertNotEquals(review1.getRating(), review2.getRating());
    }

    @Test
    void testReviewValidationBoundaries() {
        review.setEvent(event);
        review.setUser(user);
        review.setReviewDate("2026-05-15");

        // Test boundary - rating 1 (minimum valid)
        review.setRating(1);
        assertTrue(review.isValid());

        // Test boundary - rating 5 (maximum valid)
        review.setRating(5);
        assertTrue(review.isValid());
    }
}

