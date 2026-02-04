package service;

import dto.EventReviewDTO;
import dto.LocationPrivateDTO;
import dto.TicketPrivateDTO;
import model.Event;
import model.Location;
import model.Ticket;
import model.EventReview;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PrivateDTOMapper
 */
class PrivateDTOMapperTest {

    @Test
    void testToTicketDTOSuccess() {
        Event event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(1);

        User user = new User("testuser", "test@example.com");
        user.setId(1);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTicketType("Standard");
        ticket.setPrice(50.0);
        ticket.setEvent(event);
        ticket.setUser(user);

        TicketPrivateDTO result = PrivateDTOMapper.toTicketDTO(ticket);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Standard", result.getTicketType());
        assertEquals(50.0, result.getPrice());
        assertEquals(1, result.getEventId());
        assertEquals("Concert", result.getEventName());
    }

    @Test
    void testToTicketDTONull() {
        TicketPrivateDTO result = PrivateDTOMapper.toTicketDTO(null);

        assertNull(result);
    }

    @Test
    void testToLocationDTOSuccess() {
        Location location = new Location(1, "Pałac Kultury", "pl. Defilad 1, Warszawa");
        location.setMaxAvailableSeats(5000);

        List<Ticket> userTickets = new ArrayList<>();

        LocationPrivateDTO result = PrivateDTOMapper.toLocationDTO(location, userTickets);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Pałac Kultury", result.getName());
        assertEquals("pl. Defilad 1, Warszawa", result.getAddress());
    }

    @Test
    void testToLocationDTONull() {
        LocationPrivateDTO result = PrivateDTOMapper.toLocationDTO(null, new ArrayList<>());

        assertNull(result);
    }

    @Test
    void testToReviewDTOSuccess() {
        Event event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(1);

        User user = new User("reviewer", "reviewer@example.com");
        user.setId(1);

        EventReview review = new EventReview();
        review.setId(1);
        review.setRating(5);
        review.setReviewText("Excellent!");
        review.setReviewDate("2026-05-20");
        review.setEvent(event);
        review.setUser(user);

        EventReviewDTO result = PrivateDTOMapper.toReviewDTO(review);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(5, result.getRating());
        assertEquals("Excellent!", result.getReviewText());
    }

    @Test
    void testToReviewDTONull() {
        EventReviewDTO result = PrivateDTOMapper.toReviewDTO(null);

        assertNull(result);
    }

    @Test
    void testToTicketDTOListSuccess() {
        Event event = new Event("Event", "Description", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", true);
        event.setId(1);

        User user = new User("user", "user@example.com");
        user.setId(1);

        Ticket ticket1 = new Ticket();
        ticket1.setId(1);
        ticket1.setTicketType("Standard");
        ticket1.setPrice(50.0);
        ticket1.setEvent(event);
        ticket1.setUser(user);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2);
        ticket2.setTicketType("VIP");
        ticket2.setPrice(100.0);
        ticket2.setEvent(event);
        ticket2.setUser(user);

        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

        List<TicketPrivateDTO> result = PrivateDTOMapper.toTicketDTOList(tickets);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToLocationDTOListSuccess() {
        Location loc1 = new Location(1, "Location1", "Address1");
        Location loc2 = new Location(2, "Location2", "Address2");
        List<Location> locations = Arrays.asList(loc1, loc2);

        List<Ticket> userTickets = new ArrayList<>();

        List<LocationPrivateDTO> result = PrivateDTOMapper.toLocationDTOList(locations, userTickets);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToReviewDTOListSuccess() {
        Event event = new Event("Event", "Description", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", true);
        event.setId(1);

        User user1 = new User("user1", "user1@example.com");
        user1.setId(1);

        User user2 = new User("user2", "user2@example.com");
        user2.setId(2);

        EventReview review1 = new EventReview();
        review1.setId(1);
        review1.setRating(5);
        review1.setReviewText("Great");
        review1.setEvent(event);
        review1.setUser(user1);

        EventReview review2 = new EventReview();
        review2.setId(2);
        review2.setRating(4);
        review2.setReviewText("Good");
        review2.setEvent(event);
        review2.setUser(user2);

        List<EventReview> reviews = Arrays.asList(review1, review2);

        List<EventReviewDTO> result = PrivateDTOMapper.toReviewDTOList(reviews);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToTicketDTOListEmpty() {
        List<TicketPrivateDTO> result = PrivateDTOMapper.toTicketDTOList(new ArrayList<>());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testDataIntegrityAfterTicketMapping() {
        Event event = new Event("Test Event", "Test Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(5);

        User user = new User("testuser", "test@example.com");
        user.setId(10);

        Ticket originalTicket = new Ticket();
        originalTicket.setId(20);
        originalTicket.setTicketType("Premium");
        originalTicket.setPrice(150.0);
        originalTicket.setSeatNumber("A1");
        originalTicket.setEvent(event);
        originalTicket.setUser(user);
        originalTicket.setValidFromDate("2026-05-15");
        originalTicket.setValidToDate("2026-05-31");

        TicketPrivateDTO dto = PrivateDTOMapper.toTicketDTO(originalTicket);

        assertEquals(originalTicket.getId(), dto.getId());
        assertEquals(originalTicket.getTicketType(), dto.getTicketType());
        assertEquals(originalTicket.getPrice(), dto.getPrice());
        assertEquals(originalTicket.getSeatNumber(), dto.getSeatNumber());
        assertEquals(5, dto.getEventId());
        assertEquals("Test Event", dto.getEventName());
    }

    @Test
    void testDataIntegrityAfterLocationMapping() {
        Location originalLocation = new Location(10, "Original Location", "Original Address");
        originalLocation.setDescription("Test Description");
        originalLocation.setMaxAvailableSeats(1000);

        LocationPrivateDTO dto = PrivateDTOMapper.toLocationDTO(originalLocation, new ArrayList<>());

        assertEquals(originalLocation.getId(), dto.getId());
        assertEquals(originalLocation.getName(), dto.getName());
        assertEquals(originalLocation.getAddress(), dto.getAddress());
    }

    @Test
    void testDataIntegrityAfterReviewMapping() {
        Event event = new Event("Original Event", "Original Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(30);

        User user = new User("reviewer", "reviewer@example.com");
        user.setId(40);

        EventReview originalReview = new EventReview();
        originalReview.setId(50);
        originalReview.setRating(4);
        originalReview.setReviewText("Good review");
        originalReview.setReviewDate("2026-05-20");
        originalReview.setEvent(event);
        originalReview.setUser(user);

        EventReviewDTO dto = PrivateDTOMapper.toReviewDTO(originalReview);

        assertEquals(originalReview.getId(), dto.getId());
        assertEquals(originalReview.getRating(), dto.getRating());
        assertEquals(originalReview.getReviewText(), dto.getReviewText());
        assertEquals(30, dto.getEventId());
        assertEquals(40, dto.getUserId());
    }

    @Test
    void testToLocationDTOWithUserTickets() {
        Location location = new Location(1, "Concert Hall", "Main Street");

        Event event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        event.setId(1);
        List<Location> locations = new ArrayList<>();
        locations.add(location);
        event.setLocations(locations);

        User user = new User("ticketholder", "ticket@example.com");
        user.setId(1);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTicketType("Standard");
        ticket.setPrice(50.0);
        ticket.setEvent(event);
        ticket.setUser(user);

        List<Ticket> userTickets = Arrays.asList(ticket);

        LocationPrivateDTO result = PrivateDTOMapper.toLocationDTO(location, userTickets);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Concert Hall", result.getName());
    }
}


