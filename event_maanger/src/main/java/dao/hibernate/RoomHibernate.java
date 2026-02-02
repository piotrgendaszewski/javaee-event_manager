package dao.hibernate;

import dao.RoomDAO;
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

public class RoomHibernate implements RoomDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public RoomHibernate() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Ticket.class)
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
    public Room addRoom(String name, String description) {
        Room room = new Room(name, description);
        session.persist(room);
        return room;
    }

    @Override
    public Room getRoomById(int id) {
        return session.get(Room.class, id);
    }

    @Override
    public void updateRoom(Room room) {
        session.merge(room);
    }

    @Override
    public void deleteRoom(Room room) {
        session.remove(room);
    }

    @Override
    public List<Room> getAllRooms() {
        String query = "FROM Room";
        return session.createQuery(query, Room.class).list();
    }

    @Override
    public List<Room> getRoomsByLocationId(int locationId) {
        String query = "FROM Room WHERE location.id = :locationId";
        return session.createQuery(query, Room.class)
                .setParameter("locationId", locationId)
                .list();
    }

    @Override
    public int getTotalCapacityByLocation(int locationId) {
        String query = "SELECT COALESCE(SUM(seatCapacity), 0) FROM Room WHERE location.id = :locationId";
        return Math.toIntExact(session.createQuery(query, Long.class)
                .setParameter("locationId", locationId)
                .getSingleResult());
    }

    @Override
    public java.util.List<Room> getRoomsByEventId(int eventId) {
        String query = "SELECT r FROM Event e JOIN e.rooms r WHERE e.id = :eventId";
        return session.createQuery(query, Room.class)
                .setParameter("eventId", eventId)
                .list();
    }
}
