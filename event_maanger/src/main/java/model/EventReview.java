package model;

import jakarta.persistence.*;

/**
 * EventReview entity representing a review for an event.
 *
 * Constraints:
 * - Each user can add only one review per event (unique constraint on event_id + user_id)
 * - Rating must be between 1 and 5 stars
 */
@Entity
@Table(name = "event_reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "user_id"}, name = "uc_event_user_review")
})
public class EventReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "review_date")
    private String reviewDate;

    public EventReview() {}

    public EventReview(Event event, User user, int rating, String reviewText, String reviewDate) {
        validateRating(rating);
        this.event = event;
        this.user = user;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public EventReview(Event event, User user, int rating, String reviewDate) {
        validateRating(rating);
        this.event = event;
        this.user = user;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        validateRating(rating);
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Validates that rating is between 1 and 5
     * @throws IllegalArgumentException if rating is out of valid range
     */
    private void validateRating(int rating) throws IllegalArgumentException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }
    }

    /**
     * Checks if the review is valid (all required fields are present)
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return event != null && user != null && rating >= 1 && rating <= 5 && reviewDate != null;
    }
}

