package repository_tests;

import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.GeneralRepositoryException;
import model.exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.exceptions.update_exceptions.MovieRepositoryUpdateException;
import model.exceptions.update_exceptions.RepositoryUpdateException;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;
import model.ticket_types.Reduced;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

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
        clientRepositoryForTests = new ClientRepository();
        movieRepositoryForTests = new MovieRepository();
        ticketRepositoryForTests = new TicketRepository();
    }

    @AfterAll
    public static void destroy() {
        ticketRepositoryForTests.close();
        clientRepositoryForTests.close();
        movieRepositoryForTests.close();
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
    public void ticketRepositoryConstructorTest() throws CassandraConfigNotFound {
        TicketRepository ticketRepository = new TicketRepository();
        assertNotNull(ticketRepository);
    }

    @Test
    public void createNewTicketTestPositive() throws TicketRepositoryReadException, TicketRepositoryCreateException {
        int numberOfAvailableSeatsBefore = movieNo2.getNumberOfAvailableSeats();
        Ticket ticket = ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        int numberOfAvailableSeatsAfter = movieNo2.getNumberOfAvailableSeats();
        assertNotNull(ticket);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticket);
        assertNotEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter);
        assertEquals(numberOfAvailableSeatsBefore - 1, numberOfAvailableSeatsAfter);
    }

    @Test
    public void checkIfScreeningRoomNumOfSeatsIsUpdatedCorrectly() throws GeneralRepositoryException {
        int numberOfAvailableSeatsBefore = movieRepositoryForTests.findByUUID(movieNo2.getMovieID()).getNumberOfAvailableSeats();
        Ticket ticket = ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        int numberOfAvailableSeatsAfter = movieRepositoryForTests.findByUUID(movieNo2.getMovieID()).getNumberOfAvailableSeats();
        assertNotNull(ticket);
        Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        assertNotNull(foundTicket);
        assertEquals(foundTicket, ticket);
        assertNotEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfter);
        assertEquals(numberOfAvailableSeatsBefore - 1, numberOfAvailableSeatsAfter);
        ticketRepositoryForTests.delete(foundTicket.getTicketID());
        int numberOfAvailableSeatsAfterDeletion = movieRepositoryForTests.findByUUID(movieNo2.getMovieID()).getNumberOfAvailableSeats();
        assertNotEquals(numberOfAvailableSeatsAfter, numberOfAvailableSeatsAfterDeletion);
        assertEquals(numberOfAvailableSeatsBefore, numberOfAvailableSeatsAfterDeletion);
    }

    @Test
    public void createNewTicketWithNullMovieTimeTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(null, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID()));
    }

    @Test
    public void createNewTicketWithNullReservationTimeTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTimeNo2, null, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID()));
    }

    @Test
    public void createNewTicketWithNullClientTestNegative() {
        assertThrows(TicketRepositoryCreateException.class, () -> ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2,  movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), null));
    }

    @Test
    public void createNewTicketWithAScreeningRoomWithoutAnyAvailableSeat() throws MovieRepositoryReadException, MovieRepositoryUpdateException {
        Movie movie = movieRepositoryForTests.findAll().get(0);
        movie.setNumberOfAvailableSeats(0);
        movieRepositoryForTests.update(movie);

        assertThrows(TicketRepositoryCreateException.class, () -> {
            Ticket ticket = ticketRepositoryForTests.createReducedTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        });
    }

    @Test
    public void addTwoTicketsWhenThereIsOnlyOneSeat() throws TicketRepositoryCreateException {
        movieNo2.setNumberOfAvailableSeats(1);
        int numberOfSeatsBefore = movieNo2.getNumberOfAvailableSeats();
        Ticket ticketN1 = ticketRepositoryForTests.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        int numberOfSeatsAfterTicketOne = movieNo2.getNumberOfAvailableSeats();
        assertNotNull(ticketN1);
        assertEquals(1, numberOfSeatsBefore);
        assertEquals(0, numberOfSeatsAfterTicketOne);
        assertThrows(TicketRepositoryCreateException.class, () -> {
            ticketRepositoryForTests.createReducedTicket(ticketNo2.getMovieTime(), ticketNo2.getReservationTime(), ticketNo2.getTicketBasePrice(), movieNo2.getMovieID(), clientNo2.getClientID());
        });
    }

    @Test
    public void updateCertainTicketTestNegative() {
        Ticket ticket = new Reduced(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), ticketNo1.getTicketBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        assertThrows(RepositoryUpdateException.class, () -> ticketRepositoryForTests.update(ticket));
    }

    @Test
    public void deleteCertainTicketTestPositive() throws TicketRepositoryReadException {
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
    public void deleteCertainTicketThatIsNotInTheDatabaseTestNegative() {
        Ticket ticket = new Reduced(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), ticketNo1.getTicketBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket));
    }

    @Test
    public void deleteCertainTicketWithUUIDThatIsNotInTheDatabaseTestNegative() {
        Ticket ticket = new Reduced(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), ticketNo1.getTicketBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        assertThrows(TicketRepositoryDeleteException.class, () -> ticketRepositoryForTests.delete(ticket.getTicketID()));
    }

    @Test
    public void findCertainTicketTestPositive() throws TicketRepositoryReadException {
        Ticket ticket = ticketRepositoryForTests.findByUUID(ticketNo1.getTicketID());
        assertNotNull(ticket);
        assertEquals(ticket, ticketNo1);
    }

    @Test
    public void findCertainTicketTestNegative() {
        Ticket ticket = new Reduced(UUID.randomUUID(), ticketNo1.getMovieTime(), ticketNo1.getReservationTime(), ticketNo1.getTicketBasePrice(), movieNo1.getMovieID(), clientNo1.getClientID());
        assertNotNull(ticket);
        assertThrows(TicketRepositoryReadException.class, () -> {
            Ticket foundTicket = ticketRepositoryForTests.findByUUID(ticket.getTicketID());
        });
    }

    @Test
    public void findAllTicketsTestPositive() throws TicketRepositoryReadException {
        List<Ticket> listOfTickets = ticketRepositoryForTests.findAll();
        assertNotNull(listOfTickets);
        assertEquals(3, listOfTickets.size());
    }
}
