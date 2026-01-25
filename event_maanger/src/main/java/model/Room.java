package model;

import jakarta.persistence.*;

@Entity
@Table(name="rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="seat_capacity")
    private int seatCapacity;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Room() {}

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        if (seatCapacity <= 0) {
            throw new IllegalArgumentException("Seat capacity must be greater than 0");
        }
        this.seatCapacity = seatCapacity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Validates room against its location's capacity constraints
     * @throws IllegalArgumentException if room capacity exceeds location's max capacity
     */
    public void validateAgainstLocation() throws IllegalArgumentException {
        if (location != null && location.getMaxAvailableSeats() > 0) {
            if (seatCapacity > location.getMaxAvailableSeats()) {
                throw new IllegalArgumentException(
                    "Room capacity (" + seatCapacity + ") cannot exceed location's max capacity (" +
                    location.getMaxAvailableSeats() + ")"
                );
            }
        }
    }

    /**
     * Checks if adding this room would exceed location's total capacity
     * @param totalCapacityInLocation current total capacity of all rooms in location (excluding this room)
     * @return true if room can be added, false otherwise
     */
    public boolean canBeAddedToLocation(int totalCapacityInLocation) {
        if (location == null || location.getMaxAvailableSeats() == 0) {
            return true;
        }
        return (totalCapacityInLocation + seatCapacity) <= location.getMaxAvailableSeats();
    }

    /**
     * Gets room capacity information
     * @return capacity info string
     */
    public String getCapacityInfo() {
        return "Room: " + name + " - Capacity: " + seatCapacity + " seats";
    }
}

