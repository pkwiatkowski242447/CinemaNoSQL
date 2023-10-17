package model_tests;

import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.ticket_types.Normal;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTests {

    private final ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 90);
    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Pulp Fiction", screeningRoom);
    private final Movie movieNo2 = null;
    private final Client clientNo1 = new Client(UUID.randomUUID(), "Jules", "Winnfield", 74);
    private final Client clientNo2 = null;

    @Test
    public void ticketConstructorAndGettersTestPositive() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketIDNo1 = UUID.randomUUID();
        double basePriceNo1 = 25;
        TypeOfTicket typeOfTicketNo1 = new Normal(UUID.randomUUID(), basePriceNo1);

        Ticket ticketNo1 = new Ticket(ticketIDNo1, movieTime, reservationTime, movieNo1, clientNo1, typeOfTicketNo1);

        assertNotNull(ticketNo1);

        assertEquals(ticketIDNo1, ticketNo1.getTicketID());
        assertEquals(movieTime, ticketNo1.getMovieTime());
        assertEquals(reservationTime, ticketNo1.getReservationTime());
        assertTrue(ticketNo1.isTicketStatusActive());
        assertEquals(clientNo1, ticketNo1.getClient());
        assertEquals(movieNo1, ticketNo1.getMovie());
        assertEquals(typeOfTicketNo1, ticketNo1.getTicketType());
        assertEquals(basePriceNo1, ticketNo1.getTicketFinalPrice());
    }

    @Test
    public void ticketSetterTicketStatusActiveTest() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();
        boolean ticketStatusActive = false;
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID(), 25);

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, typeOfTicket);

        assertNotNull(ticket);
        assertTrue(ticket.isTicketStatusActive());

        ticket.setTicketStatusActive(ticketStatusActive);

        assertNotNull(ticket);
        assertFalse(ticket.isTicketStatusActive());
    }

    @Test
    public void getTicketInfoTestStatusActive() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID(), 25);

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, typeOfTicket);

        assertNotNull(ticket);
        assertNotNull(ticket.getTicketInfo());
        assertNotEquals("", ticket.getTicketInfo());
    }

    @Test
    public void getTicketInfoTestStatusNotActive() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String exampleDateNo1 = "2023-09-30 12:12";
        String exampleDateNo2 = "2023-10-02 20:15";
        Date reservationTime = df.parse(exampleDateNo1);
        Date movieTime = df.parse(exampleDateNo2);

        UUID ticketID = UUID.randomUUID();
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID(), 25);

        Ticket ticket = new Ticket(ticketID, movieTime, reservationTime, movieNo1, clientNo1, typeOfTicket);
        ticket.setTicketStatusActive(false);

        assertNotNull(ticket);
        assertNotNull(ticket.getTicketInfo());
        assertNotEquals("", ticket.getTicketInfo());
    }
}
