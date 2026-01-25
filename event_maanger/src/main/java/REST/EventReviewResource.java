package REST;

import dao.hibernate.EventReviewHibernate;
import jakarta.ws.rs.*;
import model.EventReview;
import service.EventReviewService;

import java.util.List;

@Path("/reviews")
@Produces("application/json")
@Consumes("application/json")
public class EventReviewResource {

    private final EventReviewService eventReviewService;

    public EventReviewResource() {
        this.eventReviewService = new EventReviewService(new EventReviewHibernate());
    }

    @GET
    public List<EventReview> getAllReviews() {
        return eventReviewService.getAllReviews();
    }

    @GET
    @Path("/{id}")
    public EventReview getReview(@PathParam("id") int id) {
        return eventReviewService.getReview(id);
    }

    @GET
    @Path("/event/{eventId}")
    public List<EventReview> getReviewsByEvent(@PathParam("eventId") int eventId) {
        return eventReviewService.getReviewsByEvent(eventId);
    }

    @GET
    @Path("/user/{userId}")
    public List<EventReview> getReviewsByUser(@PathParam("userId") int userId) {
        return eventReviewService.getReviewsByUser(userId);
    }

    @GET
    @Path("/event/{eventId}/user/{userId}")
    public EventReview getReviewByEventAndUser(@PathParam("eventId") int eventId, @PathParam("userId") int userId) {
        return eventReviewService.getReviewByEventAndUser(eventId, userId);
    }

    @GET
    @Path("/event/{eventId}/average-rating")
    public double getAverageRatingForEvent(@PathParam("eventId") int eventId) {
        return eventReviewService.getAverageRatingForEvent(eventId);
    }

    @GET
    @Path("/event/{eventId}/user/{userId}/exists")
    public boolean hasUserReviewedEvent(@PathParam("eventId") int eventId, @PathParam("userId") int userId) {
        return eventReviewService.hasUserReviewedEvent(eventId, userId);
    }

    @POST
    public EventReview addReview(EventReview review) throws Exception {
        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }

        // Check if user already reviewed this event
        int eventId = review.getEvent().getId();
        int userId = review.getUser().getId();

        if (eventReviewService.hasUserReviewedEvent(eventId, userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " has already reviewed event with ID " + eventId + ". Each user can only add one review per event.");
        }

        // Validate review
        if (!review.isValid()) {
            throw new IllegalArgumentException("Review is missing required fields (event, user, rating, reviewDate)");
        }

        EventReview newReview = eventReviewService.addReview(
                eventId,
                userId,
                review.getRating(),
                review.getReviewText(),
                review.getReviewDate()
        );
        eventReviewService.commit();
        return newReview;
    }

    @PUT
    @Path("/{id}")
    public EventReview updateReview(@PathParam("id") int id, EventReview review) throws Exception {
        review.setId(id);

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }

        // Validate review
        if (!review.isValid()) {
            throw new IllegalArgumentException("Review is missing required fields (event, user, rating, reviewDate)");
        }

        eventReviewService.updateReview(review);
        eventReviewService.commit();
        return review;
    }

    @DELETE
    @Path("/{id}")
    public void deleteReview(@PathParam("id") int id) {
        EventReview review = eventReviewService.getReview(id);
        if (review != null) {
            eventReviewService.deleteReview(review);
            eventReviewService.commit();
        }
    }
}

