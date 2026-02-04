package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(location);
        assertEquals(0, location.getId());
        assertNull(location.getName());
    }

    @Test
    void testConstructorWithAllFields() {
        Location testLocation = new Location(1, "Pałac Kultury", "pl. Defilad 1, Warszawa");
        assertNotNull(testLocation);
        assertEquals(1, testLocation.getId());
        assertEquals("Pałac Kultury", testLocation.getName());
        assertEquals("pl. Defilad 1, Warszawa", testLocation.getAddress());
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        location.setId(5);
        assertEquals(5, location.getId());
    }

    @Test
    void setId() {
        location.setId(10);
        assertEquals(10, location.getId());
    }

    // ===== NAME TESTS =====

    @Test
    void getName() {
        location.setName("Stadion Narodowy");
        assertEquals("Stadion Narodowy", location.getName());
    }

    @Test
    void setName() {
        location.setName("Hala Energa");
        assertEquals("Hala Energa", location.getName());
    }

    @Test
    void setNameNull() {
        location.setName(null);
        assertNull(location.getName());
    }

    // ===== ADDRESS TESTS =====

    @Test
    void getAddress() {
        location.setAddress("ul. Wiosny Ludów 4, Warszawa");
        assertEquals("ul. Wiosny Ludów 4, Warszawa", location.getAddress());
    }

    @Test
    void setAddress() {
        location.setAddress("ul. Długa 10, Gdańsk");
        assertEquals("ul. Długa 10, Gdańsk", location.getAddress());
    }

    @Test
    void setAddressNull() {
        location.setAddress(null);
        assertNull(location.getAddress());
    }

    // ===== DESCRIPTION TESTS =====

    @Test
    void getDescription() {
        location.setName("Test Location");
        // Description not set in constructor, testing getter
        assertNull(location.getDescription());
    }

    // ===== MAX AVAILABLE SEATS TESTS =====

    @Test
    void getMaxAvailableSeats() {
        location.setMaxAvailableSeats(5000);
        assertEquals(5000, location.getMaxAvailableSeats());
    }

    @Test
    void setMaxAvailableSeats() {
        location.setMaxAvailableSeats(3000);
        assertEquals(3000, location.getMaxAvailableSeats());
    }

    @Test
    void setMaxAvailableSeatsZero() {
        location.setMaxAvailableSeats(0);
        assertEquals(0, location.getMaxAvailableSeats());
    }

    @Test
    void setMaxAvailableSeatsLarge() {
        location.setMaxAvailableSeats(100000);
        assertEquals(100000, location.getMaxAvailableSeats());
    }

    // ===== CAPACITY VALIDATION TESTS =====

    @Test
    void testCanAddRoomWithinCapacity() {
        location.setMaxAvailableSeats(1000);
        assertTrue(location.canAddRoom(500, 300));  // 500 + 300 = 800 <= 1000
    }

    @Test
    void testCanAddRoomAtCapacity() {
        location.setMaxAvailableSeats(1000);
        assertTrue(location.canAddRoom(500, 500));  // 500 + 500 = 1000 <= 1000
    }

    @Test
    void testCannotAddRoomExceedsCapacity() {
        location.setMaxAvailableSeats(1000);
        assertFalse(location.canAddRoom(600, 500));  // 600 + 500 = 1100 > 1000
    }

    @Test
    void testCanAddRoomZeroExisting() {
        location.setMaxAvailableSeats(500);
        assertTrue(location.canAddRoom(0, 400));  // 0 + 400 = 400 <= 500
    }

    @Test
    void testCanAddRoomNoLocationCapacity() {
        location.setMaxAvailableSeats(0);
        assertFalse(location.canAddRoom(100, 200));  // Should not allow if location capacity is 0
    }

    // ===== CAPACITY INFO TESTS =====

    @Test
    void testGetCapacityInfo() {
        location.setName("Amfiteatr");
        location.setMaxAvailableSeats(2500);
        String capacityInfo = location.getCapacityInfo();

        assertNotNull(capacityInfo);
        assertTrue(capacityInfo.contains("Amfiteatr"));
        assertTrue(capacityInfo.contains("2500"));
        assertTrue(capacityInfo.contains("seats"));
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        location.setId(1);
        location.setName("Krako Arena");
        location.setAddress("ul. Sdemografia 3, Kraków");
        location.setMaxAvailableSeats(3500);

        String result = location.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Krako Arena'"));
        assertTrue(result.contains("address='ul. Sdemografia 3, Kraków'"));
        assertTrue(result.contains("maxAvailableSeats=3500"));
    }

    @Test
    void testToStringWithNullValues() {
        location.setId(2);
        location.setName(null);
        location.setAddress(null);

        String result = location.toString();
        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("name='null'"));
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullLocationFlow() {
        Location testLocation = new Location(1, "Hala Torwar", "ul. Wybrzeże Gdańskie, Warszawa");
        testLocation.setMaxAvailableSeats(4000);

        assertEquals(1, testLocation.getId());
        assertEquals("Hala Torwar", testLocation.getName());
        assertEquals("ul. Wybrzeże Gdańskie, Warszawa", testLocation.getAddress());
        assertEquals(4000, testLocation.getMaxAvailableSeats());

        // Test capacity calculation
        assertTrue(testLocation.canAddRoom(2000, 1500));  // 2000 + 1500 = 3500 <= 4000
    }

    @Test
    void testTwoLocationsAreIndependent() {
        Location location1 = new Location(1, "Location 1", "Address 1");
        Location location2 = new Location(2, "Location 2", "Address 2");

        location1.setMaxAvailableSeats(1000);
        location2.setMaxAvailableSeats(2000);

        assertEquals(1000, location1.getMaxAvailableSeats());
        assertEquals(2000, location2.getMaxAvailableSeats());
        assertNotEquals(location1.getId(), location2.getId());
    }

    @Test
    void testMultipleRoomAdditionValidation() {
        location.setMaxAvailableSeats(1000);

        // Add multiple rooms gradually
        assertTrue(location.canAddRoom(0, 300));       // 300 total
        assertTrue(location.canAddRoom(300, 350));     // 650 total
        assertTrue(location.canAddRoom(650, 300));     // 950 total
        assertFalse(location.canAddRoom(950, 100));    // 1050 total - exceeds
    }

    @Test
    void testLocationWithBoundaryCapacity() {
        location.setMaxAvailableSeats(1000);

        // Boundary tests
        assertTrue(location.canAddRoom(0, 1000));      // Exactly at limit
        assertFalse(location.canAddRoom(0, 1001));     // Exceeds by 1
        assertTrue(location.canAddRoom(1, 999));       // At limit
        assertFalse(location.canAddRoom(1, 1000));     // Exceeds by 1
    }
}

