package model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private String eventDate;

    @Column(name = "event_time")
    private String eventTime;

    @Column(name = "event_start_date")
    private String eventStartDate;

    @Column(name = "event_end_date")
    private String eventEndDate;

    @Column(name = "numbered_seats")
    private boolean numberedSeats;

    @ManyToMany
    @JoinTable(
        name = "event_locations",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> locations;

    @ManyToMany
    @JoinTable(
        name = "event_rooms",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @ElementCollection
    @CollectionTable(name = "ticket_prices", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "ticket_type")
    @Column(name = "price")
    private Map<String, Double> ticketPrices;

    @ElementCollection
    @CollectionTable(name = "ticket_quantities", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "ticket_type")
    @Column(name = "quantity")
    private Map<String, Integer> ticketQuantities;

    public Event() {}

    public Event(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats) {
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.numberedSeats = numberedSeats;
    }

    // Getters and Setters
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public boolean isNumberedSeats() {
        return numberedSeats;
    }

    public void setNumberedSeats(boolean numberedSeats) {
        this.numberedSeats = numberedSeats;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Map<String, Double> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(Map<String, Double> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    public Map<String, Integer> getTicketQuantities() {
        return ticketQuantities;
    }

    public void setTicketQuantities(Map<String, Integer> ticketQuantities) {
        this.ticketQuantities = ticketQuantities;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                ", numberedSeats=" + numberedSeats +
                '}';
    }
}

