package dao.hibernate;

import dao.EventDAO;
import model.Event;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EventHibernate implements EventDAO {

    public EventHibernate() {
        // No-arg constructor - SessionFactory is shared via singleton
    }

    @Override
    public void commit() {
        // No-op: transactions are handled by filter or per-operation
    }

    @Override
    public void rollback() {
        // No-op: transactions are handled by filter or per-operation
    }

    @Override
    public Event addEvent(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Event event = new Event(name, description, eventDate, eventTime, eventStartDate, eventEndDate, numberedSeats);
            session.persist(event);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
            return event;
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding event: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Event getEventById(int id) {
        Session session = HibernateSessionHelper.getCurrentSession();
        return session.get(Event.class, id);
    }

    @Override
    public Event getEventByName(String name) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Event WHERE name = :name";
        return session.createQuery(query, Event.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public void updateEvent(Event event) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.merge(event);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating event: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteEvent(Event event) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.remove(session.merge(event));
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting event: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Event> getAllEvents() {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Event";
        return session.createQuery(query, Event.class).list();
    }

    @Override
    public void setTicketPrice(int eventId, String ticketType, double price) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Event event = session.get(Event.class, eventId);
            if (event != null) {
                event.getTicketPrices().put(ticketType, price);
                session.merge(event);
            }
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error setting ticket price: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void setTicketQuantity(int eventId, String ticketType, int quantity) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Event event = session.get(Event.class, eventId);
            if (event != null) {
                event.getTicketQuantities().put(ticketType, quantity);
                session.merge(event);
            }
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error setting ticket quantity: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public double getTicketPrice(int eventId, String ticketType) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Event event = session.get(Event.class, eventId);
        if (event != null && event.getTicketPrices() != null) {
            return event.getTicketPrices().getOrDefault(ticketType, 0.0);
        }
        return 0.0;
    }

    @Override
    public int getTicketQuantity(int eventId, String ticketType) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Event event = session.get(Event.class, eventId);
        if (event != null && event.getTicketQuantities() != null) {
            return event.getTicketQuantities().getOrDefault(ticketType, 0);
        }
        return 0;
    }
}

