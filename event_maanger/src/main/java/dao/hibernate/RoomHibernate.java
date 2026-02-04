package dao.hibernate;

import dao.RoomDAO;
import model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoomHibernate implements RoomDAO {

    public RoomHibernate() {
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
    public Room addRoom(String name, String description) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Room room = new Room(name, description);
            session.persist(room);
            transaction.commit();
            return room;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding room: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public Room getRoomById(int id) {
        Session session = getSession();

        try {
            return session.get(Room.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateRoom(Room room) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating room: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteRoom(Room room) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(session.merge(room));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting room: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Room> getAllRooms() {
        Session session = getSession();

        try {
            String query = "FROM Room";
            return session.createQuery(query, Room.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Room> getRoomsByLocationId(int locationId) {
        Session session = getSession();

        try {
            String query = "FROM Room WHERE location.id = :locationId";
            return session.createQuery(query, Room.class)
                    .setParameter("locationId", locationId)
                    .list();
        } finally {
            session.close();
        }
    }

    @Override
    public int getTotalCapacityByLocation(int locationId) {
        Session session = getSession();

        try {
            String query = "SELECT COALESCE(SUM(seatCapacity), 0) FROM Room WHERE location.id = :locationId";
            return Math.toIntExact(session.createQuery(query, Long.class)
                    .setParameter("locationId", locationId)
                    .getSingleResult());
        } finally {
            session.close();
        }
    }

    @Override
    public java.util.List<Room> getRoomsByEventId(int eventId) {
        Session session = getSession();

        try {
            String query = "SELECT r FROM Event e JOIN e.rooms r WHERE e.id = :eventId";
            return session.createQuery(query, Room.class)
                    .setParameter("eventId", eventId)
                    .list();
        } finally {
            session.close();
        }
    }
}
