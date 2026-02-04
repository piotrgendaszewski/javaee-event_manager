package REST;

import dao.hibernate.EventHibernate;
import dao.hibernate.UserHibernate;
import dto.EventPublicDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Event;
import model.User;
import service.AuthService;
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
    private final AuthService authService;

    public PublicResource() {
        this.eventService = new EventService(new EventHibernate());
        this.ticketService = new TicketService(new dao.hibernate.TicketHibernate());
        this.authService = new AuthService(new UserHibernate());
    }

    // ===== EVENTS ENDPOINTS =====

    /**
     * Get all available events - PUBLIC endpoint
     * GET /public/events
     * Returns EventPublicDTO with essential data only (no full entity graph)
     */
    @GET
    @Path("/events")
    public List<EventPublicDTO> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(event -> {
                    Map<String, Integer> remaining = ticketService.getRemainingTicketsByEvent(event.getId());
                    double avgRating = 0.0; // Average rating calculation removed with EventReview
                    return new EventPublicDTO(event, remaining, avgRating);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get events with optional filters
     * GET /public/events/search
     * Returns EventPublicDTO with essential data only
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
    public List<EventPublicDTO> searchEvents(
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

        // Convert to EventPublicDTO
        return events.stream()
                .map(event -> {
                    Map<String, Integer> remaining = ticketService.getRemainingTicketsByEvent(event.getId());
                    double avgRating = 0.0; // Average rating calculation removed with EventReview
                    return new EventPublicDTO(event, remaining, avgRating);
                })
                .collect(Collectors.toList());
    }

    /**
     * Get event by ID - PUBLIC endpoint
     * GET /public/events/{id}
     * Returns EventPublicDTO with essential data only
     */
    @GET
    @Path("/events/{id}")
    public EventPublicDTO getEvent(@PathParam("id") int id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        Map<String, Integer> remaining = ticketService.getRemainingTicketsByEvent(id);
        double avgRating = 0.0; // Average rating calculation removed with EventReview
        return new EventPublicDTO(event, remaining, avgRating);
    }

    /**
     * Get event by name - PUBLIC endpoint
     * GET /public/events/name/{name}
     * Returns EventPublicDTO with essential data only
     */
    @GET
    @Path("/events/name/{name}")
    public EventPublicDTO getEventByName(@PathParam("name") String name) {
        Event event = eventService.getEventByName(name);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        Map<String, Integer> remaining = ticketService.getRemainingTicketsByEvent(event.getId());
        double avgRating = 0.0; // Average rating calculation removed with EventReview
        return new EventPublicDTO(event, remaining, avgRating);
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
        } catch (WebApplicationException e) {
            // Re-throw WebApplicationException from SQLErrorHandler
            throw e;
        } catch (Exception e) {
            // Try to handle as SQL error
            try {
                SQLErrorHandler.handleSQLException(e, "register user");
            } catch (WebApplicationException sqlEx) {
                throw sqlEx;
            }
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

