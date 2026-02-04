package dto;

import java.util.List;

/**
 * Location DTO dla zalogowanego użytkownika
 * Zawiera info o lokacji + lista biletów użytkownika dla tej lokacji
 * Bez zagnieżdżonych kolekcji z lokacji (rooms, contacts, events)
 */
public class LocationPrivateDTO {
    private int id;
    private String name;
    private String address;
    private int maxAvailableSeats;
    private List<TicketAtLocationDTO> userTickets; // Bilety użytkownika z tej lokacji

    public LocationPrivateDTO(int id, String name, String address, int maxAvailableSeats,
                             List<TicketAtLocationDTO> userTickets) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.maxAvailableSeats = maxAvailableSeats;
        this.userTickets = userTickets;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getMaxAvailableSeats() { return maxAvailableSeats; }
    public List<TicketAtLocationDTO> getUserTickets() { return userTickets; }

    /**
     * Nested DTO - ticket info dla ticketów w lokacji (bez event/location details)
     */
    public static class TicketAtLocationDTO {
        private int ticketId;
        private int eventId;
        private String eventName;
        private String ticketType;
        private double price;
        private String validFromDate;
        private String validToDate;

        public TicketAtLocationDTO(int ticketId, int eventId, String eventName,
                                 String ticketType, double price,
                                 String validFromDate, String validToDate) {
            this.ticketId = ticketId;
            this.eventId = eventId;
            this.eventName = eventName;
            this.ticketType = ticketType;
            this.price = price;
            this.validFromDate = validFromDate;
            this.validToDate = validToDate;
        }

        public int getTicketId() { return ticketId; }
        public int getEventId() { return eventId; }
        public String getEventName() { return eventName; }
        public String getTicketType() { return ticketType; }
        public double getPrice() { return price; }
        public String getValidFromDate() { return validFromDate; }
        public String getValidToDate() { return validToDate; }
    }
}

