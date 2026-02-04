package service;

import model.EventReview;

/**
 * DTO for public event review listing
 * Contains only essential review data - no sensitive user information
 */
public class EventReviewPublicDTO {
    private int id;
    private int rating;
    private String reviewText;
    private String reviewDate;
    private String reviewerName; // First name + last name (no email/sensitive data)

    public EventReviewPublicDTO() {
    }

    public EventReviewPublicDTO(EventReview review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.reviewDate = review.getReviewDate();
        // Build reviewer name from user
        if (review.getUser() != null) {
            String firstName = review.getUser().getFirstName() != null ? review.getUser().getFirstName() : "Anonymous";
            String lastName = review.getUser().getLastName() != null ? review.getUser().getLastName() : "";
            this.reviewerName = firstName + (lastName.isEmpty() ? "" : " " + lastName);
        } else {
            this.reviewerName = "Anonymous";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    @Override
    public String toString() {
        return "EventReviewPublicDTO{" +
                "id=" + id +
                ", rating=" + rating +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                '}';
    }
}

