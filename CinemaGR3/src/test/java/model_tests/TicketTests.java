package model_tests;

import model.Client;
import model.Movie;
import model.Ticket;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTests {

    private final double movieBasePrice = 40;
    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Pulp Fiction", 37.75, 1, 90);
    private final Movie movieNo2 = null;
    private final Client clientNo1 = new Client(UUID.randomUUID(), "Jules", "Winnfield", 74);
    private final Client clientNo2 = null;
    private Ticket testTicket;
    private Ticket newTestTicket;

    @BeforeEach
    public void init() {
        Instant reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        testTicket = new Normal(UUID.randomUUID(), movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());
        newTestTicket = new Normal(testTicket.getTicketID(),
                testTicket.getMovieTime(),
                testTicket.getReservationTime(),
                testTicket.getTicketBasePrice(),
                testTicket.getMovieId(),
                testTicket.getClientId()
        );
    }

    @Test
    public void ticketConstructorAndGettersTestPositive() {
        Instant reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        UUID ticketIDNo1 = UUID.randomUUID();

        Ticket ticketNo1 = new Normal(ticketIDNo1, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticketNo1);

        assertEquals(ticketIDNo1, ticketNo1.getTicketID());
        assertEquals(movieTime, ticketNo1.getMovieTime());
        assertEquals(reservationTime, ticketNo1.getReservationTime());
        assertEquals(clientNo1.getClientID(), ticketNo1.getClientId());
        assertEquals(movieNo1.getMovieID(), ticketNo1.getMovieId());
        assertEquals(Normal.class, ticketNo1.getClass());
        assertEquals(movieBasePrice, ticketNo1.getTicketFinalPrice());

        UUID ticketIDNo2 = UUID.randomUUID();

        Ticket ticketNo2 = new Reduced(ticketIDNo2, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticketNo2);

        assertEquals(ticketIDNo2, ticketNo2.getTicketID());
        assertEquals(movieTime, ticketNo2.getMovieTime());
        assertEquals(reservationTime, ticketNo2.getReservationTime());
        assertEquals(clientNo1.getClientID(), ticketNo2.getClientId());
        assertEquals(movieNo1.getMovieID(), ticketNo2.getMovieId());
        assertEquals(Reduced.class, ticketNo2.getClass());
        assertEquals(movieBasePrice * 0.75, ticketNo2.getTicketFinalPrice());
    }

    @Test
    public void ticketToStringTest() {
        Instant reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        UUID ticketID = UUID.randomUUID();

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticket);
        assertNotNull(ticket.toString());
        assertNotEquals("", ticket.toString());
    }

    @Test
    public void ticketEqualsTestWithItself() {
        boolean result = testTicket.equals(testTicket);
        assertTrue(result);
    }

    @Test
    public void ticketEqualsTestWithNull() {
        boolean result = testTicket.equals(null);
        assertFalse(result);
    }

    @Test
    public void ticketEqualsTestWithObjectFromOtherClass() {
        boolean result = testTicket.equals(new Date());
        assertFalse(result);
    }

    @Test
    public void ticketEqualsTestWithTheSameObject() {
        boolean result = testTicket.equals(newTestTicket);
        assertTrue(result);
    }

    @Test
    public void ticketHashCodeTestPositive() {
        int hashCodeNo1 = testTicket.hashCode();
        int hashCodeNo2 = newTestTicket.hashCode();
        assertEquals(hashCodeNo1, hashCodeNo2);
    }

    @Test
    public void ticketHashCodeTestNegative() {
        testTicket.setTicketBasePrice(50);
        int hashCodeNo1 = testTicket.hashCode();
        int hashCodeNo2 = newTestTicket.hashCode();
        assertNotEquals(hashCodeNo1, hashCodeNo2);
    }
}
