package model_tests;

import model.Movie;
import model.ScreeningRoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTests {

    private final ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 90);
    private Movie testMovie;
    private Movie newTestMovie;

    @BeforeEach
    public void init() {
        testMovie = new Movie(UUID.randomUUID(), "Oppenheimer", 20, screeningRoom);
        newTestMovie = new Movie(testMovie.getMovieID(),
                testMovie.getMovieTitle(),
                testMovie.getMovieBasePrice(),
                testMovie.getScreeningRoom());
    }

    @Test
    public void movieConstructorAndGettersTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Die Hard";
        double movieBasePrice = 20;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, screeningRoom);

        assertNotNull(movie);

        assertEquals(movieID, movie.getMovieID());
        assertEquals(movieTitle, movie.getMovieTitle());
        assertEquals(movieBasePrice, movie.getMovieBasePrice());
        assertTrue(movie.isMovieStatusActive());
        assertEquals(screeningRoom, movie.getScreeningRoom());
    }

    @Test
    public void movieTitleSetterTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitleNo1 = "Pulp Fiction";
        String movieTitleNo2 = "Other title";
        double movieBasePrice = 20;

        Movie movie = new Movie(movieID, movieTitleNo1, movieBasePrice, screeningRoom);

        assertNotNull(movie);
        assertEquals(movieTitleNo1, movie.getMovieTitle());

        movie.setMovieTitle(movieTitleNo2);

        assertNotNull(movie);
        assertEquals(movieTitleNo2, movie.getMovieTitle());
    }

    @Test
    public void movieBasePriceSetterTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePriceNo1 = 20;
        double movieBasePriceNo2 = -10;

        Movie movie = new Movie(movieID, movieTitle, movieBasePriceNo1, screeningRoom);

        assertNotNull(movie);
        assertEquals(movieBasePriceNo1, movie.getMovieBasePrice());

        movie.setMovieBasePrice(movieBasePriceNo2);
        assertEquals(movieBasePriceNo2, movie.getMovieBasePrice());
    }

    @Test
    public void movieSetterMovieStatusActiveTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePrice = 20;
        boolean movieStatusActive = false;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, screeningRoom);

        assertNotNull(movie);
        assertTrue(movie.isMovieStatusActive());

        movie.setMovieStatusActive(movieStatusActive);
        assertFalse(movie.isMovieStatusActive());
    }

    @Test
    public void getMovieInfoTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePrice = 20;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, screeningRoom);

        assertNotNull(movie);
        assertNotNull(movie.getMovieInfo());
        assertNotEquals("", movie.getMovieInfo());
    }

    @Test
    public void movieEqualsTestWithItself() {
        boolean result = testMovie.equals(testMovie);
        assertTrue(result);
    }

    @Test
    public void movieEqualsTestWithNull() {
        boolean result = testMovie.equals(null);
        assertFalse(result);
    }

    @Test
    public void movieEqualsTestWithObjectFromOtherClass() {
        boolean result = testMovie.equals(new Date());
        assertFalse(result);
    }

    @Test
    public void movieEqualsTestWithTheSameObject() {
        boolean result = testMovie.equals(newTestMovie);
        assertTrue(result);
    }

    @Test
    public void movieHashCodeTestPositive() {
        int hashCodeNo1 = testMovie.hashCode();
        int hashCodeNo2 = newTestMovie.hashCode();
        assertEquals(hashCodeNo1, hashCodeNo2);
    }

    @Test
    public void movieHashCodeTestNegative() {
        testMovie.setMovieTitle("Other title");
        int hashCodeNo1 = testMovie.hashCode();
        int hashCodeNo2 = newTestMovie.hashCode();
        assertNotEquals(hashCodeNo1, hashCodeNo2);
    }
}
