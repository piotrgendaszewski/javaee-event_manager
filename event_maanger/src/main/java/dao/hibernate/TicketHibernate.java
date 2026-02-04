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

    private Session getSession() {
        return HibernateSessionFactory.getSessionFactory().openSession();
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
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Event event = session.get(Event.class, eventId);
            User user = session.get(User.class, userId);

            if (event != null && user != null) {
                Ticket ticket = new Ticket(event, user, ticketType, price);
                ticket.setPurchaseDate(purchaseDate);
                ticket.setValidFromDate(validFromDate);
                ticket.setValidToDate(validToDate);
                session.persist(ticket);
                transaction.commit();
                return ticket;
            }
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
            return null;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding ticket: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public Ticket getTicketById(int id) {
        Session session = getSession();

        try {
            return session.get(Ticket.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateTicket(Ticket ticket) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating ticket: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(session.merge(ticket));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting ticket: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        Session session = getSession();

        try {
            String query = "FROM Ticket";
            return session.createQuery(query, Ticket.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Ticket> getTicketsByEventId(int eventId) {
        Session session = getSession();

        try {
            String query = "FROM Ticket WHERE event.id = :eventId";
            return session.createQuery(query, Ticket.class)
                    .setParameter("eventId", eventId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Ticket> getTicketsByUserId(int userId) {
        Session session = getSession();

        try {
            String query = "FROM Ticket WHERE user.id = :userId";
            return session.createQuery(query, Ticket.class)
                    .setParameter("userId", userId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public int countTicketsByEventAndType(int eventId, String ticketType) {
        Session session = getSession();

        try {
            String query = "SELECT COUNT(*) FROM Ticket WHERE event.id = :eventId AND ticketType = :ticketType";
            return Math.toIntExact(session.createQuery(query, Long.class)
                    .setParameter("eventId", eventId)
                    .setParameter("ticketType", ticketType)
                    .getSingleResult());
        } finally {
            session.close();
        }
    }

    @Override
    public Ticket getTicketByEventAndSeat(int eventId, String seatNumber) {
        Session session = getSession();

        try {
            String query = "FROM Ticket WHERE event.id = :eventId AND seatNumber = :seatNumber";
            return session.createQuery(query, Ticket.class)
                    .setParameter("eventId", eventId)
                    .setParameter("seatNumber", seatNumber)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isTicketForSeatExists(int eventId, String seatNumber) {
        return getTicketByEventAndSeat(eventId, seatNumber) != null;
    }

    @Override
    public List<User> getUsersByEventId(int eventId) {
        Session session = getSession();

        try {
            String query = "SELECT DISTINCT t.user FROM Ticket t WHERE t.event.id = :eventId";
            return session.createQuery(query, User.class)
                    .setParameter("eventId", eventId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public Map<String, Integer> getRemainingTicketsByEvent(int eventId) {
        Session session = getSession();

        try {
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
        } finally {
            session.close();
        }
    }
}
