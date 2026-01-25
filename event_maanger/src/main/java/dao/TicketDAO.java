package dao;

import model.Ticket;
import java.util.List;

public interface TicketDAO {
    void rollback();
    void commit();

    Ticket addTicket(int eventId, int userId, String ticketType, double price, String purchaseDate, String validFromDate, String validToDate);
    Ticket getTicketById(int id);
    void updateTicket(Ticket ticket);
    void deleteTicket(Ticket ticket);
    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByEventId(int eventId);
    List<Ticket> getTicketsByUserId(int userId);
    int countTicketsByEventAndType(int eventId, String ticketType);

    Ticket getTicketByEventAndSeat(int eventId, String seatNumber);
    boolean isTicketForSeatExists(int eventId, String seatNumber);
}

