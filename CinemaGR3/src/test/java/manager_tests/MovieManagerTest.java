package manager_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Movie;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.managers.MovieManager;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.MovieRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieManagerTest {

    private static MovieRepository movieRepositoryForTests;
    private static MovieManager movieManagerForTests;
    private static CqlSession cqlSession;


    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();
        movieRepositoryForTests = new MovieRepository(cqlSession);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        cqlSession.close();
    }

    @BeforeEach
    public void populateMovieRepositoryForTests() {
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        String movieNo2Title = "The Da Vinci Code";
        String movieNo3Title = "A Space Odyssey";

        double movieNo1BasePrice = 25.50;
        double movieNo2BasePrice = 35.75;
        double movieNo3BasePrice = 42.25;

        try {
            movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
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
    public void createMovieManagerTest() throws CassandraConfigNotFound {
        MovieRepository movieRepository = new MovieRepository(cqlSession);
        assertNotNull(movieRepository);
        MovieManager movieManager = new MovieManager(movieRepository);
        assertNotNull(movieManager);
    }

    @Test
    public void setMovieRepositoryForMovieManagerTest() throws CassandraConfigNotFound {
        MovieRepository movieRepositoryNo1 = new MovieRepository(cqlSession);
        assertNotNull(movieRepositoryNo1);
        MovieRepository movieRepositoryNo2 = new MovieRepository(cqlSession);
        assertNotNull(movieRepositoryNo2);
        MovieManager movieManager = new MovieManager(movieRepositoryNo1);
        assertNotNull(movieManager);
        movieManager.setMovieRepository(movieRepositoryNo2);
        assertNotEquals(movieRepositoryNo1, movieManager.getMovieRepository());
        assertEquals(movieRepositoryNo2, movieManager.getMovieRepository());
        movieRepositoryNo1.close();
        movieRepositoryNo2.close();
    }

    @Test
    public void registerNewMovieTestPositive() {
        int numOfMoviesBefore = movieManagerForTests.getAll().size();
        Movie movie = movieManagerForTests.register("American Psycho", 42.50, 5, 50);
        assertNotNull(movie);
        int numOfMoviesAfter = movieManagerForTests.getAll().size();
        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void registerNewMovieTestNegative() {
        Movie movie = movieManagerForTests.register(null, 42.50, 5, 50);
        assertNull(movie);
    }

    @Test
    public void getCertainMovieFromMovieRepositoryTestPositive() {
        Movie someMovieFromRepo = movieManagerForTests.getAll().get(0);
        assertNotNull(someMovieFromRepo);
        Movie foundMovie = movieManagerForTests.get(someMovieFromRepo.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(someMovieFromRepo, foundMovie);
    }

    @Test
    public void getCertainMovieFromMovieRepositoryTestNegative() {
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 42.50,  5, 50);
        assertNotNull(movie);
        Movie foundMovie = movieManagerForTests.get(movie.getMovieID());
        assertNull(foundMovie);
    }

    @Test
    public void getAllMoviesFromRepositoryTest() throws MovieRepositoryReadException {
        List<Movie> listOfAllMoviesNo1 = movieManagerForTests.getMovieRepository().findAll();
        List<Movie> listOfAllMoviesNo2 = movieManagerForTests.getAll();
        assertNotNull(listOfAllMoviesNo1);
        assertNotNull(listOfAllMoviesNo2);
        assertEquals(listOfAllMoviesNo1.size(), listOfAllMoviesNo2.size());
    }
}
