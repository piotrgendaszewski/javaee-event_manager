package dao.hibernate;

import dao.LocationDAO;
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

public class LocationHibernate implements LocationDAO {

    SessionFactory sessionFactory;
    Session session;
    Transaction transaction;

    public LocationHibernate() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Room.class)
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
    public Location addLocation(String name, String address) {
        Location location = new Location();
        location.setName(name);
        location.setAddress(address);
        session.persist(location);
        return location;
    }

    @Override
    public Location getLocationById(int id) {
        return session.get(Location.class, id);
    }

    @Override
    public Location getLocationByName(String name) {
        String query = "FROM Location WHERE name = :name";
        return session.createQuery(query, Location.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public void updateLocation(Location location) {
        session.merge(location);
    }

    @Override
    public void deleteLocation(Location location) {
        session.remove(location);
    }

    @Override
    public List<Location> getAllLocations() {
        String query = "FROM Location";
        return session.createQuery(query, Location.class).list();
    }

    @Override
    public List<User> getContactsByLocationId(int locationId) {
        String query = "SELECT contacts FROM Location l JOIN l.contacts contacts WHERE l.id = :locationId";
        return session.createQuery(query, User.class)
                .setParameter("locationId", locationId)
                .list();
    }
}
