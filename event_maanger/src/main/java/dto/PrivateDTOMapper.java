package dto;

import model.Location;
import model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper do konwersji model entities na DTO dla authenticated user endpoints
 * Zapobiega zaciąganiu zagnieżdżonych kolekcji
 */
public class PrivateDTOMapper {

    public static TicketPrivateDTO toTicketDTO(Ticket ticket) {
        if (ticket == null) return null;

        int eventId = 0;
        String eventName = null;
        String eventDescription = null;
        String eventStartDate = null;
        String eventEndDate = null;

        if (ticket.getEvent() != null) {
            eventId = ticket.getEvent().getId();
            eventName = ticket.getEvent().getName();
            eventDescription = ticket.getEvent().getDescription();
            eventStartDate = ticket.getEvent().getEventStartDate();
            eventEndDate = ticket.getEvent().getEventEndDate();
        }

        int locationId = 0;
        String locationName = null;
        String locationAddress = null;

        // Get location from event's first location
        if (ticket.getEvent() != null && ticket.getEvent().getLocations() != null
            && !ticket.getEvent().getLocations().isEmpty()) {
            Location location = ticket.getEvent().getLocations().get(0);
            locationId = location.getId();
            locationName = location.getName();
            locationAddress = location.getAddress();
        }

        return new TicketPrivateDTO(
                ticket.getId(),
                ticket.getTicketType(),
                ticket.getPrice(),
                ticket.getPurchaseDate(),
                ticket.getValidFromDate(),
                ticket.getValidToDate(),
                ticket.getSeatNumber(),
                eventId,
                eventName,
                eventDescription,
                eventStartDate,
                eventEndDate,
                locationId,
                locationName,
                locationAddress
        );
    }


    /**
     * Convert Location to LocationPrivateDTO with user's tickets from that location
     * @param location Location entity
     * @param userTickets List of user's tickets filtered for this location
     */
    public static LocationPrivateDTO toLocationDTO(Location location, List<Ticket> userTickets) {
        if (location == null) return null;

        List<LocationPrivateDTO.TicketAtLocationDTO> ticketsList = userTickets.stream()
                .filter(t -> t.getEvent() != null && t.getEvent().getLocations() != null
                        && t.getEvent().getLocations().stream()
                            .anyMatch(l -> l.getId() == location.getId()))
                .map(t -> new LocationPrivateDTO.TicketAtLocationDTO(
                        t.getId(),
                        t.getEvent().getId(),
                        t.getEvent().getName(),
                        t.getTicketType(),
                        t.getPrice(),
                        t.getValidFromDate(),
                        t.getValidToDate()
                ))
                .collect(Collectors.toList());

        return new LocationPrivateDTO(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getMaxAvailableSeats(),
                ticketsList
        );
    }

    public static List<TicketPrivateDTO> toTicketDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(PrivateDTOMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    public static List<LocationPrivateDTO> toLocationDTOList(List<Location> locations, List<Ticket> userTickets) {
        return locations.stream()
                .map(loc -> toLocationDTO(loc, userTickets))
                .collect(Collectors.toList());
    }
}

