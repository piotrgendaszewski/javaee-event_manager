package REST;

import dao.hibernate.TicketHibernate;
import dao.hibernate.LocationHibernate;
import dto.LocationPrivateDTO;
import dto.PrivateDTOMapper;
import dto.TicketPrivateDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import model.Location;
import model.Ticket;
import service.LocationService;
import service.TicketService;

import java.util.List;
import java.util.stream.Collectors;


@Path("/private")
@Produces("application/json")
@Consumes("application/json")
public class AuthenticatedUserResource {

    private final TicketService ticketService;
    private final LocationService locationService;

    public AuthenticatedUserResource() {
        this.ticketService = new TicketService(new TicketHibernate());
        this.locationService = new LocationService(new LocationHibernate());
    }

    // ===== TICKETS =====

    @GET
    @Path("/tickets")
    public List<TicketPrivateDTO> getUserTickets(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(userId);
        return PrivateDTOMapper.toTicketDTOList(tickets);
    }

    @GET
    @Path("/tickets/search")
    public List<TicketPrivateDTO> searchUserTickets(
            @QueryParam("validOnly") Boolean validOnly,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);
        String today = java.time.LocalDate.now().toString();

        // Filter by valid only
        if (validOnly != null && validOnly) {
            userTickets = userTickets.stream()
                    .filter(t -> {
                        String validFrom = t.getValidFromDate();
                        String validTo = t.getValidToDate();
                        return (validFrom == null || validFrom.compareTo(today) <= 0) &&
                               (validTo == null || validTo.compareTo(today) >= 0);
                    })
                    .collect(Collectors.toList());
        }

        // Filter by start date (ticket's validFromDate >= startDate)
        if (startDate != null && !startDate.trim().isEmpty()) {
            userTickets = userTickets.stream()
                    .filter(t -> t.getValidFromDate() != null && t.getValidFromDate().compareTo(startDate) >= 0)
                    .collect(Collectors.toList());
        }

        // Filter by end date (ticket's validToDate <= endDate)
        if (endDate != null && !endDate.trim().isEmpty()) {
            userTickets = userTickets.stream()
                    .filter(t -> t.getValidToDate() != null && t.getValidToDate().compareTo(endDate) <= 0)
                    .collect(Collectors.toList());
        }

        return PrivateDTOMapper.toTicketDTOList(userTickets);
    }

    @GET
    @Path("/tickets/{ticketId}")
    public TicketPrivateDTO getUserTicket(@PathParam("ticketId") int ticketId,
                                @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        Ticket ticket = ticketService.getTicket(ticketId);
        if (ticket == null) {
            throw new NotFoundException("Ticket not found");
        }

        // Verify ownership
        if (ticket.getUser().getId() != userId) {
            throw new ForbiddenException("You don't have access to this ticket");
        }

        return PrivateDTOMapper.toTicketDTO(ticket);
    }

    // ===== LOCATIONS =====

    @GET
    @Path("/locations")
    public List<LocationPrivateDTO> getUserLocations(@Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        // Get user's tickets
        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);

        // Extract unique locations from user's tickets (deduplicate by ID using LinkedHashSet)
        java.util.Set<Integer> seenLocationIds = new java.util.LinkedHashSet<>();
        List<Location> userLocations = userTickets.stream()
                .map(ticket -> ticket.getEvent().getLocations())
                .flatMap(List::stream)
                .filter(location -> seenLocationIds.add(location.getId()))
                .collect(Collectors.toList());

        return PrivateDTOMapper.toLocationDTOList(userLocations, userTickets);
    }

    @GET
    @Path("/locations/{locationId}")
    public LocationPrivateDTO getUserLocation(@PathParam("locationId") int locationId,
                                             @Context ContainerRequestContext requestContext) {
        Integer userId = (Integer) requestContext.getProperty("userId");

        if (userId == null) {
            throw new ForbiddenException("User ID not found in token");
        }

        Location location = locationService.getLocation(locationId);
        if (location == null) {
            throw new NotFoundException("Location not found with id: " + locationId);
        }

        List<Ticket> userTickets = ticketService.getTicketsByUser(userId);
        return PrivateDTOMapper.toLocationDTO(location, userTickets);
    }
}



