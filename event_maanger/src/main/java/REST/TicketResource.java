package REST;

import dao.hibernate.TicketHibernate;
import jakarta.ws.rs.*;
import model.Event;
import model.Ticket;
import service.TicketService;

import java.util.List;

@Path("/tickets")
@Produces("application/json")
@Consumes("application/json")
public class TicketResource {

    private final TicketService ticketService;

    public TicketResource() {
        this.ticketService = new TicketService(new TicketHibernate());
    }

    @GET
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GET
    @Path("/{id}")
    public Ticket getTicket(@PathParam("id") int id) {
        return ticketService.getTicket(id);
    }

    @GET
    @Path("/event/{eventId}")
    public List<Ticket> getTicketsByEvent(@PathParam("eventId") int eventId) {
        return ticketService.getTicketsByEvent(eventId);
    }

    @GET
    @Path("/user/{userId}")
    public List<Ticket> getTicketsByUser(@PathParam("userId") int userId) {
        return ticketService.getTicketsByUser(userId);
    }

    @POST
    public Ticket addTicket(Ticket ticket) throws Exception {
        // Validate ticket
        ticket.validateForNumberedSeats();

        // Check if event has numbered seats and if seat already exists
        Event event = ticket.getEvent();
        if (event != null && event.isNumberedSeats() && ticket.getSeatNumber() != null) {
            boolean seatExists = ticketService.isTicketForSeatExists(event.getId(), ticket.getSeatNumber());
            if (seatExists) {
                throw new IllegalArgumentException("Seat number '" + ticket.getSeatNumber() + "' for event ID " + event.getId() + " is already booked");
            }
        }

        Ticket newTicket = ticketService.addTicket(
                ticket.getEvent().getId(),
                ticket.getUser().getId(),
                ticket.getTicketType(),
                ticket.getPrice(),
                ticket.getPurchaseDate(),
                ticket.getValidFromDate(),
                ticket.getValidToDate()
        );
        ticketService.commit();
        return newTicket;
    }

    @PUT
    @Path("/{id}")
    public Ticket updateTicket(@PathParam("id") int id, Ticket ticket) throws Exception {
        ticket.setId(id);

        // Validate ticket
        ticket.validateForNumberedSeats();

        // Check if event has numbered seats and if seat already exists (excluding current ticket)
        Event event = ticket.getEvent();
        if (event != null && event.isNumberedSeats() && ticket.getSeatNumber() != null) {
            Ticket existingTicket = ticketService.getTicketByEventAndSeat(event.getId(), ticket.getSeatNumber());
            if (existingTicket != null && existingTicket.getId() != id) {
                throw new IllegalArgumentException("Seat number '" + ticket.getSeatNumber() + "' for event ID " + event.getId() + " is already booked");
            }
        }

        ticketService.updateTicket(ticket);
        ticketService.commit();
        return ticket;
    }

    @DELETE
    @Path("/{id}")
    public void deleteTicket(@PathParam("id") int id) {
        Ticket ticket = ticketService.getTicket(id);
        if (ticket != null) {
            ticketService.deleteTicket(ticket);
            ticketService.commit();
        }
    }

    @GET
    @Path("/event/{eventId}/type/{ticketType}/count")
    public int countTicketsByEventAndType(@PathParam("eventId") int eventId,
                                          @PathParam("ticketType") String ticketType) {
        return ticketService.countTicketsByEventAndType(eventId, ticketType);
    }
}

