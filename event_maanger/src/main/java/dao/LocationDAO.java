package dao;

import model.Location;
import model.User;

import java.util.List;

public interface LocationDAO {
    void rollback();
    void commit();

    Location addLocation(String name, String address);
    Location getLocationById(int id);
    Location getLocationByName(String name);
    void updateLocation(Location location);
    void deleteLocation(Location location);
    List<Location> getAllLocations();

    // Returns list of users who are contacts/managers for a given location
    List<User> getContactsByLocationId(int locationId);
}
