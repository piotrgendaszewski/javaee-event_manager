package REST;

import dao.hibernate.TicketHibernate;
import dao.hibernate.EventReviewHibernate;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import model.Ticket;
import model.EventReview;
import service.TicketService;
import service.EventReviewService;

import java.util.List;

/**
 * API for authenticated users
 * Allows users to view their tickets, reviews and bookings
 */
@Path("/authenticated/user")
@Produces("application/json")
@Consumes("application/json")
public class AuthenticatedUserResource {

    private final TicketService ticketService;
    private final EventReviewService eventReviewService;

    public AuthenticatedUserResource() {
        this.ticketService = new TicketService(new TicketHibernate());
        this.eventReviewService = new EventReviewService(new EventReviewHibernate());
    }

    /**
     * Get user's tickets
     * Requires authentication
     */
    @GET
    @Path("/tickets")
    public List<Ticket> getUserTickets(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        return ticketService.getTicketsByUser(userId);
    }

    /**
     * Get specific ticket details
     * Requires authentication and ticket ownership
     */
    @GET
    @Path("/tickets/{ticketId}")
    public Ticket getUserTicket(@PathParam("ticketId") int ticketId,
                                @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null) {
            throw new NotFoundException("Ticket not found");
        }

        // Verify ownership
        if (ticket.getUser().getId() != userId) {
            throw new ForbiddenException("You don't have access to this ticket");
        }

        return ticket;
    }

    /**
     * Get user's reviews
     * Requires authentication
     */
    @GET
    @Path("/reviews")
    public List<EventReview> getUserReviews(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        return eventReviewService.getReviewsByUser(userId);
    }

    /**
     * Add review for event
     * Requires authentication
     */
    @POST
    @Path("/reviews")
    public EventReview addReview(EventReview review, @Context ContainerRequestContext requestContext) throws Exception {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }

        // Check if user already reviewed this event
        int eventId = review.getEvent().getId();
        if (eventReviewService.hasUserReviewedEvent(eventId, userId)) {
            throw new BadRequestException("You have already reviewed this event");
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

    /**
     * Update user's review
     * Requires authentication and review ownership
     */
    @PUT
    @Path("/reviews/{reviewId}")
    public EventReview updateReview(@PathParam("reviewId") int reviewId,
                                   EventReview review,
                                   @Context ContainerRequestContext requestContext) throws Exception {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        EventReview existingReview = eventReviewService.getReview(reviewId);
        if (existingReview == null) {
            throw new NotFoundException("Review not found");
        }

        // Verify ownership
        if (existingReview.getUser().getId() != userId) {
            throw new ForbiddenException("You don't have access to this review");
        }

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }

        review.setId(reviewId);
        eventReviewService.updateReview(review);
        eventReviewService.commit();

        return review;
    }

    /**
     * Delete user's review
     * Requires authentication and review ownership
     */
    @DELETE
    @Path("/reviews/{reviewId}")
    public void deleteReview(@PathParam("reviewId") int reviewId,
                            @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        EventReview review = eventReviewService.getReview(reviewId);
        if (review == null) {
            throw new NotFoundException("Review not found");
        }

        // Verify ownership
        if (review.getUser().getId() != userId) {
            throw new ForbiddenException("You don't have access to this review");
        }

        eventReviewService.deleteReview(review);
        eventReviewService.commit();
    }
}

