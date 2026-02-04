package service;

import dao.LocationDAO;
import model.Location;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LocationService
 */
class LocationServiceTest {

    private LocationService locationService;
    private TestLocationDAO testLocationDAO;

    @BeforeEach
    void setUp() {
        testLocationDAO = new TestLocationDAO();
        locationService = new LocationService(testLocationDAO);
    }

    @Test
    void testAddLocation() {
        Location result = locationService.addLocation("Pałac Kultury", "pl. Defilad 1, Warszawa");

        assertNotNull(result);
        assertEquals("Pałac Kultury", result.getName());
    }

    @Test
    void testGetLocationById() {
        Location location = locationService.addLocation("Stadion", "Stadium address");
        Location result = locationService.getLocation(location.getId());

        assertNotNull(result);
        assertEquals("Stadion", result.getName());
    }

    @Test
    void testGetLocationByName() {
        locationService.addLocation("Hala Energa", "Gdańsk address");
        Location result = locationService.getLocationByName("Hala Energa");

        assertNotNull(result);
        assertEquals("Hala Energa", result.getName());
    }

    @Test
    void testUpdateLocation() {
        Location location = locationService.addLocation("Location", "Address");
        location.setName("Updated Name");

        assertDoesNotThrow(() -> locationService.updateLocation(location));
    }

    @Test
    void testDeleteLocation() {
        Location location = locationService.addLocation("To Delete", "Address");

        assertDoesNotThrow(() -> locationService.deleteLocation(location));
    }

    @Test
    void testGetAllLocations() {
        locationService.addLocation("Loc1", "Addr1");
        locationService.addLocation("Loc2", "Addr2");

        List<Location> result = locationService.getAllLocations();

        assertEquals(2, result.size());
    }

    @Test
    void testGetContactsByLocationId() {
        Location location = locationService.addLocation("Location", "Address");

        List<User> result = locationService.getContactsForLocation(location.getId());

        assertNotNull(result);
    }

    @Test
    void testCommit() {
        assertDoesNotThrow(() -> locationService.commit());
    }

    @Test
    void testRollback() {
        assertDoesNotThrow(() -> locationService.rollback());
    }

    private static class TestLocationDAO implements LocationDAO {
        private List<Location> locations = new ArrayList<>();

        @Override
        public void rollback() {}
        @Override
        public void commit() {}
        @Override
        public Location addLocation(String name, String address) {
            Location location = new Location(locations.size() + 1, name, address);
            locations.add(location);
            return location;
        }
        @Override
        public Location getLocationById(int id) {
            return locations.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
        }
        @Override
        public Location getLocationByName(String name) {
            return locations.stream().filter(l -> l.getName() != null && l.getName().equals(name)).findFirst().orElse(null);
        }
        @Override
        public void updateLocation(Location location) {}
        @Override
        public void deleteLocation(Location location) {
            locations.removeIf(l -> l.getId() == location.getId());
        }
        @Override
        public List<Location> getAllLocations() {
            return new ArrayList<>(locations);
        }
        @Override
        public List<User> getContactsByLocationId(int locationId) {
            return new ArrayList<>();
        }
    }
}

