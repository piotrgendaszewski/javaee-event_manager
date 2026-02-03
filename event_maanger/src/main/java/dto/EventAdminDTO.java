package dto;

import java.util.List;
import java.util.Map;

/**
 * Event DTO - minimalne dane o evencie dla admin panel
 * Bez zagnieżdżonych kolekcji (tickets, locations, rooms)
 */
public class EventAdminDTO {
    private int id;
    private String name;
    private String description;
    private String eventDate;
    private String eventTime;
    private String eventStartDate;
    private String eventEndDate;
    private boolean numberedSeats;
    private Map<String, Double> ticketPrices;
    private Map<String, Integer> ticketQuantities;

    public EventAdminDTO(int id, String name, String description, String eventDate, String eventTime,
                        String eventStartDate, String eventEndDate, boolean numberedSeats,
                        Map<String, Double> ticketPrices, Map<String, Integer> ticketQuantities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.numberedSeats = numberedSeats;
        this.ticketPrices = ticketPrices;
        this.ticketQuantities = ticketQuantities;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getEventDate() { return eventDate; }
    public String getEventTime() { return eventTime; }
    public String getEventStartDate() { return eventStartDate; }
    public String getEventEndDate() { return eventEndDate; }
    public boolean isNumberedSeats() { return numberedSeats; }
    public Map<String, Double> getTicketPrices() { return ticketPrices; }
    public Map<String, Integer> getTicketQuantities() { return ticketQuantities; }
}

