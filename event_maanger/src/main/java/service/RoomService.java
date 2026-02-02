package service;

import dao.RoomDAO;
import model.Room;

import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO;

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public void commit() {
        roomDAO.commit();
    }

    public void rollback() {
        roomDAO.rollback();
    }

    public Room addRoom(String name, String description) {
        return roomDAO.addRoom(name, description);
    }

    public Room getRoom(int id) {
        return roomDAO.getRoomById(id);
    }

    public void updateRoom(Room room) {
        roomDAO.updateRoom(room);
    }

    public void deleteRoom(Room room) {
        roomDAO.deleteRoom(room);
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public List<Room> getRoomsByLocation(int locationId) {
        return roomDAO.getRoomsByLocationId(locationId);
    }

    public int getTotalCapacityByLocation(int locationId) {
        return roomDAO.getTotalCapacityByLocation(locationId);
    }

    public java.util.List<Room> getRoomsByEvent(int eventId) {
        return roomDAO.getRoomsByEventId(eventId);
    }
}
