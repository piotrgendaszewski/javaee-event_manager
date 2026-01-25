package REST;

import dao.hibernate.EventHibernate;
import jakarta.ws.rs.*;
import model.Event;
import service.EventService;

import java.util.List;
import java.util.Map;

@Path("/events")
@Produces("application/json")
@Consumes("application/json")
public class EventResource {

    private final EventService eventService;

    public EventResource() {
        this.eventService = new EventService(new EventHibernate());
    }

    @GET
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GET
    @Path("/{id}")
    public Event getEvent(@PathParam("id") int id) {
        return eventService.getEvent(id);
    }

    @GET
    @Path("/name/{name}")
    public Event getEventByName(@PathParam("name") String name) {
        return eventService.getEventByName(name);
    }

    @POST
    public Event addEvent(Event event) {
        Event newEvent = eventService.addEvent(
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventTime(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.isNumberedSeats()
        );
        eventService.commit();
        return newEvent;
    }

    @PUT
    @Path("/{id}")
    public Event updateEvent(@PathParam("id") int id, Event event) {
        event.setId(id);
        eventService.updateEvent(event);
        eventService.commit();
        return event;
    }

    @DELETE
    @Path("/{id}")
    public void deleteEvent(@PathParam("id") int id) {
        Event event = eventService.getEvent(id);
        if (event != null) {
            eventService.deleteEvent(event);
            eventService.commit();
        }
    }

    @POST
    @Path("/{eventId}/ticket-price/{ticketType}/{price}")
    public void setTicketPrice(@PathParam("eventId") int eventId,
                               @PathParam("ticketType") String ticketType,
                               @PathParam("price") double price) {
        eventService.setTicketPrice(eventId, ticketType, price);
        eventService.commit();
    }

    @POST
    @Path("/{eventId}/ticket-quantity/{ticketType}/{quantity}")
    public void setTicketQuantity(@PathParam("eventId") int eventId,
                                  @PathParam("ticketType") String ticketType,
                                  @PathParam("quantity") int quantity) {
        eventService.setTicketQuantity(eventId, ticketType, quantity);
        eventService.commit();
    }

    @GET
    @Path("/{eventId}/ticket-price/{ticketType}")
    public double getTicketPrice(@PathParam("eventId") int eventId,
                                 @PathParam("ticketType") String ticketType) {
        return eventService.getTicketPrice(eventId, ticketType);
    }

    @GET
    @Path("/{eventId}/ticket-quantity/{ticketType}")
    public int getTicketQuantity(@PathParam("eventId") int eventId,
                                 @PathParam("ticketType") String ticketType) {
        return eventService.getTicketQuantity(eventId, ticketType);
    }
}

