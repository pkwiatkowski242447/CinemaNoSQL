package repository_tests;

import model.Movie;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.exceptions.update_exceptions.RepositoryUpdateException;
import model.repositories.implementations.MovieRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRepositoryTest {

    private static MovieRepository movieRepositoryForTests;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        movieRepositoryForTests = new MovieRepository();
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
        movieRepositoryForTests.close();
    }

    @Test
    public void movieRepositoryConstructorTest() throws CassandraConfigNotFound {
        MovieRepository movieRepository = new MovieRepository();
        assertNotNull(movieRepository);
    }

    @Test
    public void createNewMovieTestPositive() throws MovieRepositoryReadException, MovieRepositoryCreateException {
        Movie movie = movieRepositoryForTests.create("American Psycho", 25.60, 1, 90);
        assertNotNull(movie);
        Movie foundMovie = movieRepositoryForTests.findByUUID(movie.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movie);
    }

    @Test
    public void createNewMovieWithNullTitleTestNegative() {
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(null, 25.60, 1, 90));
    }

    @Test
    public void createNewMovieWithEmptyTitleTestNegative() {
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create("",  25.60, 1, 90));
    }

    @Test
    public void createNewMovieWithTooLongTitleTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        assertThrows(MovieRepositoryCreateException.class, () -> movieRepositoryForTests.create(movieTitle, 25.60, 1, 90));
    }

    @Test
    public void updateCertainMovieTestPositive() throws MovieRepositoryReadException {
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
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 25.60, 5, 50);
        assertNotNull(movie);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(movie));
    }

    @Test
    public void updateCertainMovieWithNullTitleTestNegative() throws MovieRepositoryReadException {
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(null);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void updateCertainMovieWithEmptyTitleTestNegative() throws MovieRepositoryReadException {
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle("");
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void updateCertainMovieWithTooLongTitleTestNegative() throws MovieRepositoryReadException {
        String newTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        Movie foundMovie = movieRepositoryForTests.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(newTitle);
        assertThrows(RepositoryUpdateException.class, () -> movieRepositoryForTests.update(foundMovie));
    }

    @Test
    public void deleteCertainMovieTestPositive() throws MovieRepositoryReadException {
        UUID removedMovieID = movieNo1.getMovieID();
        int numberOfMoviesBeforeDelete = movieRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> movieRepositoryForTests.delete(movieNo1));
        int numberOfMoviesAfterDelete = movieRepositoryForTests.findAll().size();
        assertNotEquals(numberOfMoviesBeforeDelete, numberOfMoviesAfterDelete);
        assertEquals(numberOfMoviesBeforeDelete - 1, numberOfMoviesAfterDelete);
        assertThrows(MovieRepositoryReadException.class, () -> {
            Movie foundMovie = movieRepositoryForTests.findByUUID(removedMovieID);
        });
    }

    @Test
    public void deleteCertainMovieThatIsNotInTheDatabaseTestNegative() {
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 25.60, 3, 45);
        assertNotNull(movie);
        assertThrows(MovieRepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie));
    }

    @Test
    public void deleteCertainMovieWithUUIDThatIsNotInTheDatabaseTestNegative() {
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 25.60,  3, 45);
        assertNotNull(movie);
        assertThrows(MovieRepositoryDeleteException.class, () -> movieRepositoryForTests.delete(movie.getMovieID()));
    }

    @Test
    public void findCertainMovieTestPositive() throws MovieRepositoryReadException {
        Movie foundMovie = movieRepositoryForTests.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void findCertainMovieTestNegative() throws MovieRepositoryReadException {
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 25.60, 3, 45);
        assertNotNull(movie);
        assertThrows(MovieRepositoryReadException.class, () -> {
            Movie foundMovie = movieRepositoryForTests.findByUUID(movie.getMovieID());
        });
    }

    @Test
    public void findAllMoviesTestPositive() throws MovieRepositoryReadException {
        List<Movie> listOfAllMovies = movieRepositoryForTests.findAll();
        assertNotNull(listOfAllMovies);
        assertEquals(3, listOfAllMovies.size());
    }
}
