package manager_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.managers.*;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;
import model.ticket_types.Normal;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketManagerTest {

    private final Instant movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime().toInstant();
    private final Instant movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime().toInstant();
    private final Instant movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime().toInstant();

    private final Instant reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime().toInstant();
    private final Instant reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime().toInstant();
    private final Instant reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime().toInstant();

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    private Ticket ticketNo1;
    private Ticket ticketNo2;
    private Ticket ticketNo3;

    private static TicketRepository ticketRepositoryForTests;
    private static ClientRepository clientRepositoryForTests;
    private static MovieRepository movieRepositoryForTests;
    private static TicketManager ticketManagerForTests;
    private static ClientManager clientManagerForTests;
    private static MovieManager movieManagerForTests;

    private static CqlSession cqlSession;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();

        ticketRepositoryForTests = new TicketRepository(cqlSession);
        clientRepositoryForTests = new ClientRepository(cqlSession);
        movieRepositoryForTests = new MovieRepository(cqlSession);
        ticketManagerForTests = new TicketManager(ticketRepositoryForTests);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        cqlSession.close();
    }

    @BeforeEach
    public void populateTicketRepositoryForTests() {
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
            throw new RuntimeException("Sample clients could not be created in repository.", exception);
        }

        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo3Floor = 0;
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
            throw new RuntimeException("Sample movies could not be created in repository.", exception);
        }

        try {
            ticketNo1 = ticketManagerForTests.getTicketRepository().createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
            ticketNo2 = ticketManagerForTests.getTicketRepository().createNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
            ticketNo3 = ticketManagerForTests.getTicketRepository().createNormalTicket(movieTimeNo3, reservationTimeNo3, movieNo3.getMovieBasePrice(), movieNo3.getMovieID(), clientNo3.getClientID());
        } catch (TicketRepositoryCreateException exception) {
            throw new RuntimeException("Sample tickets could not be created in repository.", exception);
        }
    }

    @AfterEach
    public void depopulateTicketRepositoryAfterEach() {
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
    public void createTicketManagerTest() throws CassandraConfigNotFound {
        TicketRepository ticketRepository = new TicketRepository(cqlSession);
        assertNotNull(ticketRepository);
        TicketManager ticketManager = new TicketManager(ticketRepository);
        assertNotNull(ticketManager);
        ticketRepository.close();
    }

    @Test
    public void setTicketRepositoryForTicketManagerTest() throws CassandraConfigNotFound {
        TicketRepository ticketRepositoryNo1 = new TicketRepository(cqlSession);
        assertNotNull(ticketRepositoryNo1);
        TicketRepository ticketRepositoryNo2 = new TicketRepository(cqlSession);
        assertNotNull(ticketRepositoryNo2);
        TicketManager ticketManager = new TicketManager(ticketRepositoryNo1);
        assertNotNull(ticketManager);
        ticketManager.setTicketRepository(ticketRepositoryNo2);
        assertNotEquals(ticketRepositoryNo1, ticketManager.getTicketRepository());
        assertEquals(ticketRepositoryNo2, ticketManager.getTicketRepository());
        ticketRepositoryNo1.close();
        ticketRepositoryNo2.close();
    }

    @Test
    public void registerNewTicketTestPositive() {
        int numOfTicketsBefore = ticketManagerForTests.getAll().size();
        Ticket ticket = ticketManagerForTests.registerNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getAll().size();
        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void registerNewTicketTestNegative() {
        Ticket ticket = ticketManagerForTests.registerNormalTicket(null, reservationTimeNo1, movieNo1.getMovieBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNull(ticket);
    }

    @Test
    public void getCertainTicketFromTicketRepositoryTestPositive() {
        Ticket someTicketFromRepo = ticketManagerForTests.getAll().get(0);
        assertNotNull(someTicketFromRepo);
        Ticket foundTicket = ticketManagerForTests.get(someTicketFromRepo.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(someTicketFromRepo, foundTicket);
    }

    @Test
    public void getCertainTicketFromTicketRepositoryTestNegative() {
        Ticket ticket = new Normal(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1.getMovieBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        Ticket foundTicket = ticketManagerForTests.get(ticket.getTicketID());
        assertNull(foundTicket);
    }

    @Test
    public void getAllTicketsFromRepositoryTest() throws TicketRepositoryReadException {
        List<Ticket> listOfAllTicketsNo1 = ticketManagerForTests.getTicketRepository().findAll();
        List<Ticket> listOfAllTicketsNo2 = ticketManagerForTests.getAll();
        assertNotNull(listOfAllTicketsNo1);
        assertNotNull(listOfAllTicketsNo2);
        assertEquals(listOfAllTicketsNo1.size(), listOfAllTicketsNo2.size());
    }
}
