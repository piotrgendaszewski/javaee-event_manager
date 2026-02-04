package service;

import dao.TicketDAO;
import model.Ticket;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TicketService
 */
class TicketServiceTest {

    private TicketService ticketService;
    private TestTicketDAO testTicketDAO;

    @BeforeEach
    void setUp() {
        testTicketDAO = new TestTicketDAO();
        ticketService = new TicketService(testTicketDAO);
    }

    @Test
    void testAddTicket() {
        Ticket result = ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");

        assertNotNull(result);
        assertEquals("Standard", result.getTicketType());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    void testGetTicketById() {
        Ticket ticket = ticketService.addTicket(1, 1, "VIP", 100.0, "2026-04-01", "2026-05-01", "2026-05-31");
        Ticket result = ticketService.getTicket(ticket.getId());

        assertNotNull(result);
        assertEquals("VIP", result.getTicketType());
    }

    @Test
    void testUpdateTicket() {
        Ticket ticket = ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticket.setPrice(75.0);

        assertDoesNotThrow(() -> ticketService.updateTicket(ticket));
    }

    @Test
    void testDeleteTicket() {
        Ticket ticket = ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");

        assertDoesNotThrow(() -> ticketService.deleteTicket(ticket));
    }

    @Test
    void testGetAllTickets() {
        ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticketService.addTicket(1, 2, "VIP", 100.0, "2026-04-01", "2026-05-01", "2026-05-31");

        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(2, result.size());
    }

    @Test
    void testGetTicketsByEventId() {
        ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");

        List<Ticket> result = ticketService.getTicketsByEvent(1);

        assertNotNull(result);
    }

    @Test
    void testGetTicketsByUserId() {
        ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");

        List<Ticket> result = ticketService.getTicketsByUser(1);

        assertNotNull(result);
    }

    @Test
    void testCountTicketsByEventAndType() {
        ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticketService.addTicket(1, 2, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");

        int result = ticketService.countTicketsByEventAndType(1, "Standard");

        assertEquals(2, result);
    }

    @Test
    void testGetTicketByEventAndSeat() {
        Ticket ticket = ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticket.setSeatNumber("A1");

        Ticket result = ticketService.getTicketByEventAndSeat(1, "A1");

        assertNotNull(result);
        assertEquals("A1", result.getSeatNumber());
    }

    @Test
    void testIsTicketForSeatExists() {
        Ticket ticket = ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticket.setSeatNumber("A1");

        boolean result = ticketService.isTicketForSeatExists(1, "A1");

        assertTrue(result);
    }

    @Test
    void testGetUsersByEventId() {
        List<User> result = ticketService.getUsersByEvent(1);

        assertNotNull(result);
    }

    @Test
    void testGetRemainingTicketsByEvent() {
        ticketService.addTicket(1, 1, "Standard", 50.0, "2026-04-01", "2026-05-01", "2026-05-31");
        ticketService.addTicket(1, 2, "VIP", 100.0, "2026-04-01", "2026-05-01", "2026-05-31");

        Map<String, Integer> result = ticketService.getRemainingTicketsByEvent(1);

        assertNotNull(result);
    }

    @Test
    void testCommit() {
        assertDoesNotThrow(() -> ticketService.commit());
    }

    @Test
    void testRollback() {
        assertDoesNotThrow(() -> ticketService.rollback());
    }

    private static class TestTicketDAO implements TicketDAO {
        private List<Ticket> tickets = new ArrayList<>();

        @Override
        public void rollback() {}
        @Override
        public void commit() {}
        @Override
        public Ticket addTicket(int eventId, int userId, String ticketType, double price, String purchaseDate, String validFromDate, String validToDate) {
            Ticket ticket = new Ticket();
            ticket.setId(tickets.size() + 1);
            ticket.setTicketType(ticketType);
            ticket.setPrice(price);
            tickets.add(ticket);
            return ticket;
        }
        @Override
        public Ticket getTicketById(int id) {
            return tickets.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        }
        @Override
        public void updateTicket(Ticket ticket) {}
        @Override
        public void deleteTicket(Ticket ticket) {
            tickets.removeIf(t -> t.getId() == ticket.getId());
        }
        @Override
        public List<Ticket> getAllTickets() {
            return new ArrayList<>(tickets);
        }
        @Override
        public List<Ticket> getTicketsByEventId(int eventId) {
            return new ArrayList<>();
        }
        @Override
        public List<Ticket> getTicketsByUserId(int userId) {
            return new ArrayList<>();
        }
        @Override
        public int countTicketsByEventAndType(int eventId, String ticketType) {
            return (int) tickets.stream().filter(t -> t.getTicketType() != null && t.getTicketType().equals(ticketType)).count();
        }
        @Override
        public Ticket getTicketByEventAndSeat(int eventId, String seatNumber) {
            return tickets.stream().filter(t -> t.getSeatNumber() != null && t.getSeatNumber().equals(seatNumber)).findFirst().orElse(null);
        }
        @Override
        public boolean isTicketForSeatExists(int eventId, String seatNumber) {
            return tickets.stream().anyMatch(t -> t.getSeatNumber() != null && t.getSeatNumber().equals(seatNumber));
        }
        @Override
        public List<User> getUsersByEventId(int eventId) {
            return new ArrayList<>();
        }
        @Override
        public Map<String, Integer> getRemainingTicketsByEvent(int eventId) {
            return new HashMap<>();
        }
    }
}


