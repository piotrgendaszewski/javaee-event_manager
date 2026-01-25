package dao.hibernate;

import dao.EventDAO;
import model.Event;
import model.EventReview;
import model.Location;
import model.Room;
import model.Ticket;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Map;

public class EventHibernate implements EventDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public EventHibernate() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(EventReview.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    public void commit() {
        transaction.commit();
    }

    public void rollback() {
        transaction.rollback();
    }

    @Override
    public Event addEvent(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats) {
        Event event = new Event(name, description, eventDate, eventTime, eventStartDate, eventEndDate, numberedSeats);
        session.persist(event);
        return event;
    }

    @Override
    public Event getEventById(int id) {
        return session.get(Event.class, id);
    }

    @Override
    public Event getEventByName(String name) {
        String query = "FROM Event WHERE name = :name";
        return session.createQuery(query, Event.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public void updateEvent(Event event) {
        session.merge(event);
    }

    @Override
    public void deleteEvent(Event event) {
        session.remove(event);
    }

    @Override
    public List<Event> getAllEvents() {
        String query = "FROM Event";
        return session.createQuery(query, Event.class).list();
    }

    @Override
    public void setTicketPrice(int eventId, String ticketType, double price) {
        Event event = getEventById(eventId);
        if (event != null) {
            event.getTicketPrices().put(ticketType, price);
            session.merge(event);
        }
    }

    @Override
    public void setTicketQuantity(int eventId, String ticketType, int quantity) {
        Event event = getEventById(eventId);
        if (event != null) {
            event.getTicketQuantities().put(ticketType, quantity);
            session.merge(event);
        }
    }

    @Override
    public double getTicketPrice(int eventId, String ticketType) {
        Event event = getEventById(eventId);
        if (event != null && event.getTicketPrices() != null) {
            return event.getTicketPrices().getOrDefault(ticketType, 0.0);
        }
        return 0.0;
    }

    @Override
    public int getTicketQuantity(int eventId, String ticketType) {
        Event event = getEventById(eventId);
        if (event != null && event.getTicketQuantities() != null) {
            return event.getTicketQuantities().getOrDefault(ticketType, 0);
        }
        return 0;
    }

    @Override
    public double getAverageRatingForEvent(int eventId) {
        String query = "SELECT AVG(rating) FROM EventReview WHERE event.id = :eventId";
        Double result = session.createQuery(query, Double.class)
                .setParameter("eventId", eventId)
                .getSingleResult();
        return result != null ? result : 0.0;
    }
}

