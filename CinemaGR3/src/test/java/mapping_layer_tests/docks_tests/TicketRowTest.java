package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.TicketRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRowTest {

    private UUID ticketID;
    private Date movieTime;
    private Date reservationTime;
    private double ticketFinalPrice;
    private boolean ticketStatusActive;
    private UUID clientId;
    private UUID movieId;
    private UUID typeOfTicketId;

    private TicketRow ticketRow;

    @BeforeEach
    public void init() {
        ticketID = UUID.randomUUID();
        movieTime = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
        reservationTime = new Calendar.Builder().setDate(2023, 9, 27).setTimeOfDay(12, 37, 0).build().getTime();
        ticketFinalPrice = 45;
        ticketStatusActive = true;
        clientId = UUID.randomUUID();
        movieId = UUID.randomUUID();
        typeOfTicketId = UUID.randomUUID();

        ticketRow = new TicketRow(ticketID, movieTime, reservationTime, ticketStatusActive, ticketFinalPrice, clientId, movieId, typeOfTicketId);
    }

    @Test
    public void ticketDocNoArgsConstructorTestPositive() {
        TicketRow ticketRow = new TicketRow();
        assertNotNull(ticketRow);
    }

    @Test
    public void ticketDocAllArgsConstructorAndGettersTestPositive() {
        TicketRow ticketRow = new TicketRow(ticketID, movieTime, reservationTime, ticketStatusActive, ticketFinalPrice, movieId, clientId, typeOfTicketId);
        assertNotNull(ticketRow);
        assertEquals(ticketID, ticketRow.getTicketID());
        assertEquals(movieTime, ticketRow.getMovieTime());
        assertEquals(reservationTime, ticketRow.getReservationTime());
        assertEquals(ticketStatusActive, ticketRow.isTicketStatusActive());
        assertEquals(ticketFinalPrice, ticketRow.getTicketFinalPrice());
        assertEquals(movieId, ticketRow.getMovieID());
        assertEquals(clientId, ticketRow.getClientID());
        assertEquals(typeOfTicketId, ticketRow.getTypeOfTicketID());
    }

    @Test
    public void ticketDocSetTicketIdTestPositive() {
        UUID ticketIdBefore = ticketRow.getTicketID();
        assertNotNull(ticketIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketRow.setTicketID(newUUID);
        UUID ticketIdAfter = ticketRow.getTicketID();
        assertNotNull(ticketIdAfter);
        assertEquals(newUUID, ticketIdAfter);
        assertNotEquals(ticketIdBefore, ticketIdAfter);
    }

    @Test
    public void ticketDocSetTicketMovieTimeTestPositive() {
        Date movieTimeBefore = ticketRow.getMovieTime();
        assertNotNull(movieTimeBefore);
        Date newDate = new Calendar.Builder().setDate(2023, 11, 27).setTimeOfDay(12, 37, 0).build().getTime();
        assertNotNull(newDate);
        ticketRow.setMovieTime(newDate);
        Date movieTimeAfter = ticketRow.getMovieTime();
        assertNotNull(movieTimeAfter);
        assertEquals(newDate, movieTimeAfter);
        assertNotEquals(movieTimeBefore, movieTimeAfter);
    }

    @Test
    public void ticketDocSetTicketReservationTimeTestPositive() {
        Date reservationTimeBefore = ticketRow.getReservationTime();
        assertNotNull(reservationTimeBefore);
        Date newDate = new Calendar.Builder().setDate(2023, 11, 27).setTimeOfDay(12, 37, 0).build().getTime();
        assertNotNull(newDate);
        ticketRow.setReservationTime(newDate);
        Date reservationTimeAfter = ticketRow.getReservationTime();
        assertNotNull(reservationTimeAfter);
        assertEquals(newDate, reservationTimeAfter);
        assertNotEquals(reservationTimeBefore, reservationTimeAfter);
    }

    @Test
    public void ticketDocSetTicketStatusActiveTestPositive() {
        boolean ticketStatusBefore = ticketRow.isTicketStatusActive();
        boolean newStatus = false;
        ticketRow.setTicketStatusActive(newStatus);
        boolean ticketStatusAfter = ticketRow.isTicketStatusActive();
        assertEquals(newStatus, ticketStatusAfter);
        assertNotEquals(ticketStatusBefore, ticketStatusAfter);
    }

    @Test
    public void ticketDocSetTicketFinalPriceTestPositive() {
        double ticketFinalPriceBefore = ticketRow.getTicketFinalPrice();
        double newFinalPrice = 47.75;
        ticketRow.setTicketFinalPrice(newFinalPrice);
        double ticketFinalPriceAfter = ticketRow.getTicketFinalPrice();
        assertEquals(newFinalPrice, ticketFinalPriceAfter);
        assertNotEquals(ticketFinalPriceBefore, ticketFinalPriceAfter);
    }

    @Test
    public void ticketDocSetTicketClientIdTestPositive() {
        UUID clientIdBefore = ticketRow.getClientID();
        assertNotNull(clientIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketRow.setClientID(newUUID);
        UUID clientIdAfter = ticketRow.getClientID();
        assertNotNull(clientIdAfter);
        assertEquals(newUUID, clientIdAfter);
        assertNotEquals(clientIdBefore, clientIdAfter);
    }

    @Test
    public void ticketDocSetTicketMovieIdTestPositive() {
        UUID movieIdBefore = ticketRow.getMovieID();
        assertNotNull(movieIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketRow.setMovieID(newUUID);
        UUID movieIdAfter = ticketRow.getMovieID();
        assertNotNull(movieIdAfter);
        assertEquals(newUUID, movieIdAfter);
        assertNotEquals(movieIdBefore, movieIdAfter);
    }

    @Test
    public void ticketDocSetTicketTypeOfTicketIdTestPositive() {
        UUID typeOfTicketIdBefore = ticketRow.getTypeOfTicketID();
        assertNotNull(typeOfTicketIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketRow.setTypeOfTicketID(newUUID);
        UUID typeOfTicketIdAfter = ticketRow.getTypeOfTicketID();
        assertNotNull(typeOfTicketIdAfter);
        assertEquals(newUUID, typeOfTicketIdAfter);
        assertNotEquals(typeOfTicketIdBefore, typeOfTicketIdAfter);
    }
}
