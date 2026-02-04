package service;

import dto.EventAdminDTO;
import dto.LocationAdminDTO;
import dto.RoomAdminDTO;
import dto.UserAdminDTO;
import model.Event;
import model.Location;
import model.Room;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdminDTOMapper
 */
class AdminDTOMapperTest {

    @Test
    void testToEventDTOSuccess() {
        Event event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(1);
        Map<String, Double> prices = new HashMap<>();
        prices.put("Standard", 50.0);
        event.setTicketPrices(prices);

        EventAdminDTO result = AdminDTOMapper.toEventDTO(event);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Concert", result.getName());
        assertTrue(result.isNumberedSeats());
    }

    @Test
    void testToEventDTONull() {
        EventAdminDTO result = AdminDTOMapper.toEventDTO(null);

        assertNull(result);
    }

    @Test
    void testToLocationDTOSuccess() {
        Location location = new Location(1, "Pałac Kultury", "pl. Defilad 1, Warszawa");
        location.setMaxAvailableSeats(5000);

        LocationAdminDTO result = AdminDTOMapper.toLocationDTO(location);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Pałac Kultury", result.getName());
        assertEquals(5000, result.getMaxAvailableSeats());
    }

    @Test
    void testToLocationDTONull() {
        LocationAdminDTO result = AdminDTOMapper.toLocationDTO(null);

        assertNull(result);
    }

    @Test
    void testToRoomDTOSuccess() {
        Room room = new Room(1, "Main Hall", "Large hall");
        room.setSeatCapacity(1000);

        RoomAdminDTO result = AdminDTOMapper.toRoomDTO(room);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Main Hall", result.getName());
        assertEquals(1000, result.getSeatCapacity());
    }

    @Test
    void testToRoomDTONull() {
        RoomAdminDTO result = AdminDTOMapper.toRoomDTO(null);

        assertNull(result);
    }

    @Test
    void testToUserDTOSuccess() {
        User user = new User("admin_user", "admin@example.com");
        user.setId(1);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setAdmin(true);

        UserAdminDTO result = AdminDTOMapper.toUserDTO(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("admin_user", result.getLogin());
        assertTrue(result.isAdmin());
    }

    @Test
    void testToUserDTONull() {
        UserAdminDTO result = AdminDTOMapper.toUserDTO(null);

        assertNull(result);
    }

    @Test
    void testToEventDTOListSuccess() {
        Event event1 = new Event("Event1", "Desc1", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", true);
        Event event2 = new Event("Event2", "Desc2", "2026-06-01", "20:00", "2026-06-01", "2026-06-01", false);
        List<Event> events = Arrays.asList(event1, event2);

        List<EventAdminDTO> result = AdminDTOMapper.toEventDTOList(events);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToLocationDTOListSuccess() {
        Location loc1 = new Location(1, "Location1", "Address1");
        Location loc2 = new Location(2, "Location2", "Address2");
        List<Location> locations = Arrays.asList(loc1, loc2);

        List<LocationAdminDTO> result = AdminDTOMapper.toLocationDTOList(locations);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToRoomDTOListSuccess() {
        Room room1 = new Room(1, "Room1", "Desc1");
        Room room2 = new Room(2, "Room2", "Desc2");
        List<Room> rooms = Arrays.asList(room1, room2);

        List<RoomAdminDTO> result = AdminDTOMapper.toRoomDTOList(rooms);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToUserDTOListSuccess() {
        User user1 = new User("user1", "user1@example.com");
        User user2 = new User("user2", "user2@example.com");
        List<User> users = Arrays.asList(user1, user2);

        List<UserAdminDTO> result = AdminDTOMapper.toUserDTOList(users);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToEventDTOListEmpty() {
        List<EventAdminDTO> result = AdminDTOMapper.toEventDTOList(Arrays.asList());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testDataIntegrityAfterMapping() {
        User originalUser = new User("testuser", "test@example.com");
        originalUser.setId(5);
        originalUser.setFirstName("Test");
        originalUser.setLastName("User");

        UserAdminDTO dto = AdminDTOMapper.toUserDTO(originalUser);

        assertEquals(originalUser.getId(), dto.getId());
        assertEquals(originalUser.getLogin(), dto.getLogin());
        assertEquals(originalUser.getFirstName(), dto.getFirstName());
    }
}

