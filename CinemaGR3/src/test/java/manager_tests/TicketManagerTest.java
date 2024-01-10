package manager_tests;

import model.constants.GeneralConstants;
import model.exceptions.managers.create_exceptions.ClientManagerCreateException;
import model.exceptions.managers.create_exceptions.CreateManagerException;
import model.exceptions.managers.create_exceptions.MovieManagerCreateException;
import model.exceptions.managers.create_exceptions.TicketManagerCreateException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.delete_exceptions.TicketManagerDeleteException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.read_exceptions.TicketManagerReadException;
import model.exceptions.managers.update_exceptions.TicketManagerUpdateException;
import model.exceptions.managers.update_exceptions.UpdateManagerException;
import model.managers.implementations.ClientManager;
import model.managers.implementations.MovieManager;
import model.managers.implementations.TicketManager;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.exceptions.MongoConfigNotFoundException;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;
import model.model.ticket_types.Normal;
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


    @BeforeAll
    public static void init() throws MongoConfigNotFoundException {
        ticketRepositoryForTests = new TicketRepository(GeneralConstants.TEST_DB_NAME);
        clientRepositoryForTests = new ClientRepository(GeneralConstants.TEST_DB_NAME);
        movieRepositoryForTests = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        ticketManagerForTests = new TicketManager(clientRepositoryForTests, movieRepositoryForTests, ticketRepositoryForTests);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        clientRepositoryForTests.close();
        movieRepositoryForTests.close();
        ticketRepositoryForTests.close();
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
            clientNo1 = clientManagerForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
            clientNo2 = clientManagerForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
            clientNo3 = clientManagerForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);
        } catch (ClientManagerCreateException exception) {
            throw new RuntimeException("Sample clients could not be created in repository.", exception);
        }

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo1Number = 10;

        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo2Number = 5;

        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        int screeningRoomNo3Number = 19;

        try {
            movieNo1 = movieManagerForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            movieNo2 = movieManagerForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            movieNo3 = movieManagerForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
        } catch (MovieManagerCreateException exception) {
            throw new RuntimeException("Sample movies could not be created in repository.", exception);
        }

        try {
            ticketNo1 = ticketManagerForTests.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieID(), clientNo1.getClientID());
            ticketNo2 = ticketManagerForTests.createNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieID(), clientNo2.getClientID());
            ticketNo3 = ticketManagerForTests.createNormalTicket(movieTimeNo3, reservationTimeNo3, movieNo3.getMovieID(), clientNo3.getClientID());
        } catch (TicketManagerCreateException exception) {
            throw new RuntimeException("Sample tickets could not be created in repository.", exception);
        }
    }

    @AfterEach
    public void depopulateTicketRepositoryAfterEach() {
        try {
            List<Ticket> listOfTickets = ticketManagerForTests.findAll();
            for (Ticket ticket : listOfTickets) {
                ticketManagerForTests.delete(ticket);
            }
        } catch (DeleteManagerException exception) {
            throw new RuntimeException("Sample tickets could not be deleted from the repository.", exception);
        } catch (ReadManagerException exception) {
            throw new RuntimeException("Sample tickets could not be read from the repository.", exception);
        }

        try {
            List<Movie> listOfMovies = movieManagerForTests.findAll();
            for (Movie movie : listOfMovies) {
                movieManagerForTests.delete(movie);
            }
        } catch (DeleteManagerException exception) {
            throw new RuntimeException("Sample movies could not be deleted from the repository.", exception);
        } catch (ReadManagerException exception) {
            throw new RuntimeException("Sample movies could not be read from the repository.", exception);
        }

        try {
            List<Client> listOfClients = clientManagerForTests.findAll();
            for (Client client : listOfClients) {
                clientManagerForTests.delete(client);
            }
        } catch (DeleteManagerException exception) {
            throw new RuntimeException("Sample clients could not be deleted from the repository.", exception);
        } catch (ReadManagerException exception) {
            throw new RuntimeException("Sample clients could not be read from the repository.", exception);
        }
    }

    @Test
    public void createTicketManagerTest() throws MongoConfigNotFoundException {
        ClientRepository clientRepository = new ClientRepository(GeneralConstants.TEST_DB_NAME);
        MovieRepository movieRepository = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        TicketRepository ticketRepository = new TicketRepository(GeneralConstants.TEST_DB_NAME);

        assertNotNull(clientRepository);
        assertNotNull(movieRepository);
        assertNotNull(ticketRepository);

        TicketManager ticketManager = new TicketManager(clientRepository, movieRepository, ticketRepository);

        assertNotNull(ticketManager);
    }

    @Test
    public void setTicketRepositoryForTicketManagerTest() throws MongoConfigNotFoundException {
        TicketRepository ticketRepositoryNo1 = new TicketRepository(GeneralConstants.TEST_DB_NAME);
        assertNotNull(ticketRepositoryNo1);

        TicketRepository ticketRepositoryNo2 = new TicketRepository(GeneralConstants.TEST_DB_NAME);
        assertNotNull(ticketRepositoryNo2);

        TicketManager ticketManager = new TicketManager(clientRepositoryForTests, movieRepositoryForTests, ticketRepositoryNo1);
        assertNotNull(ticketManager);

        ticketManager.setTicketRepository(ticketRepositoryNo2);

        assertNotEquals(ticketRepositoryNo1, ticketManager.getTicketRepository());
        assertEquals(ticketRepositoryNo2, ticketManager.getTicketRepository());
    }

    @Test
    public void ticketManagerCreateTicketTestPositive() throws ReadManagerException, CreateManagerException {
        int numOfTicketsBefore = ticketManagerForTests.findAll().size();
        int numberOfAvailableSeatsBefore = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();

        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId);
        assertNotNull(ticket);

        int numOfTicketsAfter = ticketManagerForTests.findAll().size();
        int numberOfAvailableSeatsAfter = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();

        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
        assertEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter + 1);
    }

    @Test
    public void ticketManagerCreateTwoTicketsWhenThereIsOnlyOneAvailableSeatTestPositive() throws UpdateManagerException, ReadManagerException, CreateManagerException {
        movieNo1.setNumberOfAvailableSeats(1);
        movieManagerForTests.update(movieNo1);

        int numberOfAvailableSeatsBeforeFirstOne = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();

        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId);
        assertNotNull(ticket);

        int numberOfAvailableSeatsBeforeSecondOne = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();

        assertThrows(TicketManagerCreateException.class, () -> ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId));

        int numberOfAvailableSeatsAfterSecondOne = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();

        assertEquals(numberOfAvailableSeatsBeforeFirstOne, 1);
        assertEquals(numberOfAvailableSeatsBeforeSecondOne, 0);
        assertEquals(numberOfAvailableSeatsAfterSecondOne, 0);
    }

    @Test
    public void ticketManagerCreateTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        Instant reservationTime = Instant.now();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketManagerCreateException.class, () -> ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId));
    }

    @Test
    public void ticketManagerCreateTicketWithNullReservationTimeTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = null;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketManagerCreateException.class, () -> ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId));
    }

    @Test
    public void ticketManagerCreateTicketWithNullMovieIdTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        UUID movieId = null;
        UUID clientId = clientNo1.getClientID();
        assertThrows(TicketManagerCreateException.class, () -> ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId));
    }

    @Test
    public void ticketManagerCreateTicketWithNullClientIdTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = null;
        assertThrows(TicketManagerCreateException.class, () -> ticketManagerForTests.createNormalTicket(movieTime, reservationTime, movieId, clientId));
    }

    @Test
    public void ticketManagerUpdateTicketThatIsNotInTheRepositoryTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        double movieBasePrice = 42.50;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, movieBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketManagerUpdateException.class, () -> ticketManagerForTests.update(ticket));
    }

    @Test
    public void ticketManagerUpdateTicketWithNullMovieTimeTestNegative() {
        Instant movieTime = null;
        ticketNo1.setMovieTime(movieTime);
        assertThrows(TicketManagerUpdateException.class, () -> ticketManagerForTests.update(ticketNo1));
    }

    @Test
    public void ticketManagerUpdateTicketWithMovieBasePriceTooLowTestNegative() {
        double movieBasePrice = -0.01;
        ticketNo1.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketManagerUpdateException.class, () -> ticketManagerForTests.update(ticketNo1));
    }

    @Test
    public void ticketManagerUpdateTicketWithMovieBasePriceTooHighTestNegative() {
        double movieBasePrice = 100.01;
        ticketNo1.setTicketBasePrice(movieBasePrice);
        assertThrows(TicketManagerUpdateException.class, () -> ticketManagerForTests.update(ticketNo1));
    }

    @Test
    public void ticketManagerUpdateTicketWithMovieBasePriceEqualTo0TestPositive() throws UpdateManagerException, ReadManagerException{
        double movieBasePrice = 0;
        ticketNo1.setTicketBasePrice(movieBasePrice);

        ticketManagerForTests.update(ticketNo1);

        Ticket foundTicket = ticketManagerForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(foundTicket);

        assertEquals(ticketNo1, foundTicket);
    }

    @Test
    public void ticketManagerUpdateTicketWithMovieBasePriceEqualTo100TestPositive() throws UpdateManagerException, ReadManagerException{
        double movieBasePrice = 100;
        ticketNo1.setTicketBasePrice(movieBasePrice);

        ticketManagerForTests.update(ticketNo1);

        Ticket foundTicket = ticketManagerForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(foundTicket);

        assertEquals(ticketNo1, foundTicket);
    }

    @Test
    public void ticketManagerDeleteTicketTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfAvailableSeatsBefore = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();
        int numberOfTicketsBefore = ticketManagerForTests.findAll().size();

        UUID removedTicketUUID = ticketNo1.getTicketID();

        ticketManagerForTests.delete(ticketNo1);

        int numberOfAvailableSeatsAfter = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();
        int numberOfTicketsAfter = ticketManagerForTests.findAll().size();

        assertEquals(numberOfTicketsBefore, 3);
        assertEquals(numberOfTicketsAfter, 2);
        assertEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter - 1);

        assertThrows(TicketManagerReadException.class, () -> ticketManagerForTests.findByUUID(removedTicketUUID));
    }

    @Test
    public void ticketManagerDeleteTicketThatIsNotInTheRepositoryTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        double movieBasePrice = 42.50;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, movieBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketManagerDeleteException.class, () -> ticketManagerForTests.delete(ticket));
    }

    @Test
    public void ticketManagerDeleteTicketByIdTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfAvailableSeatsBefore = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();
        int numberOfTicketsBefore = ticketManagerForTests.findAll().size();

        UUID removedTicketUUID = ticketNo1.getTicketID();

        ticketManagerForTests.delete(ticketNo1.getTicketID());

        int numberOfAvailableSeatsAfter = movieManagerForTests.findByUUID(movieNo1.getMovieID()).getNumberOfAvailableSeats();
        int numberOfTicketsAfter = ticketManagerForTests.findAll().size();

        assertEquals(numberOfTicketsBefore, 3);
        assertEquals(numberOfTicketsAfter, 2);
        assertEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter - 1);

        assertThrows(TicketManagerReadException.class, () -> ticketManagerForTests.findByUUID(removedTicketUUID));
    }

    @Test
    public void ticketManagerDeleteTicketByIdThatIsNotInTheRepositoryTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        double movieBasePrice = 42.50;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, movieBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketManagerDeleteException.class, () -> ticketManagerForTests.delete(ticket.getTicketID()));
    }

    @Test
    public void ticketManagerFindTicketTestPositive() throws ReadManagerException {
        Ticket foundTicket = ticketManagerForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(ticketNo1, foundTicket);
    }

    @Test
    public void ticketManagerFindTicketThatIsNotInTheRepositoryTestNegative() {
        Instant movieTime = Instant.now();
        Instant reservationTime = Instant.now();
        double movieBasePrice = 25.75;
        UUID movieId = movieNo1.getMovieID();
        UUID clientId = clientNo1.getClientID();

        Ticket ticket = new Normal(UUID.randomUUID(), movieTime, reservationTime, movieBasePrice, movieId, clientId);
        assertNotNull(ticket);

        assertThrows(TicketManagerReadException.class, () -> ticketManagerForTests.findByUUID(ticket.getTicketID()));
    }

    @Test
    public void ticketManagerFindAllTicketsTestPositive() throws ReadManagerException, DeleteManagerException {
        List<Ticket> startingListOfTickets = ticketManagerForTests.findAll();
        assertNotNull(startingListOfTickets);

        ticketManagerForTests.delete(ticketNo1);

        List<Ticket> finalListOfTickets = ticketManagerForTests.findAll();
        assertNotNull(finalListOfTickets);

        assertEquals(startingListOfTickets.size(), 3);
        assertEquals(finalListOfTickets.size(), 2);
    }
    
    @Test
    public void ticketManagerCreateThreeNewTickets() throws TicketManagerCreateException {
        ticketManagerForTests.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieID(), clientNo1.getClientID());
        ticketManagerForTests.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo2.getMovieID(), clientNo1.getClientID());
        ticketManagerForTests.createReducedTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieID(), clientNo1.getClientID());
        ticketManagerForTests.createReducedTicket(movieTimeNo1, reservationTimeNo1, movieNo2.getMovieID(), clientNo1.getClientID());
    }
}
