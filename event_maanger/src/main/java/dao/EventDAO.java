package dao;

import model.Event;
import java.util.List;
import java.util.Map;

public interface EventDAO {
    void rollback();
    void commit();

    Event addEvent(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats);
    Event getEventById(int id);
    Event getEventByName(String name);
    void updateEvent(Event event);
    void deleteEvent(Event event);
    List<Event> getAllEvents();

    void setTicketPrice(int eventId, String ticketType, double price);
    void setTicketQuantity(int eventId, String ticketType, int quantity);
    double getTicketPrice(int eventId, String ticketType);
    int getTicketQuantity(int eventId, String ticketType);
    double getAverageRatingForEvent(int eventId);
}

