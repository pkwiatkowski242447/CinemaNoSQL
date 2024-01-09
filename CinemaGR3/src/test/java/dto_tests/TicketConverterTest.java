package dto_tests;

import model.dtos.TicketClientDTO;
import model.dtos.TicketMovieDTO;
import model.dtos.converter.TicketConverter;
import model.exceptions.TicketTypeNotFoundException;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Calendar;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TicketConverterTest {

    private static UUID uuidNo1;
    private static UUID uuidNo2;
    private static Instant reservationTime;
    private static Instant movieTime;
    private static double movieBasePrice;
    private static Movie movieNo1;
    private static Client clientNo1;

    private Ticket ticketNo1;
    private Ticket ticketNo2;
    private TicketClientDTO ticketClientDTONo1;
    private TicketClientDTO ticketClientDTONo2;
    private TicketMovieDTO ticketMovieDTONo1;
    private TicketMovieDTO ticketMovieDTONo2;

    @BeforeAll
    public static void init() {
        uuidNo1 = UUID.randomUUID();
        uuidNo2 = UUID.randomUUID();
        reservationTime = new Calendar.Builder().setDate(2023, 9, 30).setTimeOfDay(12, 12, 0).build().getTime().toInstant();
        movieTime = new Calendar.Builder().setDate(2023, 10, 2).setTimeOfDay(20, 15, 0).build().getTime().toInstant();
        movieBasePrice = 40;
        movieNo1 = new Movie(UUID.randomUUID(), "Pulp Fiction", 37.75, 1, 90);
        clientNo1 = new Client(UUID.randomUUID(), "Jules", "Winnfield", 74);
    }

    @BeforeEach
    public void initializeTickets() {
        ticketNo1 = new Normal(uuidNo1, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());
        ticketNo2 = new Reduced(uuidNo2, movieTime, reservationTime, movieBasePrice, movieNo1.getMovieID(), clientNo1.getClientID());

        ticketClientDTONo1 = new TicketClientDTO(ticketNo1.getClientId(),
                ticketNo1.getTicketID(),
                ticketNo1.getMovieTime(),
                ticketNo1.getReservationTime(),
                ticketNo1.getTicketBasePrice(),
                ticketNo1.getTicketFinalPrice(),
                ticketNo1.getMovieId(),
                ticketNo1.getTicketTypeDiscriminator());

        ticketClientDTONo2 = new TicketClientDTO(ticketNo2.getClientId(),
                ticketNo2.getTicketID(),
                ticketNo2.getMovieTime(),
                ticketNo2.getReservationTime(),
                ticketNo2.getTicketBasePrice(),
                ticketNo2.getTicketFinalPrice(),
                ticketNo2.getMovieId(),
                ticketNo2.getTicketTypeDiscriminator());

        ticketMovieDTONo1 = new TicketMovieDTO(ticketNo1.getMovieId(),
                ticketNo1.getTicketID(),
                ticketNo1.getMovieTime(),
                ticketNo1.getReservationTime(),
                ticketNo1.getTicketBasePrice(),
                ticketNo1.getTicketFinalPrice(),
                ticketNo1.getClientId(),
                ticketNo1.getTicketTypeDiscriminator());

        ticketMovieDTONo2 = new TicketMovieDTO(ticketNo2.getMovieId(),
                ticketNo2.getTicketID(),
                ticketNo2.getMovieTime(),
                ticketNo2.getReservationTime(),
                ticketNo2.getTicketBasePrice(),
                ticketNo2.getTicketFinalPrice(),
                ticketNo2.getClientId(),
                ticketNo2.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterConstructorTestPositive() {
        TicketConverter ticketConverter = new TicketConverter();
        assertNotNull(ticketConverter);
    }

    @Test
    public void ticketConverterFromNormalTicketToTicketClientDTOTestPositive() {
        TicketClientDTO ticketClientDTO = TicketConverter.toTicketClientDTO(ticketNo1);
        assertEquals(ticketNo1.getTicketID(), ticketClientDTO.getTicketID());
        assertEquals(ticketNo1.getMovieTime(), ticketClientDTO.getMovieTime());
        assertEquals(ticketNo1.getReservationTime(), ticketClientDTO.getReservationTime());
        assertEquals(ticketNo1.getTicketBasePrice(), ticketClientDTO.getTicketBasePrice());
        assertEquals(ticketNo1.getTicketFinalPrice(), ticketClientDTO.getTicketFinalPrice());
        assertEquals(ticketNo1.getMovieId(), ticketClientDTO.getMovieId());
        assertEquals(ticketNo1.getClientId(), ticketClientDTO.getClientId());
        assertEquals(ticketNo1.getTicketTypeDiscriminator(), ticketClientDTO.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromReducedTicketToTicketClientDTOTestPositive() {
        TicketClientDTO ticketClientDTO = TicketConverter.toTicketClientDTO(ticketNo2);
        assertEquals(ticketNo2.getTicketID(), ticketClientDTO.getTicketID());
        assertEquals(ticketNo2.getMovieTime(), ticketClientDTO.getMovieTime());
        assertEquals(ticketNo2.getReservationTime(), ticketClientDTO.getReservationTime());
        assertEquals(ticketNo2.getTicketBasePrice(), ticketClientDTO.getTicketBasePrice());
        assertEquals(ticketNo2.getTicketFinalPrice(), ticketClientDTO.getTicketFinalPrice());
        assertEquals(ticketNo2.getMovieId(), ticketClientDTO.getMovieId());
        assertEquals(ticketNo2.getClientId(), ticketClientDTO.getClientId());
        assertEquals(ticketNo2.getTicketTypeDiscriminator(), ticketClientDTO.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromNormalTicketToTicketMovieDTOTestPositive() {
        TicketMovieDTO ticketMovieDTO = TicketConverter.toTicketMovieDTO(ticketNo1);
        assertEquals(ticketNo1.getTicketID(), ticketMovieDTO.getTicketID());
        assertEquals(ticketNo1.getMovieTime(), ticketMovieDTO.getMovieTime());
        assertEquals(ticketNo1.getReservationTime(), ticketMovieDTO.getReservationTime());
        assertEquals(ticketNo1.getTicketBasePrice(), ticketMovieDTO.getTicketBasePrice());
        assertEquals(ticketNo1.getTicketFinalPrice(), ticketMovieDTO.getTicketFinalPrice());
        assertEquals(ticketNo1.getMovieId(), ticketMovieDTO.getMovieId());
        assertEquals(ticketNo1.getClientId(), ticketMovieDTO.getClientId());
        assertEquals(ticketNo1.getTicketTypeDiscriminator(), ticketMovieDTO.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromReducedTicketToTicketMovieDTOTestPositive() {
        TicketMovieDTO ticketMovieDTO = TicketConverter.toTicketMovieDTO(ticketNo2);
        assertEquals(ticketNo2.getTicketID(), ticketMovieDTO.getTicketID());
        assertEquals(ticketNo2.getMovieTime(), ticketMovieDTO.getMovieTime());
        assertEquals(ticketNo2.getReservationTime(), ticketMovieDTO.getReservationTime());
        assertEquals(ticketNo2.getTicketBasePrice(), ticketMovieDTO.getTicketBasePrice());
        assertEquals(ticketNo2.getTicketFinalPrice(), ticketMovieDTO.getTicketFinalPrice());
        assertEquals(ticketNo2.getMovieId(), ticketMovieDTO.getMovieId());
        assertEquals(ticketNo2.getClientId(), ticketMovieDTO.getClientId());
        assertEquals(ticketNo2.getTicketTypeDiscriminator(), ticketMovieDTO.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromNormalTicketClientDTOToReducedTicketTestPositive() throws TicketTypeNotFoundException {
        Ticket ticket = TicketConverter.toTicketFromTicketClientDTO(ticketClientDTONo1);
        assertEquals(ticketClientDTONo1.getClientId(), ticket.getClientId());
        assertEquals(ticketClientDTONo1.getTicketID(), ticket.getTicketID());
        assertEquals(ticketClientDTONo1.getMovieTime(), ticket.getMovieTime());
        assertEquals(ticketClientDTONo1.getReservationTime(), ticket.getReservationTime());
        assertEquals(ticketClientDTONo1.getTicketBasePrice(), ticket.getTicketBasePrice());
        assertEquals(ticketClientDTONo1.getTicketFinalPrice(), ticket.getTicketFinalPrice());
        assertEquals(ticketClientDTONo1.getMovieId(), ticket.getMovieId());
        assertEquals(ticketClientDTONo1.getTicketTypeDiscriminator(), ticket.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromReducedTicketClientDTOToReducedTicketTestPositive() throws TicketTypeNotFoundException {
        Ticket ticket = TicketConverter.toTicketFromTicketClientDTO(ticketClientDTONo2);
        assertEquals(ticketClientDTONo2.getClientId(), ticket.getClientId());
        assertEquals(ticketClientDTONo2.getTicketID(), ticket.getTicketID());
        assertEquals(ticketClientDTONo2.getMovieTime(), ticket.getMovieTime());
        assertEquals(ticketClientDTONo2.getReservationTime(), ticket.getReservationTime());
        assertEquals(ticketClientDTONo2.getTicketBasePrice(), ticket.getTicketBasePrice());
        assertEquals(ticketClientDTONo2.getTicketFinalPrice(), ticket.getTicketFinalPrice());
        assertEquals(ticketClientDTONo2.getMovieId(), ticket.getMovieId());
        assertEquals(ticketClientDTONo2.getTicketTypeDiscriminator(), ticket.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromNormalTicketMovieDTOToReducedTicketTestPositive() throws TicketTypeNotFoundException {
        Ticket ticket = TicketConverter.toTicketFromTicketMovieDTO(ticketMovieDTONo1);
        assertEquals(ticketMovieDTONo1.getClientId(), ticket.getClientId());
        assertEquals(ticketMovieDTONo1.getTicketID(), ticket.getTicketID());
        assertEquals(ticketMovieDTONo1.getMovieTime(), ticket.getMovieTime());
        assertEquals(ticketMovieDTONo1.getReservationTime(), ticket.getReservationTime());
        assertEquals(ticketMovieDTONo1.getTicketBasePrice(), ticket.getTicketBasePrice());
        assertEquals(ticketMovieDTONo1.getTicketFinalPrice(), ticket.getTicketFinalPrice());
        assertEquals(ticketMovieDTONo1.getMovieId(), ticket.getMovieId());
        assertEquals(ticketMovieDTONo1.getTicketTypeDiscriminator(), ticket.getTicketTypeDiscriminator());
    }

    @Test
    public void ticketConverterFromReducedTicketMovieDTOToReducedTicketTestPositive() throws TicketTypeNotFoundException {
        Ticket ticket = TicketConverter.toTicketFromTicketMovieDTO(ticketMovieDTONo2);
        assertEquals(ticketMovieDTONo2.getClientId(), ticket.getClientId());
        assertEquals(ticketMovieDTONo2.getTicketID(), ticket.getTicketID());
        assertEquals(ticketMovieDTONo2.getMovieTime(), ticket.getMovieTime());
        assertEquals(ticketMovieDTONo2.getReservationTime(), ticket.getReservationTime());
        assertEquals(ticketMovieDTONo2.getTicketBasePrice(), ticket.getTicketBasePrice());
        assertEquals(ticketMovieDTONo2.getTicketFinalPrice(), ticket.getTicketFinalPrice());
        assertEquals(ticketMovieDTONo2.getMovieId(), ticket.getMovieId());
        assertEquals(ticketMovieDTONo2.getTicketTypeDiscriminator(), ticket.getTicketTypeDiscriminator());
    }
}
