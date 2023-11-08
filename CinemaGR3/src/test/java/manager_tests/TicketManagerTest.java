package manager_tests;

import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.managers.*;
import model.repositories.*;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketManagerTest {

    private final static String databaseName = "test";
    private final Date movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime();
    private final Date movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime();
    private final Date movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime();

    private final Date reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
    private final Date reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime();
    private final Date reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime();

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;

    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    private Ticket ticketNo1;
    private Ticket ticketNo2;
    private Ticket ticketNo3;

    private static TicketRepository ticketRepositoryForTests;
    private static ClientRepository clientRepositoryForTests;
    private static MovieRepository movieRepositoryForTests;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;
    private static TicketManager ticketManagerForTests;
    private static ClientManager clientManagerForTests;
    private static MovieManager movieManagerForTests;
    private static ScreeningRoomManager screeningRoomManagerForTests;

    @BeforeAll
    public static void init() {
        ticketRepositoryForTests = new TicketRepository(databaseName);
        clientRepositoryForTests = new ClientRepository(databaseName);
        movieRepositoryForTests = new MovieRepository(databaseName);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        ticketManagerForTests = new TicketManager(ticketRepositoryForTests);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
        screeningRoomManagerForTests = new ScreeningRoomManager(screeningRoomRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        ticketRepositoryForTests.close();
        clientRepositoryForTests.close();
        movieRepositoryForTests.close();
        screeningRoomRepositoryForTests.close();
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

        clientNo1 = clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
        clientNo2 = clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
        clientNo3 = clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);

        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        screeningRoomNo1 = screeningRoomRepositoryForTests.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        screeningRoomNo2 = screeningRoomRepositoryForTests.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        screeningRoomNo3 = screeningRoomRepositoryForTests.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        movieNo1 = movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1);
        movieNo2 = movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2);
        movieNo3 = movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3);

        ticketNo1 = ticketManagerForTests.getTicketRepository().create(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        ticketNo2 = ticketManagerForTests.getTicketRepository().create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "normal");
        ticketNo3 = ticketManagerForTests.getTicketRepository().create(movieTimeNo3, reservationTimeNo3, movieNo3, clientNo3, "normal");
    }

    @AfterEach
    public void depopulateTicketRepositoryAfterEach() {
        List<Ticket> listOfTickets = ticketRepositoryForTests.findAll();
        for (Ticket ticket : listOfTickets) {
            ticketRepositoryForTests.delete(ticket);
        }
        List<Client> listOfClients = clientRepositoryForTests.findAll();
        for (Client client : listOfClients) {
            clientRepositoryForTests.delete(client);
        }
        List<Movie> listOfMovies = movieRepositoryForTests.findAll();
        for (Movie movie : listOfMovies) {
            movieRepositoryForTests.delete(movie);
        }
        List<ScreeningRoom> listOfScreeningRooms = screeningRoomRepositoryForTests.findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            screeningRoomRepositoryForTests.delete(screeningRoom);
        }
    }

    @Test
    public void createTicketManagerTest() {
        TicketRepository ticketRepository = new TicketRepository(databaseName);
        assertNotNull(ticketRepository);
        TicketManager ticketManager = new TicketManager(ticketRepository);
        assertNotNull(ticketManager);
        ticketRepository.close();
    }

    @Test
    public void setTicketRepositoryForTicketManagerTest() {
        TicketRepository ticketRepositoryNo1 = new TicketRepository(databaseName);
        assertNotNull(ticketRepositoryNo1);
        TicketRepository ticketRepositoryNo2 = new TicketRepository(databaseName);
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
        Ticket ticket = ticketManagerForTests.register(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        assertNotNull(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getAll().size();
        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void registerNewTicketTestNegative() {
        Ticket ticket = ticketManagerForTests.register(null, reservationTimeNo1, movieNo1, clientNo1, "normal");
        assertNull(ticket);
    }

    @Test
    public void unregisterCertainTicketTestPositive() {
        int numOfTicketsBefore = ticketManagerForTests.getAllActive().size();
        Ticket someTicketFromRepo = ticketManagerForTests.getAllActive().get(0);
        assertNotNull(someTicketFromRepo);
        assertTrue(someTicketFromRepo.isTicketStatusActive());
        UUID removedTicketID = someTicketFromRepo.getTicketID();
        ticketManagerForTests.unregister(someTicketFromRepo);
        int numOfTicketsAfter = ticketManagerForTests.getAllActive().size();
        Ticket foundTicket = ticketManagerForTests.get(removedTicketID);
        assertNotNull(foundTicket);
        assertFalse(foundTicket.isTicketStatusActive());
        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void unregisterCertainTicketTestNegative() throws TicketReservationException {
        int numOfTicketsBefore = ticketManagerForTests.getAll().size();
        Ticket ticket = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        assertNotNull(ticket);
        ticketManagerForTests.unregister(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getAll().size();
        assertEquals(numOfTicketsBefore, numOfTicketsAfter);
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
    public void getCertainTicketFromTicketRepositoryTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        assertNotNull(ticket);
        Ticket foundTicket = ticketManagerForTests.get(ticket.getTicketID());
        assertNull(foundTicket);
    }

    @Test
    public void getAllTicketsFromRepositoryTest() {
        List<Ticket> listOfAllTicketsNo1 = ticketManagerForTests.getTicketRepository().findAll();
        List<Ticket> listOfAllTicketsNo2 = ticketManagerForTests.getAll();
        assertNotNull(listOfAllTicketsNo1);
        assertNotNull(listOfAllTicketsNo2);
        assertEquals(listOfAllTicketsNo1.size(), listOfAllTicketsNo2.size());
    }
}
