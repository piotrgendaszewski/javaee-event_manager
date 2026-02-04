package service;

import model.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for public event listing - contains only essential event data + ticket pricing/availability
 * Used by PublicResource to avoid exposing unnecessary internal data
 */
public class EventPublicDTO {
    private int id;
    private String name;
    private String description;
    private String eventStartDate;
    private String eventEndDate;
    private double averageRating;
    private Map<String, TicketAvailability> ticketAvailability; // ticketType -> TicketAvailability

    public EventPublicDTO() {
    }

    public EventPublicDTO(Event event, Map<String, Integer> remainingTickets, double averageRating) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.eventStartDate = event.getEventStartDate();
        this.eventEndDate = event.getEventEndDate();
        this.averageRating = averageRating;

        // Build ticket availability map
        this.ticketAvailability = new HashMap<>();
        if (event.getTicketPrices() != null) {
            event.getTicketPrices().forEach((ticketType, price) -> {
                int remaining = remainingTickets.getOrDefault(ticketType, 0);
                this.ticketAvailability.put(ticketType, new TicketAvailability(price, remaining));
            });
        }
    }

    // Getters and setters
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Map<String, TicketAvailability> getTicketAvailability() {
        return ticketAvailability;
    }

    public void setTicketAvailability(Map<String, TicketAvailability> ticketAvailability) {
        this.ticketAvailability = ticketAvailability;
    }

    /**
     * Inner class to represent ticket pricing and availability
     */
    public static class TicketAvailability {
        private double price;
        private int availableCount;

        public TicketAvailability() {
        }

        public TicketAvailability(double price, int availableCount) {
            this.price = price;
            this.availableCount = availableCount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getAvailableCount() {
            return availableCount;
        }

        public void setAvailableCount(int availableCount) {
            this.availableCount = availableCount;
        }

        @Override
        public String toString() {
            return "TicketAvailability{" +
                    "price=" + price +
                    ", availableCount=" + availableCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EventPublicDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventStartDate='" + eventStartDate + '\'' +
                ", eventEndDate='" + eventEndDate + '\'' +
                ", averageRating=" + averageRating +
                ", ticketAvailability=" + ticketAvailability +
                '}';
    }
}

