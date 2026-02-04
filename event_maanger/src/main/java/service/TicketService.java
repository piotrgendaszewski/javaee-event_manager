package service;

import dao.TicketDAO;
import model.Ticket;

import java.util.List;

public class TicketService {
    private final TicketDAO ticketDAO;

    public TicketService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void commit() {
        ticketDAO.commit();
    }

    public void rollback() {
        ticketDAO.rollback();
    }

    public Ticket addTicket(int eventId, int userId, String ticketType, double price, String purchaseDate, String validFromDate, String validToDate) {
        return ticketDAO.addTicket(eventId, userId, ticketType, price, purchaseDate, validFromDate, validToDate);
    }

    public Ticket getTicket(int id) {
        return ticketDAO.getTicketById(id);
    }

    public void updateTicket(Ticket ticket) {
        ticketDAO.updateTicket(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        ticketDAO.deleteTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getTicketsByEvent(int eventId) {
        return ticketDAO.getTicketsByEventId(eventId);
    }

    public List<Ticket> getTicketsByUser(int userId) {
        return ticketDAO.getTicketsByUserId(userId);
    }

    public int countTicketsByEventAndType(int eventId, String ticketType) {
        return ticketDAO.countTicketsByEventAndType(eventId, ticketType);
    }

    public boolean isTicketForSeatExists(int eventId, String seatNumber) {
        return ticketDAO.isTicketForSeatExists(eventId, seatNumber);
    }

    public Ticket getTicketByEventAndSeat(int eventId, String seatNumber) {
        return ticketDAO.getTicketByEventAndSeat(eventId, seatNumber);
    }

    public java.util.List<model.User> getUsersByEvent(int eventId) {
        return ticketDAO.getUsersByEventId(eventId);
    }

    public java.util.Map<String, Integer> getRemainingTicketsByEvent(int eventId) {
        return ticketDAO.getRemainingTicketsByEvent(eventId);
    }

    public List<Ticket> getTicketsByUserMinimal(int userId) {
        List<Ticket> tickets = ticketDAO.getTicketsByUserId(userId);

        // Clear nested collections to reduce payload
        for (Ticket ticket : tickets) {
            if (ticket.getEvent() != null) {
                // Keep only basic event data, clear expensive collections
                ticket.getEvent().setTickets(null);
                ticket.getEvent().setLocations(null);
                ticket.getEvent().setRooms(null);
                ticket.getEvent().setTicketPrices(null);
                ticket.getEvent().setTicketQuantities(null);
            }
        }

        return tickets;
    }
}
