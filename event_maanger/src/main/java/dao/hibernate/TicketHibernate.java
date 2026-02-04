package dao.hibernate;

import dao.TicketDAO;
import model.Event;
import model.Ticket;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TicketHibernate implements TicketDAO {

    public TicketHibernate() {
        // No-arg constructor - SessionFactory is shared via singleton
    }

    @Override
    public void commit() {
        // No-op: transactions are handled per-operation
    }

    @Override
    public void rollback() {
        // No-op: transactions are handled per-operation
    }

    @Override
    public Ticket addTicket(int eventId, int userId, String ticketType, double price, String purchaseDate, String validFromDate, String validToDate) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Event event = session.get(Event.class, eventId);
            User user = session.get(User.class, userId);

            if (event != null && user != null) {
                Ticket ticket = new Ticket(event, user, ticketType, price);
                ticket.setPurchaseDate(purchaseDate);
                ticket.setValidFromDate(validFromDate);
                ticket.setValidToDate(validToDate);
                session.persist(ticket);
                if (!isManaged && transaction.isActive()) {
                    transaction.commit();
                }
                return ticket;
            }
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
            return null;
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding ticket: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Ticket getTicketById(int id) {
        Session session = HibernateSessionHelper.getCurrentSession();
        return session.get(Ticket.class, id);
    }

    @Override
    public void updateTicket(Ticket ticket) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.merge(ticket);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating ticket: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.remove(session.merge(ticket));
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting ticket: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Ticket";
        return session.createQuery(query, Ticket.class).list();
    }

    @Override
    public List<Ticket> getTicketsByEventId(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Ticket WHERE event.id = :eventId";
        return session.createQuery(query, Ticket.class)
                .setParameter("eventId", eventId)
                .list();
    }

    @Override
    public List<Ticket> getTicketsByUserId(int userId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Ticket WHERE user.id = :userId";
        return session.createQuery(query, Ticket.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public int countTicketsByEventAndType(int eventId, String ticketType) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT COUNT(*) FROM Ticket WHERE event.id = :eventId AND ticketType = :ticketType";
        return Math.toIntExact(session.createQuery(query, Long.class)
                .setParameter("eventId", eventId)
                .setParameter("ticketType", ticketType)
                .getSingleResult());
    }

    @Override
    public Ticket getTicketByEventAndSeat(int eventId, String seatNumber) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Ticket WHERE event.id = :eventId AND seatNumber = :seatNumber";
        return session.createQuery(query, Ticket.class)
                .setParameter("eventId", eventId)
                .setParameter("seatNumber", seatNumber)
                .uniqueResult();
    }

    @Override
    public boolean isTicketForSeatExists(int eventId, String seatNumber) {
        return getTicketByEventAndSeat(eventId, seatNumber) != null;
    }

    @Override
    public List<User> getUsersByEventId(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT DISTINCT t.user FROM Ticket t WHERE t.event.id = :eventId";
        return session.createQuery(query, User.class)
                .setParameter("eventId", eventId)
                .list();
    }

    @Override
    public Map<String, Integer> getRemainingTicketsByEvent(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Event event = session.get(Event.class, eventId);
        Map<String, Integer> remaining = new HashMap<>();
        if (event == null) return remaining;

        if (event.getTicketQuantities() != null) {
            for (Map.Entry<String, Integer> entry : event.getTicketQuantities().entrySet()) {
                String type = entry.getKey();
                Integer total = entry.getValue() != null ? entry.getValue() : 0;
                // count sold
                String countQuery = "SELECT COUNT(*) FROM Ticket WHERE event.id = :eventId AND ticketType = :type";
                Integer sold = Math.toIntExact(session.createQuery(countQuery, Long.class)
                        .setParameter("eventId", eventId)
                        .setParameter("type", type)
                        .getSingleResult());
                remaining.put(type, Math.max(0, total - sold));
            }
        }
        return remaining;
    }
}
