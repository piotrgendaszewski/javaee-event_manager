package service;

import dao.EventDAO;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EventService
 * Tests business logic without mocking DAO (specification-based)
 * Uses in-memory TestEventDAO instead of real Hibernate to avoid database locking issues
 */
class EventServiceTest {

    private EventService eventService;
    private TestEventDAO testEventDAO;

    @BeforeEach
    void setUp() {
        testEventDAO = new TestEventDAO();
        eventService = new EventService(testEventDAO);
    }

    // ...existing code...
    @Test
    void testAddEvent() {
        Event result = eventService.addEvent("Concert", "Live music", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        assertNotNull(result);
        assertEquals("Concert", result.getName());
        assertTrue(result.isNumberedSeats());
    }

    @Test
    void testAddEventWithoutSeats() {
        Event result = eventService.addEvent("Marathon", "City marathon", "2026-07-15", "07:00", "2026-07-15", "2026-07-15", false);

        assertNotNull(result);
        assertEquals("Marathon", result.getName());
        assertFalse(result.isNumberedSeats());
    }

    // ...existing code...
    @Test
    void testGetEvent() {
        Event event = eventService.addEvent("Festival", "Music festival", "2026-06-01", "10:00", "2026-06-01", "2026-06-01", false);

        Event result = eventService.getEvent(event.getId());

        assertNotNull(result);
        assertEquals("Festival", result.getName());
    }

    @Test
    void testGetEventNotFound() {
        Event result = eventService.getEvent(999);

        assertNull(result);
    }

    // ...existing code...
    @Test
    void testGetEventByName() {
        eventService.addEvent("Webinar", "Online webinar", "2026-08-15", "14:00", "2026-08-15", "2026-08-15", false);

        Event result = eventService.getEventByName("Webinar");

        assertNotNull(result);
        assertEquals("Webinar", result.getName());
    }

    @Test
    void testGetEventByNameNotFound() {
        Event result = eventService.getEventByName("NonexistentEvent");

        assertNull(result);
    }

    // ...existing code...
    @Test
    void testUpdateEvent() {
        Event event = eventService.addEvent("Conference", "Tech conference", "2026-09-15", "09:00", "2026-09-15", "2026-09-15", false);
        event.setDescription("Updated description");

        assertDoesNotThrow(() -> eventService.updateEvent(event));
    }

    // ...existing code...
    @Test
    void testDeleteEvent() {
        Event event = eventService.addEvent("Deleted Event", "To be deleted", "2026-10-15", "10:00", "2026-10-15", "2026-10-15", false);

        assertDoesNotThrow(() -> eventService.deleteEvent(event));
    }

    // ...existing code...
    @Test
    void testGetAllEvents() {
        eventService.addEvent("Event1", "Description1", "2026-05-01", "10:00", "2026-05-01", "2026-05-01", true);
        eventService.addEvent("Event2", "Description2", "2026-06-01", "20:00", "2026-06-01", "2026-06-01", false);

        List<Event> result = eventService.getAllEvents();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEventsEmpty() {
        List<Event> result = eventService.getAllEvents();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ...existing code...
    @Test
    void testSetTicketPrice() {
        Event event = eventService.addEvent("Priced Event", "Event with prices", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        assertDoesNotThrow(() -> eventService.setTicketPrice(event.getId(), "Standard", 50.0));
    }

    @Test
    void testGetTicketPrice() {
        Event event = eventService.addEvent("Priced Event", "Event with prices", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        eventService.setTicketPrice(event.getId(), "VIP", 100.0);

        double result = eventService.getTicketPrice(event.getId(), "VIP");

        assertEquals(100.0, result);
    }

    @Test
    void testMultipleTicketPrices() {
        Event event = eventService.addEvent("Multi-price Event", "Event with multiple prices", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        eventService.setTicketPrice(event.getId(), "Standard", 50.0);
        eventService.setTicketPrice(event.getId(), "VIP", 100.0);
        eventService.setTicketPrice(event.getId(), "Premium", 150.0);

        assertEquals(50.0, eventService.getTicketPrice(event.getId(), "Standard"));
        assertEquals(100.0, eventService.getTicketPrice(event.getId(), "VIP"));
        assertEquals(150.0, eventService.getTicketPrice(event.getId(), "Premium"));
    }

    // ...existing code...
    @Test
    void testSetTicketQuantity() {
        Event event = eventService.addEvent("Quantity Event", "Event with quantities", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        assertDoesNotThrow(() -> eventService.setTicketQuantity(event.getId(), "Standard", 100));
    }

    @Test
    void testGetTicketQuantity() {
        Event event = eventService.addEvent("Quantity Event", "Event with quantities", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);
        eventService.setTicketQuantity(event.getId(), "VIP", 50);

        int result = eventService.getTicketQuantity(event.getId(), "VIP");

        assertEquals(50, result);
    }

    @Test
    void testMultipleTicketQuantities() {
        Event event = eventService.addEvent("Multi-qty Event", "Event with multiple quantities", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        eventService.setTicketQuantity(event.getId(), "Standard", 100);
        eventService.setTicketQuantity(event.getId(), "VIP", 50);
        eventService.setTicketQuantity(event.getId(), "Premium", 25);

        assertEquals(100, eventService.getTicketQuantity(event.getId(), "Standard"));
        assertEquals(50, eventService.getTicketQuantity(event.getId(), "VIP"));
        assertEquals(25, eventService.getTicketQuantity(event.getId(), "Premium"));
    }


    // ...existing code...
    @Test
    void testEventWithPricesAndQuantities() {
        Event event = eventService.addEvent("Full Event", "Event with prices and quantities", "2026-05-15", "19:00", "2026-05-15", "2026-05-15", true);

        eventService.setTicketPrice(event.getId(), "Standard", 50.0);
        eventService.setTicketPrice(event.getId(), "VIP", 100.0);
        eventService.setTicketQuantity(event.getId(), "Standard", 100);
        eventService.setTicketQuantity(event.getId(), "VIP", 50);

        assertEquals(50.0, eventService.getTicketPrice(event.getId(), "Standard"));
        assertEquals(100, eventService.getTicketQuantity(event.getId(), "Standard"));
    }

    // ===== TEST DAO IMPLEMENTATION =====

    /**
     * Test DAO implementation for in-memory testing
     * Avoids database locking issues and state management problems
     */
    private static class TestEventDAO implements EventDAO {
        private List<Event> events = new ArrayList<>();
        private Map<String, Map<String, Object>> ticketData = new HashMap<>();

        @Override
        public void rollback() {
        }

        @Override
        public void commit() {
        }

        @Override
        public Event addEvent(String name, String description, String eventDate, String eventTime, String eventStartDate, String eventEndDate, boolean numberedSeats) {
            Event event = new Event(name, description, eventDate, eventTime, eventStartDate, eventEndDate, numberedSeats);
            event.setId(events.size() + 1);
            events.add(event);
            return event;
        }

        @Override
        public Event getEventById(int id) {
            return events.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        }

        @Override
        public Event getEventByName(String name) {
            return events.stream().filter(e -> e.getName() != null && e.getName().equals(name)).findFirst().orElse(null);
        }

        @Override
        public void updateEvent(Event event) {
        }

        @Override
        public void deleteEvent(Event event) {
            events.removeIf(e -> e.getId() == event.getId());
        }

        @Override
        public List<Event> getAllEvents() {
            return new ArrayList<>(events);
        }

        @Override
        public void setTicketPrice(int eventId, String ticketType, double price) {
            String key = eventId + "_" + ticketType;
            ticketData.computeIfAbsent(key, k -> new HashMap<>()).put("price", price);
        }

        @Override
        public void setTicketQuantity(int eventId, String ticketType, int quantity) {
            String key = eventId + "_" + ticketType;
            ticketData.computeIfAbsent(key, k -> new HashMap<>()).put("quantity", quantity);
        }

        @Override
        public double getTicketPrice(int eventId, String ticketType) {
            String key = eventId + "_" + ticketType;
            Object price = ticketData.getOrDefault(key, new HashMap<>()).get("price");
            return price != null ? (double) price : 0.0;
        }

        @Override
        public int getTicketQuantity(int eventId, String ticketType) {
            String key = eventId + "_" + ticketType;
            Object quantity = ticketData.getOrDefault(key, new HashMap<>()).get("quantity");
            return quantity != null ? (int) quantity : 0;
        }

        @Override
        public double getAverageRatingForEvent(int eventId) {
            return 0.0;
        }
    }
}



