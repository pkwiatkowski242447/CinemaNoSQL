package repository_tests.redis;

import mapping_layer.model_docs.MovieRow;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.ScreeningRoomRepository;
import model.repositories.interfaces.MovieRepositoryInterface;
import model.repositories.interfaces.ScreeningRoomRepositoryInterface;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RedisMovieRepositoryDecoratorTest {

    private final static String databaseName = "test";
    private static MovieRepositoryInterface movieRepositoryForTests;
    private static RedisMovieRepositoryDecorator redisMovieRepository;
    private static ScreeningRoomRepositoryInterface screeningRoomRepositoryForTests;
    private static RedisScreeningRoomRepositoryDecorator redisScreeningRoomRepository;

    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @BeforeAll
    public static void init() throws RedisConfigNotFoundException, MongoConfigNotFoundException{
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        movieRepositoryForTests = new MovieRepository(databaseName);
        redisScreeningRoomRepository = new RedisScreeningRoomRepositoryDecorator(screeningRoomRepositoryForTests);
        redisMovieRepository = new RedisMovieRepositoryDecorator(movieRepositoryForTests);
    }

    @BeforeEach
    public void insertExampleMovies() {
        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        screeningRoomNo1 = redisScreeningRoomRepository.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        screeningRoomNo2 = redisScreeningRoomRepository.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        screeningRoomNo3 = redisScreeningRoomRepository.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        movieNo1 = redisMovieRepository.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1);
        movieNo2 = redisMovieRepository.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2);
        movieNo3 = redisMovieRepository.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3);
    }

    @AfterEach
    public void deleteExampleMovies() {
        List<UUID> listOfMoviesID = redisMovieRepository.findAllUUIDs();
        for (UUID movieID : listOfMoviesID) {
            redisMovieRepository.delete(movieID);
        }

        List<UUID> listOfScreeningRoomsUUIDs = redisScreeningRoomRepository.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            redisScreeningRoomRepository.delete(screeningRoomID);
        }

        redisMovieRepository.clearEntireCache();
        redisScreeningRoomRepository.clearEntireCache();
    }

    @AfterAll
    public static void destroy() {
        redisMovieRepository.close();
        redisScreeningRoomRepository.close();
    }

    @Test
    public void movieRepositoryConstructorTest() throws RedisConfigNotFoundException, MongoConfigNotFoundException {
        MovieRepository movieRepository = new MovieRepository(databaseName);
        assertNotNull(movieRepository);
        RedisMovieRepositoryDecorator redisMovieRepository = new RedisMovieRepositoryDecorator(movieRepository);
        assertNotNull(redisScreeningRoomRepository);
    }

    @Test
    public void createNewMovieTestPositive() {
        ScreeningRoom screeningRoom = redisScreeningRoomRepository.create(0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = redisMovieRepository.create("American Psycho", 35.75, screeningRoom);
        assertNotNull(movie);
        Movie foundMovie = redisMovieRepository.findByUUID(movie.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movie);
    }

    @Test
    public void createNewMovieWithNullTitleTestNegative() {
        ScreeningRoom screeningRoom = redisScreeningRoomRepository.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> redisMovieRepository.create(null, 35.75, screeningRoom));
    }

    @Test
    public void createNewMovieWithEmptyTitleTestNegative() {
        ScreeningRoom screeningRoom = redisScreeningRoomRepository.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> redisMovieRepository.create("", 35.75, screeningRoom));
    }

    @Test
    public void createNewMovieWithTooLongTitleTestNegative() {
        String movieTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        ScreeningRoom screeningRoom = redisScreeningRoomRepository.create(0, 5, 50);
        assertNotNull(screeningRoom);
        assertThrows(MovieRepositoryCreateException.class, () -> redisMovieRepository.create(movieTitle, 35.75,  screeningRoom));
    }

    @Test
    public void createNewMovieWithNullScreeningRoom() {
        assertThrows(MovieRepositoryCreateException.class, () -> redisMovieRepository.create("Pulp fiction", 35.75, null));
    }

    @Test
    public void updateCertainMovieTestPositive() {
        String oldTitle = movieNo1.getMovieTitle();
        movieNo1.setMovieTitle("Cars");
        String newTitle = movieNo1.getMovieTitle();
        assertDoesNotThrow(() -> redisMovieRepository.updateAllFields(movieNo1));
        Movie foundMovie = redisMovieRepository.findByUUID(movieNo1.getMovieID());
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
        assertThrows(RepositoryUpdateException.class, () -> redisMovieRepository.updateAllFields(movie));
    }

    @Test
    public void updateCertainMovieWithNullTitleTestNegative() {
        Movie foundMovie = redisMovieRepository.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(null);
        assertThrows(RepositoryUpdateException.class, () -> redisMovieRepository.updateAllFields(foundMovie));
    }

    @Test
    public void updateCertainMovieWithEmptyTitleTestNegative() {
        Movie foundMovie = redisMovieRepository.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle("");
        assertThrows(RepositoryUpdateException.class, () -> redisMovieRepository.updateAllFields(foundMovie));
    }

    @Test
    public void updateCertainMovieWithTooLongTitleTestNegative() {
        String newTitle = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        Movie foundMovie = redisMovieRepository.findAll().get(0);
        assertNotNull(foundMovie);
        foundMovie.setMovieTitle(newTitle);
        assertThrows(RepositoryUpdateException.class, () -> redisMovieRepository.updateAllFields(foundMovie));
    }

    @Test
    public void deleteCertainMovieTestPositive() {
        UUID removedMovieID = movieNo1.getMovieID();
        int numberOfMoviesBeforeDelete = redisMovieRepository.findAll().size();
        assertDoesNotThrow(() -> redisMovieRepository.delete(movieNo1));
        int numberOfMoviesAfterDelete = redisMovieRepository.findAll().size();
        assertNotEquals(numberOfMoviesBeforeDelete, numberOfMoviesAfterDelete);
        assertEquals(numberOfMoviesBeforeDelete - 1, numberOfMoviesAfterDelete);
        assertThrows(MovieRepositoryReadException.class, () -> {
            Movie foundMovie = redisMovieRepository.findByUUID(removedMovieID);
        });
    }

    @Test
    public void deleteCertainMovieThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
        assertNotNull(movie);
        assertThrows(MovieRepositoryDeleteException.class, () -> redisMovieRepository.delete(movie));
    }

    @Test
    public void deleteCertainMovieWithUUIDThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
        assertNotNull(movie);
        assertThrows(MovieRepositoryDeleteException.class, () -> redisMovieRepository.delete(movie.getMovieID()));
    }

    @Test
    public void expireCertainMovieTestPositive() {
        UUID expiredMovieUUID = movieNo1.getMovieID();
        int beforeExpiringMovie = redisMovieRepository.findAll().size();
        int numOfActiveMoviesBefore = redisMovieRepository.findAllActive().size();
        redisMovieRepository.expire(movieNo1);
        int afterExpiringMovie = redisMovieRepository.findAll().size();
        int numOfActiveMoviesAfter = redisMovieRepository.findAllActive().size();
        Movie foundMovie = redisMovieRepository.findByUUID(expiredMovieUUID);
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
        assertFalse(movieNo1.isMovieStatusActive());
        assertEquals(beforeExpiringMovie, afterExpiringMovie);
        assertNotEquals(numOfActiveMoviesBefore, numOfActiveMoviesAfter);
        assertEquals(numOfActiveMoviesBefore - 1, numOfActiveMoviesAfter);
    }

    @Test
    public void expireCertainMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
        assertNotNull(movie);
        assertThrows(MovieRepositoryUpdateException.class, () -> {
            redisMovieRepository.expire(movie);
        });
    }

    @Test
    public void findCertainMovieTestPositive() {
        Movie foundMovie = redisMovieRepository.findByUUID(movieNo1.getMovieID());
        assertNotNull(foundMovie);
        assertEquals(foundMovie, movieNo1);
    }

    @Test
    public void findCertainMovieTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 5, 50);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "American Psycho", 45.85, screeningRoom);
        assertNotNull(movie);
        assertThrows(MovieRepositoryReadException.class, () -> {
            Movie foundMovie = redisMovieRepository.findByUUID(movie.getMovieID());
        });
    }

    @Test
    public void findAllMoviesTestPositive() {
        List<Movie> listOfAllMovies = redisMovieRepository.findAll();
        assertNotNull(listOfAllMovies);
        assertEquals(3, listOfAllMovies.size());
    }

    @Test
    public void findAllActiveMoviesTestPositive() {
        List<Movie> startingListOfMovies = redisMovieRepository.findAllActive();
        assertNotNull(startingListOfMovies);
        redisMovieRepository.expire(startingListOfMovies.get(0));
        List<Movie> endingListOfMovies = redisMovieRepository.findAllActive();
        assertNotNull(startingListOfMovies);
        assertEquals(startingListOfMovies.size(), 3);
        assertEquals(endingListOfMovies.size(), 2);
    }

    // Other

    @Test
    public void mongoRepositoryFindMovieDocTestPositive () {
        MovieRow movieRow = redisMovieRepository.findMovieDoc(movieNo1.getMovieID());
        assertNotNull(movieRow);
        assertEquals(movieRow.getMovieID(), movieNo1.getMovieID());
        assertEquals(movieRow.getMovieTitle(), movieNo1.getMovieTitle());
        assertEquals(movieRow.getMovieBasePrice(), movieNo1.getMovieBasePrice());
        assertEquals(movieRow.isMovieStatusActive(), movieNo1.isMovieStatusActive());
        assertEquals(movieRow.getScreeningRoomID(), movieNo1.getScreeningRoom().getScreeningRoomID());
    }

    @Test
    public void mongoRepositoryFindMovieDocTestNegative () {
        Movie movie = new Movie(UUID.randomUUID(), "SomeTitle", 40, screeningRoomNo1);
        assertNotNull(movie);
        assertThrows(MovieDocNotFoundException.class, () -> {
            redisMovieRepository.findMovieDoc(movie.getMovieID());
        });
    }

    @Test
    public void addMovieToRedisCacheTestPositive() {
        Movie testMovie = new Movie(UUID.randomUUID(), "SomeTitle", 40, screeningRoomNo1);
        assertNotNull(testMovie);
        assertThrows(MovieRepositoryReadException.class, () -> {
            redisMovieRepository.readMovieFromCache(testMovie.getMovieID());
        });
        redisMovieRepository.addToCache(testMovie);
        Movie cacheMovie = redisMovieRepository.readMovieFromCache(testMovie.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(testMovie, cacheMovie);
    }

    @Test
    public void readMovieFromRedisCacheTestPositive() {
        Movie testMovie = new Movie(UUID.randomUUID(), "SomeTitle", 40, screeningRoomNo1);
        assertNotNull(testMovie);
        assertThrows(MovieRepositoryReadException.class, () -> {
            redisMovieRepository.readMovieFromCache(testMovie.getMovieID());
        });
        redisMovieRepository.addToCache(testMovie);
        Movie cacheMovie = redisMovieRepository.readMovieFromCache(testMovie.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(testMovie, cacheMovie);
        redisMovieRepository.clearFromCache(testMovie.getMovieID());
        assertThrows(MovieRepositoryReadException.class, () -> {
            redisMovieRepository.readMovieFromCache(testMovie.getMovieID());
        });
    }

    @Test
    public void readMovieDataFromDBEvenWithoutWorkingRedis() {
        // Redis needs to be turned off.
        Movie cacheMovie = redisMovieRepository.findByUUID(movieNo1.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(movieNo1, cacheMovie);
    }

    @Test
    public void readMovieDataFromRedisCacheWhenItIsInCache() {
        Movie testMovie = redisMovieRepository.create("SomeTitle", 40, screeningRoomNo1);
        Movie cacheMovie = redisMovieRepository.findByUUID(testMovie.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(testMovie, cacheMovie);
    }

    @Test
    public void readMovieDataFromRedisCacheWhenItIsNotInCache() {
        Movie testMovie = redisMovieRepository.create("SomeTitle", 40, screeningRoomNo1);
        redisMovieRepository.clearFromCache(testMovie.getMovieID());
        Movie cacheMovie = redisMovieRepository.findByUUID(testMovie.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(testMovie, cacheMovie);
    }
}
