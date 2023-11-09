package mapping_layer_tests.mappers_tests;

import mapping_layer.mappers.MovieMapper;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.ScreeningRoomNullException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieMapperTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void movieMapperConstructorTest() {
        MovieMapper movieMapper = new MovieMapper();
        assertNotNull(movieMapper);
    }

    @Test
    public void movieMapperFromMovieToMovieDocTestPositive() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 70);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "SomeTitle", 25.00, screeningRoom);
        assertNotNull(movie);
        MovieDoc movieDoc = MovieMapper.toMovieDoc(movie);
        assertNotNull(movieDoc);
        assertEquals(movie.getMovieID(), movieDoc.getMovieID());
        assertEquals(movie.getMovieTitle(), movieDoc.getMovieTitle());
        assertEquals(movie.getMovieBasePrice(), movieDoc.getMovieBasePrice());
        assertEquals(movie.isMovieStatusActive(), movieDoc.isMovieStatusActive());
        assertEquals(movie.getScreeningRoom().getScreeningRoomID(), movieDoc.getScreeningRoomID());
    }

    @Test
    public void movieMapperFromMovieToMovieDocTestNegative() {
        ScreeningRoom screeningRoom = null;
        assertNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "SomeTitle", 25.00, screeningRoom);
        assertNotNull(movie);
        assertThrows(ScreeningRoomNullException.class, () -> {
            MovieDoc movieDoc = MovieMapper.toMovieDoc(movie);
        });
    }

    @Test
    public void movieMapperFromMovieDocToMovieTestPositive() {
        ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc(UUID.randomUUID(), 1, 4, 45, true);
        assertNotNull(screeningRoomDoc);
        MovieDoc movieDoc = new MovieDoc(UUID.randomUUID(), "SomeTitle", true, 45, screeningRoomDoc.getScreeningRoomID());
        assertNotNull(movieDoc);
        Movie movie = MovieMapper.toMovie(movieDoc, screeningRoomDoc);
        assertNotNull(movie);
        assertEquals(movieDoc.getMovieID(), movie.getMovieID());
        assertEquals(movieDoc.getMovieTitle(), movie.getMovieTitle());
        assertEquals(movieDoc.getMovieBasePrice(), movie.getMovieBasePrice());
        assertEquals(movieDoc.isMovieStatusActive(), movie.isMovieStatusActive());
        assertEquals(screeningRoomDoc.getScreeningRoomID(), movie.getScreeningRoom().getScreeningRoomID());
        assertEquals(screeningRoomDoc.getScreeningRoomFloor(), movie.getScreeningRoom().getScreeningRoomFloor());
        assertEquals(screeningRoomDoc.getScreeningRoomNumber(), movie.getScreeningRoom().getScreeningRoomNumber());
        assertEquals(screeningRoomDoc.getNumberOfAvailableSeats(), movie.getScreeningRoom().getNumberOfAvailableSeats());
        assertEquals(screeningRoomDoc.isScreeningRoomStatusActive(), movie.getScreeningRoom().isScreeningRoomStatusActive());
    }
}
