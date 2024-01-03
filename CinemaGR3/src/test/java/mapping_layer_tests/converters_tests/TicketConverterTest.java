package mapping_layer_tests.converters_tests;

import mapping_layer.converters.TicketConverter;
import mapping_layer.model_rows.ClientRow;
import mapping_layer.model_rows.MovieRow;
import mapping_layer.model_rows.ScreeningRoomRow;
import mapping_layer.model_rows.TicketRow;
import mapping_layer.model_rows.ticket_types.NormalRow;
import mapping_layer.model_rows.ticket_types.ReducedRow;
import mapping_layer.model_rows.ticket_types.TypeOfTicketRow;
import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.model_docs_exceptions.TicketCreateException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ClientNullException;
import model.exceptions.model_docs_exceptions.date_null_exceptions.NullMovieTimeException;
import model.exceptions.model_docs_exceptions.date_null_exceptions.NullReservationTimeException;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ObjectNullReferenceException;
import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketConverterTest {

    private Client client;
    private Movie movie;
    private ScreeningRoom screeningRoom;
    private TypeOfTicket typeOfTicketNo1;
    private TypeOfTicket typeOfTicketNo2;
    private Ticket ticketNo1;
    private Ticket ticketNo2;

    private ClientRow clientRow;
    private MovieRow movieRow;
    private ScreeningRoomRow screeningRoomRow;
    private TypeOfTicketRow typeOfTicketRowNo1;
    private TypeOfTicketRow typeOfTicketRowNo2;
    private TicketRow ticketRowNo1;
    private TicketRow ticketRowNo2;

    private final Instant instantNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime().toInstant();
    private final Instant instantNo2 = new Calendar.Builder().setDate(2023, 9, 28).setTimeOfDay(12, 37, 0).build().getTime().toInstant();


    @BeforeEach
    public void init() {
        client = new Client(UUID.randomUUID(), "SomeName", "SomeSurname", 30);
        screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 4, 45);
        movie = new Movie(UUID.randomUUID(), "SomeTitle", 40, screeningRoom);
        try {
            ticketNo1 = new Ticket(UUID.randomUUID(), instantNo1, instantNo2, movie, client, "normal");
            ticketNo2 = new Ticket(UUID.randomUUID(), instantNo1, instantNo2, movie, client, "reduced");
        } catch (TicketReservationException exception) {
            throw new RuntimeException("Error while creating ticket for tests.", exception);
        }

        clientRow = new ClientRow(UUID.randomUUID(), true, "SomeName", "SomeSurname", 30);
        screeningRoomRow = new ScreeningRoomRow(UUID.randomUUID(), true, 1, 4, 45);
        movieRow = new MovieRow(UUID.randomUUID(), screeningRoomRow.getScreeningRoomID(), "SomeTitle", true, 50);
        typeOfTicketRowNo1 = new ReducedRow(UUID.randomUUID());
        typeOfTicketRowNo2 = new NormalRow(UUID.randomUUID());
        ticketRowNo1 = new TicketRow(UUID.randomUUID(),  typeOfTicketRowNo1.getTypeOfTicketID(), instantNo1, instantNo2, true, 50, movieRow.getMovieID(), clientRow.getClientID());
        ticketRowNo2 = new TicketRow(UUID.randomUUID(), typeOfTicketRowNo2.getTypeOfTicketID(), instantNo1, instantNo2, true, 50, movieRow.getMovieID(), clientRow.getClientID());
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void ticketMapperConstructorTest() {
        TicketConverter ticketConverter = new TicketConverter();
        assertNotNull(ticketConverter);
    }

    @Test
    public void ticketMapperFromTicketToTicketDocTestPositive() {
        assertNotNull(client);
        assertNotNull(screeningRoom);
        assertNotNull(movie);
        assertNotNull(ticketNo1);
        TicketRow ticketRow;
        try {
            ticketRow = TicketConverter.toTicketRow(ticketNo1);
        } catch (ObjectNullReferenceException exception) {
            throw new RuntimeException("Could not create ticketRow object from ticket object since some reference in ticket object is null.");
        }
        assertNotNull(ticketRow);
        assertEquals(ticketNo1.getTicketID(), ticketRow.getTicketID());
        assertEquals(ticketNo1.getMovieTime(), ticketRow.getMovieTime());
        assertEquals(ticketNo1.getReservationTime(), ticketRow.getReservationTime());
        assertEquals(ticketNo1.getTicketFinalPrice(), ticketRow.getTicketFinalPrice());
        assertEquals(ticketNo1.getMovie().getMovieID(), ticketRow.getMovieID());
        assertEquals(ticketNo1.getClient().getClientID(), ticketRow.getClientID());
        assertEquals(ticketNo1.getTicketType().getTicketTypeID(), ticketRow.getTypeOfTicketID());
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullMovieTimeTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), null, instantNo2, movie, client, "reduced");
        assertThrows(NullMovieTimeException.class, () -> {
           TicketRow ticketRow = TicketConverter.toTicketRow(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullReservationTimeTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), instantNo1, null, movie, client, "reduced");
        assertThrows(NullReservationTimeException.class, () -> {
            TicketRow ticketRow = TicketConverter.toTicketRow(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketToTicketDocWithNullClientTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), instantNo1, instantNo2, movie, null, "reduced");
        assertThrows(ClientNullException.class, () -> {
            TicketRow ticketRow = TicketConverter.toTicketRow(ticket);
        });
    }

    @Test
    public void ticketMapperFromTicketDocReducedToTicketTestPositive() {
        assertNotNull(clientRow);
        assertNotNull(screeningRoomRow);
        assertNotNull(movieRow);
        assertNotNull(typeOfTicketRowNo1);
        assertNotNull(ticketRowNo1);
        Ticket ticket;
        try {
            ticket = TicketConverter.toTicket(ticketRowNo1, movieRow, screeningRoomRow, clientRow, typeOfTicketRowNo1);
        } catch (TicketCreateException exception) {
            throw new RuntimeException("Ticket object could not be created.");
        }
        assertNotNull(ticket);
        assertEquals(ticket.getTicketID(), ticketRowNo1.getTicketID());
        assertEquals(ticket.getMovieTime(), ticketRowNo1.getMovieTime());
        assertEquals(ticket.getReservationTime(), ticketRowNo1.getReservationTime());
        assertEquals(ticket.getTicketFinalPrice(), ticketRowNo1.getTicketFinalPrice());
        assertEquals(ticket.isTicketStatusActive(), ticketRowNo1.isTicketStatusActive());
        assertEquals(ticket.getMovie().getMovieID(), movieRow.getMovieID());
        assertEquals(ticket.getMovie().getMovieTitle(), movieRow.getMovieTitle());
        assertEquals(ticket.getMovie().getMovieBasePrice(), movieRow.getMovieBasePrice());
        assertEquals(ticket.getMovie().isMovieStatusActive(), movieRow.isMovieStatusActive());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomID(), screeningRoomRow.getScreeningRoomID());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomFloor(), screeningRoomRow.getScreeningRoomFloor());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomNumber(), screeningRoomRow.getScreeningRoomNumber());
        assertEquals(ticket.getMovie().getScreeningRoom().getNumberOfAvailableSeats(), screeningRoomRow.getNumberOfAvailableSeats());
        assertEquals(ticket.getMovie().getScreeningRoom().isScreeningRoomStatusActive(), screeningRoomRow.isScreeningRoomStatusActive());
        assertEquals(ticket.getClient().getClientID(), clientRow.getClientID());
        assertEquals(ticket.getClient().getClientName(), clientRow.getClientName());
        assertEquals(ticket.getClient().getClientSurname(), clientRow.getClientSurname());
        assertEquals(ticket.getClient().getClientAge(), clientRow.getClientAge());
        assertEquals(ticket.getClient().isClientStatusActive(), clientRow.isClientStatusActive());
        assertEquals(ticket.getTicketType().getTicketTypeID(), typeOfTicketRowNo1.getTypeOfTicketID());
        assertEquals(ticket.getTicketType().getClass(), Reduced.class);
    }

    @Test
    public void ticketMapperFromTicketDocNormalToTicketTestPositive() {
        assertNotNull(clientRow);
        assertNotNull(screeningRoomRow);
        assertNotNull(movieRow);
        assertNotNull(typeOfTicketRowNo2);
        assertNotNull(ticketRowNo2);
        Ticket ticket;
        try {
            ticket = TicketConverter.toTicket(ticketRowNo2, movieRow, screeningRoomRow, clientRow, typeOfTicketRowNo2);
        } catch (TicketCreateException exception) {
            throw new RuntimeException("Ticket object could not be created.");
        }
        assertNotNull(ticket);
        assertEquals(ticket.getTicketID(), ticketRowNo2.getTicketID());
        assertEquals(ticket.getMovieTime(), ticketRowNo2.getMovieTime());
        assertEquals(ticket.getReservationTime(), ticketRowNo2.getReservationTime());
        assertEquals(ticket.getTicketFinalPrice(), ticketRowNo2.getTicketFinalPrice());
        assertEquals(ticket.isTicketStatusActive(), ticketRowNo2.isTicketStatusActive());
        assertEquals(ticket.getMovie().getMovieID(), movieRow.getMovieID());
        assertEquals(ticket.getMovie().getMovieTitle(), movieRow.getMovieTitle());
        assertEquals(ticket.getMovie().getMovieBasePrice(), movieRow.getMovieBasePrice());
        assertEquals(ticket.getMovie().isMovieStatusActive(), movieRow.isMovieStatusActive());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomID(), screeningRoomRow.getScreeningRoomID());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomFloor(), screeningRoomRow.getScreeningRoomFloor());
        assertEquals(ticket.getMovie().getScreeningRoom().getScreeningRoomNumber(), screeningRoomRow.getScreeningRoomNumber());
        assertEquals(ticket.getMovie().getScreeningRoom().getNumberOfAvailableSeats(), screeningRoomRow.getNumberOfAvailableSeats());
        assertEquals(ticket.getMovie().getScreeningRoom().isScreeningRoomStatusActive(), screeningRoomRow.isScreeningRoomStatusActive());
        assertEquals(ticket.getClient().getClientID(), clientRow.getClientID());
        assertEquals(ticket.getClient().getClientName(), clientRow.getClientName());
        assertEquals(ticket.getClient().getClientSurname(), clientRow.getClientSurname());
        assertEquals(ticket.getClient().getClientAge(), clientRow.getClientAge());
        assertEquals(ticket.getClient().isClientStatusActive(), clientRow.isClientStatusActive());
        assertEquals(ticket.getTicketType().getTicketTypeID(), typeOfTicketRowNo2.getTypeOfTicketID());
        assertEquals(ticket.getTicketType().getClass(), Normal.class);
    }
}
