package dao.hibernate;

import dao.TicketDAO;
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

public class TicketHibernate implements TicketDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public TicketHibernate() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(EventReview.class)
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
    public Ticket addTicket(int eventId, int userId, String ticketType, double price, String purchaseDate, String validFromDate, String validToDate) {
        Event event = session.get(Event.class, eventId);
        User user = session.get(User.class, userId);

        if (event != null && user != null) {
            Ticket ticket = new Ticket(event, user, ticketType, price);
            ticket.setPurchaseDate(purchaseDate);
            ticket.setValidFromDate(validFromDate);
            ticket.setValidToDate(validToDate);
            session.persist(ticket);
            return ticket;
        }
        return null;
    }

    @Override
    public Ticket getTicketById(int id) {
        return session.get(Ticket.class, id);
    }

    @Override
    public void updateTicket(Ticket ticket) {
        session.merge(ticket);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        session.remove(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        String query = "FROM Ticket";
        return session.createQuery(query, Ticket.class).list();
    }

    @Override
    public List<Ticket> getTicketsByEventId(int eventId) {
        String query = "FROM Ticket WHERE event.id = :eventId";
        return session.createQuery(query, Ticket.class)
                .setParameter("eventId", eventId)
                .list();
    }

    @Override
    public List<Ticket> getTicketsByUserId(int userId) {
        String query = "FROM Ticket WHERE user.id = :userId";
        return session.createQuery(query, Ticket.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public int countTicketsByEventAndType(int eventId, String ticketType) {
        String query = "SELECT COUNT(*) FROM Ticket WHERE event.id = :eventId AND ticketType = :ticketType";
        return Math.toIntExact(session.createQuery(query, Long.class)
                .setParameter("eventId", eventId)
                .setParameter("ticketType", ticketType)
                .getSingleResult());
    }

    @Override
    public Ticket getTicketByEventAndSeat(int eventId, String seatNumber) {
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
}
