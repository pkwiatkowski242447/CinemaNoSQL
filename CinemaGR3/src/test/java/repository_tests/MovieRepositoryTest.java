package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;
import model.repositories.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<Movie> movieRepositoryForTests;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTests;

    private final ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
    private final ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
    private final ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

    private final Movie movieNo1 = new Movie(UUID.randomUUID(), "Harry Potter and The Goblet of Fire", screeningRoomNo1);
    private final Movie movieNo2 = new Movie(UUID.randomUUID(), "The Da Vinci Code", screeningRoomNo2);
    private final Movie movieNo3 = new Movie(UUID.randomUUID(), "A Space Odyssey", screeningRoomNo3);

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        movieRepositoryForTests = new MovieRepository(entityManager);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        // entityManager.getTransaction().commit();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void insertExampleMovies() {
        screeningRoomRepositoryForTests.create(screeningRoomNo1);
        screeningRoomRepositoryForTests.create(screeningRoomNo2);
        screeningRoomRepositoryForTests.create(screeningRoomNo3);

        movieRepositoryForTests.create(movieNo1);
        movieRepositoryForTests.create(movieNo2);
        movieRepositoryForTests.create(movieNo3);
    }

    @AfterEach
    public void deleteExampleMovies() {
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
    public void movieRepositoryConstructorTest() {
        Repository<Movie> movieRepository = new MovieRepository(entityManager);
        assertNotNull(movieRepository);
    }

    @Test
    public void createNewMovieTestPositive() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        screeningRoomRepositoryForTests.create(screeningRoom);
        assertDoesNotThrow(() -> movieRepositoryForTests.create(movie));
        Movie foundMovie = movieRepositoryForTests.findByUUID(movie.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movie);
    }

    @Test
    public void createNewMovieWithNullIdTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(null, "American Psycho", screeningRoom);
        assertNotNull(movie);
        screeningRoomRepositoryForTests.create(screeningRoom);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void createNewMovieWithNullTitleTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), null, screeningRoom);
        assertNotNull(movie);
        screeningRoomRepositoryForTests.create(screeningRoom);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void createNewMovieWithEmptyTitleTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "", screeningRoom);
        assertNotNull(movie);
        screeningRoomRepositoryForTests.create(screeningRoom);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void createNewMovieWithTooLongTitleTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), movieTitle, screeningRoom);
        assertNotNull(movie);
        screeningRoomRepositoryForTests.create(screeningRoom);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void createNewMovieWithNullScreeningRoom() {
        Movie movie = new Movie(UUID.randomUUID(), "Pulp fiction", null);
        assertNotNull(movie);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void createNewMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(movieNo1.getMovieID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        assertThrows(RepositoryCreateException.class, () -> movieRepositoryForTests.create(movie));
    }

    @Test
    public void updateCertainMovieTestPositive() {
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
    public void updateCertainMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(movie));
    }

    @Test
    public void updateCertainMovieWithNullTitleTestNegative() {
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(null);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void updateCertainMovieWithEmptyTitleTestNegative() {
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle("");
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void updateCertainMovieWithTooLongTitleTestNegative() {
        String newTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(newTitle);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void deleteCertainMovieTestPositive() {
        UUID removedMovieID = movieNo1.getMovieID();
        int numberOfMoviesBeforeDelete = movieRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> movieRepositoryForTests.delete(movieNo1));
        int numberOfMoviesAfterDelete = movieRepositoryForTests.findAll().size();
        assertNotEquals(numberOfMoviesBeforeDelete, numberOfMoviesAfterDelete);
        assertEquals(numberOfMoviesBeforeDelete - 1, numberOfMoviesAfterDelete);
        Movie foundMovie = movieRepositoryForTests.findByUUID(removedMovieID);
        assertNull(foundMovie);
    }

    @Test
    public void deleteCertainMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        assertThrows(RepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie));
    }

    @Test
    public void findCertainMovieTestPositive() {
        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void findCertainMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", screeningRoom);
        assertNotNull(movie);
        Movie foundMovie = movieRepositoryForTests.findByUUID(movie.getMovieID());
        assertNull(foundMovie);
    }

    @Test
    public void findAllMoviesTestPositive() {
        List<Movie> listOfAllMovies = movieRepositoryForTests.findAll();
        assertNotNull(listOfAllMovies);
        assertEquals(3, listOfAllMovies.size());
    }
}
