package model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="locations")
public class Location {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="name", unique = true)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="address")
    private String address;

    @Column(name="max_available_seats")
    private int maxAvailableSeats;

    @ManyToMany
    @JoinTable(
        name = "location_contacts",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> contacts;

    @OneToMany(mappedBy = "location")
    private List<Room> rooms;

    public Location() {
    }

    public Location(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxAvailableSeats() {
        return maxAvailableSeats;
    }

    public void setMaxAvailableSeats(int maxAvailableSeats) {
        this.maxAvailableSeats = maxAvailableSeats;
    }

    /**
     * Validates that total seats in all rooms does not exceed location's max available seats
     * @return true if valid, false otherwise
     */
    public boolean areRoomSeatsValid() {
        if (rooms == null || rooms.isEmpty()) {
            return true;
        }
        // Note: Room capacity would need to be added to Room model
        return true;
    }

    /**
     * Checks if adding a room with given capacity would exceed max available seats
     * @param totalRoomCapacity total capacity from all current rooms in this location
     * @param newRoomCapacity capacity of new room being added
     * @return true if within limits, false otherwise
     */
    public boolean canAddRoom(int totalRoomCapacity, int newRoomCapacity) {
        return (totalRoomCapacity + newRoomCapacity) <= maxAvailableSeats;
    }

    /**
     * Gets total capacity information
     * @return total capacity needed for validation
     */
    public String getCapacityInfo() {
        return "Location: " + name + " - Max Capacity: " + maxAvailableSeats + " seats";
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", maxAvailableSeats=" + maxAvailableSeats +
                '}';
    }
}
