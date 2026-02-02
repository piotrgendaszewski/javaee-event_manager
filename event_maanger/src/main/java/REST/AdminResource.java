package REST;

import dao.hibernate.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import model.*;
import service.*;

import java.util.List;

/**
 * Admin API - Full access to all resources
 * Requires authentication and admin role
 */
@Path("/admin")
@Produces("application/json")
@Consumes("application/json")
public class AdminResource {

    private final EventService eventService;
    private final LocationService locationService;
    private final RoomService roomService;
    private final TicketService ticketService;
    private final EventReviewService eventReviewService;
    private final UserService userService;

    public AdminResource() {
        this.eventService = new EventService(new EventHibernate());
        this.locationService = new LocationService(new LocationHibernate());
        this.roomService = new RoomService(new RoomHibernate());
        this.ticketService = new TicketService(new TicketHibernate());
        this.eventReviewService = new EventReviewService(new EventReviewHibernate());
        this.userService = new UserService(new UserHibernate());
    }

    /**
     * Verify admin access
     */
    private void verifyAdminAccess(ContainerRequestContext requestContext) {
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        if (isAdmin == null || !isAdmin) {
            throw new ForbiddenException("Admin access required");
        }
    }

    // ===== EVENT MANAGEMENT =====

    @GET
    @Path("/events")
    public List<Event> getAllEvents(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return eventService.getAllEvents();
    }

    @GET
    @Path("/events/{id}")
    public Event getEvent(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event event = eventService.getEvent(id);
        if (event == null) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        return event;
    }

    @POST
    @Path("/events")
    public Event createEvent(Event event, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new BadRequestException("Event name is required");
        }
        Event newEvent = eventService.addEvent(
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventTime(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.isNumberedSeats()
        );
        if (event.getTicketPrices() != null) {
            for (java.util.Map.Entry<String, Double> entry : event.getTicketPrices().entrySet()) {
                eventService.setTicketPrice(newEvent.getId(), entry.getKey(), entry.getValue());
            }
        }
        if (event.getTicketQuantities() != null) {
            for (java.util.Map.Entry<String, Integer> entry : event.getTicketQuantities().entrySet()) {
                eventService.setTicketQuantity(newEvent.getId(), entry.getKey(), entry.getValue());
            }
        }
        eventService.commit();
        return newEvent;
    }

    @PUT
    @Path("/events/{id}")
    public Event updateEvent(@PathParam("id") int id, Event event,
                            @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event existingEvent = eventService.getEvent(id);
        if (existingEvent == null) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        event.setId(id);
        eventService.updateEvent(event);
        eventService.commit();
        return event;
    }

    @DELETE
    @Path("/events/{id}")
    public void deleteEvent(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event event = eventService.getEvent(id);
        if (event != null) {
            eventService.deleteEvent(event);
            eventService.commit();
        }
    }

    // ===== LOCATION MANAGEMENT =====

    @GET
    @Path("/locations")
    public List<Location> getAllLocations(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return locationService.getAllLocations();
    }

    @GET
    @Path("/locations/{id}")
    public Location getLocation(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Location location = locationService.getLocation(id);
        if (location == null) {
            throw new NotFoundException("Location not found with id: " + id);
        }
        return location;
    }

    @POST
    @Path("/locations")
    public Location createLocation(Location location, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (location.getName() == null || location.getName().trim().isEmpty()) {
            throw new BadRequestException("Location name is required");
        }
        if (location.getAddress() == null || location.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Location address is required");
        }
        if (location.getMaxAvailableSeats() <= 0) {
            throw new BadRequestException("Location max available seats must be greater than 0");
        }
        Location newLocation = locationService.addLocation(location.getName(), location.getAddress());
        newLocation.setMaxAvailableSeats(location.getMaxAvailableSeats());
        locationService.updateLocation(newLocation);
        locationService.commit();
        return newLocation;
    }

    @PUT
    @Path("/locations/{id}")
    public Location updateLocation(@PathParam("id") int id, Location location,
                                  @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Location existingLocation = locationService.getLocation(id);
        if (existingLocation == null) {
            throw new NotFoundException("Location not found with id: " + id);
        }
        if (location.getName() == null || location.getName().trim().isEmpty()) {
            throw new BadRequestException("Location name is required");
        }
        if (location.getAddress() == null || location.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Location address is required");
        }
        location.setId(id);
        locationService.updateLocation(location);
        locationService.commit();
        return location;
    }

    @DELETE
    @Path("/locations/{id}")
    public void deleteLocation(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Location location = locationService.getLocation(id);
        if (location != null) {
            locationService.deleteLocation(location);
            locationService.commit();
        }
    }

    // ===== ROOM MANAGEMENT =====

    @GET
    @Path("/rooms")
    public List<Room> getAllRooms(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return roomService.getAllRooms();
    }

    @GET
    @Path("/rooms/{id}")
    public Room getRoom(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Room room = roomService.getRoom(id);
        if (room == null) {
            throw new NotFoundException("Room not found with id: " + id);
        }
        return room;
    }

    @POST
    @Path("/rooms")
    public Room createRoom(Room room, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);

        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new BadRequestException("Room name is required");
        }

        if (room.getSeatCapacity() <= 0) {
            throw new BadRequestException("Room seat capacity must be greater than 0");
        }

        if (room.getLocation() != null) {
            int locationId = room.getLocation().getId();
            int totalCapacity = roomService.getTotalCapacityByLocation(locationId);

            if (!room.canBeAddedToLocation(totalCapacity)) {
                throw new BadRequestException(
                    "Room capacity would exceed location's max capacity"
                );
            }
        }

        Room newRoom = roomService.addRoom(room.getName(), room.getDescription());
        newRoom.setLocation(room.getLocation());
        newRoom.setSeatCapacity(room.getSeatCapacity());
        roomService.updateRoom(newRoom);
        roomService.commit();
        return newRoom;
    }

    @PUT
    @Path("/rooms/{id}")
    public Room updateRoom(@PathParam("id") int id, Room room,
                          @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Room existingRoom = roomService.getRoom(id);
        if (existingRoom == null) {
            throw new NotFoundException("Room not found with id: " + id);
        }

        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new BadRequestException("Room name is required");
        }

        if (room.getSeatCapacity() <= 0) {
            throw new BadRequestException("Room seat capacity must be greater than 0");
        }

        room.setId(id);
        roomService.updateRoom(room);
        roomService.commit();
        return room;
    }

    @DELETE
    @Path("/rooms/{id}")
    public void deleteRoom(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Room room = roomService.getRoom(id);
        if (room != null) {
            roomService.deleteRoom(room);
            roomService.commit();
        }
    }

    // ===== TICKET MANAGEMENT =====

    @GET
    @Path("/tickets")
    public List<Ticket> getAllTickets(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return ticketService.getAllTickets();
    }

    @GET
    @Path("/tickets/{id}")
    public Ticket getTicket(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Ticket ticket = ticketService.getTicket(id);
        if (ticket == null) {
            throw new NotFoundException("Ticket not found with id: " + id);
        }
        return ticket;
    }

    @GET
    @Path("/tickets/event/{eventId}")
    public List<Ticket> getTicketsByEvent(@PathParam("eventId") int eventId,
                                         @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return ticketService.getTicketsByEvent(eventId);
    }

    @GET
    @Path("/tickets/user/{userId}")
    public List<Ticket> getTicketsByUser(@PathParam("userId") int userId,
                                        @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return ticketService.getTicketsByUser(userId);
    }

    @POST
    @Path("/tickets")
    public Ticket createTicket(Ticket ticket, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (ticket.getEvent() == null || ticket.getUser() == null) {
            throw new BadRequestException("Event and User are required for ticket creation");
        }
        if (ticket.getTicketType() == null || ticket.getTicketType().trim().isEmpty()) {
            throw new BadRequestException("Ticket type is required");
        }
        if (ticket.getPrice() < 0) {
            throw new BadRequestException("Ticket price cannot be negative");
        }

        Ticket newTicket = ticketService.addTicket(
                ticket.getEvent().getId(),
                ticket.getUser().getId(),
                ticket.getTicketType(),
                ticket.getPrice(),
                ticket.getPurchaseDate(),
                ticket.getValidFromDate(),
                ticket.getValidToDate()
        );
        ticketService.commit();
        return newTicket;
    }

    @PUT
    @Path("/tickets/{id}")
    public Ticket updateTicket(@PathParam("id") int id, Ticket ticket,
                              @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Ticket existingTicket = ticketService.getTicket(id);
        if (existingTicket == null) {
            throw new NotFoundException("Ticket not found with id: " + id);
        }
        ticket.setId(id);
        ticketService.updateTicket(ticket);
        ticketService.commit();
        return ticket;
    }

    @DELETE
    @Path("/tickets/{id}")
    public void deleteTicket(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Ticket ticket = ticketService.getTicket(id);
        if (ticket != null) {
            ticketService.deleteTicket(ticket);
            ticketService.commit();
        }
    }

    // ===== REVIEW MANAGEMENT =====

    @GET
    @Path("/reviews")
    public List<EventReview> getAllReviews(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return eventReviewService.getAllReviews();
    }

    @GET
    @Path("/reviews/{id}")
    public EventReview getReview(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        EventReview review = eventReviewService.getReview(id);
        if (review == null) {
            throw new NotFoundException("Review not found with id: " + id);
        }
        return review;
    }

    @GET
    @Path("/reviews/event/{eventId}")
    public List<EventReview> getReviewsByEvent(@PathParam("eventId") int eventId,
                                              @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return eventReviewService.getReviewsByEvent(eventId);
    }

    @GET
    @Path("/reviews/user/{userId}")
    public List<EventReview> getReviewsByUser(@PathParam("userId") int userId,
                                             @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return eventReviewService.getReviewsByUser(userId);
    }

    @POST
    @Path("/reviews")
    public EventReview createReview(EventReview review, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (review.getEvent() == null || review.getUser() == null) {
            throw new BadRequestException("Event and User are required for review creation");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }

        int eventId = review.getEvent().getId();
        int userId = review.getUser().getId();

        if (eventReviewService.hasUserReviewedEvent(eventId, userId)) {
            throw new BadRequestException("User has already reviewed this event");
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
    @Path("/reviews/{id}")
    public EventReview updateReview(@PathParam("id") int id, EventReview review,
                                   @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        EventReview existingReview = eventReviewService.getReview(id);
        if (existingReview == null) {
            throw new NotFoundException("Review not found with id: " + id);
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }

        review.setId(id);
        eventReviewService.updateReview(review);
        eventReviewService.commit();
        return review;
    }

    @DELETE
    @Path("/reviews/{id}")
    public void deleteReview(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        EventReview review = eventReviewService.getReview(id);
        if (review != null) {
            eventReviewService.deleteReview(review);
            eventReviewService.commit();
        }
    }

    // ===== USER MANAGEMENT =====

    @GET
    @Path("/users")
    public List<User> getAllUsers(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        return userService.getAllUsers();
    }

    @GET
    @Path("/users/{id}")
    public User getUser(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return user;
    }

    @POST
    @Path("/users")
    public User createUser(User user, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new BadRequestException("User login is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new BadRequestException("User email is required");
        }

        try {
            User newUser = userService.addUser(user.getLogin(), user.getEmail());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setAddress(user.getAddress());
            newUser.setPhoneNumber(user.getPhoneNumber());
            newUser.setAdmin(user.isAdmin());
            userService.updateUser(newUser);
            userService.commit();
            return newUser;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Create new admin user with password
     * POST /admin/users/create-admin
     * Accepts User object and automatically sets isAdmin=true
     */
    @POST
    @Path("/users/create-admin")
    public User createAdminUser(User user, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);

        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new BadRequestException("Admin login is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Admin email is required");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new BadRequestException("Admin password is required");
        }

        try {
            User newAdmin = userService.createAdminUser(
                    user.getLogin(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getFirstName(),
                    user.getLastName()
            );
            userService.commit();
            return newAdmin;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PUT
    @Path("/users/{id}")
    public User updateUser(@PathParam("id") int id, User user,
                          @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("User not found with id: " + id);
        }

        user.setId(id);
        userService.updateUser(user);
        userService.commit();
        return user;
    }

    @DELETE
    @Path("/users/{id}")
    public void deleteUser(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(user);
            userService.commit();
        }
    }
}

