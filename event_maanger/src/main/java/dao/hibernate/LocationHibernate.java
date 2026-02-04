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
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            Location location = new Location();
            location.setName(name);
            location.setAddress(address);
            session.persist(location);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
            return location;
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding location: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Location getLocationById(int id) {
        Session session = HibernateSessionHelper.getCurrentSession();
        return session.get(Location.class, id);
    }

    @Override
    public Location getLocationByName(String name) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Location WHERE name = :name";
        return session.createQuery(query, Location.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public void updateLocation(Location location) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.merge(location);
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating location: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void deleteLocation(Location location) {
        Session session = HibernateSessionHelper.getCurrentSession();
        Transaction transaction = HibernateSessionHelper.getCurrentTransaction(session);
        boolean isManaged = HibernateSessionHelper.isTransactionManagedByFilter();

        try {
            session.remove(session.merge(location));
            if (!isManaged && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (!isManaged && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting location: " + e.getMessage(), e);
        } finally {
            if (!isManaged && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Location> getAllLocations() {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "FROM Location";
        return session.createQuery(query, Location.class).list();
    }

    @Override
    public List<User> getContactsByLocationId(int locationId) {
        Session session = HibernateSessionHelper.getCurrentSession();
        String query = "SELECT contacts FROM Location l JOIN l.contacts contacts WHERE l.id = :locationId";
        return session.createQuery(query, User.class)
                .setParameter("locationId", locationId)
                .list();
    }
}
