package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(event);
        assertEquals(0, event.getId());
        assertNull(event.getName());
    }

    @Test
    void testConstructorWithAllFields() {
        Event testEvent = new Event("Concert 2026", "Live music concert", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        assertNotNull(testEvent);
        assertEquals("Concert 2026", testEvent.getName());
        assertEquals("Live music concert", testEvent.getDescription());
        assertEquals("2026-05-15", testEvent.getEventDate());
        assertEquals("19:00", testEvent.getEventTime());
        assertEquals("2026-05-15", testEvent.getEventStartDate());
        assertEquals("2026-05-15", testEvent.getEventEndDate());
        assertTrue(testEvent.isNumberedSeats());
    }

    // ===== ID TESTS =====

    @Test
    void getId() {
        event.setId(1);
        assertEquals(1, event.getId());
    }

    @Test
    void setId() {
        event.setId(99);
        assertEquals(99, event.getId());
    }

    // ===== NAME TESTS =====

    @Test
    void getName() {
        event.setName("Festival");
        assertEquals("Festival", event.getName());
    }

    @Test
    void setName() {
        event.setName("Rock Concert");
        assertEquals("Rock Concert", event.getName());
    }

    @Test
    void setNameNull() {
        event.setName(null);
        assertNull(event.getName());
    }

    // ===== DESCRIPTION TESTS =====

    @Test
    void getDescription() {
        event.setDescription("Annual jazz festival");
        assertEquals("Annual jazz festival", event.getDescription());
    }

    @Test
    void setDescription() {
        event.setDescription("New description");
        assertEquals("New description", event.getDescription());
    }

    @Test
    void setDescriptionNull() {
        event.setDescription(null);
        assertNull(event.getDescription());
    }

    // ===== EVENT DATE TESTS =====

    @Test
    void getEventDate() {
        event.setEventDate("2026-06-20");
        assertEquals("2026-06-20", event.getEventDate());
    }

    @Test
    void setEventDate() {
        event.setEventDate("2026-07-10");
        assertEquals("2026-07-10", event.getEventDate());
    }

    // ===== EVENT TIME TESTS =====

    @Test
    void getEventTime() {
        event.setEventTime("18:30");
        assertEquals("18:30", event.getEventTime());
    }

    @Test
    void setEventTime() {
        event.setEventTime("20:00");
        assertEquals("20:00", event.getEventTime());
    }

    // ===== EVENT START DATE TESTS =====

    @Test
    void getEventStartDate() {
        event.setEventStartDate("2026-05-01");
        assertEquals("2026-05-01", event.getEventStartDate());
    }

    @Test
    void setEventStartDate() {
        event.setEventStartDate("2026-06-01");
        assertEquals("2026-06-01", event.getEventStartDate());
    }

    // ===== EVENT END DATE TESTS =====

    @Test
    void getEventEndDate() {
        event.setEventEndDate("2026-05-31");
        assertEquals("2026-05-31", event.getEventEndDate());
    }

    @Test
    void setEventEndDate() {
        event.setEventEndDate("2026-06-30");
        assertEquals("2026-06-30", event.getEventEndDate());
    }

    // ===== NUMBERED SEATS TESTS =====

    @Test
    void isNumberedSeats() {
        event.setNumberedSeats(true);
        assertTrue(event.isNumberedSeats());
    }

    @Test
    void setNumberedSeatsTrue() {
        event.setNumberedSeats(true);
        assertTrue(event.isNumberedSeats());
    }

    @Test
    void setNumberedSeatsFalse() {
        event.setNumberedSeats(false);
        assertFalse(event.isNumberedSeats());
    }

    @Test
    void setNumberedSeatsDefault() {
        // Default should be false
        assertFalse(event.isNumberedSeats());
    }

    // ===== TICKET PRICES TESTS =====

    @Test
    void getTicketPrices() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Standard", 50.0);
        prices.put("VIP", 100.0);
        event.setTicketPrices(prices);

        assertNotNull(event.getTicketPrices());
        assertEquals(2, event.getTicketPrices().size());
        assertEquals(50.0, event.getTicketPrices().get("Standard"));
        assertEquals(100.0, event.getTicketPrices().get("VIP"));
    }

    @Test
    void setTicketPrices() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Economy", 30.0);
        event.setTicketPrices(prices);

        assertEquals(30.0, event.getTicketPrices().get("Economy"));
    }

    @Test
    void setTicketPricesNull() {
        event.setTicketPrices(null);
        assertNull(event.getTicketPrices());
    }

    @Test
    void setTicketPricesEmpty() {
        Map<String, Double> prices = new HashMap<>();
        event.setTicketPrices(prices);
        assertNotNull(event.getTicketPrices());
        assertEquals(0, event.getTicketPrices().size());
    }

    // ===== TICKET QUANTITIES TESTS =====

    @Test
    void getTicketQuantities() {
        Map<String, Integer> quantities = new HashMap<>();
        quantities.put("Standard", 100);
        quantities.put("VIP", 50);
        event.setTicketQuantities(quantities);

        assertNotNull(event.getTicketQuantities());
        assertEquals(2, event.getTicketQuantities().size());
        assertEquals(100, event.getTicketQuantities().get("Standard"));
        assertEquals(50, event.getTicketQuantities().get("VIP"));
    }

    @Test
    void setTicketQuantities() {
        Map<String, Integer> quantities = new HashMap<>();
        quantities.put("Economy", 200);
        event.setTicketQuantities(quantities);

        assertEquals(200, event.getTicketQuantities().get("Economy"));
    }

    @Test
    void setTicketQuantitiesNull() {
        event.setTicketQuantities(null);
        assertNull(event.getTicketQuantities());
    }

    // ===== TOSTRING TESTS =====

    @Test
    void testToString() {
        event.setId(1);
        event.setName("Concert");
        event.setDescription("Live music");
        event.setEventDate("2026-05-15");
        event.setEventTime("19:00");
        event.setEventStartDate("2026-05-15");
        event.setEventEndDate("2026-05-15");
        event.setNumberedSeats(true);

        String result = event.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Concert'"));
        assertTrue(result.contains("description='Live music'"));
        assertTrue(result.contains("eventDate='2026-05-15'"));
        assertTrue(result.contains("eventTime='19:00'"));
        assertTrue(result.contains("eventStartDate='2026-05-15'"));
        assertTrue(result.contains("eventEndDate='2026-05-15'"));
        assertTrue(result.contains("numberedSeats=true"));
    }

    @Test
    void testToStringWithNullValues() {
        event.setId(2);
        event.setName(null);
        event.setDescription(null);

        String result = event.toString();
        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("name='null'"));
        assertTrue(result.contains("description='null'"));
    }

    // ===== INTEGRATION TESTS =====

    @Test
    void testFullEventFlow() {
        Event testEvent = new Event("Marathon", "City marathon 2026", "2026-06-15", "07:00", "2026-06-15", "2026-06-15", false);
        testEvent.setId(1);

        Map<String, Double> prices = new HashMap<>();
        prices.put("Standard", 25.0);
        prices.put("Premium", 50.0);
        testEvent.setTicketPrices(prices);

        Map<String, Integer> quantities = new HashMap<>();
        quantities.put("Standard", 500);
        quantities.put("Premium", 100);
        testEvent.setTicketQuantities(quantities);

        assertEquals(1, testEvent.getId());
        assertEquals("Marathon", testEvent.getName());
        assertEquals(2, testEvent.getTicketPrices().size());
        assertEquals(2, testEvent.getTicketQuantities().size());
        assertFalse(testEvent.isNumberedSeats());
    }

    @Test
    void testTwoEventsAreIndependent() {
        Event event1 = new Event("Event 1", "Description 1", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", true);
        Event event2 = new Event("Event 2", "Description 2", "2026-06-01", "20:00", "2026-06-01", "2026-06-01", false);

        assertNotEquals(event1.getName(), event2.getName());
        assertNotEquals(event1.isNumberedSeats(), event2.isNumberedSeats());
    }

    @Test
    void testEventWithMultipleTicketTypes() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Standard", 50.0);
        prices.put("VIP", 100.0);
        prices.put("Premium", 150.0);

        Map<String, Integer> quantities = new HashMap<>();
        quantities.put("Standard", 100);
        quantities.put("VIP", 50);
        quantities.put("Premium", 25);

        event.setTicketPrices(prices);
        event.setTicketQuantities(quantities);

        assertEquals(3, event.getTicketPrices().size());
        assertEquals(3, event.getTicketQuantities().size());
        assertTrue(event.getTicketPrices().containsKey("VIP"));
        assertTrue(event.getTicketQuantities().containsKey("Premium"));
    }
}

