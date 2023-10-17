package model_tests;

import model.Movie;
import model.ScreeningRoom;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTests {

    private final ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 6, 90);
    private final ScreeningRoom screeningRoomNo2 = null;

    @Test
    public void movieConstructorAndGettersTestPositive() {
        UUID movieIDNo1 = UUID.randomUUID();
        String movieTitleNo1 = "Die Hard";

        Movie movieNo1 = new Movie(movieIDNo1, movieTitleNo1, screeningRoomNo1);

        assertNotNull(movieNo1);

        assertEquals(movieIDNo1, movieNo1.getMovieID());
        assertEquals(movieTitleNo1, movieNo1.getMovieTitle());
        assertTrue(movieNo1.isMovieStatusActive());
        assertEquals(screeningRoomNo1, movieNo1.getScreeningRoom());
    }

    @Test
    public void movieTitleSetterTestPositive() {
        UUID movieID = UUID.randomUUID();
        String movieTitleNo1 = "Pulp Fiction";
        String movieTitleNo2 = "Other title";

        Movie movie = new Movie(movieID, movieTitleNo1, screeningRoomNo1);

        assertNotNull(movie);
        assertEquals(movieTitleNo1, movie.getMovieTitle());

        movie.setMovieTitle(movieTitleNo2);

        assertNotNull(movie);
        assertEquals(movieTitleNo2, movie.getMovieTitle());
    }

    @Test
    public void movieSetterMovieStatusActiveTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";
        boolean movieStatusActive = false;

        Movie movie = new Movie(movieID, movieTitle, screeningRoomNo1);

        assertNotNull(movie);
        assertTrue(movie.isMovieStatusActive());

        movie.setMovieStatusActive(movieStatusActive);
        assertFalse(movie.isMovieStatusActive());
    }

    @Test
    public void getMovieInfoTest() {
        UUID movieID = UUID.randomUUID();
        String movieTitle = "Pulp Fiction";

        Movie movie = new Movie(movieID, movieTitle, screeningRoomNo1);

        assertNotNull(movie);
        assertNotNull(movie.getMovieInfo());
        assertNotEquals("", movie.getMovieInfo());
    }
}
