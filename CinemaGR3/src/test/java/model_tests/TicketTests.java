package model_tests;

import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;
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
        assertEquals(movieBasePrice, ticketNo1.getTicketFinalPrice());
        assertEquals(movieNo1.getMovieID(), ticketNo1.getMovieId());
        assertEquals(clientNo1.getClientID(), ticketNo1.getClientId());
        assertEquals(Normal.class, ticketNo1.getClass());

        UUID ticketIDNo2 = UUID.randomUUID();

        Ticket ticketNo2 = new Reduced(ticketIDNo2, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticketNo2);

        assertEquals(ticketIDNo2, ticketNo2.getTicketID());
        assertEquals(movieTime, ticketNo2.getMovieTime());
        assertEquals(reservationTime, ticketNo2.getReservationTime());
        assertEquals(movieBasePrice * 0.75, ticketNo2.getTicketFinalPrice());
        assertEquals(movieNo1.getMovieID(), ticketNo2.getMovieId());
        assertEquals(clientNo1.getClientID(), ticketNo2.getClientId());
        assertEquals(Reduced.class, ticketNo2.getClass());
    }

    @Test
    public void movieTimeSetterTestPositive() {
        Instant reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();
        Instant movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 3).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTimeNo1, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticket);
        assertEquals(movieTimeNo1, ticket.getMovieTime());

        ticket.setMovieTime(movieTimeNo2);
        assertEquals(movieTimeNo2, ticket.getMovieTime());
    }

    @Test
    public void reservationTimeSetterTestPositive() {
        Instant reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTimeNo1, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        assertNotNull(ticket);
        assertEquals(reservationTimeNo1, ticket.getReservationTime());

        ticket.setMovieTime(reservationTimeNo2);
        assertEquals(reservationTimeNo1, ticket.getReservationTime());
    }

    @Test
    public void ticketToStringTest() {
        Instant reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        Instant movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();

        UUID ticketID = UUID.randomUUID();

        Ticket ticket = new Normal(ticketID, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

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
