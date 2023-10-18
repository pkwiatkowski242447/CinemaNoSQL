package manager_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.managers.*;
import model.repositories.*;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketManagerTest {

    private final Date movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime();
    private final Date movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime();
    private final Date movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime();

    private final Date reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
    private final Date reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime();
    private final Date reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime();

    private final Client clientNo1 = new Client(UUID.randomUUID(), "John", "Smith", 21);
    private final Client clientNo2 = new Client(UUID.randomUUID(), "Mary", "Jane", 18);
    private final Client clientNo3 = new Client(UUID.randomUUID(), "Vincent", "Vega", 40);

    private final ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
    private final ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
    private final ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Harry Potter and The Goblet of Fire", screeningRoomNo1);
    private final Movie movieNo2 = new Movie(UUID.randomUUID(), "The Da Vinci Code", screeningRoomNo2);
    private final Movie movieNo3 = new Movie(UUID.randomUUID(), "A Space Odyssey", screeningRoomNo3);

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<Ticket> ticketRepositoryForTests;
    private static Repository<Client> clientRepositoryForTests;
    private static Repository<Movie> movieRepositoryForTests;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTests;
    private static TicketManager ticketManagerForTests;
    private static ClientManager clientManagerForTests;
    private static MovieManager movieManagerForTests;
    private static ScreeningRoomManager screeningRoomManagerForTests;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        ticketRepositoryForTests = new TicketRepository(entityManager);
        clientRepositoryForTests = new ClientRepository(entityManager);
        movieRepositoryForTests = new MovieRepository(entityManager);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
        ticketManagerForTests = new TicketManager(ticketRepositoryForTests);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
        screeningRoomManagerForTests = new ScreeningRoomManager(screeningRoomRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        // entityManager.getTransaction().commit();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void populateTicketRepositoryForTests() {
        clientRepositoryForTests.create(clientNo1);
        clientRepositoryForTests.create(clientNo2);
        clientRepositoryForTests.create(clientNo3);

        screeningRoomRepositoryForTests.create(screeningRoomNo1);
        screeningRoomRepositoryForTests.create(screeningRoomNo2);
        screeningRoomRepositoryForTests.create(screeningRoomNo3);

        movieRepositoryForTests.create(movieNo1);
        movieRepositoryForTests.create(movieNo2);
        movieRepositoryForTests.create(movieNo3);

        Ticket ticketNo1 = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1 , movieNo1, clientNo1, new Normal(UUID.randomUUID(), 30));
        Ticket ticketNo2 = new Ticket(UUID.randomUUID(), movieTimeNo2, reservationTimeNo2 , movieNo2, clientNo2, new Reduced(UUID.randomUUID(), 25));
        Ticket ticketNo3 = new Ticket(UUID.randomUUID(), movieTimeNo3, reservationTimeNo3 , movieNo3, clientNo3, new Normal(UUID.randomUUID(), 40));

        ticketRepositoryForTests.create(ticketNo1);
        ticketRepositoryForTests.create(ticketNo2);
        ticketRepositoryForTests.create(ticketNo3);
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
        Repository<Ticket> ticketRepository = new TicketRepository(entityManager);
        assertNotNull(ticketRepository);
        Manager<Ticket> ticketManager = new TicketManager(ticketRepository);
        assertNotNull(ticketManager);
    }

    @Test
    public void setTicketRepositoryForTicketManagerTest() {
        Repository<Ticket> ticketRepositoryNo1 = new TicketRepository(entityManager);
        assertNotNull(ticketRepositoryNo1);
        Repository<Ticket> ticketRepositoryNo2 = new TicketRepository(entityManager);
        assertNotNull(ticketRepositoryNo2);
        Manager<Ticket> ticketManager = new TicketManager(ticketRepositoryNo1);
        assertNotNull(ticketManager);
        ticketManager.setObjectRepository(ticketRepositoryNo2);
        assertNotEquals(ticketRepositoryNo1, ticketManager.getObjectRepository());
        assertEquals(ticketRepositoryNo2, ticketManager.getObjectRepository());
    }

    @Test
    public void registerNewTicketTest() {
        int numOfTicketsBefore = ticketManagerForTests.getObjectRepository().findAll().size();
        Ticket ticket = ticketManagerForTests.register(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 20));
        assertNotNull(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getObjectRepository().findAll().size();
        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void registerNewTicketTestNegative() {
        int numOfTicketsBefore = ticketManagerForTests.getObjectRepository().findAll().size();
        Ticket ticket = ticketManagerForTests.register(null, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 20));
        assertNotNull(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getObjectRepository().findAll().size();
        assertEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void unregisterCertainTicketTestPositive() {
        int numOfTicketsBefore = ticketManagerForTests.getObjectRepository().findAll().size();
        Ticket someTicketFromRepo = ticketManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someTicketFromRepo);
        UUID removedTicketID = someTicketFromRepo.getTicketID();
        ticketRepositoryForTests.delete(someTicketFromRepo);
        int numOfTicketsAfter = ticketManagerForTests.getObjectRepository().findAll().size();
        Ticket foundTicket = ticketManagerForTests.get(removedTicketID);
        assertNull(foundTicket);
        assertNotEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void unregisterCertainTicketTestNegative() {
        int numOfTicketsBefore = ticketManagerForTests.getObjectRepository().findAll().size();
        Ticket ticket = ticketManagerForTests.register(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 20));
        assertNotNull(ticket);
        ticketManagerForTests.unregister(ticket);
        int numOfTicketsAfter = ticketManagerForTests.getObjectRepository().findAll().size();
        assertEquals(numOfTicketsBefore, numOfTicketsAfter);
    }

    @Test
    public void getCertainTicketFromTicketRepositoryTestPositive() {
        Ticket someTicketFromRepo = ticketManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someTicketFromRepo);
        Ticket foundTicket = ticketManagerForTests.get(someTicketFromRepo.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(someTicketFromRepo, foundTicket);
    }

    @Test
    public void getCertainTicketFromTicketRepositoryTestNegative() {
        Ticket ticket = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 20));
        assertNotNull(ticket);
        Ticket foundTicket = ticketManagerForTests.get(ticket.getTicketID());
        assertNull(foundTicket);
    }

    @Test
    public void getAllTicketsFromRepositoryTest() {
        List<Ticket> listOfAllTickets = ticketManagerForTests.getObjectRepository().findAll();
        assertNotNull(listOfAllTickets);
        assertEquals(3, listOfAllTickets.size());
    }
}
