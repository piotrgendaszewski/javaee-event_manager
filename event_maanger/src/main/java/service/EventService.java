package service;

import dao.EventDAO;
import model.Event;

import java.util.List;
import java.util.Map;

public class EventService {
    private final EventDAO eventDAO;

    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }


    public Event addEvent(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats) {
        return eventDAO.addEvent(name, description, eventDate, eventTime, eventStartDate, eventEndDate, numberedSeats);
    }

    public Event getEvent(int id) {
        return eventDAO.getEventById(id);
    }

    public Event getEventByName(String name) {
        return eventDAO.getEventByName(name);
    }

    public void updateEvent(Event event) {
        eventDAO.updateEvent(event);
    }

    public void deleteEvent(Event event) {
        eventDAO.deleteEvent(event);
    }

    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    public void setTicketPrice(int eventId, String ticketType, double price) {
        eventDAO.setTicketPrice(eventId, ticketType, price);
    }

    public void setTicketQuantity(int eventId, String ticketType, int quantity) {
        eventDAO.setTicketQuantity(eventId, ticketType, quantity);
    }

    public double getTicketPrice(int eventId, String ticketType) {
        return eventDAO.getTicketPrice(eventId, ticketType);
    }

    public int getTicketQuantity(int eventId, String ticketType) {
        return eventDAO.getTicketQuantity(eventId, ticketType);
    }
}

