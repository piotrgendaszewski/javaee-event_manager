package dto;

/**
 * EventReview DTO - dane recenzji bez zagnieżdżonych obiektów
 * Zawiera tylko basic info o evencie i użytkowniku
 */
public class EventReviewDTO {
    private int id;
    private int eventId;
    private String eventName;
    private int userId;
    private String userName;
    private int rating;
    private String reviewText;
    private String reviewDate;

    public EventReviewDTO(int id, int eventId, String eventName, int userId,
                         String userName, int rating, String reviewText, String reviewDate) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    // Getters
    public int getId() { return id; }
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getReviewText() { return reviewText; }
    public String getReviewDate() { return reviewDate; }
}

