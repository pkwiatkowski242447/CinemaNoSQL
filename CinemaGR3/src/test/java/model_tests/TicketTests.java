package model_tests;

import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTests {

    private final double movieBasePrice = 40;
    private final ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 90);
    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Pulp Fiction", movieBasePrice, screeningRoom);
    private final Movie movieNo2 = null;
    private final Client clientNo1 = new Client(UUID.randomUUID(), "Jules", "Winnfield", 74);
    private final Client clientNo2 = null;
    private Ticket testTicket;
    private Ticket newTestTicket;

    @BeforeEach
    public void init() throws ParseException, TicketReservationException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);
        testTicket = new Ticket(UUID.randomUUID(), movieTime, reservationTime, movieNo1, clientNo1, "normal");
        newTestTicket = new Ticket(testTicket.getTicketID(),
                testTicket.getMovieTime(),
                testTicket.getReservationTime(),
                testTicket.getMovie(),
                testTicket.getClient(),
                "normal");
        newTestTicket.setTypeOfTicket(testTicket.getTicketType());
    }

    @Test
    public void ticketConstructorAndGettersTestPositive() throws ParseException, TicketReservationException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketIDNo1 = UUID.randomUUID();

        Ticket ticketNo1 = new Ticket(ticketIDNo1, movieTime, reservationTime, movieNo1, clientNo1, "normal");

        assertNotNull(ticketNo1);

        assertEquals(ticketIDNo1, ticketNo1.getTicketID());
        assertEquals(movieTime, ticketNo1.getMovieTime());
        assertEquals(reservationTime, ticketNo1.getReservationTime());
        assertTrue(ticketNo1.isTicketStatusActive());
        assertEquals(clientNo1, ticketNo1.getClient());
        assertEquals(movieNo1, ticketNo1.getMovie());
        assertEquals(Normal.class, ticketNo1.getTicketType().getClass());
        assertEquals(movieBasePrice, ticketNo1.getTicketFinalPrice());

        UUID ticketIDNo2 = UUID.randomUUID();

        Ticket ticketNo2 = new Ticket(ticketIDNo2, movieTime, reservationTime, movieNo1, clientNo1, "reduced");

        assertNotNull(ticketNo2);

        assertEquals(ticketIDNo2, ticketNo2.getTicketID());
        assertEquals(movieTime, ticketNo2.getMovieTime());
        assertEquals(reservationTime, ticketNo2.getReservationTime());
        assertTrue(ticketNo2.isTicketStatusActive());
        assertEquals(clientNo1, ticketNo2.getClient());
        assertEquals(movieNo1, ticketNo2.getMovie());
        assertEquals(Reduced.class, ticketNo2.getTicketType().getClass());
        assertEquals(movieBasePrice * 0.75, ticketNo2.getTicketFinalPrice());
    }

    @Test
    public void ticketSetterTicketStatusActiveTest() throws ParseException, TicketReservationException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();
        boolean ticketStatusActive = false;

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, "normal");

        assertNotNull(ticket);
        assertTrue(ticket.isTicketStatusActive());

        ticket.setTicketStatusActive(ticketStatusActive);

        assertNotNull(ticket);
        assertFalse(ticket.isTicketStatusActive());
    }

    @Test
    public void getTicketInfoTestStatusActive() throws ParseException, TicketReservationException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, "normal");

        assertNotNull(ticket);
        assertNotNull(ticket.getTicketInfo());
        assertNotEquals("", ticket.getTicketInfo());
    }

    @Test
    public void getTicketInfoTestStatusNotActive() throws ParseException, TicketReservationException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, "normal");
        ticket.setTicketStatusActive(false);

        assertNotNull(ticket);
        assertNotNull(ticket.getTicketInfo());
        assertNotEquals("", ticket.getTicketInfo());
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
        testTicket.setTicketStatusActive(false);
        int hashCodeNo1 = testTicket.hashCode();
        int hashCodeNo2 = newTestTicket.hashCode();
        assertNotEquals(hashCodeNo1, hashCodeNo2);
    }
}
