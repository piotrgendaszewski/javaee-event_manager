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
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Room room = new Room(name, description);
            session.persist(room);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
            return room;
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding room: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Room getRoomById(int id) {
        Session session = HibernateSessionHelper.getCurrentSession();
        return session.get(Room.class, id);
    }

    @Override
    public void updateRoom(Room room) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.merge(room);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating room: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteRoom(Room room) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.remove(session.merge(room));
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting room: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Room> getAllRooms() {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Room";
        return session.createQuery(query, Room.class).list();
    }

    @Override
    public List<Room> getRoomsByLocationId(int locationId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Room WHERE location.id = :locationId";
        return session.createQuery(query, Room.class)
                .setParameter("locationId", locationId)
                .list();
    }

    @Override
    public int getTotalCapacityByLocation(int locationId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT COALESCE(SUM(seatCapacity), 0) FROM Room WHERE location.id = :locationId";
        return Math.toIntExact(session.createQuery(query, Long.class)
                .setParameter("locationId", locationId)
                .getSingleResult());
    }

    @Override
    public java.util.List<Room> getRoomsByEventId(int eventId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT r FROM Event e JOIN e.rooms r WHERE e.id = :eventId";
        return session.createQuery(query, Room.class)
                .setParameter("eventId", eventId)
                .list();
    }
}
