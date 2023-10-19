package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static MovieRepository movieRepositoryForTests;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;

    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        movieRepositoryForTests = new MovieRepository(entityManager);
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void insertExampleMovies() {
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
        ScreeningRoom screeningRoom = screeningRoomRepositoryForTests.create(0, 5, 50);
        assertNotNull(screeningRoom);
        final Movie[] movie = new Movie[1];
        assertDoesNotThrow(() -> {
            movie[0] = movieRepositoryForTests.create("American Psycho", 35.75, screeningRoom);
        });
        assertNotNull(movie[0]);
        Movie foundMovie = movieRepositoryForTests.findByUUID(movie[0].getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movie[0]);
    }

    @Test
    public void createNewMovieWithNullTitleTestNegative() {
        ScreeningRoom screeningRoom = screeningRoomRepositoryForTests.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(null, 35.75, screeningRoom));
    }

    @Test
    public void createNewMovieWithEmptyTitleTestNegative() {
        ScreeningRoom screeningRoom = screeningRoomRepositoryForTests.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create("", 35.75, screeningRoom));
    }

    @Test
    public void createNewMovieWithTooLongTitleTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        ScreeningRoom screeningRoom = screeningRoomRepositoryForTests.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, 35.75,  screeningRoom));
    }

    @Test
    public void createNewMovieWithNullScreeningRoom() {
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create("Pulp fiction", 35.75, null));
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
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
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
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
        assertNotNull(movie);
        assertThrows(MovieRepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie));
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
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
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
