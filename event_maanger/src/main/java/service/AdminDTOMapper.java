package service;

import dto.EventAdminDTO;
import dto.LocationAdminDTO;
import dto.RoomAdminDTO;
import dto.UserAdminDTO;
import model.Event;
import model.Location;
import model.Room;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper do konwersji model entities na DTO dla admin panelu
 * Zapobiega zaciąganiu zagnieżdżonych kolekcji
 */
public class AdminDTOMapper {

    public static EventAdminDTO toEventDTO(Event event) {
        if (event == null) return null;

        return new EventAdminDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventTime(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.isNumberedSeats(),
                event.getTicketPrices(),
                event.getTicketQuantities()
        );
    }

    public static LocationAdminDTO toLocationDTO(Location location) {
        if (location == null) return null;

        return new LocationAdminDTO(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getMaxAvailableSeats()
        );
    }

    public static RoomAdminDTO toRoomDTO(Room room) {
        if (room == null) return null;

        String locationName = (room.getLocation() != null) ? room.getLocation().getName() : null;
        int locationId = (room.getLocation() != null) ? room.getLocation().getId() : 0;

        return new RoomAdminDTO(
                room.getId(),
                room.getName(),
                room.getDescription(),
                locationId,
                locationName,
                room.getSeatCapacity()
        );
    }

    public static UserAdminDTO toUserDTO(User user) {
        if (user == null) return null;

        return new UserAdminDTO(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.isAdmin()
        );
    }

    public static List<EventAdminDTO> toEventDTOList(List<Event> events) {
        return events.stream()
                .map(AdminDTOMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    public static List<LocationAdminDTO> toLocationDTOList(List<Location> locations) {
        return locations.stream()
                .map(AdminDTOMapper::toLocationDTO)
                .collect(Collectors.toList());
    }

    public static List<RoomAdminDTO> toRoomDTOList(List<Room> rooms) {
        return rooms.stream()
                .map(AdminDTOMapper::toRoomDTO)
                .collect(Collectors.toList());
    }

    public static List<UserAdminDTO> toUserDTOList(List<User> users) {
        return users.stream()
                .map(AdminDTOMapper::toUserDTO)
                .collect(Collectors.toList());
    }
}

