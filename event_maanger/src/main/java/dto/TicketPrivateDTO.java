package dto;

/**
 * Ticket DTO dla zalogowanego użytkownika - bez zagnieżdżonych obiektów Event
 * Zawiera basic info o evencie i lokacji
 */
public class TicketPrivateDTO {
    private int id;
    private String ticketType;
    private double price;
    private String purchaseDate;
    private String validFromDate;
    private String validToDate;
    private String seatNumber;

    private int eventId;
    private String eventName;
    private String eventDescription;
    private String eventStartDate;
    private String eventEndDate;

    private int locationId;
    private String locationName;
    private String locationAddress;

    public TicketPrivateDTO(int id, String ticketType, double price, String purchaseDate,
                           String validFromDate, String validToDate, String seatNumber,
                           int eventId, String eventName, String eventDescription,
                           String eventStartDate, String eventEndDate,
                           int locationId, String locationName, String locationAddress) {
        this.id = id;
        this.ticketType = ticketType;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.validFromDate = validFromDate;
        this.validToDate = validToDate;
        this.seatNumber = seatNumber;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
    }

    public int getId() { return id; }
    public String getTicketType() { return ticketType; }
    public double getPrice() { return price; }
    public String getPurchaseDate() { return purchaseDate; }
    public String getValidFromDate() { return validFromDate; }
    public String getValidToDate() { return validToDate; }
    public String getSeatNumber() { return seatNumber; }
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getEventDescription() { return eventDescription; }
    public String getEventStartDate() { return eventStartDate; }
    public String getEventEndDate() { return eventEndDate; }
    public int getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }
    public String getLocationAddress() { return locationAddress; }
}

