package repository_tests.mongo;

import mapping_layer.model_docs.TicketRow;
import mapping_layer.model_docs.ticket_types.TypeOfTicketRow;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.exceptions.repository_exceptions.*;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.ScreeningRoomRepository;
import model.repositories.implementations.TicketRepository;
import model.ticket_types.Normal;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    private final static String databaseName = "test";
    private static TicketRepository ticketRepositoryForTests;
    private static ClientRepository clientRepositoryForTests;
    private static MovieRepository movieRepositoryForTests;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;

    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;

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
    public static void init() throws MongoConfigNotFoundException {
        clientRepositoryForTests = new ClientRepository(databaseName);
        movieRepositoryForTests = new MovieRepository(databaseName);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        ticketRepositoryForTests = new TicketRepository(databaseName);
    }

    @AfterAll
    public static void destroy() {
        ticketRepositoryForTests.close();
        clientRepositoryForTests.close();
        movieRepositoryForTests.close();
        screeningRoomRepositoryForTests.close();
    }

    @BeforeEach
    public void insertExampleTickets() {
        String clientNo1Name = "John";
        String clientNo1Surname = "Smith";
        int clientNo1Age = 21;
        clientNo1 = clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
        String clientNo2Name = "Mary";
        String clientNo2Surname = "Jane";
        int clientNo2Age = 18;
        clientNo2 = clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
        String clientNo3Name = "Vincent";
        String clientNo3Surname = "Vega";
        int clientNo3Age = 40;
        clientNo3 = clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);

        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        screeningRoomNo1 = screeningRoomRepositoryForTests.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        screeningRoomNo2 = screeningRoomRepositoryForTests.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;
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

        ticketNo1 = ticketRepositoryForTests.create(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        ticketNo2 = ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "reduced");
        ticketNo3 = ticketRepositoryForTests.create(movieTimeNo3, reservationTimeNo3, movieNo3, clientNo3, "normal");
    }

    @AfterEach
    public void deleteExampleTickets() {
        List<UUID> listOfTicketsUUIDs = ticketRepositoryForTests.findAllUUIDs();
        for (UUID ticketUUID : listOfTicketsUUIDs) {
            ticketRepositoryForTests.delete(ticketUUID);
        }

        List<UUID> listOfMoviesUUID = movieRepositoryForTests.findAllUUIDs();
        for (UUID movieUUID : listOfMoviesUUID) {
            movieRepositoryForTests.delete(movieUUID);
        }

        List<UUID> listOfAllClientsUUIDs = clientRepositoryForTests.findAllUUIDs();
        for (UUID clientID : listOfAllClientsUUIDs) {
            clientRepositoryForTests.delete(clientID);
        }

        List<UUID> listOfScreeningRoomsUUIDs = screeningRoomRepositoryForTests.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            screeningRoomRepositoryForTests.delete(screeningRoomID);
        }
    }

    @Test
    public void ticketRepositoryConstructorTest() throws MongoConfigNotFoundException {
        TicketRepository ticketRepository = new TicketRepository(databaseName);
        assertNotNull(ticketRepository);
    }

    @Test
    public void createNewTicketTestPositive() {
        int numberOfAvailableSeatsBefore = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        Ticket ticket = ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "reduced");
        int numberOfAvailableSeatsAfter = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        assertNotNull(ticket);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticket);
        assertNotEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter);
        assertEquals(numberOfAvailableSeatsBefore - 1, numberOfAvailableSeatsAfter);
    }

    @Test
    public void checkIfScreeningRoomNumOfSeatsIsUpdatedCorrectly() throws GeneralRepositoryException {
        int numberOfAvailableSeatsBefore = screeningRoomRepositoryForTests.findByUUID(movieNo2.getScreeningRoom().getScreeningRoomID()).getNumberOfAvailableSeats();
        Ticket ticket = ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "reduced");
        int numberOfAvailableSeatsAfter = screeningRoomRepositoryForTests.findByUUID(movieNo2.getScreeningRoom().getScreeningRoomID()).getNumberOfAvailableSeats();
        assertNotNull(ticket);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticket);
        assertNotEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter);
        assertEquals(numberOfAvailableSeatsBefore - 1, numberOfAvailableSeatsAfter);
        ticketRepositoryForTests.delete(foundTicket.getTicketID());
        int numberOfAvailableSeatsAfterDeletion = screeningRoomRepositoryForTests.findByUUID(movieNo2.getScreeningRoom().getScreeningRoomID()).getNumberOfAvailableSeats();
        assertNotEquals(numberOfAvailableSeatsAfter, numberOfAvailableSeatsAfterDeletion);
        assertEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfterDeletion);
    }

    @Test
    public void createNewTicketWithNullMovieTimeTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.create(null, reservationTimeNo2, movieNo2, clientNo2, "reduced"));
    }

    @Test
    public void createNewTicketWithNullReservationTimeTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.create(movieTimeNo2, null, movieNo2, clientNo2, "reduced"));
    }

    @Test
    public void createNewTicketWithNullClientTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, null, "reduced"));
    }

    @Test
    public void createNewTicketWithNullTypeOfTicketTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, null));
    }

    @Test
    public void createNewTicketWithNullMovieTestNegative() {
        assertThrows(TicketReservationException.class, () -> {
            Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), null, clientNo2, "reduced");
        });
    }

    @Test
    public void createNewTicketWithAScreeningRoomWithoutAnyAvailableSeat() {
        ScreeningRoom screeningRoom = screeningRoomRepositoryForTests.findAll().get(0);
        screeningRoom.setNumberOfAvailableSeats(0);
        screeningRoomRepositoryForTests.updateAllFields(screeningRoom);

        assertThrows(TicketRepositoryCreateException.class, () -> {
            Ticket ticket = ticketRepositoryForTests.create(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "reduced");
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
        int numberOfSeatsBefore = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        final Ticket[] ticketN1 = new Ticket[1];
        assertDoesNotThrow(() -> {
            ticketN1[0] = ticketRepositoryForTests.create(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "reduced");
        });
        int numberOfSeatsAfterTicketOne = movieNo2.getScreeningRoom().getNumberOfAvailableSeats();
        assertNotNull(ticketN1[0]);
        assertEquals(1, numberOfSeatsBefore);
        assertEquals(0, numberOfSeatsAfterTicketOne);
        assertThrows(TicketReservationException.class, () -> {
            Ticket ticketN2 = new Ticket(UUID.randomUUID(), ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), movieNo2, clientNo2, "reduced");
        });
    }

    @Test
    public void updateCertainTicketTestPositive() {
        boolean oldActiveStatus = ticketNo1.isTicketStatusActive();
        boolean newActiveStatus = false;
        ticketNo1.setTicketStatusActive(newActiveStatus);
        assertDoesNotThrow(() -> ticketRepositoryForTests.updateAllFields(ticketNo1));
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticketNo1);
        assertNotEquals(oldActiveStatus, newActiveStatus);
        assertEquals(newActiveStatus, foundTicket.isTicketStatusActive());
    }

    @Test
    public void updateCertainTicketTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, "reduced");
        assertNotNull(ticket);
        assertThrows(RepositoryUpdateException.class, () -> ticketRepositoryForTests.updateAllFields(ticket));
    }

    @Test
    public void deleteCertainTicketTestPositive() {
        UUID removedTicketID = ticketNo1.getTicketID();
        int numberOfTicketsBeforeDelete = ticketRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> ticketRepositoryForTests.delete(ticketNo1));
        int numberOfTicketsAfterDelete = ticketRepositoryForTests.findAll().size();
        assertNotEquals(numberOfTicketsBeforeDelete, numberOfTicketsAfterDelete);
        assertEquals(numberOfTicketsBeforeDelete - 1, numberOfTicketsAfterDelete);
        assertThrows(TicketRepositoryReadException.class, () -> {
            Ticket foundTicket = ticketRepositoryForTests.findByUUID(removedTicketID);
        });
    }

    @Test
    public void deleteCertainTicketThatIsNotInTheDatabaseTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, "reduced");
        assertNotNull(ticket);
        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket));
    }

    @Test
    public void deleteCertainTicketWithUUIDThatIsNotInTheDatabaseTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, "reduced");
        assertNotNull(ticket);
        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket.getTicketID()));
    }

    @Test
    public void expireCertainTicketTestPositive() {
        UUID expiredTicketUUID = ticketNo1.getTicketID();
        int beforeExpiringTicket = ticketRepositoryForTests.findAll().size();
        int numOfActiveTicketsBefore = ticketRepositoryForTests.findAllActive().size();
        ticketRepositoryForTests.expire(ticketNo1);
        int afterExpiringTicket = ticketRepositoryForTests.findAll().size();
        int numOfActiveTicketsAfter = ticketRepositoryForTests.findAllActive().size();
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(expiredTicketUUID);
        assertNotNull(foundTicket);
        assertEquals(beforeExpiringTicket, afterExpiringTicket);
        assertFalse(ticketNo1.isTicketStatusActive());
        assertEquals(beforeExpiringTicket, afterExpiringTicket);
        assertNotEquals(numOfActiveTicketsBefore, numOfActiveTicketsAfter);
        assertEquals(numOfActiveTicketsBefore - 1, numOfActiveTicketsAfter);
    }

    @Test
    public void expireCertainTicketTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, "reduced");
        assertNotNull(ticket);
        assertThrows(TicketRepositoryUpdateException.class, () -> {
            ticketRepositoryForTests.expire(ticket);
        });
    }

    @Test
    public void findCertainTicketTestPositive() {
        Ticket ticket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(ticket);
        assertEquals(ticket, ticketNo1);
    }

    @Test
    public void findCertainTicketTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), movieNo1, clientNo1, "reduced");
        assertNotNull(ticket);
        assertThrows(TicketRepositoryReadException.class, () -> {
            Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        });
    }

    @Test
    public void findAllTicketsTestPositive() {
        List<Ticket> listOfTickets = ticketRepositoryForTests.findAll();
        assertNotNull(listOfTickets);
        assertEquals(3, listOfTickets.size());
    }

    @Test
    public void findAllActiveTicketsTestPositive() {
        List<Ticket> startingListOfTickets = ticketRepositoryForTests.findAllActive();
        assertNotNull(startingListOfTickets);
        ticketRepositoryForTests.expire(startingListOfTickets.get(0));
        List<Ticket> endingListOfTickets = ticketRepositoryForTests.findAllActive();
        assertNotNull(endingListOfTickets);
        assertEquals(startingListOfTickets.size(), 3);
        assertEquals(endingListOfTickets.size(), 2);
    }

    // Other

    @Test
    public void mongoRepositoryFindTypeOfTicketDocTestPositive() {
        TypeOfTicketRow typeOfTicketRow = ticketRepositoryForTests.findTypeOfTicketDoc(ticketNo1.getTicketType().getTicketTypeID());
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getTypeOfTicketID(), ticketNo1.getTicketType().getTicketTypeID());
    }

    @Test
    public void mongoRepositoryFindTypeOfTicketDocTestNegative() {
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID());
        assertNotNull(typeOfTicket);
        assertThrows(TypeOfTicketDocNotFoundException.class, () -> {
            ticketRepositoryForTests.findTypeOfTicketDoc(typeOfTicket.getTicketTypeID());
        });
    }

    @Test
    public void mongoRepositoryFindTicketDocTestPositive() {
        TicketRow ticketRow = ticketRepositoryForTests.findTicketDoc(ticketNo1.getTicketID());
        assertNotNull(ticketRow);
        assertEquals(ticketNo1.getTicketID(), ticketRow.getTicketID());
        assertEquals(ticketNo1.getMovieTime(), ticketRow.getMovieTime());
        assertEquals(ticketNo1.getReservationTime(), ticketRow.getReservationTime());
        assertEquals(ticketNo1.getTicketFinalPrice(), ticketRow.getTicketFinalPrice());
        assertEquals(ticketNo1.isTicketStatusActive(), ticketRow.isTicketStatusActive());
        assertEquals(ticketNo1.getClient().getClientID(), ticketRow.getClientID());
        assertEquals(ticketNo1.getMovie().getMovieID(), ticketRow.getMovieID());
        assertEquals(ticketNo1.getTicketType().getTicketTypeID(), ticketRow.getTypeOfTicketID());
    }

    @Test
    public void mongoRepositoryFindTicketDocTestNegative() throws TicketReservationException {
        Ticket ticket = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        assertThrows(TicketDocNotFoundException.class, () -> {
            ticketRepositoryForTests.findTicketDoc(ticket.getTicketID());
        });
    }
}
