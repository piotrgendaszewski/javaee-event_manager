package service;

import dao.LocationDAO;
import model.Location;

import java.util.List;

public class LocationService {
    private final LocationDAO locationDAO;

    public LocationService(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }


    public Location addLocation(String name, String address) {
        return locationDAO.addLocation(name, address);
    }

    public Location getLocation(int id) {
        return locationDAO.getLocationById(id);
    }

    public Location getLocationByName(String name) {
        return locationDAO.getLocationByName(name);
    }

    public void updateLocation(Location location) {
        locationDAO.updateLocation(location);
    }

    public void deleteLocation(Location location) {
        locationDAO.deleteLocation(location);
    }

    public List<Location> getAllLocations() {
        return locationDAO.getAllLocations();
    }

    public List<model.User> getContactsForLocation(int locationId) {
        return locationDAO.getContactsByLocationId(locationId);
    }
}
