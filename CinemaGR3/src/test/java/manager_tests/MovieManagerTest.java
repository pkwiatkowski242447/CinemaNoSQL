package manager_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Movie;
import model.ScreeningRoom;
import model.managers.Manager;
import model.managers.MovieManager;
import model.managers.ScreeningRoomManager;
import model.repositories.MovieRepository;
import model.repositories.Repository;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieManagerTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<Movie> movieRepositoryForTests;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTests;
    private static MovieManager movieManagerForTests;
    private static ScreeningRoomManager screeningRoomManagerForTests;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        movieRepositoryForTests = new MovieRepository(entityManager);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
        movieManagerForTests = new MovieManager(movieRepositoryForTests);
        screeningRoomManagerForTests = new ScreeningRoomManager(screeningRoomRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void populateMovieRepositoryForTests() {
        ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
        ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
        ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

        Movie movieNo1 = new Movie(UUID.randomUUID(), "Harry Potter and The Globlet of Fire", screeningRoomNo1);
        Movie movieNo2 = new Movie(UUID.randomUUID(), "The Da Vinci Code", screeningRoomNo2);
        Movie movieNo3 = new Movie(UUID.randomUUID(), "A Space Odyssey", screeningRoomNo3);

        screeningRoomRepositoryForTests.create(screeningRoomNo1);
        screeningRoomRepositoryForTests.create(screeningRoomNo2);
        screeningRoomRepositoryForTests.create(screeningRoomNo3);

        movieRepositoryForTests.create(movieNo1);
        movieRepositoryForTests.create(movieNo2);
        movieRepositoryForTests.create(movieNo3);
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
        Repository<Movie> movieRepository = new MovieRepository(entityManager);
        assertNotNull(movieRepository);
        Manager<Movie> movieManager = new MovieManager(movieRepository);
        assertNotNull(movieManager);
    }

    @Test
    public void setMovieRepositoryForMovieManagerTest() {
        Repository<Movie> movieRepositoryNo1 = new MovieRepository(entityManager);
        assertNotNull(movieRepositoryNo1);
        Repository<Movie> movieRepositoryNo2 = new MovieRepository(entityManager);
        assertNotNull(movieRepositoryNo2);
        Manager<Movie> movieManager = new MovieManager(movieRepositoryNo1);
        assertNotNull(movieManager);
        movieManager.setObjectRepository(movieRepositoryNo2);
        assertNotEquals(movieRepositoryNo1, movieManager.getObjectRepository());
        assertEquals(movieRepositoryNo2, movieManager.getObjectRepository());
    }

    @Test
    public void registerNewMovieTestPositive() {
        int numOfMoviesBefore = movieManagerForTests.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        screeningRoomManagerForTests.getObjectRepository().create(screeningRoom);
        Movie movie = movieManagerForTests.register("American Psycho", screeningRoom);
        assertNotNull(movie);
        int numOfMoviesAfter = movieManagerForTests.getObjectRepository().findAll().size();
        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void registerNewMovieTestNegative() {
        int numOfMoviesBefore = movieManagerForTests.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        screeningRoomManagerForTests.getObjectRepository().create(screeningRoom);
        Movie movie = movieManagerForTests.register(null, screeningRoom);
        assertNotNull(movie);
        int numOfMoviesAfter = movieManagerForTests.getObjectRepository().findAll().size();
        assertEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void unregisterCertainMovieTestPositive() {
        int numOfMoviesBefore = movieManagerForTests.getObjectRepository().findAll().size();
        Movie someMovieFromRepo = movieManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someMovieFromRepo);
        UUID removedMovieID = someMovieFromRepo.getMovieID();
        movieManagerForTests.unregister(someMovieFromRepo);
        int numOfMoviesAfter = movieManagerForTests.getObjectRepository().findAll().size();
        Movie foundMovie = movieManagerForTests.get(removedMovieID);
        assertNull(foundMovie);
        assertNotEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void unregisterCertainMovieTestNegative() {
        int numOfMoviesBefore = movieManagerForTests.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        movieManagerForTests.unregister(movie);
        int numOfMoviesAfter = movieManagerForTests.getObjectRepository().findAll().size();
        assertEquals(numOfMoviesBefore, numOfMoviesAfter);
    }

    @Test
    public void getCertainMovieFromMovieRepositoryTestPositive() {
        Movie someMovieFromRepo = movieManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someMovieFromRepo);
        Movie foundMovie = movieManagerForTests.get(someMovieFromRepo.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(someMovieFromRepo, foundMovie);
    }

    @Test
    public void getCertainMovieFromMovieRepositoryTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        Movie foundMovie = movieManagerForTests.get(movie.getMovieID());
        assertNull(foundMovie);
    }

    @Test
    public void getAllMoviesFromRepositoryTest() {
        List<Movie> listOfAllMovies = movieManagerForTests.getObjectRepository().findAll();
        assertNotNull(listOfAllMovies);
        assertEquals(3, listOfAllMovies.size());
    }
}
