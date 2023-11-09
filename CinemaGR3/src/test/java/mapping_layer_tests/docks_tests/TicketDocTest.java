package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.TicketDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDocTest {

    private UUID ticketID;
    private Date movieTime;
    private Date reservationTime;
    private double ticketFinalPrice;
    private boolean ticketStatusActive;
    private UUID clientId;
    private UUID movieId;
    private UUID typeOfTicketId;

    private TicketDoc ticketDoc;

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

        ticketDoc = new TicketDoc(ticketID, movieTime, reservationTime, ticketStatusActive, ticketFinalPrice, clientId, movieId, typeOfTicketId);
    }

    @Test
    public void ticketDocNoArgsConstructorTestPositive() {
        TicketDoc ticketDoc = new TicketDoc();
        assertNotNull(ticketDoc);
    }

    @Test
    public void ticketDocAllArgsConstructorAndGettersTestPositive() {
        TicketDoc ticketDoc = new TicketDoc(ticketID, movieTime, reservationTime, ticketStatusActive, ticketFinalPrice, movieId, clientId, typeOfTicketId);
        assertNotNull(ticketDoc);
        assertEquals(ticketID, ticketDoc.getTicketID());
        assertEquals(movieTime, ticketDoc.getMovieTime());
        assertEquals(reservationTime, ticketDoc.getReservationTime());
        assertEquals(ticketStatusActive, ticketDoc.isTicketStatusActive());
        assertEquals(ticketFinalPrice, ticketDoc.getTicketFinalPrice());
        assertEquals(movieId, ticketDoc.getMovieID());
        assertEquals(clientId, ticketDoc.getClientID());
        assertEquals(typeOfTicketId, ticketDoc.getTypeOfTicketID());
    }

    @Test
    public void ticketDocSetTicketIdTestPositive() {
        UUID ticketIdBefore = ticketDoc.getTicketID();
        assertNotNull(ticketIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketDoc.setTicketID(newUUID);
        UUID ticketIdAfter = ticketDoc.getTicketID();
        assertNotNull(ticketIdAfter);
        assertEquals(newUUID, ticketIdAfter);
        assertNotEquals(ticketIdBefore, ticketIdAfter);
    }

    @Test
    public void ticketDocSetTicketMovieTimeTestPositive() {
        Date movieTimeBefore = ticketDoc.getMovieTime();
        assertNotNull(movieTimeBefore);
        Date newDate = new Calendar.Builder().setDate(2023, 11, 27).setTimeOfDay(12, 37, 0).build().getTime();
        assertNotNull(newDate);
        ticketDoc.setMovieTime(newDate);
        Date movieTimeAfter = ticketDoc.getMovieTime();
        assertNotNull(movieTimeAfter);
        assertEquals(newDate, movieTimeAfter);
        assertNotEquals(movieTimeBefore, movieTimeAfter);
    }

    @Test
    public void ticketDocSetTicketReservationTimeTestPositive() {
        Date reservationTimeBefore = ticketDoc.getReservationTime();
        assertNotNull(reservationTimeBefore);
        Date newDate = new Calendar.Builder().setDate(2023, 11, 27).setTimeOfDay(12, 37, 0).build().getTime();
        assertNotNull(newDate);
        ticketDoc.setReservationTime(newDate);
        Date reservationTimeAfter = ticketDoc.getReservationTime();
        assertNotNull(reservationTimeAfter);
        assertEquals(newDate, reservationTimeAfter);
        assertNotEquals(reservationTimeBefore, reservationTimeAfter);
    }

    @Test
    public void ticketDocSetTicketStatusActiveTestPositive() {
        boolean ticketStatusBefore = ticketDoc.isTicketStatusActive();
        boolean newStatus = false;
        ticketDoc.setTicketStatusActive(newStatus);
        boolean ticketStatusAfter = ticketDoc.isTicketStatusActive();
        assertEquals(newStatus, ticketStatusAfter);
        assertNotEquals(ticketStatusBefore, ticketStatusAfter);
    }

    @Test
    public void ticketDocSetTicketFinalPriceTestPositive() {
        double ticketFinalPriceBefore = ticketDoc.getTicketFinalPrice();
        double newFinalPrice = 47.75;
        ticketDoc.setTicketFinalPrice(newFinalPrice);
        double ticketFinalPriceAfter = ticketDoc.getTicketFinalPrice();
        assertEquals(newFinalPrice, ticketFinalPriceAfter);
        assertNotEquals(ticketFinalPriceBefore, ticketFinalPriceAfter);
    }

    @Test
    public void ticketDocSetTicketClientIdTestPositive() {
        UUID clientIdBefore = ticketDoc.getClientID();
        assertNotNull(clientIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketDoc.setClientID(newUUID);
        UUID clientIdAfter = ticketDoc.getClientID();
        assertNotNull(clientIdAfter);
        assertEquals(newUUID, clientIdAfter);
        assertNotEquals(clientIdBefore, clientIdAfter);
    }

    @Test
    public void ticketDocSetTicketMovieIdTestPositive() {
        UUID movieIdBefore = ticketDoc.getMovieID();
        assertNotNull(movieIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketDoc.setMovieID(newUUID);
        UUID movieIdAfter = ticketDoc.getMovieID();
        assertNotNull(movieIdAfter);
        assertEquals(newUUID, movieIdAfter);
        assertNotEquals(movieIdBefore, movieIdAfter);
    }

    @Test
    public void ticketDocSetTicketTypeOfTicketIdTestPositive() {
        UUID typeOfTicketIdBefore = ticketDoc.getTypeOfTicketID();
        assertNotNull(typeOfTicketIdBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        ticketDoc.setTypeOfTicketID(newUUID);
        UUID typeOfTicketIdAfter = ticketDoc.getTypeOfTicketID();
        assertNotNull(typeOfTicketIdAfter);
        assertEquals(newUUID, typeOfTicketIdAfter);
        assertNotEquals(typeOfTicketIdBefore, typeOfTicketIdAfter);
    }
}
