package REST;

import dao.hibernate.LocationHibernate;
import jakarta.ws.rs.*;
import model.Location;
import service.LocationService;

import java.util.List;

@Path("/locations")
@Produces("application/json")
@Consumes("application/json")
public class LocationResource {

    private final LocationService locationService;

    public LocationResource() {
        this.locationService = new LocationService(new LocationHibernate());
    }

    @GET
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GET
    @Path("/{id}")
    public Location getLocation(@PathParam("id") int id) {
        return locationService.getLocation(id);
    }

    @GET
    @Path("/name/{name}")
    public Location getLocationByName(@PathParam("name") String name) {
        return locationService.getLocationByName(name);
    }

    @GET
    @Path("/{id}/capacity-info")
    public String getLocationCapacityInfo(@PathParam("id") int id) {
        Location location = locationService.getLocation(id);
        if (location != null) {
            return location.getCapacityInfo();
        }
        return "Location not found";
    }

    @POST
    public Location addLocation(Location location) {
        Location newLocation = locationService.addLocation(location.getName(), location.getAddress());
        locationService.commit();
        return newLocation;
    }

    @PUT
    @Path("/{id}")
    public Location updateLocation(@PathParam("id") int id, Location location) {
        location.setId(id);
        locationService.updateLocation(location);
        locationService.commit();
        return location;
    }

    @DELETE
    @Path("/{id}")
    public void deleteLocation(@PathParam("id") int id) {
        Location location = locationService.getLocation(id);
        if (location != null) {
            locationService.deleteLocation(location);
            locationService.commit();
        }
    }
}

