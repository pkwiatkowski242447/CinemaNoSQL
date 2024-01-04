package model_tests;

import model.Movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTests {

    private Movie testMovie;
    private Movie newTestMovie;

    @BeforeEach
    public void init() {
        testMovie = new Movie(UUID.randomUUID(), "Oppenheimer", 45.00, 90, 1);
        newTestMovie = new Movie(testMovie.getMovieID(),
                testMovie.getMovieTitle(),
                testMovie.getMovieBasePrice(),
                testMovie.getNumberOfAvailableSeats(),
                testMovie.getScreeningRoomNumber());
    }

    @Test
    public void movieConstructorAndGettersTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Die Hard";
        double movieBasePrice = 37.75;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertNotNull(movie);

        assertEquals(movieID, movie.getMovieID());
        assertEquals(movieTitle, movie.getMovieTitle());
        assertEquals(numberOfAvailableSeats, movie.getNumberOfAvailableSeats());
        assertEquals(screeningRoomNumber, movie.getScreeningRoomNumber());
    }

    @Test
    public void movieTitleSetterTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitleNo1 = "Pulp Fiction";
        String movieTitleNo2 = "Other title";
        double movieBasePrice = 37.75;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(movieID, movieTitleNo1, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertNotNull(movie);
        assertEquals(movieTitleNo1, movie.getMovieTitle());

        movie.setMovieTitle(movieTitleNo2);

        assertNotNull(movie);
        assertEquals(movieTitleNo2, movie.getMovieTitle());
    }

    @Test
    public void numberOfAvailableSeatsSetterTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePrice = 37.75;
        int numberOfAvailableSeatsNo1 = 90;
        int numberOfAvailableSeatsNo2 = 10;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, numberOfAvailableSeatsNo1, screeningRoomNumber);

        assertNotNull(movie);
        assertEquals(numberOfAvailableSeatsNo1, movie.getNumberOfAvailableSeats());

        movie.setNumberOfAvailableSeats(numberOfAvailableSeatsNo2);
        assertEquals(numberOfAvailableSeatsNo2, movie.getNumberOfAvailableSeats());
    }

    @Test
    public void screeningRoomNumberSetterTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePrice = 37.75;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumberNo1 = 1;
        int screeningRoomNumberNo2 = 5;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumberNo1);

        assertNotNull(movie);
        assertEquals(screeningRoomNumberNo1, movie.getScreeningRoomNumber());

        movie.setScreeningRoomNumber(screeningRoomNumberNo2);
        assertEquals(screeningRoomNumberNo2, movie.getScreeningRoomNumber());
    }

    @Test
    public void toStringTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        double movieBasePrice = 37.75;
        int numberOfAvailableSeats = 90;
        int screeningRoomNumber = 1;

        Movie movie = new Movie(movieID, movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);

        assertNotNull(movie);
        assertNotNull(movie.toString());
        assertNotEquals("", movie.toString());
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
