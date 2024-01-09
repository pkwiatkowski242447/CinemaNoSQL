package repository_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.model.Movie;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.exceptions.repositories.update_exceptions.MovieRepositoryUpdateException;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.MovieRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRepositoryTest {

    private static CqlSession cqlSession;

    private static MovieRepository movieRepositoryForTests;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();
        movieRepositoryForTests = new MovieRepository(cqlSession);
    }

    @BeforeEach
    public void insertExampleMovies() {

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 35.00;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;

        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 37.50;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;

        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 42.25;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        try {
            movieNo1 = movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            movieNo2 = movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            movieNo3 = movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
        } catch (MovieRepositoryCreateException exception) {
            throw new RuntimeException("Sample movies could not be created in the repository." ,exception);
        }
    }

    @AfterEach
    public void deleteExampleMovies() {
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
    }

    @AfterAll
    public static void destroy() {
        cqlSession.close();
    }

    @Test
    public void movieRepositoryConstructorTest() {
        MovieRepository movieRepository = new MovieRepository(cqlSession);
        assertNotNull(movieRepository);
    }

    @Test
    public void movieRepositoryCreateMovieTestPositive() throws MovieRepositoryReadException, MovieRepositoryCreateException {
        Movie movie = movieRepositoryForTests.create("American Psycho", 25.60, 90, 1);
        assertNotNull(movie);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movie.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(foundMovie, movie);
    }

    @Test
    public void movieRepositoryCreateMovieWithNullTitleTestNegative() {
        String movieTitle = null;
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithEmptyTitleTestNegative() {
        String movieTitle = "";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithTitleTooShortTestNegative() {
        String movieTitle = "";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithTitleTooLongTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieTitleLengthEqualTo1TestPositive() throws MovieRepositoryCreateException {
        String movieTitle = "d";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieTitleLengthEqualTo150TestPositive() throws MovieRepositoryCreateException {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieBasePriceNegativeTestNegative() {
        String movieTitle = "American Psycho";;
        double movieBasePrice = -1.50;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieBasePriceNegativeTooHighNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 100.01;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieBasePriceEqualTo0TestPositive() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 0;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithMovieBasePriceEqualTo100TestPositive() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 100;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 90;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithScreeningRoomNumberLowerThan1TestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 0;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithScreeningRoomNumberHigherThan20TestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 21;
        int numberOfAvailableSeats = 90;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithScreeningRoomNumberEqualTo1TestNegative() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 1;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithScreeningRoomNumberEqualTo20TestNegative() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 20;
        int numberOfAvailableSeats = 90;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithNegativeNumberOfAvailableSeatsTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = -1;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithNumberOfAvailableSeatsTooHighTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 151;
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieRepositoryCreateMovieWithNumberOfAvailableSeatsEqualTo0TestNegative() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 0;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryCreateMovieWithNumberOfAvailableSeatsEqualTo150TestPositive() throws MovieRepositoryCreateException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 150;

        Movie testMovie = movieRepositoryForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieRepositoryUpdateMovieTestPositive() throws MovieRepositoryReadException {
        String oldTitle = movieNo1.getMovieTitle();

        movieNo1.setMovieTitle("Cars");
        String newTitle = movieNo1.getMovieTitle();

        assertDoesNotThrow(() -> movieRepositoryForTests.update(movieNo1));

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(foundMovie, movieNo1);
        assertNotEquals(oldTitle, newTitle);
        assertEquals(newTitle, foundMovie.getMovieTitle());
    }

    @Test
    public void movieRepositoryUpdateMovieThatIsNotInTheDatabaseTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int screeningRoomNumber = 1;
        int numberOfAvailableSeats = 150;

        Movie testMovie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(testMovie));
    }

    @Test
    public void movieRepositoryUpdateMovieWithNullMovieTitleTestNegative() {
        String movieTitle = null;
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithEmptyMovieTitleTestNegative() {
        String movieTitle = "";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithMovieTitleTooShortTestNegative() {
        String movieTitle = "";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithMovieTitleTooLongTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithMovieTitleLengthEqualTo1TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        String movieTitle = "d";
        movieNo1.setMovieTitle(movieTitle);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithMovieTitleLengthEqualTo150TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        movieNo1.setMovieTitle(movieTitle);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithNegativeBasePriceTestNegative() {
        double movieBasePrice = -0.01;
        movieNo1.setMovieBasePrice(movieBasePrice);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithBasePriceTooHighTestNegative() {
        double movieBasePrice = 100.01;
        movieNo1.setMovieBasePrice(movieBasePrice);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithBasePriceEqualTo0TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        double movieBasePrice = 0;
        movieNo1.setMovieBasePrice(movieBasePrice);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithBasePriceEqualTo100TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        double movieBasePrice = 100;
        movieNo1.setMovieBasePrice(movieBasePrice);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithNegativeNumberOfAvailableSeatsTestNegative() {
        int numberOfAvailableSeats = -1;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithNumberOfAvailableSeatsTooHighTestNegative() {
        int numberOfAvailableSeats = 151;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWithNumberOfAvailableSeatsEqualTo0TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        int numberOfAvailableSeats = 0;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithNumberOfAvailableSeatsEqualTo150TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        int numberOfAvailableSeats = 150;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWithNonPositiveScreeningRoomNumberTestNegative() {
        int screeningRoomNumber = 0;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWitheScreeningRoomNumberTooHighTestNegative() {
        int screeningRoomNumber = 21;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);
        assertThrows(MovieRepositoryUpdateException.class, () -> movieRepositoryForTests.update(movieNo1));
    }

    @Test
    public void movieRepositoryUpdateMovieWitheScreeningRoomNumberEqualTo1TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        int screeningRoomNumber = 1;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryUpdateMovieWitheScreeningRoomNumberEqualTo20TestPositive() throws MovieRepositoryUpdateException, MovieRepositoryReadException {
        int screeningRoomNumber = 20;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);

        movieRepositoryForTests.update(movieNo1);

        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryDeleteMovieTestPositive() throws MovieRepositoryReadException {
        UUID removedMovieID = movieNo1.getMovieID();

        int numberOfMoviesBeforeDelete = movieRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> movieRepositoryForTests.delete(movieNo1));

        int numberOfMoviesAfterDelete = movieRepositoryForTests.findAll().size();

        assertNotEquals(numberOfMoviesBeforeDelete, numberOfMoviesAfterDelete);
        assertEquals(numberOfMoviesBeforeDelete - 1, numberOfMoviesAfterDelete);

        assertThrows(MovieRepositoryReadException.class, () -> movieRepositoryForTests.findByUUID(removedMovieID));
    }

    @Test
    public void movieRepositoryDeleteMovieThatIsNotInTheDatabaseTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieRepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie));
    }

    @Test
    public void movieRepositoryDeleteMovieByIdTestPositive() throws MovieRepositoryReadException {
        UUID removedMovieID = movieNo1.getMovieID();

        int numberOfMoviesBeforeDelete = movieRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> movieRepositoryForTests.delete(movieNo1.getMovieID()));

        int numberOfMoviesAfterDelete = movieRepositoryForTests.findAll().size();

        assertNotEquals(numberOfMoviesBeforeDelete, numberOfMoviesAfterDelete);
        assertEquals(numberOfMoviesBeforeDelete - 1, numberOfMoviesAfterDelete);

        assertThrows(MovieRepositoryReadException.class, () -> movieRepositoryForTests.findByUUID(removedMovieID));
    }

    @Test
    public void movieRepositoryDeleteMovieByIdThatIsNotInTheDatabaseTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieRepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie.getMovieID()));
    }

    @Test
    public void movieRepositoryFindCertainMovieTestPositive() throws MovieRepositoryReadException {
        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void movieRepositoryFindMovieThatIsNotInTheDatabaseTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 25.60;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieRepositoryReadException.class, () -> movieRepositoryForTests.findByUUID(movie.getMovieID()));
    }

    @Test
    public void movieRepositoryFindAllMoviesTestPositive() throws MovieRepositoryReadException, MovieRepositoryDeleteException {
        List<Movie> startingListOfAllMovies = movieRepositoryForTests.findAll();
        assertNotNull(startingListOfAllMovies);

        movieRepositoryForTests.delete(startingListOfAllMovies.get(0));

        List<Movie> finalListOfAllMovies = movieRepositoryForTests.findAll();
        assertNotNull(finalListOfAllMovies);

        assertEquals(startingListOfAllMovies.size(), 3);
        assertEquals(finalListOfAllMovies.size(), 2);
    }
}
