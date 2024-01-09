package manager_tests;

import model.constants.GeneralConstants;
import model.exceptions.managers.create_exceptions.CreateManagerException;
import model.exceptions.managers.create_exceptions.MovieManagerCreateException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.delete_exceptions.MovieManagerDeleteException;
import model.exceptions.managers.read_exceptions.MovieManagerReadException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.update_exceptions.MovieManagerUpdateException;
import model.exceptions.managers.update_exceptions.UpdateManagerException;
import model.model.Movie;
import model.exceptions.MongoConfigNotFoundException;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.managers.implementations.MovieManager;
import model.repositories.implementations.MovieRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieManagerTest {

    private static MovieRepository movieRepositoryForTests;
    private static MovieManager movieManagerForTests;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @BeforeAll
    public static void init() throws MongoConfigNotFoundException {
        movieRepositoryForTests = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        movieRepositoryForTests.close();
    }

    @BeforeEach
    public void populateMovieRepositoryForTests() {
        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 25.50;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo1Number = 10;

        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 35.75;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo2Number = 5;

        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 42.25;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        int screeningRoomNo3Number = 19;

        try {
            movieNo1 = movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            movieNo2 = movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            movieNo3 = movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
        } catch (MovieRepositoryCreateException exception) {
            throw new RuntimeException("Sample movies could not be created in the repository.", exception);
        }
    }

    @AfterEach
    public void depopulateMovieRepositoryForTests() {
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

    @Test
    public void movieManagerCreateMovieManagerTestPositive() throws MongoConfigNotFoundException {
        MovieRepository movieRepository = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        assertNotNull(movieRepository);
        MovieManager movieManager = new MovieManager(movieRepository);
        assertNotNull(movieManager);
    }

    @Test
    public void movieManagerSetMovieRepositoryForMovieManagerTestPositive() throws MongoConfigNotFoundException {
        MovieRepository movieRepositoryNo1 = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        assertNotNull(movieRepositoryNo1);

        MovieRepository movieRepositoryNo2 = new MovieRepository(GeneralConstants.TEST_DB_NAME);
        assertNotNull(movieRepositoryNo2);

        MovieManager movieManager = new MovieManager(movieRepositoryNo1);
        assertNotNull(movieManager);

        movieManager.setMovieRepository(movieRepositoryNo2);

        assertNotEquals(movieRepositoryNo1, movieManager.getMovieRepository());
        assertEquals(movieRepositoryNo2, movieManager.getMovieRepository());
    }

    @Test
    public void registerNewMovieTestPositive() throws ReadManagerException, CreateManagerException {
        int numOfMoviesBefore = movieManagerForTests.findAll().size();

        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie movie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        int numOfMoviesAfter = movieManagerForTests.findAll().size();

        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void movieManagerCreateMovieWithNullMovieTitleTestNegative() {
        String movieTitle = null;
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithEmptyMovieTitleTestNegative() {
        String movieTitle = "";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithMovieTitleTooShortTestNegative() {
        String movieTitle = "";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithMovieTitleTooLongTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithMovieTitleLengthEqualTo1TestPositive() throws CreateManagerException {
        String movieTitle = "d";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithMovieTitleLengthEqualTo150TestPositive() throws CreateManagerException {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithNegativeMovieBasePriceTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = -0.01;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithMovieBasePriceTooHighTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 100.01;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithMovieBasePriceEqualTo0TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 0;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithMovieBasePriceEqualTo100TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 100;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithNegativeNumberOfAvailableSeatsTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = -1;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithNumberOfAvailableSeatsTooHighTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 151;
        int screeningRoomNumber = 5;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithNumberOfAvailableSeatsEqualTo0TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 0;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithNumberOfAvailableSeatsEqualTo150TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 150;
        int screeningRoomNumber = 5;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithScreeningRoomNumberTooLowTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 0;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithScreeningRoomNumberTooHighTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 21;

        assertThrows(MovieManagerCreateException.class, () -> movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber));
    }

    @Test
    public void movieManagerCreateMovieWithScreeningRoomNumberEqualTo1TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 1;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerCreateMovieWithScreeningRoomNumberEqualTo20TestPositive() throws CreateManagerException {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 20;

        Movie testMovie = movieManagerForTests.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(testMovie);

        assertEquals(movieTitle, testMovie.getMovieTitle());
        assertEquals(movieBasePrice, testMovie.getMovieBasePrice());
        assertEquals(numberOfAvailableSeats, testMovie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieManagerUpdateMovieThatIsNotInTheRepositoryTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 20;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movie));
    }

    @Test
    public void movieManagerUpdateMovieWithNullMovieTitleTestNegative() {
        String movieTitle = null;
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithEmptyMovieTitleTestNegative() {
        String movieTitle = "";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithMovieTitleTooShortTestNegative() {
        String movieTitle = "";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithMovieTitleTooLongTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        movieNo1.setMovieTitle(movieTitle);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithMovieTitleLengthEqualTo1TestPositive() throws ReadManagerException, UpdateManagerException {
        String movieTitle = "d";
        movieNo1.setMovieTitle(movieTitle);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithMovieTitleLengthEqualTo150TestPositive() throws ReadManagerException, UpdateManagerException {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        movieNo1.setMovieTitle(movieTitle);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithNegativeMovieBasePriceTestNegative() {
        double movieBasePrice = -0.01;
        movieNo1.setMovieBasePrice(movieBasePrice);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithMovieBasePriceTooHighTestNegative() {
        double movieBasePrice = 100.01;
        movieNo1.setMovieBasePrice(movieBasePrice);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithMovieBasePriceEqualTo0TestPositive() throws UpdateManagerException, ReadManagerException {
        double movieBasePrice = 0;
        movieNo1.setMovieBasePrice(movieBasePrice);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithMovieBasePriceEqualTo100TestPositive() throws UpdateManagerException, ReadManagerException {
        double movieBasePrice = 100;
        movieNo1.setMovieBasePrice(movieBasePrice);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithNegativeNumberOfAvailableSeatsTestNegative() {
        int numberOfAvailableSeats = -1;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithNumberOfAvailableSeatsTooHighTestNegative() {
        int numberOfAvailableSeats = 151;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithNumberOfAvailableSeatsEqualTo0TestPositive() throws UpdateManagerException, ReadManagerException {
        int numberOfAvailableSeats = 0;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithNumberOfAvailableSeatsEqualTo150TestPositive() throws UpdateManagerException, ReadManagerException {
        int numberOfAvailableSeats = 0;
        movieNo1.setNumberOfAvailableSeats(numberOfAvailableSeats);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithScreeningRoomNumberTooLowTestNegative() {
        int screeningRoomNumber = 0;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithScreeningRoomNumberTooHighTestNegative() {
        int screeningRoomNumber = 21;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);
        assertThrows(MovieManagerUpdateException.class, () -> movieManagerForTests.update(movieNo1));
    }

    @Test
    public void movieManagerUpdateMovieWithScreeningRoomNumberEqualTo1TestPositive() throws UpdateManagerException, ReadManagerException {
        int screeningRoomNumber = 1;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerUpdateMovieWithScreeningRoomNumberEqualTo20TestPositive() throws UpdateManagerException, ReadManagerException {
        int screeningRoomNumber = 20;
        movieNo1.setScreeningRoomNumber(screeningRoomNumber);

        movieManagerForTests.update(movieNo1);

        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);

        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerDeleteMovieTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfMoviesBefore = movieManagerForTests.findAll().size();

        UUID removedMovieUUID = movieNo1.getMovieID();

        movieManagerForTests.delete(movieNo1);

        int numberOfMoviesAfter = movieManagerForTests.findAll().size();

        assertNotEquals(numberOfMoviesBefore, numberOfMoviesAfter);

        assertThrows(MovieManagerReadException.class, () -> movieManagerForTests.findByUUID(removedMovieUUID));
    }

    @Test
    public void movieManagerDeleteMovieThatIsNotInTheRepositoryTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 20;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieManagerDeleteException.class, () -> movieManagerForTests.delete(movie));
    }

    @Test
    public void movieManagerDeleteMovieByIdTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfMoviesBefore = movieManagerForTests.findAll().size();

        UUID removedMovieUUID = movieNo1.getMovieID();

        movieManagerForTests.delete(movieNo1.getMovieID());

        int numberOfMoviesAfter = movieManagerForTests.findAll().size();

        assertNotEquals(numberOfMoviesBefore, numberOfMoviesAfter);

        assertThrows(MovieManagerReadException.class, () -> movieManagerForTests.findByUUID(removedMovieUUID));
    }

    @Test
    public void movieManagerDeleteMovieByIdThatIsNotInTheRepositoryTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 20;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieManagerDeleteException.class, () -> movieManagerForTests.delete(movie.getMovieID()));
    }

    @Test
    public void movieManagerFindMovieTestPositive() throws ReadManagerException {
        Movie foundMovie = movieManagerForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(movieNo1, foundMovie);
    }

    @Test
    public void movieManagerFindMovieThatIsNotInTheRepositoryTestNegative() {
        String movieTitle = "American Psycho";
        double movieBasePrice = 42.50;
        int numberOfAvailableSeats = 50;
        int screeningRoomNumber = 5;

        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        assertNotNull(movie);

        assertThrows(MovieManagerReadException.class, () -> movieManagerForTests.findByUUID(movie.getMovieID()));
    }

    @Test
    public void movieManagerFindAllMoviesTestPositive() throws ReadManagerException, DeleteManagerException {
        List<Movie> startingListOfMovies = movieManagerForTests.findAll();
        assertNotNull(startingListOfMovies);

        movieManagerForTests.delete(startingListOfMovies.get(0));

        List<Movie> finalListOfMovies = movieManagerForTests.findAll();
        assertNotNull(finalListOfMovies);

        assertEquals(startingListOfMovies.size(), 3);
        assertEquals(finalListOfMovies.size(), 2);
    }
}
