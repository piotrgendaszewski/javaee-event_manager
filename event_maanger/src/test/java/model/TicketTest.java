package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    private Ticket ticket;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        event = new Event("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", false);
        user = new User("john_doe", "john@example.com");
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(ticket);
        assertEquals(0, ticket.getId());
        assertNull(ticket.getEvent());
        assertNull(ticket.getUser());
    }

    @Test
    void testConstructorWithBasicFields() {
        Ticket testTicket = new Ticket(event, user, "Standard", 50.0);
        assertNotNull(testTicket);
        assertEquals(event, testTicket.getEvent());
        assertEquals(user, testTicket.getUser());
        assertEquals("Standard", testTicket.getTicketType());
        assertEquals(50.0, testTicket.getPrice());
    }

    @Test
    void testConstructorWithSeatNumber() {
        Ticket testTicket = new Ticket(event, user, "VIP", 100.0, "A1");
        assertNotNull(testTicket);
        assertEquals("A1", testTicket.getSeatNumber());
        assertEquals("VIP", testTicket.getTicketType());
    }

    @Test
    void testConstructorWithAllFields() {
        Ticket testTicket = new Ticket(event, user, "Premium", 150.0, "B5", "2026-05-01", "2026-05-31");
        assertNotNull(testTicket);
        assertEquals("Premium", testTicket.getTicketType());
        assertEquals(150.0, testTicket.getPrice());
        assertEquals("B5", testTicket.getSeatNumber());
        assertEquals("2026-05-01", testTicket.getValidFromDate());
        assertEquals("2026-05-31", testTicket.getValidToDate());
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        ticket.setId(1);
        assertEquals(1, ticket.getId());
    }

    @Test
    void setId() {
        ticket.setId(999);
        assertEquals(999, ticket.getId());
    }

    // ===== EVENT TESTS =====

    @Test
    void getEvent() {
        ticket.setEvent(event);
        assertEquals(event, ticket.getEvent());
        assertEquals("Concert", ticket.getEvent().getName());
    }

    @Test
    void setEvent() {
        Event newEvent = new Event("Festival", "Music festival", "2026-06-01", "10:00", "2026-06-01", "2026-06-01", true);
        ticket.setEvent(newEvent);
        assertEquals(newEvent, ticket.getEvent());
    }

    @Test
    void setEventNull() {
        ticket.setEvent(null);
        assertNull(ticket.getEvent());
    }

    // ===== USER TESTS =====

    @Test
    void getUser() {
        ticket.setUser(user);
        assertEquals(user, ticket.getUser());
        assertEquals("john_doe", ticket.getUser().getLogin());
    }

    @Test
    void setUser() {
        User newUser = new User("jane_smith", "jane@example.com");
        ticket.setUser(newUser);
        assertEquals(newUser, ticket.getUser());
    }

    @Test
    void setUserNull() {
        ticket.setUser(null);
        assertNull(ticket.getUser());
    }

    // ===== TICKET TYPE TESTS =====

    @Test
    void getTicketType() {
        ticket.setTicketType("Student");
        assertEquals("Student", ticket.getTicketType());
    }

    @Test
    void setTicketType() {
        ticket.setTicketType("Senior");
        assertEquals("Senior", ticket.getTicketType());
    }

    @Test
    void setTicketTypeNull() {
        ticket.setTicketType(null);
        assertNull(ticket.getTicketType());
    }

    // ===== PRICE TESTS =====

    @Test
    void getPrice() {
        ticket.setPrice(75.50);
        assertEquals(75.50, ticket.getPrice());
    }

    @Test
    void setPrice() {
        ticket.setPrice(99.99);
        assertEquals(99.99, ticket.getPrice());
    }

    @Test
    void setPriceZero() {
        ticket.setPrice(0);
        assertEquals(0, ticket.getPrice());
    }

    @Test
    void setPriceNegative() {
        ticket.setPrice(-10.0);
        assertEquals(-10.0, ticket.getPrice());  // No validation, just storage
    }

    // ===== SEAT NUMBER TESTS =====

    @Test
    void getSeatNumber() {
        ticket.setSeatNumber("A1");
        assertEquals("A1", ticket.getSeatNumber());
    }

    @Test
    void setSeatNumber() {
        ticket.setSeatNumber("C15");
        assertEquals("C15", ticket.getSeatNumber());
    }

    @Test
    void setSeatNumberNull() {
        ticket.setSeatNumber(null);
        assertNull(ticket.getSeatNumber());
    }

    @Test
    void setSeatNumberEmpty() {
        ticket.setSeatNumber("");
        assertEquals("", ticket.getSeatNumber());
    }

    // ===== PURCHASE DATE TESTS =====

    @Test
    void getPurchaseDate() {
        ticket.setPurchaseDate("2026-04-01");
        assertEquals("2026-04-01", ticket.getPurchaseDate());
    }

    @Test
    void setPurchaseDate() {
        ticket.setPurchaseDate("2026-04-15");
        assertEquals("2026-04-15", ticket.getPurchaseDate());
    }

    // ===== VALID FROM DATE TESTS =====

    @Test
    void getValidFromDate() {
        ticket.setValidFromDate("2026-05-01");
        assertEquals("2026-05-01", ticket.getValidFromDate());
    }

    @Test
    void setValidFromDate() {
        ticket.setValidFromDate("2026-06-01");
        assertEquals("2026-06-01", ticket.getValidFromDate());
    }

    // ===== VALID TO DATE TESTS =====

    @Test
    void getValidToDate() {
        ticket.setValidToDate("2026-05-31");
        assertEquals("2026-05-31", ticket.getValidToDate());
    }

    @Test
    void setValidToDate() {
        ticket.setValidToDate("2026-12-31");
        assertEquals("2026-12-31", ticket.getValidToDate());
    }

    // ===== VALIDATION TESTS =====

    @Test
    void testIsValidWithoutSeats() {
        Event nonSeatedEvent = new Event("Concert", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", false);
        ticket.setEvent(nonSeatedEvent);
        ticket.setSeatNumber(null);
        assertTrue(ticket.isValid());  // Valid even without seat number
    }

    @Test
    void testIsValidWithSeatsNoSeatNumber() {
        Event seatedEvent = new Event("Theater", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        ticket.setEvent(seatedEvent);
        ticket.setSeatNumber(null);
        assertFalse(ticket.isValid());  // Invalid - requires seat number
    }

    @Test
    void testIsValidWithSeatsAndSeatNumber() {
        Event seatedEvent = new Event("Theater", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        ticket.setEvent(seatedEvent);
        ticket.setSeatNumber("A1");
        assertTrue(ticket.isValid());  // Valid with seat number
    }

    @Test
    void testIsValidWithEmptySeatNumber() {
        Event seatedEvent = new Event("Theater", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        ticket.setEvent(seatedEvent);
        ticket.setSeatNumber("");
        assertFalse(ticket.isValid());  // Invalid - empty seat number
    }

    @Test
    void testValidateForNumberedSeatsSuccess() {
        Event seatedEvent = new Event("Theater", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        ticket.setEvent(seatedEvent);
        ticket.setSeatNumber("B5");

        assertDoesNotThrow(() -> ticket.validateForNumberedSeats());
    }

    @Test
    void testValidateForNumberedSeatsFail() {
        Event seatedEvent = new Event("Theater", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        ticket.setEvent(seatedEvent);
        ticket.setSeatNumber(null);

        assertThrows(IllegalArgumentException.class, () -> ticket.validateForNumberedSeats());
    }

    @Test
    void testValidateForNumberedSeatsNonSeatedEvent() {
        Event nonSeatedEvent = new Event("Concert", "Description", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", false);
        ticket.setEvent(nonSeatedEvent);
        ticket.setSeatNumber(null);

        assertDoesNotThrow(() -> ticket.validateForNumberedSeats());  // Should not throw for non-seated events
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        event.setId(1);
        user.setId(5);
        ticket.setId(10);
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setTicketType("Standard");
        ticket.setPrice(50.0);
        ticket.setSeatNumber("A1");
        ticket.setPurchaseDate("2026-04-01");
        ticket.setValidFromDate("2026-05-01");
        ticket.setValidToDate("2026-05-31");

        String result = ticket.toString();
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("eventId=1"));
        assertTrue(result.contains("userId=5"));
        assertTrue(result.contains("ticketType='Standard'"));
        assertTrue(result.contains("price=50.0"));
        assertTrue(result.contains("seatNumber='A1'"));
    }

    @Test
    void testToStringNullReferences() {
        ticket.setId(20);
        ticket.setEvent(null);
        ticket.setUser(null);
        ticket.setTicketType("Premium");

        String result = ticket.toString();
        assertTrue(result.contains("id=20"));
        assertTrue(result.contains("eventId=0"));
        assertTrue(result.contains("userId=0"));
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullTicketFlow() {
        event.setId(1);
        user.setId(1);

        Ticket testTicket = new Ticket(event, user, "VIP", 150.0, "A1", "2026-05-01", "2026-05-31");
        testTicket.setId(1);
        testTicket.setPurchaseDate("2026-04-15");

        assertEquals(1, testTicket.getId());
        assertEquals(event, testTicket.getEvent());
        assertEquals(user, testTicket.getUser());
        assertEquals("VIP", testTicket.getTicketType());
        assertEquals(150.0, testTicket.getPrice());
        assertEquals("A1", testTicket.getSeatNumber());
        assertEquals("2026-04-15", testTicket.getPurchaseDate());
        assertTrue(testTicket.isValid());
    }

    @Test
    void testTwoTicketsAreIndependent() {
        Event event1 = new Event("Event1", "Desc1", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", false);
        Event event2 = new Event("Event2", "Desc2", "2026-06-01", "20:00", "2026-06-01", "2026-06-01", true);

        Ticket ticket1 = new Ticket(event1, user, "Standard", 50.0);
        Ticket ticket2 = new Ticket(event2, user, "Premium", 100.0, "B5");

        assertEquals("Standard", ticket1.getTicketType());
        assertEquals("Premium", ticket2.getTicketType());
        assertNull(ticket1.getSeatNumber());
        assertNotNull(ticket2.getSeatNumber());
    }

    @Test
    void testMultipleTicketsForSameEvent() {
        event.setId(1);
        user.setId(1);

        Ticket ticket1 = new Ticket(event, user, "Standard", 50.0);
        Ticket ticket2 = new Ticket(event, user, "VIP", 100.0);
        Ticket ticket3 = new Ticket(event, user, "Premium", 150.0);

        assertEquals(event, ticket1.getEvent());
        assertEquals(event, ticket2.getEvent());
        assertEquals(event, ticket3.getEvent());
        assertEquals(50.0, ticket1.getPrice());
        assertEquals(100.0, ticket2.getPrice());
        assertEquals(150.0, ticket3.getPrice());
    }
}

