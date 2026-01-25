package dao;

import model.Location;

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
}
