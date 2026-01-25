package REST;

import dao.hibernate.*;
import io.jsonwebtoken.Claims;
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

    @POST
    @Path("/events")
    public Event createEvent(Event event, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event newEvent = eventService.addEvent(
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventTime(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.isNumberedSeats()
        );
        eventService.commit();
        return newEvent;
    }

    @PUT
    @Path("/events/{id}")
    public Event updateEvent(@PathParam("id") int id, Event event,
                            @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
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

    @POST
    @Path("/locations")
    public Location createLocation(Location location, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
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

    @POST
    @Path("/rooms")
    public Room createRoom(Room room, @Context ContainerRequestContext requestContext) throws Exception {
        verifyAdminAccess(requestContext);

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
                          @Context ContainerRequestContext requestContext) throws Exception {
        verifyAdminAccess(requestContext);
        room.setId(id);

        if (room.getSeatCapacity() <= 0) {
            throw new BadRequestException("Room seat capacity must be greater than 0");
        }

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
}

