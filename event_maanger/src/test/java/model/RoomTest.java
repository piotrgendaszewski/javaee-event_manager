package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(room);
        assertEquals(0, room.getId());
        assertNull(room.getName());
    }

    @Test
    void testConstructorWithNameAndDescription() {
        Room testRoom = new Room("Main Hall", "Large conference hall");
        assertNotNull(testRoom);
        assertEquals("Main Hall", testRoom.getName());
        assertEquals("Large conference hall", testRoom.getDescription());
    }

    @Test
    void testConstructorWithAllFields() {
        Room testRoom = new Room(1, "VIP Room", "Exclusive VIP lounge");
        assertNotNull(testRoom);
        assertEquals(1, testRoom.getId());
        assertEquals("VIP Room", testRoom.getName());
        assertEquals("Exclusive VIP lounge", testRoom.getDescription());
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        room.setId(10);
        assertEquals(10, room.getId());
    }

    @Test
    void setId() {
        room.setId(25);
        assertEquals(25, room.getId());
    }

    // ===== NAME TESTS =====

    @Test
    void getName() {
        room.setName("Conference Room A");
        assertEquals("Conference Room A", room.getName());
    }

    @Test
    void setName() {
        room.setName("Meeting Room B");
        assertEquals("Meeting Room B", room.getName());
    }

    @Test
    void setNameNull() {
        room.setName(null);
        assertNull(room.getName());
    }

    // ===== DESCRIPTION TESTS =====

    @Test
    void getDescription() {
        room.setDescription("Small room for meetings");
        assertEquals("Small room for meetings", room.getDescription());
    }

    @Test
    void setDescription() {
        room.setDescription("Updated description");
        assertEquals("Updated description", room.getDescription());
    }

    @Test
    void setDescriptionNull() {
        room.setDescription(null);
        assertNull(room.getDescription());
    }

    // ===== SEAT CAPACITY TESTS =====

    @Test
    void getSeatCapacity() {
        room.setSeatCapacity(100);
        assertEquals(100, room.getSeatCapacity());
    }

    @Test
    void setSeatCapacity() {
        room.setSeatCapacity(250);
        assertEquals(250, room.getSeatCapacity());
    }

    @Test
    void setSeatCapacityInvalid() {
        assertThrows(IllegalArgumentException.class, () -> room.setSeatCapacity(0));
    }

    @Test
    void setSeatCapacityNegative() {
        assertThrows(IllegalArgumentException.class, () -> room.setSeatCapacity(-50));
    }

    @Test
    void setSeatCapacityOne() {
        room.setSeatCapacity(1);  // Should be allowed
        assertEquals(1, room.getSeatCapacity());
    }

    @Test
    void setSeatCapacityLarge() {
        room.setSeatCapacity(10000);
        assertEquals(10000, room.getSeatCapacity());
    }

    // ===== LOCATION TESTS =====

    @Test
    void getLocation() {
        Location testLocation = new Location(1, "Test Location", "Test Address");
        room.setLocation(testLocation);
        assertNotNull(room.getLocation());
        assertEquals(1, room.getLocation().getId());
    }

    @Test
    void setLocation() {
        Location location = new Location(2, "New Location", "New Address");
        room.setLocation(location);
        assertEquals("New Location", room.getLocation().getName());
    }

    @Test
    void setLocationNull() {
        room.setLocation(null);
        assertNull(room.getLocation());
    }

    // ===== CAPACITY VALIDATION TESTS =====

    @Test
    void testCanBeAddedToLocationWithinCapacity() {
        room.setSeatCapacity(300);
        assertTrue(room.canBeAddedToLocation(200));  // 200 + 300 = 500
    }

    @Test
    void testCanBeAddedToLocationAtCapacity() {
        Location location = new Location();
        location.setMaxAvailableSeats(500);
        room.setLocation(location);
        room.setSeatCapacity(300);

        assertTrue(room.canBeAddedToLocation(200));  // 200 + 300 = 500 <= 500
    }

    @Test
    void testCanBeAddedToLocationExceedsCapacity() {
        Location location = new Location();
        location.setMaxAvailableSeats(500);
        room.setLocation(location);
        room.setSeatCapacity(400);

        assertFalse(room.canBeAddedToLocation(200));  // 200 + 400 = 600 > 500
    }

    @Test
    void testCanBeAddedToLocationNoLocation() {
        room.setSeatCapacity(300);
        assertTrue(room.canBeAddedToLocation(200));  // Should allow if no location
    }

    @Test
    void testCanBeAddedToLocationZeroCapacity() {
        Location location = new Location();
        location.setMaxAvailableSeats(0);
        room.setLocation(location);
        room.setSeatCapacity(100);

        assertTrue(room.canBeAddedToLocation(0));  // Should allow if location capacity is 0
    }

    // ===== CAPACITY INFO TESTS =====

    @Test
    void testGetCapacityInfo() {
        room.setName("Theater");
        room.setSeatCapacity(500);
        String capacityInfo = room.getCapacityInfo();

        assertNotNull(capacityInfo);
        assertTrue(capacityInfo.contains("Theater"));
        assertTrue(capacityInfo.contains("500"));
        assertTrue(capacityInfo.contains("seats"));
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        room.setId(1);
        room.setName("Main Auditorium");
        room.setDescription("Large auditorium");
        room.setSeatCapacity(1000);
        Location location = new Location(5, "Location", "Address");
        room.setLocation(location);

        String result = room.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Main Auditorium'"));
        assertTrue(result.contains("description='Large auditorium'"));
        assertTrue(result.contains("seatCapacity=1000"));
        assertTrue(result.contains("locationId=5"));
    }

    @Test
    void testToStringWithoutLocation() {
        room.setId(2);
        room.setName("Small Room");
        room.setSeatCapacity(50);

        String result = room.toString();
        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("name='Small Room'"));
        assertTrue(result.contains("seatCapacity=50"));
        assertTrue(result.contains("locationId=0"));  // null location
    }

    @Test
    void testToStringWithNullLocation() {
        room.setId(3);
        room.setName("Test Room");
        room.setSeatCapacity(100);
        room.setLocation(null);

        String result = room.toString();
        assertTrue(result.contains("locationId=0"));
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullRoomFlow() {
        Location testLocation = new Location(1, "Convention Center", "ul. Główna 100");
        testLocation.setMaxAvailableSeats(5000);

        Room testRoom = new Room("Hall A", "Main conference hall");
        testRoom.setId(1);
        testRoom.setSeatCapacity(1500);
        testRoom.setLocation(testLocation);

        assertEquals(1, testRoom.getId());
        assertEquals("Hall A", testRoom.getName());
        assertEquals(1500, testRoom.getSeatCapacity());
        assertEquals("Convention Center", testRoom.getLocation().getName());
        assertTrue(testRoom.canBeAddedToLocation(2000));  // 2000 + 1500 = 3500 <= 5000
    }

    @Test
    void testTwoRoomsAreIndependent() {
        Room room1 = new Room("Room 1", "Description 1");
        Room room2 = new Room("Room 2", "Description 2");

        room1.setSeatCapacity(100);
        room2.setSeatCapacity(200);

        assertEquals(100, room1.getSeatCapacity());
        assertEquals(200, room2.getSeatCapacity());
        assertNotEquals(room1.getName(), room2.getName());
    }

    @Test
    void testMultipleRoomsInSameLocation() {
        Location location = new Location(1, "Convention Hall", "Address");
        location.setMaxAvailableSeats(5000);

        Room room1 = new Room("Room A", "Description A");
        Room room2 = new Room("Room B", "Description B");
        Room room3 = new Room("Room C", "Description C");

        room1.setSeatCapacity(1500);
        room2.setSeatCapacity(1500);
        room3.setSeatCapacity(1500);

        room1.setLocation(location);
        room2.setLocation(location);
        room3.setLocation(location);

        assertTrue(room1.canBeAddedToLocation(0));        // 1500
        assertTrue(room2.canBeAddedToLocation(1500));     // 1500 + 1500 = 3000
        assertTrue(room3.canBeAddedToLocation(3000));     // 3000 + 1500 = 4500
        assertTrue(room3.canBeAddedToLocation(3500));     // 3500 + 1500 = 5000 (exactly at limit)
    }

    @Test
    void testRoomCapacityBoundary() {
        Location location = new Location();
        location.setMaxAvailableSeats(1000);
        room.setLocation(location);
        room.setSeatCapacity(1000);

        assertTrue(room.canBeAddedToLocation(0));         // Exactly at limit
        assertFalse(room.canBeAddedToLocation(1));        // Exceeds by 1
    }
}

