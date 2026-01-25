package REST;

import dao.hibernate.RoomHibernate;
import jakarta.ws.rs.*;
import model.Room;
import service.RoomService;

import java.util.List;

@Path("/rooms")
@Produces("application/json")
@Consumes("application/json")
public class RoomResource {

    private final RoomService roomService;

    public RoomResource() {
        this.roomService = new RoomService(new RoomHibernate());
    }

    @GET
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GET
    @Path("/{id}")
    public Room getRoom(@PathParam("id") int id) {
        return roomService.getRoom(id);
    }

    @GET
    @Path("/location/{locationId}")
    public List<Room> getRoomsByLocation(@PathParam("locationId") int locationId) {
        return roomService.getRoomsByLocation(locationId);
    }

    @GET
    @Path("/location/{locationId}/total-capacity")
    public int getTotalCapacityByLocation(@PathParam("locationId") int locationId) {
        return roomService.getTotalCapacityByLocation(locationId);
    }

    @POST
    public Room addRoom(Room room) throws Exception {
        // Validate room
        if (room.getSeatCapacity() <= 0) {
            throw new IllegalArgumentException("Room seat capacity must be greater than 0");
        }

        // Validate against location capacity
        if (room.getLocation() != null) {
            int locationId = room.getLocation().getId();
            int totalCapacity = roomService.getTotalCapacityByLocation(locationId);

            if (!room.canBeAddedToLocation(totalCapacity)) {
                throw new IllegalArgumentException(
                    "Adding room with " + room.getSeatCapacity() + " seats would exceed location's max capacity. " +
                    "Current total: " + totalCapacity + " seats, Location max: " + room.getLocation().getMaxAvailableSeats() + " seats"
                );
            }
        }

        Room newRoom = roomService.addRoom(room.getName(), room.getDescription());
        if (room.getLocation() != null) {
            newRoom.setLocation(room.getLocation());
        }
        newRoom.setSeatCapacity(room.getSeatCapacity());
        roomService.updateRoom(newRoom);
        roomService.commit();
        return newRoom;
    }

    @PUT
    @Path("/{id}")
    public Room updateRoom(@PathParam("id") int id, Room room) throws Exception {
        room.setId(id);

        // Validate room
        if (room.getSeatCapacity() <= 0) {
            throw new IllegalArgumentException("Room seat capacity must be greater than 0");
        }

        // Validate against location capacity (excluding current room)
        if (room.getLocation() != null) {
            int locationId = room.getLocation().getId();
            int totalCapacity = roomService.getTotalCapacityByLocation(locationId);

            // Get current room to subtract its capacity from total
            Room existingRoom = roomService.getRoom(id);
            if (existingRoom != null && existingRoom.getLocation() != null &&
                existingRoom.getLocation().getId() == locationId) {
                totalCapacity -= existingRoom.getSeatCapacity();
            }

            if (!room.canBeAddedToLocation(totalCapacity)) {
                throw new IllegalArgumentException(
                    "Updating room to " + room.getSeatCapacity() + " seats would exceed location's max capacity. " +
                    "Current total (excluding this room): " + totalCapacity + " seats, Location max: " +
                    room.getLocation().getMaxAvailableSeats() + " seats"
                );
            }
        }

        roomService.updateRoom(room);
        roomService.commit();
        return room;
    }

    @DELETE
    @Path("/{id}")
    public void deleteRoom(@PathParam("id") int id) {
        Room room = roomService.getRoom(id);
        if (room != null) {
            roomService.deleteRoom(room);
            roomService.commit();
        }
    }
}

