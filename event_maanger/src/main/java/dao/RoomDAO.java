package dao;

import model.Room;

import java.util.List;

public interface RoomDAO {
    void rollback();
    void commit();

    Room addRoom(String name, String description);
    Room getRoomById(int id);
    void updateRoom(Room room);
    void deleteRoom(Room room);
    List<Room> getAllRooms();
    List<Room> getRoomsByLocationId(int locationId);
    int getTotalCapacityByLocation(int locationId);
    List<Room> getRoomsByEventId(int eventId);
}
