package dao.hibernate;

import dao.LocationDAO;
import model.Location;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LocationHibernate implements LocationDAO {

    public LocationHibernate() {
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
    public Location addLocation(String name, String address) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Location location = new Location();
            location.setName(name);
            location.setAddress(address);
            session.persist(location);
            transaction.commit();
            return location;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding location: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public Location getLocationById(int id) {
        Session session = getSession();

        try {
            return session.get(Location.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public Location getLocationByName(String name) {
        Session session = getSession();

        try {
            String query = "FROM Location WHERE name = :name";
            return session.createQuery(query, Location.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateLocation(Location location) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(location);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating location: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteLocation(Location location) {
        Session session = getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(session.merge(location));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting location: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Location> getAllLocations() {
        Session session = getSession();

        try {
            String query = "FROM Location";
            return session.createQuery(query, Location.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getContactsByLocationId(int locationId) {
        Session session = getSession();

        try {
            String query = "SELECT contacts FROM Location l JOIN l.contacts contacts WHERE l.id = :locationId";
            return session.createQuery(query, User.class)
                    .setParameter("locationId", locationId)
                    .list();
        } finally {
            session.close();
        }
    }
}
