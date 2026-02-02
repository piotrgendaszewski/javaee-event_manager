package REST;

import dao.hibernate.EventHibernate;
import dao.hibernate.EventReviewHibernate;
import dao.hibernate.UserHibernate;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Event;
import model.EventReview;
import model.User;
import service.AuthService;
import service.EventReviewService;
import service.EventService;
import service.TicketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Public API for unauthenticated users
 * Allows viewing events and user registration only
 * No authentication required
 */
@Path("/public")
@Produces("application/json")
@Consumes("application/json")
public class PublicResource {

    private final EventService eventService;
    private final TicketService ticketService;
    private final EventReviewService eventReviewService;
    private final AuthService authService;

    public PublicResource() {
        this.eventService = new EventService(new EventHibernate());
        this.ticketService = new TicketService(new dao.hibernate.TicketHibernate());
        this.eventReviewService = new EventReviewService(new EventReviewHibernate());
        this.authService = new AuthService(new UserHibernate());
    }

    // ===== EVENTS ENDPOINTS =====

    /**
     * Get all available events - PUBLIC endpoint
     * GET /public/events
     */
    @GET
    @Path("/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Get events with optional filters
     * GET /public/events/search
     * @param eventName filter by event name (fragment)
     * @param locationName filter by location name (fragment)
     * @param startDate filter by event start date (YYYY-MM-DD format)
     * @param endDate filter by event end date (YYYY-MM-DD format)
     * @param minPrice filter by minimum ticket price
     * @param maxPrice filter by maximum ticket price
     * @param onlyAvailable filter only events with available tickets
     */
    @GET
    @Path("/events/search")
    public List<Event> searchEvents(
            @QueryParam("eventName") String eventName,
            @QueryParam("locationName") String locationName,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("minPrice") Double minPrice,
            @QueryParam("maxPrice") Double maxPrice,
            @QueryParam("onlyAvailable") Boolean onlyAvailable) {

        List<Event> events = eventService.getAllEvents();

        // Filter by event name (fragment match)
        if (eventName != null && !eventName.trim().isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getName().toLowerCase().contains(eventName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by location name (fragment match)
        if (locationName != null && !locationName.trim().isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getLocations() != null && e.getLocations().stream()
                            .anyMatch(loc -> loc.getName().toLowerCase().contains(locationName.toLowerCase())))
                    .collect(Collectors.toList());
        }

        // Filter by event start date (on or after)
        if (startDate != null && !startDate.trim().isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getEventStartDate() != null && e.getEventStartDate().compareTo(startDate) >= 0)
                    .collect(Collectors.toList());
        }

        // Filter by event end date (on or before)
        if (endDate != null && !endDate.trim().isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getEventEndDate() != null && e.getEventEndDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        // Filter by price range
        if (minPrice != null || maxPrice != null) {
            events = events.stream()
                    .filter(e -> {
                        if (e.getTicketPrices() == null || e.getTicketPrices().isEmpty()) {
                            return true;
                        }
                        double minEventPrice = e.getTicketPrices().values().stream()
                                .mapToDouble(Double::doubleValue).min().orElse(0);
                        double maxEventPrice = e.getTicketPrices().values().stream()
                                .mapToDouble(Double::doubleValue).max().orElse(Double.MAX_VALUE);

                        boolean minCheck = minPrice == null || minEventPrice >= minPrice;
                        boolean maxCheck = maxPrice == null || maxEventPrice <= maxPrice;
                        return minCheck && maxCheck;
                    })
                    .collect(Collectors.toList());
        }

        // Filter by ticket availability
        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(e -> {
                        Map<String, Integer> remaining = ticketService.getRemainingTicketsByEvent(e.getId());
                        return !remaining.isEmpty() && remaining.values().stream().anyMatch(count -> count > 0);
                    })
                    .collect(Collectors.toList());
        }

        return events;
    }

    /**
     * Get event by ID - PUBLIC endpoint
     * GET /public/events/{id}
     */
    @GET
    @Path("/events/{id}")
    public Event getEvent(@PathParam("id") int id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        return event;
    }

    /**
     * Get event by name - PUBLIC endpoint
     * GET /public/events/name/{name}
     */
    @GET
    @Path("/events/name/{name}")
    public Event getEventByName(@PathParam("name") String name) {
        Event event = eventService.getEventByName(name);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        return event;
    }

    /**
     * Get average rating for event - PUBLIC endpoint
     * GET /public/events/{eventId}/average-rating
     */
    @GET
    @Path("/events/{eventId}/average-rating")
    public double getAverageRatingForEvent(@PathParam("eventId") int eventId) {
        return eventService.getAverageRatingForEvent(eventId);
    }

    /**
     * Get remaining tickets count for event - PUBLIC endpoint
     * GET /public/events/{eventId}/tickets/remaining
     */
    @GET
    @Path("/events/{eventId}/tickets/remaining")
    public Map<String, Integer> getRemainingTickets(@PathParam("eventId") int eventId) {
        return ticketService.getRemainingTicketsByEvent(eventId);
    }

    // ===== REVIEWS ENDPOINTS =====

    /**
     * Get all reviews for event - PUBLIC endpoint
     * GET /public/events/{eventId}/reviews
     */
    @GET
    @Path("/events/{eventId}/reviews")
    public List<EventReview> getEventReviews(@PathParam("eventId") int eventId) {
        return eventReviewService.getReviewsByEvent(eventId);
    }

    /**
     * Get reviews for event with filters and sorting
     * GET /public/events/{eventId}/reviews/search
     * @param startDate filter reviews from this date (YYYY-MM-DD)
     * @param endDate filter reviews until this date (YYYY-MM-DD)
     * @param minRating filter reviews with minimum rating (1-5)
     * @param maxRating filter reviews with maximum rating (1-5)
     * @param sortBy sort by: "date" (default) or "rating"
     * @param sortOrder sort order: "asc" (ascending) or "desc" (descending, default)
     */
    @GET
    @Path("/events/{eventId}/reviews/search")
    public List<EventReview> searchEventReviews(
            @PathParam("eventId") int eventId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("minRating") Integer minRating,
            @QueryParam("maxRating") Integer maxRating,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortOrder") String sortOrder) {

        List<EventReview> reviews = eventReviewService.getReviewsByEvent(eventId);

        // Filter by review date range
        if (startDate != null && !startDate.trim().isEmpty()) {
            reviews = reviews.stream()
                    .filter(r -> r.getReviewDate() != null && r.getReviewDate().compareTo(startDate) >= 0)
                    .collect(Collectors.toList());
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            reviews = reviews.stream()
                    .filter(r -> r.getReviewDate() != null && r.getReviewDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        // Filter by rating range
        if (minRating != null) {
            reviews = reviews.stream()
                    .filter(r -> r.getRating() >= minRating)
                    .collect(Collectors.toList());
        }

        if (maxRating != null) {
            reviews = reviews.stream()
                    .filter(r -> r.getRating() <= maxRating)
                    .collect(Collectors.toList());
        }

        // Sort results
        String sort = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy.toLowerCase() : "date";
        String order = (sortOrder != null && !sortOrder.trim().isEmpty()) ? sortOrder.toLowerCase() : "desc";

        if ("rating".equals(sort)) {
            if ("asc".equals(order)) {
                reviews.sort((r1, r2) -> Integer.compare(r1.getRating(), r2.getRating()));
            } else {
                reviews.sort((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()));
            }
        } else {
            // Default sort by date
            if ("asc".equals(order)) {
                reviews.sort((r1, r2) -> {
                    String d1 = r1.getReviewDate() != null ? r1.getReviewDate() : "";
                    String d2 = r2.getReviewDate() != null ? r2.getReviewDate() : "";
                    return d1.compareTo(d2);
                });
            } else {
                reviews.sort((r1, r2) -> {
                    String d1 = r1.getReviewDate() != null ? r1.getReviewDate() : "";
                    String d2 = r2.getReviewDate() != null ? r2.getReviewDate() : "";
                    return d2.compareTo(d1);
                });
            }
        }

        return reviews;
    }

    // ===== REGISTRATION ENDPOINT =====

    /**
     * User registration endpoint - PUBLIC
     * POST /public/register
     * No authentication required
     * Automatically sets isAdmin=false for new users
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        try {
            if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Login is required"))
                        .build();
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Email is required"))
                        .build();
            }
            if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Password is required"))
                        .build();
            }

            // Set isAdmin to false for regular users
            user.setAdmin(false);

            authService.registerUser(
                    user.getLogin(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress(),
                    user.getPhoneNumber()
            );
            authService.commit();

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("login", user.getLogin());

            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            authService.rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse("Registration failed: " + e.getMessage()))
                    .build();
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}

