package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;
import model.repositories.*;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<Ticket> ticketRepositoryForTests;
    private static Repository<Client> clientRepositoryForTests;
    private static Repository<Movie> movieRepositoryForTests;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTests;

    private final ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
    private final ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
    private final ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Harry Potter and The Goblet of Fire", screeningRoomNo1);
    private final Movie movieNo2 = new Movie(UUID.randomUUID(), "The Da Vinci Code", screeningRoomNo2);
    private final Movie movieNo3 = new Movie(UUID.randomUUID(), "A Space Odyssey", screeningRoomNo3);

    private final Client clientNo1 = new Client(UUID.randomUUID(), "John", "Smith", 21);
    private final Client clientNo2 = new Client(UUID.randomUUID(), "Mary", "Jane", 18);
    private final Client clientNo3 = new Client(UUID.randomUUID(), "Vincent", "Vega", 40);

    private final Date movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime();
    private final Date movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime();
    private final Date movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime();

    private final Date reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
    private final Date reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime();
    private final Date reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime();

    private Ticket ticketNo1;
    private Ticket ticketNo2;
    private Ticket ticketNo3;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        ticketRepositoryForTests = new TicketRepository(entityManager);
        movieRepositoryForTests = new MovieRepository(entityManager);
        clientRepositoryForTests = new ClientRepository(entityManager);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void insertExampleTickets() throws TicketReservationException {
        ticketNo1 = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 30));
        ticketNo2 = new Ticket(UUID.randomUUID(), movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, new Reduced(UUID.randomUUID(), 25));
        ticketNo3 = new Ticket(UUID.randomUUID(), movieTimeNo3, reservationTimeNo3, movieNo3, clientNo3, new Normal(UUID.randomUUID(), 40));

        clientRepositoryForTests.create(clientNo1);
        clientRepositoryForTests.create(clientNo2);
        clientRepositoryForTests.create(clientNo3);

        screeningRoomRepositoryForTests.create(screeningRoomNo1);
        screeningRoomRepositoryForTests.create(screeningRoomNo2);
        screeningRoomRepositoryForTests.create(screeningRoomNo3);

        movieRepositoryForTests.create(movieNo1);
        movieRepositoryForTests.create(movieNo2);
        movieRepositoryForTests.create(movieNo3);

        ticketRepositoryForTests.create(ticketNo1);
        ticketRepositoryForTests.create(ticketNo2);
        ticketRepositoryForTests.create(ticketNo3);
    }

    @AfterEach
    public void deleteExampleTickets() {
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
    public void ticketRepositoryConstructorTest() {
        Repository<Ticket> ticketRepository = new TicketRepository(entityManager);
        assertNotNull(ticketRepository);
    }

    @Test
    public void createNewTicketTestPositive() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(), 20);
        assertNotNull(typeOfTicket);
        int numberOfAvailableSeatsBefore = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        int numberOfAvailableSeatsAfter = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        assertNotNull(ticket);
        assertDoesNotThrow(() -> ticketRepositoryForTests.create(ticket));
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticket);
        assertNotEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter);
        assertEquals(numberOfAvailableSeatsBefore - 1, numberOfAvailableSeatsAfter);
    }

    @Test
    public void createNewTicketTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(ticketNo1.getTicketID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryCreateException.class, () -> ticketRepositoryForTests.create(ticket));
    }

    @Test
    public void createNewTicketWithNullIdTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(null, ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryCreateException.class, () -> ticketRepositoryForTests.create(ticket));
    }

    @Test
    public void createNewTicketWithNullMovieTimeTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), null, ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryCreateException.class, () -> ticketRepositoryForTests.create(ticket));
    }

    @Test
    public void createNewTicketWithNullReservationTimeTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), null, movieNo2, clientNo2, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryCreateException.class, () -> ticketRepositoryForTests.create(ticket));
    }

    @Test
    public void createNewTicketWithNullClientTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, null, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryCreateException.class, () -> ticketRepositoryForTests.create(ticket));
    }

    @Test
    public void createNewTicketWithNullMovieTestNegative() {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        assertNotNull(typeOfTicket);
        assertThrows(TicketReservationException.class, () -> {
            Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), null, clientNo2, typeOfTicket);
        });
    }

    @Test
    public void createNewTicketWithNullTicketTypeTestNegative() {
        assertThrows(TicketReservationException.class, () -> {
            Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, null);
        });
    }

    @Test
    public void addTwoTicketsWhenThereIsOnlyOneSeat() throws TicketReservationException {
        movieNo2.getScreeningRoom().setNumberOfAvailableSeats(1);
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(),20);
        int numberOfSeatsBefore = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        Ticket ticketN1 = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        int numberOfSeatsAfterTicketOne = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        assertEquals(1, numberOfSeatsBefore);
        assertEquals(0, numberOfSeatsAfterTicketOne);
        assertDoesNotThrow(() -> ticketRepositoryForTests.create(ticketN1));
        assertThrows(TicketReservationException.class, () -> {
            Ticket ticketN2 = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, typeOfTicket);
        });
    }

    @Test
    public void updateCertainTicketTestPositive() {
        boolean oldActiveStatus = ticketNo1.isTicketStatusActive();
        boolean newActiveStatus = false;
        ticketNo1.setTicketStatusActive(newActiveStatus);
        assertDoesNotThrow(() -> ticketRepositoryForTests.update(ticketNo1));
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticketNo1);
        assertNotEquals(oldActiveStatus, newActiveStatus);
        assertEquals(newActiveStatus, foundTicket.isTicketStatusActive());
    }

    @Test
    public void updateCertainTicketTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(), 20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticket));
    }

    @Test
    public void deleteCertainTicketTestPositive() {
        UUID removedTicketID = ticketNo1.getTicketID();
        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo1));
        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();
        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(removedTicketID);
        assertNull(foundTicket);
    }

    @Test
    public void deleteCertainTicketTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(), 20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, typeOfTicket);
        assertNotNull(ticket);
        assertThrows(RepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket));
    }

    @Test
    public void findCertainTicketTestPositive() {
        Ticket ticket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(ticket);
        assertEquals(ticket, ticketNo1);
    }

    @Test
    public void findCertainTicketTestNegative() throws TicketReservationException {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID(), 20);
        assertNotNull(typeOfTicket);
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, typeOfTicket);
        assertNotNull(ticket);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNull(foundTicket);
    }

    @Test
    public void removeCertainClientAlongWithTheirTicketTest() {
        int numberOfTicketsBefore = ticketRepositoryForTests.findAll().size();
        clientRepositoryForTests.delete(clientNo1);
        int numberOfTicketsAfter = ticketRepositoryForTests.findAll().size();
        assertNotEquals(numberOfTicketsBefore, numberOfTicketsAfter);
        assertEquals(numberOfTicketsBefore - 1, numberOfTicketsAfter);
    }

    @Test
    public void removeCertainMovieAlongWithTheirTicketTest() {
        int numberOfTicketsBefore = ticketRepositoryForTests.findAll().size();
        movieRepositoryForTests.delete(movieNo1);
        int numberOfTicketsAfter = ticketRepositoryForTests.findAll().size();
        assertNotEquals(numberOfTicketsBefore, numberOfTicketsAfter);
        assertEquals(numberOfTicketsBefore - 1, numberOfTicketsAfter);
    }

    @Test
    public void removeCertainScreeningRoomAlongWithTheirTicketsAndMoviesTest() {
        int numberOfMoviesBefore = movieRepositoryForTests.findAll().size();
        int numberOfTicketsBefore = ticketRepositoryForTests.findAll().size();
        screeningRoomRepositoryForTests.delete(screeningRoomNo1);
        int numberOfMoviesAfter = movieRepositoryForTests.findAll().size();
        int numberOfTicketsAfter = ticketRepositoryForTests.findAll().size();
        assertNotEquals(numberOfMoviesBefore, numberOfMoviesAfter);
        assertEquals(numberOfMoviesBefore - 1, numberOfMoviesAfter);
        assertNotEquals(numberOfTicketsBefore, numberOfTicketsAfter);
        assertEquals(numberOfTicketsBefore - 1, numberOfTicketsAfter);
    }

    @Test
    public void findAllTicketsTestPositive() {
        List<Ticket> listOfTickets = ticketRepositoryForTests.findAll();
        assertNotNull(listOfTickets);
        assertEquals(3, listOfTickets.size());
    }
}
