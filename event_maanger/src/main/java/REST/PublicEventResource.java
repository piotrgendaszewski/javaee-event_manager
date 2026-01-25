package REST;

import dao.hibernate.EventHibernate;
import jakarta.ws.rs.*;
import model.Event;
import service.EventService;

import java.util.List;

/**
 * Public API for unauthenticated users
 * Allows viewing events only
 */
@Path("/public/events")
@Produces("application/json")
public class PublicEventResource {

    private final EventService eventService;

    public PublicEventResource() {
        this.eventService = new EventService(new EventHibernate());
    }

    /**
     * Get all events - PUBLIC endpoint
     * No authentication required
     */
    @GET
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Get average rating for event - PUBLIC endpoint
     * No authentication required
     * Must be before /{id} to avoid routing conflict
     */
    @GET
    @Path("/{eventId}/average-rating")
    public double getAverageRatingForEvent(@PathParam("eventId") int eventId) {
        return eventService.getAverageRatingForEvent(eventId);
    }

    /**
     * Get event by ID - PUBLIC endpoint
     * No authentication required
     */
    @GET
    @Path("/{id}")
    public Event getEvent(@PathParam("id") int id) {
        Event event = eventService.getEvent(id);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        return event;
    }

    /**
     * Get event by name - PUBLIC endpoint
     * No authentication required
     */
    @GET
    @Path("/name/{name}")
    public Event getEventByName(@PathParam("name") String name) {
        Event event = eventService.getEventByName(name);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }
        return event;
    }
}

