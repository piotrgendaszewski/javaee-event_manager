package dto;

/**
 * Room DTO - minimalne dane o sali dla admin panel
 * Bez zagnieżdżonych kolekcji (location, events)
 */
public class RoomAdminDTO {
    private int id;
    private String name;
    private String description;
    private int locationId;
    private String locationName;
    private int seatCapacity;

    public RoomAdminDTO(int id, String name, String description, int locationId,
                       String locationName, int seatCapacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.locationId = locationId;
        this.locationName = locationName;
        this.seatCapacity = seatCapacity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }
    public int getSeatCapacity() { return seatCapacity; }
}

