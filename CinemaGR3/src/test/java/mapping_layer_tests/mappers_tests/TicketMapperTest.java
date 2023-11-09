package mapping_layer_tests.mappers_tests;

import mapping_layer.mappers.TicketMapper;
import mapping_layer.model_docs.ClientDoc;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import mapping_layer.model_docs.TicketDoc;
import mapping_layer.model_docs.ticket_types.NormalDoc;
import mapping_layer.model_docs.ticket_types.ReducedDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_docs_exceptions.ClientNullException;
import model.exceptions.model_docs_exceptions.NullMovieTimeException;
import model.exceptions.model_docs_exceptions.NullReservationTimeException;
import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketMapperTest {

    private Client client;
    private Movie movie;
    private ScreeningRoom screeningRoom;
    private TypeOfTicket typeOfTicketNo1;
    private TypeOfTicket typeOfTicketNo2;
    private Ticket ticketNo1;
    private Ticket ticketNo2;

    private ClientDoc clientDoc;
    private MovieDoc movieDoc;
    private ScreeningRoomDoc screeningRoomDoc;
    private TypeOfTicketDoc typeOfTicketDocNo1;
    private TypeOfTicketDoc typeOfTicketDocNo2;
    private TicketDoc ticketDocNo1;
    private TicketDoc ticketDocNo2;

    private final Date localDateTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
    private final Date localDateTimeNo2 = new Calendar.Builder().setDate(2023, 9, 28).setTimeOfDay(12, 37, 0).build().getTime();


    @BeforeEach
    public void init() {
        client = new Client(UUID.randomUUID(), "SomeName", "SomeSurname", 30);
        screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 4, 45);
        movie = new Movie(UUID.randomUUID(), "SomeTitle", 40, screeningRoom);
        try {
            ticketNo1 = new Ticket(UUID.randomUUID(), localDateTimeNo1, localDateTimeNo2, movie, client, "normal");
            ticketNo2 = new Ticket(UUID.randomUUID(), localDateTimeNo1, localDateTimeNo2, movie, client, "reduced");
        } catch (TicketReservationException exception) {
            throw new RuntimeException("Error while creating ticket for tests.", exception);
        }

        clientDoc = new ClientDoc(UUID.randomUUID(), "SomeName", "SomeSurname", 30, true);
        screeningRoomDoc = new ScreeningRoomDoc(UUID.randomUUID(), 1, 4, 45, true);
        movieDoc = new MovieDoc(UUID.randomUUID(), "SomeTitle", true, 50, screeningRoomDoc.getScreeningRoomID());
        typeOfTicketDocNo1 = new ReducedDoc(UUID.randomUUID());
        typeOfTicketDocNo2 = new NormalDoc(UUID.randomUUID());
        ticketDocNo1 = new TicketDoc(UUID.randomUUID(), localDateTimeNo1, localDateTimeNo2, true, 50, movieDoc.getMovieID(), clientDoc.getClientID(), typeOfTicketDocNo1.getTypeOfTicketID());
        ticketDocNo2 = new TicketDoc(UUID.randomUUID(), localDateTimeNo1, localDateTimeNo2, true, 50, movieDoc.getMovieID(), clientDoc.getClientID(), typeOfTicketDocNo2.getTypeOfTicketID());
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void ticketMapperConstructorTest() {
        TicketMapper ticketMapper = new TicketMapper();
        assertNotNull(ticketMapper);
    }

    @Test
    public void ticketMapperFromTicketToTicketDocTestPositive() {
        assertNotNull(client);
        assertNotNull(screeningRoom);
        assertNotNull(movie);
        assertNotNull(ticketNo1);
        TicketDoc ticketDoc = TicketMapper.toTicketDoc(ticketNo1);
        assertNotNull(ticketDoc);
        assertEquals(ticketNo1.getTicketID(), ticketDoc.getTicketID());
        assertEquals(ticketNo1.getMovieTime(), ticketDoc.getMovieTime());
        assertEquals(ticketNo1.getReservationTime(), ticketDoc.getReservationTime());
        assertEquals(ticketNo1.getTicketFinalPrice(), ticketDoc.getTicketFinalPrice());
        assertEquals(ticketNo1.getMovie().getMovieID(), ticketDoc.getMovieID());
        assertEquals(ticketNo1.getClient().getClientID(), ticketDoc.getClientID());
        assertEquals(ticketNo1.getTicketType().getTicketTypeID(), ticketDoc.getTypeOfTicketID());
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullMovieTimeTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), null, localDateTimeNo2, movie, client, "reduced");
        assertThrows(NullMovieTimeException.class, () -> {
           TicketDoc ticketDoc = TicketMapper.toTicketDoc(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullReservationTimeTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), localDateTimeNo1, null, movie, client, "reduced");
        assertThrows(NullReservationTimeException.class, () -> {
            TicketDoc ticketDoc = TicketMapper.toTicketDoc(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullClientTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), localDateTimeNo1, localDateTimeNo2, movie, null, "reduced");
        assertThrows(ClientNullException.class, () -> {
            TicketDoc ticketDoc = TicketMapper.toTicketDoc(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketDocReducedToTicketTestPositive() {
        assertNotNull(clientDoc);
        assertNotNull(screeningRoomDoc);
        assertNotNull(movieDoc);
        assertNotNull(typeOfTicketDocNo1);
        assertNotNull(ticketDocNo1);
        Ticket ticket = TicketMapper.toTicket(ticketDocNo1, movieDoc, screeningRoomDoc, clientDoc, typeOfTicketDocNo1, "reduced");
        assertNotNull(ticket);
        assertEquals(ticket.getTicketID(), ticketDocNo1.getTicketID());
        assertEquals(ticket.getMovieTime(), ticketDocNo1.getMovieTime());
        assertEquals(ticket.getReservationTime(), ticketDocNo1.getReservationTime());
        assertEquals(ticket.getTicketFinalPrice(), ticketDocNo1.getTicketFinalPrice());
        assertEquals(ticket.isTicketStatusActive(), ticketDocNo1.isTicketStatusActive());
        assertEquals(ticket.getMovie().getMovieID(), movieDoc.getMovieID());
        assertEquals(ticket.getMovie().getMovieTitle(), movieDoc.getMovieTitle());
        assertEquals(ticket.getMovie().getMovieBasePrice(), movieDoc.getMovieBasePrice());
        assertEquals(ticket.getMovie().isMovieStatusActive(), movieDoc.isMovieStatusActive());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomID(), screeningRoomDoc.getScreeningRoomID());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomFloor(), screeningRoomDoc.getScreeningRoomFloor());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomNumber(), screeningRoomDoc.getScreeningRoomNumber());
        assertEquals(ticket.getMovie().getScreeningRoom().getNumberOfAvailableSeats(), screeningRoomDoc.getNumberOfAvailableSeats());
        assertEquals(ticket.getMovie().getScreeningRoom().isScreeningRoomStatusActive(), screeningRoomDoc.isScreeningRoomStatusActive());
        assertEquals(ticket.getClient().getClientID(), clientDoc.getClientID());
        assertEquals(ticket.getClient().getClientName(), clientDoc.getClientName());
        assertEquals(ticket.getClient().getClientSurname(), clientDoc.getClientSurname());
        assertEquals(ticket.getClient().getClientAge(), clientDoc.getClientAge());
        assertEquals(ticket.getClient().isClientStatusActive(), clientDoc.isClientStatusActive());
        assertEquals(ticket.getTicketType().getTicketTypeID(), typeOfTicketDocNo1.getTypeOfTicketID());
        assertEquals(ticket.getTicketType().getClass(), Reduced.class);
    }

    @Test
    public void ticketMapperFromTicketDocNormalToTicketTestPositive() {
        assertNotNull(clientDoc);
        assertNotNull(screeningRoomDoc);
        assertNotNull(movieDoc);
        assertNotNull(typeOfTicketDocNo2);
        assertNotNull(ticketDocNo2);
        Ticket ticket = TicketMapper.toTicket(ticketDocNo2, movieDoc, screeningRoomDoc, clientDoc, typeOfTicketDocNo2, "normal");
        assertNotNull(ticket);
        assertEquals(ticket.getTicketID(), ticketDocNo2.getTicketID());
        assertEquals(ticket.getMovieTime(), ticketDocNo2.getMovieTime());
        assertEquals(ticket.getReservationTime(), ticketDocNo2.getReservationTime());
        assertEquals(ticket.getTicketFinalPrice(), ticketDocNo2.getTicketFinalPrice());
        assertEquals(ticket.isTicketStatusActive(), ticketDocNo2.isTicketStatusActive());
        assertEquals(ticket.getMovie().getMovieID(), movieDoc.getMovieID());
        assertEquals(ticket.getMovie().getMovieTitle(), movieDoc.getMovieTitle());
        assertEquals(ticket.getMovie().getMovieBasePrice(), movieDoc.getMovieBasePrice());
        assertEquals(ticket.getMovie().isMovieStatusActive(), movieDoc.isMovieStatusActive());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomID(), screeningRoomDoc.getScreeningRoomID());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomFloor(), screeningRoomDoc.getScreeningRoomFloor());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomNumber(), screeningRoomDoc.getScreeningRoomNumber());
        assertEquals(ticket.getMovie().getScreeningRoom().getNumberOfAvailableSeats(), screeningRoomDoc.getNumberOfAvailableSeats());
        assertEquals(ticket.getMovie().getScreeningRoom().isScreeningRoomStatusActive(), screeningRoomDoc.isScreeningRoomStatusActive());
        assertEquals(ticket.getClient().getClientID(), clientDoc.getClientID());
        assertEquals(ticket.getClient().getClientName(), clientDoc.getClientName());
        assertEquals(ticket.getClient().getClientSurname(), clientDoc.getClientSurname());
        assertEquals(ticket.getClient().getClientAge(), clientDoc.getClientAge());
        assertEquals(ticket.getClient().isClientStatusActive(), clientDoc.isClientStatusActive());
        assertEquals(ticket.getTicketType().getTicketTypeID(), typeOfTicketDocNo2.getTypeOfTicketID());
        assertEquals(ticket.getTicketType().getClass(), Normal.class);
    }
}
