package manager_tests;

import model.Movie;
import model.ScreeningRoom;
import model.managers.MovieManager;
import model.managers.ScreeningRoomManager;
import model.repositories.MovieRepository;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieManagerTest {

    private static MovieRepository movieRepositoryForTests;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;
    private static MovieManager movieManagerForTests;
    private static ScreeningRoomManager screeningRoomManagerForTests;

    @BeforeAll
    public static void init() {
        movieRepositoryForTests = new MovieRepository();
        screeningRoomRepositoryForTests = new ScreeningRoomRepository();
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
        screeningRoomManagerForTests = new ScreeningRoomManager(screeningRoomRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {

    }

    @BeforeEach
    public void populateMovieRepositoryForTests() {
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

        ScreeningRoom screeningRoomNo1 = screeningRoomRepositoryForTests.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        ScreeningRoom screeningRoomNo2 = screeningRoomRepositoryForTests.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        ScreeningRoom screeningRoomNo3 = screeningRoomRepositoryForTests.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);

        movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1);
        movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2);
        movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3);
    }

    @AfterEach
    public void depopulateMovieRepositoryForTests() {
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
    public void createMovieManagerTest() {
        MovieRepository movieRepository = new MovieRepository();
        assertNotNull(movieRepository);
        MovieManager movieManager = new MovieManager(movieRepository);
        assertNotNull(movieManager);
    }

    @Test
    public void setMovieRepositoryForMovieManagerTest() {
        MovieRepository movieRepositoryNo1 = new MovieRepository();
        assertNotNull(movieRepositoryNo1);
        MovieRepository movieRepositoryNo2 = new MovieRepository();
        assertNotNull(movieRepositoryNo2);
        MovieManager movieManager = new MovieManager(movieRepositoryNo1);
        assertNotNull(movieManager);
        movieManager.setMovieRepository(movieRepositoryNo2);
        assertNotEquals(movieRepositoryNo1, movieManager.getMovieRepository());
        assertEquals(movieRepositoryNo2, movieManager.getMovieRepository());
    }

    @Test
    public void registerNewMovieTestPositive() {
        int numOfMoviesBefore = movieManagerForTests.getAll().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.register(0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = movieManagerForTests.register("American Psycho", 48.25, screeningRoom);
        assertNotNull(movie);
        int numOfMoviesAfter = movieManagerForTests.getAll().size();
        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void registerNewMovieTestNegative() {
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.register(0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = movieManagerForTests.register(null, 48.25, screeningRoom);
        assertNull(movie);
    }

    @Test
    public void unregisterCertainMovieTestPositive() {
        int numOfMoviesBefore = movieManagerForTests.getAllActive().size();
        Movie someMovieFromRepo = movieManagerForTests.getAllActive().get(0);
        assertNotNull(someMovieFromRepo);
        assertTrue(someMovieFromRepo.isMovieStatusActive());
        UUID removedMovieID = someMovieFromRepo.getMovieID();
        movieManagerForTests.unregister(someMovieFromRepo);
        int numOfMoviesAfter = movieManagerForTests.getAllActive().size();
        Movie foundMovie = movieManagerForTests.get(removedMovieID);
        assertNotNull(foundMovie);
        assertFalse(foundMovie.isMovieStatusActive());
        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void unregisterCertainMovieTestNegative() {
        int numOfMoviesBefore = movieManagerForTests.getAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 48.25,  screeningRoom);
        assertNotNull(movie);
        movieManagerForTests.unregister(movie);
        int numOfMoviesAfter = movieManagerForTests.getAll().size();
        assertEquals(numOfMoviesBefore, numOfMoviesAfter);
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
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 48.25, screeningRoom);
        assertNotNull(movie);
        Movie foundMovie = movieManagerForTests.get(movie.getMovieID());
        assertNull(foundMovie);
    }

    @Test
    public void getAllMoviesFromRepositoryTest() {
        List<Movie> listOfAllMoviesNo1 = movieManagerForTests.getMovieRepository().findAll();
        List<Movie> listOfAllMoviesNo2 = movieManagerForTests.getAll();
        assertNotNull(listOfAllMoviesNo1);
        assertNotNull(listOfAllMoviesNo2);
        assertEquals(listOfAllMoviesNo1.size(), listOfAllMoviesNo2.size());
    }
}
