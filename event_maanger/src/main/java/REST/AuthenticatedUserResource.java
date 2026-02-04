package REST;

import dao.hibernate.TicketHibernate;
import dao.hibernate.EventReviewHibernate;
import dao.hibernate.LocationHibernate;
import dto.EventReviewDTO;
import dto.LocationPrivateDTO;
import dto.TicketPrivateDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import model.Location;
import model.Ticket;
import model.EventReview;
import service.LocationService;
import service.TicketService;
import service.EventReviewService;
import service.PrivateDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API for authenticated users
 * Allows users to view their tickets, reviews and bookings
 * Returns DTO objects to avoid nested collection issues
 */
@Path("/private")
@Produces("application/json")
@Consumes("application/json")
public class AuthenticatedUserResource {

    private final TicketService ticketService;
    private final EventReviewService eventReviewService;
    private final LocationService locationService;

    public AuthenticatedUserResource() {
        this.ticketService = new TicketService(new TicketHibernate());
        this.eventReviewService = new EventReviewService(new EventReviewHibernate());
        this.locationService = new LocationService(new LocationHibernate());
    }

    // ===== TICKETS =====

    /**
     * Get user's tickets with location info
     * Requires authentication
     */
    @GET
    @Path("/tickets")
    public List<TicketPrivateDTO> getUserTickets(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(userId);
        return PrivateDTOMapper.toTicketDTOList(tickets);
    }

    /**
     * Get user's tickets with filters and location info
     * Requires authentication
     * @param validOnly if true, return only valid tickets (validFromDate <= today <= validToDate)
     * @param startDate filter tickets valid from this date (format: YYYY-MM-DD)
     * @param endDate filter tickets valid until this date (format: YYYY-MM-DD)
     */
    @GET
    @Path("/tickets/search")
    public List<TicketPrivateDTO> searchUserTickets(
            @QueryParam("validOnly") Boolean validOnly,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);
        String today = java.time.LocalDate.now().toString();

        // Filter by valid only
        if (validOnly != null && validOnly) {
            userTickets = userTickets.stream()
                    .filter(t -> {
                        String validFrom = t.getValidFromDate();
                        String validTo = t.getValidToDate();
                        return (validFrom == null || validFrom.compareTo(today) <= 0) &&
                               (validTo == null || validTo.compareTo(today) >= 0);
                    })
                    .collect(Collectors.toList());
        }

        // Filter by start date (ticket's validFromDate >= startDate)
        if (startDate != null && !startDate.trim().isEmpty()) {
            userTickets = userTickets.stream()
                    .filter(t -> t.getValidFromDate() != null && t.getValidFromDate().compareTo(startDate) >= 0)
                    .collect(Collectors.toList());
        }

        // Filter by end date (ticket's validToDate <= endDate)
        if (endDate != null && !endDate.trim().isEmpty()) {
            userTickets = userTickets.stream()
                    .filter(t -> t.getValidToDate() != null && t.getValidToDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        return PrivateDTOMapper.toTicketDTOList(userTickets);
    }

    /**
     * Get specific ticket details
     * Requires authentication and ticket ownership
     */
    @GET
    @Path("/tickets/{ticketId}")
    public TicketPrivateDTO getUserTicket(@PathParam("ticketId") int ticketId,
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

        return PrivateDTOMapper.toTicketDTO(ticket);
    }

    // ===== LOCATIONS =====

    /**
     * Get all locations with user's tickets for each location
     * Requires authentication
     */
    @GET
    @Path("/locations")
    public List<LocationPrivateDTO> getUserLocations(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<Location> locations = locationService.getAllLocations();
        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);

        return PrivateDTOMapper.toLocationDTOList(locations, userTickets);
    }

    /**
     * Get specific location with user's tickets for that location
     * Requires authentication
     */
    @GET
    @Path("/locations/{locationId}")
    public LocationPrivateDTO getUserLocation(@PathParam("locationId") int locationId,
                                             @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        Location location = locationService.getLocation(locationId);
        if (location == null) {
            throw new NotFoundException("Location not found with id: " + locationId);
        }

        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);
        return PrivateDTOMapper.toLocationDTO(location, userTickets);
    }

    // ===== REVIEWS =====

    /**
     * Get user's reviews
     * Requires authentication
     */
    @GET
    @Path("/reviews")
    public List<EventReviewDTO> getUserReviews(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<EventReview> reviews = eventReviewService.getReviewsByUser(userId);
        return PrivateDTOMapper.toReviewDTOList(reviews);
    }

    /**
     * Add review for event
     * Requires authentication
     */
    @POST
    @Path("/reviews")
    public EventReviewDTO addReview(EventReview review, @Context ContainerRequestContext requestContext) {
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

        try {
            EventReview newReview = eventReviewService.addReview(
                    eventId,
                    userId,
                    review.getRating(),
                    review.getReviewText(),
                    review.getReviewDate()
            );

            return PrivateDTOMapper.toReviewDTO(newReview);
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create review");
            return null; // Will not reach due to exception
        }
    }

    /**
     * Update user's review
     * Requires authentication and review ownership
     */
    @PUT
    @Path("/reviews/{reviewId}")
    public EventReviewDTO updateReview(@PathParam("reviewId") int reviewId,
                                   EventReview review,
                                   @Context ContainerRequestContext requestContext) {
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

        return PrivateDTOMapper.toReviewDTO(review);
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
    }
}



