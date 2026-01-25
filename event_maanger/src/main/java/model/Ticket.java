package model;

import jakarta.persistence.*;

/**
 * Ticket entity representing a ticket for an event.
 *
 * For events with numbered seats (@see Event.numberedSeats):
 * - Seat number is required (cannot be null or empty)
 * - Each combination of event_id + seat_number must be unique
 * - Only one ticket can be sold per seat
 */
@Entity
@Table(name = "tickets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "seat_number"}, name = "uc_event_seat")
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "price")
    private double price;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "valid_from_date")
    private String validFromDate;

    @Column(name = "valid_to_date")
    private String validToDate;

    public Ticket() {}

    public Ticket(Event event, User user, String ticketType, double price) {
        this.event = event;
        this.user = user;
        this.ticketType = ticketType;
        this.price = price;
    }

    public Ticket(Event event, User user, String ticketType, double price, String seatNumber) {
        this.event = event;
        this.user = user;
        this.ticketType = ticketType;
        this.price = price;
        this.seatNumber = seatNumber;
    }

    public Ticket(Event event, User user, String ticketType, double price, String seatNumber, String validFromDate, String validToDate) {
        this.event = event;
        this.user = user;
        this.ticketType = ticketType;
        this.price = price;
        this.seatNumber = seatNumber;
        this.validFromDate = validFromDate;
        this.validToDate = validToDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(String validFromDate) {
        this.validFromDate = validFromDate;
    }

    public String getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(String validToDate) {
        this.validToDate = validToDate;
    }

    /**
     * Validates that if the event has numbered seats, the seat number must not be null
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        if (event != null && event.isNumberedSeats()) {
            return seatNumber != null && !seatNumber.trim().isEmpty();
        }
        return true;
    }

    /**
     * Checks if ticket has required seat information for numbered seat events.
     * For events with numbered seats, seat number is mandatory.
     * @throws IllegalArgumentException if ticket is invalid for numbered seat event
     */
    public void validateForNumberedSeats() throws IllegalArgumentException {
        if (event != null && event.isNumberedSeats()) {
            if (seatNumber == null || seatNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Seat number is required for events with numbered seats");
            }
        }
    }
}

