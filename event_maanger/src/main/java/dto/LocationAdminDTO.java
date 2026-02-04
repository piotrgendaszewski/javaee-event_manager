package dto;

/**
 * Location DTO - minimalne dane o lokacji dla admin panel
 * Bez zagnieżdżonych kolekcji (rooms, contacts, events)
 */
public class LocationAdminDTO {
    private int id;
    private String name;
    private String address;
    private int maxAvailableSeats;

    public LocationAdminDTO(int id, String name, String address, int maxAvailableSeats) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.maxAvailableSeats = maxAvailableSeats;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getMaxAvailableSeats() { return maxAvailableSeats; }
}

