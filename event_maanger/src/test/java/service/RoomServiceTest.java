package service;

import dao.RoomDAO;
import model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomService
 */
class RoomServiceTest {

    private RoomService roomService;
    private TestRoomDAO testRoomDAO;

    @BeforeEach
    void setUp() {
        testRoomDAO = new TestRoomDAO();
        roomService = new RoomService(testRoomDAO);
    }

    @Test
    void testAddRoom() {
        Room result = roomService.addRoom("Main Hall", "Large hall");

        assertNotNull(result);
        assertEquals("Main Hall", result.getName());
    }

    @Test
    void testGetRoomById() {
        Room room = roomService.addRoom("Room1", "Description1");
        Room result = roomService.getRoom(room.getId());

        assertNotNull(result);
        assertEquals("Room1", result.getName());
    }

    @Test
    void testUpdateRoom() {
        Room room = roomService.addRoom("Room", "Desc");
        room.setName("Updated");

        assertDoesNotThrow(() -> roomService.updateRoom(room));
    }

    @Test
    void testDeleteRoom() {
        Room room = roomService.addRoom("Room", "Desc");

        assertDoesNotThrow(() -> roomService.deleteRoom(room));
    }

    @Test
    void testGetAllRooms() {
        roomService.addRoom("Room1", "Desc1");
        roomService.addRoom("Room2", "Desc2");

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
    }

    @Test
    void testGetRoomsByLocationId() {
        List<Room> result = roomService.getRoomsByLocation(1);

        assertNotNull(result);
    }

    @Test
    void testGetTotalCapacityByLocation() {
        int result = roomService.getTotalCapacityByLocation(1);

        assertTrue(result >= 0);
    }

    @Test
    void testGetRoomsByEventId() {
        List<Room> result = roomService.getRoomsByEvent(1);

        assertNotNull(result);
    }


    private static class TestRoomDAO implements RoomDAO {
        private List<Room> rooms = new ArrayList<>();

        @Override
        public void rollback() {}
        @Override
        public void commit() {}
        @Override
        public Room addRoom(String name, String description) {
            Room room = new Room(rooms.size() + 1, name, description);
            rooms.add(room);
            return room;
        }
        @Override
        public Room getRoomById(int id) {
            return rooms.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
        }
        @Override
        public void updateRoom(Room room) {}
        @Override
        public void deleteRoom(Room room) {
            rooms.removeIf(r -> r.getId() == room.getId());
        }
        @Override
        public List<Room> getAllRooms() {
            return new ArrayList<>(rooms);
        }
        @Override
        public List<Room> getRoomsByLocationId(int locationId) {
            return new ArrayList<>();
        }
        @Override
        public int getTotalCapacityByLocation(int locationId) {
            return 0;
        }
        @Override
        public List<Room> getRoomsByEventId(int eventId) {
            return new ArrayList<>();
        }
    }
}

