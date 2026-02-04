package REST;

import dao.hibernate.*;
import dto.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import model.*;
import service.*;

import java.util.List;

/**
 * Admin API - Full access to all resources
 * Requires authentication and admin role
 * Returns DTO objects to avoid nested collection issues
 */
@Path("/admin")
@Produces("application/json")
@Consumes("application/json")
public class AdminResource {

    private final EventService eventService;
    private final LocationService locationService;
    private final RoomService roomService;
    private final TicketService ticketService;
    private final UserService userService;

    public AdminResource() {
        this.eventService = new EventService(new EventHibernate());
        this.locationService = new LocationService(new LocationHibernate());
        this.roomService = new RoomService(new RoomHibernate());
        this.ticketService = new TicketService(new TicketHibernate());
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
    public List<EventAdminDTO> getAllEvents(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        List<Event> events = eventService.getAllEvents();
        return AdminDTOMapper.toEventDTOList(events);
    }

    @GET
    @Path("/events/{id}")
    public EventAdminDTO getEvent(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event event = eventService.getEvent(id);
        if (event == null) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        return AdminDTOMapper.toEventDTO(event);
    }

    @POST
    @Path("/events")
    public EventAdminDTO createEvent(Event event, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new BadRequestException("Event name is required");
        }
        try {
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
            return AdminDTOMapper.toEventDTO(newEvent);
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create event");
            return null; // Will not reach due to exception
        }
    }

    @PUT
    @Path("/events/{id}")
    public EventAdminDTO updateEvent(@PathParam("id") int id, Event event,
                            @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event existingEvent = eventService.getEvent(id);
        if (existingEvent == null) {
            throw new NotFoundException("Event not found with id: " + id);
        }
        event.setId(id);
        eventService.updateEvent(event);
        return AdminDTOMapper.toEventDTO(event);
    }

    @DELETE
    @Path("/events/{id}")
    public void deleteEvent(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Event event = eventService.getEvent(id);
        if (event != null) {
            eventService.deleteEvent(event);
        }
    }

    // ===== LOCATION MANAGEMENT =====

    @GET
    @Path("/locations")
    public List<LocationAdminDTO> getAllLocations(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        List<Location> locations = locationService.getAllLocations();
        return AdminDTOMapper.toLocationDTOList(locations);
    }

    @GET
    @Path("/locations/{id}")
    public LocationAdminDTO getLocation(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Location location = locationService.getLocation(id);
        if (location == null) {
            throw new NotFoundException("Location not found with id: " + id);
        }
        return AdminDTOMapper.toLocationDTO(location);
    }

    @POST
    @Path("/locations")
    public LocationAdminDTO createLocation(Location location, @Context ContainerRequestContext requestContext) {
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
        try {
            Location newLocation = locationService.addLocation(location.getName(), location.getAddress());
            newLocation.setMaxAvailableSeats(location.getMaxAvailableSeats());
            locationService.updateLocation(newLocation);
            return AdminDTOMapper.toLocationDTO(newLocation);
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create location");
            return null; // Will not reach due to exception
        }
    }

    @PUT
    @Path("/locations/{id}")
    public LocationAdminDTO updateLocation(@PathParam("id") int id, Location location,
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
        return AdminDTOMapper.toLocationDTO(location);
    }

    @DELETE
    @Path("/locations/{id}")
    public void deleteLocation(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Location location = locationService.getLocation(id);
        if (location != null) {
            locationService.deleteLocation(location);
        }
    }

    // ===== ROOM MANAGEMENT =====

    @GET
    @Path("/rooms")
    public List<RoomAdminDTO> getAllRooms(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        List<Room> rooms = roomService.getAllRooms();
        return AdminDTOMapper.toRoomDTOList(rooms);
    }

    @GET
    @Path("/rooms/{id}")
    public RoomAdminDTO getRoom(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Room room = roomService.getRoom(id);
        if (room == null) {
            throw new NotFoundException("Room not found with id: " + id);
        }
        return AdminDTOMapper.toRoomDTO(room);
    }

    @POST
    @Path("/rooms")
    public RoomAdminDTO createRoom(Room room, @Context ContainerRequestContext requestContext) {
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

        try {
            Room newRoom = roomService.addRoom(room.getName(), room.getDescription());
            newRoom.setLocation(room.getLocation());
            newRoom.setSeatCapacity(room.getSeatCapacity());
            roomService.updateRoom(newRoom);
            return AdminDTOMapper.toRoomDTO(newRoom);
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create room");
            return null; // Will not reach due to exception
        }
    }

    @PUT
    @Path("/rooms/{id}")
    public RoomAdminDTO updateRoom(@PathParam("id") int id, Room room,
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
        return AdminDTOMapper.toRoomDTO(room);
    }

    @DELETE
    @Path("/rooms/{id}")
    public void deleteRoom(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        Room room = roomService.getRoom(id);
        if (room != null) {
            roomService.deleteRoom(room);
        }
    }

    // ===== USER MANAGEMENT =====

    @GET
    @Path("/users")
    public List<UserAdminDTO> getAllUsers(@Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        List<User> users = userService.getAllUsers();
        return AdminDTOMapper.toUserDTOList(users);
    }

    @GET
    @Path("/users/{id}")
    public UserAdminDTO getUser(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + id);
        }
        return AdminDTOMapper.toUserDTO(user);
    }

    @POST
    @Path("/users")
    public UserAdminDTO createUser(User user, @Context ContainerRequestContext requestContext) {
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
            return AdminDTOMapper.toUserDTO(newUser);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create user");
            return null; // Will not reach due to exception
        }
    }

    /**
     * Create new admin user with password
     * POST /admin/users/create-admin
     */
    @POST
    @Path("/users/create-admin")
    public UserAdminDTO createAdminUser(User user, @Context ContainerRequestContext requestContext) {
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
            return AdminDTOMapper.toUserDTO(newAdmin);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            SQLErrorHandler.handleSQLException(e, "create admin user");
            return null; // Will not reach due to exception
        }
    }

    @PUT
    @Path("/users/{id}")
    public UserAdminDTO updateUser(@PathParam("id") int id, User user,
                          @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("User not found with id: " + id);
        }

        user.setId(id);
        userService.updateUser(user);
        return AdminDTOMapper.toUserDTO(user);
    }

    @DELETE
    @Path("/users/{id}")
    public void deleteUser(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
        verifyAdminAccess(requestContext);
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(user);
        }
    }

    // ...existing code...
}


