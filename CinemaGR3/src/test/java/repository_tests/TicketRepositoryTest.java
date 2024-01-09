package repository_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.exceptions.repositories.update_exceptions.TicketRepositoryUpdateException;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.exceptions.repositories.update_exceptions.RepositoryUpdateException;
import model.model.ticket_types.Normal;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;
import model.model.ticket_types.Reduced;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    private static CqlSession cqlSession;

    private static TicketRepository ticketRepositoryForTests;
    private static ClientRepository clientRepositoryForTests;
    private static MovieRepository movieRepositoryForTests;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;

    private final Instant movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime().toInstant();
    private final Instant movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime().toInstant();
    private final Instant movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime().toInstant();

    private final Instant reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime().toInstant();
    private final Instant reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime().toInstant();
    private final Instant reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime().toInstant();

    private Ticket ticketNo1;
    private Ticket ticketNo2;
    private Ticket ticketNo3;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();
        clientRepositoryForTests = new ClientRepository(cqlSession);
        movieRepositoryForTests = new MovieRepository(cqlSession);
        ticketRepositoryForTests = new TicketRepository(cqlSession);
    }

    @AfterAll
    public static void destroy() {
        cqlSession.close();
    }

    @BeforeEach
    public void insertExampleTickets() {
        String clientNo1Name = "John";
        String clientNo1Surname = "Smith";
        int clientNo1Age = 21;

        String clientNo2Name = "Mary";
        String clientNo2Surname = "Jane";
        int clientNo2Age = 18;

        String clientNo3Name = "Vincent";
        String clientNo3Surname = "Vega";
        int clientNo3Age = 40;

        try {
            clientNo1 = clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
            clientNo2 = clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
            clientNo3 = clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);
        } catch (ClientRepositoryCreateException exception) {
            throw new RuntimeException("Sample clients could not be created in the repository.", exception);
        }

        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;

        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;

        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        try {
            movieNo1 = movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            movieNo2 = movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            movieNo3 = movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
        } catch (MovieRepositoryCreateException exception) {
            throw new RuntimeException("Sample movies could not be created in the repository.", exception);
        }

        try {
            ticketNo1 = ticketRepositoryForTests.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1BasePrice, movieNo1.getMovieID(), clientNo1.getClientID());
            ticketNo2 = ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2BasePrice, movieNo2.getMovieID(), clientNo2.getClientID());
            ticketNo3 = ticketRepositoryForTests.createNormalTicket(movieTimeNo3, reservationTimeNo3, movieNo3BasePrice, movieNo3.getMovieID(), clientNo3.getClientID());
        } catch (TicketRepositoryCreateException exception) {
            throw new RuntimeException("Sample tickets could not be created in the database.", exception);
        }
    }

    @AfterEach
    public void deleteExampleTickets() {
        try {
            List<Ticket> listOfTickets = ticketRepositoryForTests.findAll();
            for (Ticket ticket : listOfTickets) {
                ticketRepositoryForTests.delete(ticket);
            }
        } catch (TicketRepositoryDeleteException exception) {
            throw new RuntimeException("Sample tickets could not be deleted from the repository.", exception);
        } catch (TicketRepositoryReadException exception) {
            throw new RuntimeException("Sample tickets could not be read from the repository.", exception);
        }

        try {
            List<Movie> listOfMovies = movieRepositoryForTests.findAll();
            for (Movie movie : listOfMovies) {
                movieRepositoryForTests.delete(movie);
            }
        } catch (MovieRepositoryDeleteException exception) {
            throw new RuntimeException("Sample movies could not be deleted from the repository.", exception);
        } catch (MovieRepositoryReadException exception) {
            throw new RuntimeException("Sample movies could not be read from the repository.", exception);
        }

        try {
            List<Client> listOfClients = clientRepositoryForTests.findAll();
            for (Client client : listOfClients) {
                clientRepositoryForTests.delete(client);
            }
        } catch (ClientRepositoryDeleteException exception) {
            throw new RuntimeException("Sample clients could not be deleted from the repository.", exception);
        } catch (ClientRepositoryReadException exception) {
            throw new RuntimeException("Sample clients could not be read from the repository.", exception);
        }
    }

    @Test
    public void ticketRepositoryConstructorTest() {
        TicketRepository ticketRepository = new TicketRepository(cqlSession);
        assertNotNull(ticketRepository);
    }

    @Test
    public void ticketRepositoryCreateNormalTicketTestPositive() throws TicketRepositoryReadException, TicketRepositoryCreateException {
        Ticket ticket = ticketRepositoryForTests.createNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        assertNotNull(ticket);

        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);

        assertEquals(foundTicket, ticket);
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithNullReservationTimeTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = null;
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithNegativeTicketBasePriceTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = -0.01;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithTicketBasePriceTooHighTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 100.01;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithTicketBasePriceEqualTo0TestPositive() throws TicketRepositoryCreateException {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 0;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket testTicket = ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(testTicket);

        assertEquals(movieTime, testTicket.getMovieTime());
        assertEquals(reservationTime, testTicket.getReservationTime());
        assertEquals(ticketBasePrice, testTicket.getTicketBasePrice());
        assertEquals(movieId, testTicket.getMovieId());
        assertEquals(clientId, testTicket.getClientId());
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithTicketBasePriceEqualTo100TestPositive() throws TicketRepositoryCreateException {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 0;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket testTicket = ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(testTicket);

        assertEquals(movieTime, testTicket.getMovieTime());
        assertEquals(reservationTime, testTicket.getReservationTime());
        assertEquals(ticketBasePrice, testTicket.getTicketBasePrice());
        assertEquals(movieId, testTicket.getMovieId());
        assertEquals(clientId, testTicket.getClientId());
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithNullMovieIdTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = null;
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateNormalTicketWithNullClientIdTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = null;
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketTestPositive() throws TicketRepositoryReadException, TicketRepositoryCreateException {
        Ticket ticket = ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        assertNotNull(ticket);

        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);

        assertEquals(foundTicket, ticket);
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithNullReservationTimeTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = null;
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithNegativeTicketBasePriceTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = -0.01;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithTicketBasePriceTooHighTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 100.01;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithTicketBasePriceEqualTo0TestPositive() throws TicketRepositoryCreateException {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 0;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket testTicket = ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(testTicket);

        assertEquals(movieTime, testTicket.getMovieTime());
        assertEquals(reservationTime, testTicket.getReservationTime());
        assertEquals(ticketBasePrice, testTicket.getTicketBasePrice());
        assertEquals(movieId, testTicket.getMovieId());
        assertEquals(clientId, testTicket.getClientId());
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithTicketBasePriceEqualTo100TestPositive() throws TicketRepositoryCreateException {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = 0;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket testTicket = ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(testTicket);

        assertEquals(movieTime, testTicket.getMovieTime());
        assertEquals(reservationTime, testTicket.getReservationTime());
        assertEquals(ticketBasePrice, testTicket.getTicketBasePrice());
        assertEquals(movieId, testTicket.getMovieId());
        assertEquals(clientId, testTicket.getClientId());
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithNullMovieIdTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = null;
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryCreateReducedTicketWithNullClientIdTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = null;
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId));
    }

    @Test
    public void ticketRepositoryUpdateNormalTicketThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(RepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticket));
    }

    @Test
    public void ticketRepositoryUpdateNormalTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        ticketNo1.setMovieTime(movieTime);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo1));
    }

    @Test
    public void ticketRepositoryUpdateNormalTicketWithNegativeMovieBasePriceTestNegative() {
        double movieBasePrice = -0.01;
        ticketNo1.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo1));
    }

    @Test
    public void ticketRepositoryUpdateNormalTicketWithMovieBasePriceTooHighTestNegative() {
        double movieBasePrice = 100.01;
        ticketNo1.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo1));
    }

    @Test
    public void ticketRepositoryUpdateReducedTicketThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(RepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticket));
    }

    @Test
    public void ticketRepositoryUpdateReducedTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        ticketNo2.setMovieTime(movieTime);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo2));
    }

    @Test
    public void ticketRepositoryUpdateReducedTicketWithNegativeMovieBasePriceTestNegative() {
        double movieBasePrice = -0.01;
        ticketNo2.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo2));
    }

    @Test
    public void ticketRepositoryUpdateReducedTicketWithMovieBasePriceTooHighTestNegative() {
        double movieBasePrice = 100.01;
        ticketNo2.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketRepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticketNo2));
    }

    @Test
    public void ticketRepositoryDeleteNormalTicketTestPositive() throws TicketRepositoryReadException {
        UUID removedTicketID = ticketNo1.getTicketID();

        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo1));

        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();

        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);

        assertThrows(TicketRepositoryReadException.class, () -> ticketRepositoryForTests.findByUUID(removedTicketID));
    }

    @Test
    public void ticketRepositoryDeleteNormalTicketThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket));
    }

    @Test
    public void ticketRepositoryDeleteNormalTicketByIdTestPositive() throws TicketRepositoryReadException {
        UUID removedTicketID = ticketNo1.getTicketID();

        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo1.getTicketID()));

        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();

        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);

        assertThrows(TicketRepositoryReadException.class, () -> ticketRepositoryForTests.findByUUID(removedTicketID));
    }

    @Test
    public void ticketRepositoryDeleteNormalTicketByIdThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket.getTicketID()));
    }

    @Test
    public void ticketRepositoryDeleteReducedTicketTestPositive() throws TicketRepositoryReadException {
        UUID removedTicketID = ticketNo2.getTicketID();

        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo2));

        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();

        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);

        assertThrows(TicketRepositoryReadException.class, () -> ticketRepositoryForTests.findByUUID(removedTicketID));
    }

    @Test
    public void ticketRepositoryDeleteReducedTicketThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket));
    }

    @Test
    public void ticketRepositoryDeleteReducedTicketByIdTestPositive() throws TicketRepositoryReadException {
        UUID removedTicketID = ticketNo2.getTicketID();

        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo2.getTicketID()));

        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();

        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);

        assertThrows(TicketRepositoryReadException.class, () -> ticketRepositoryForTests.findByUUID(removedTicketID));
    }

    @Test
    public void ticketRepositoryDeleteReducedTicketByIdThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket.getTicketID()));
    }

    @Test
    public void ticketRepositoryFindTicketTestPositive() throws TicketRepositoryReadException {
        Ticket ticket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(ticket);
        assertEquals(ticket, ticketNo1);
    }

    @Test
    public void ticketRepositoryFindTicketThatIsNotInTheDatabaseTestNegative() {
        Instant movieTime = ticketNo1.getMovieTime();
        Instant reservationTime = ticketNo1.getReservationTime();
        double ticketBasePrice = movieNo1.getMovieBasePrice();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketRepositoryReadException.class, () -> ticketRepositoryForTests.findByUUID(ticket.getTicketID()));
    }

    @Test
    public void ticketRepositoryFindAllTicketsTestPositive() throws TicketRepositoryReadException, TicketRepositoryDeleteException {
        List<Ticket> startingListOfTickets = ticketRepositoryForTests.findAll();
        assertNotNull(startingListOfTickets);

        ticketRepositoryForTests.delete(startingListOfTickets.get(0));

        List<Ticket> finalListOfTickets = ticketRepositoryForTests.findAll();
        assertNotNull(finalListOfTickets);

        assertEquals(startingListOfTickets.size(), 3);
        assertEquals(finalListOfTickets.size(), 2);
    }

    @Test
    public void ticketRepositoryFindAllTicketsForGivenClientIdTestPositive() throws TicketRepositoryReadException, TicketRepositoryCreateException {
        List<Ticket> startingListOfTickets = ticketRepositoryForTests.findAllTicketsForAGivenClientId(clientNo1.getClientID());
        assertNotNull(startingListOfTickets);

        ticketRepositoryForTests.createNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo1.getClientID());

        List<Ticket> finalListOfTickets = ticketRepositoryForTests.findAllTicketsForAGivenClientId(clientNo1.getClientID());
        assertNotNull(finalListOfTickets);

        assertEquals(startingListOfTickets.size(), 1);
        assertEquals(finalListOfTickets.size(), 2);
    }

    @Test
    public void ticketRepositoryFindAllTicketsForGivenMovieIdTestPositive() throws TicketRepositoryCreateException, TicketRepositoryReadException {
        List<Ticket> startingListOfTickets = ticketRepositoryForTests.findAllTicketsForAGivenMovieId(movieNo2.getMovieID());
        assertNotNull(startingListOfTickets);

        ticketRepositoryForTests.createNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo1.getClientID());

        List<Ticket> finalListOfTickets = ticketRepositoryForTests.findAllTicketsForAGivenMovieId(movieNo2.getMovieID());
        assertNotNull(finalListOfTickets);

        assertEquals(startingListOfTickets.size(), 1);
        assertEquals(finalListOfTickets.size(), 2);
    }
}
