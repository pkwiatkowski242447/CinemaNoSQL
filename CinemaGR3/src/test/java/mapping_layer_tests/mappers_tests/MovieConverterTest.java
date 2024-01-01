package mapping_layer_tests.mappers_tests;

import mapping_layer.converters.MovieConverter;
import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ScreeningRoomNullException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieConverterTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void movieMapperConstructorTest() {
        MovieConverter movieConverter = new MovieConverter();
        assertNotNull(movieConverter);
    }

    @Test
    public void movieMapperFromMovieToMovieDocTestPositive() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 70);
        assertNotNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "SomeTitle", 25.00, screeningRoom);
        assertNotNull(movie);
        MovieRow movieRow = MovieConverter.toMovieRow(movie);
        assertNotNull(movieRow);
        assertEquals(movie.getMovieID(), movieRow.getMovieID());
        assertEquals(movie.getMovieTitle(), movieRow.getMovieTitle());
        assertEquals(movie.getMovieBasePrice(), movieRow.getMovieBasePrice());
        assertEquals(movie.isMovieStatusActive(), movieRow.isMovieStatusActive());
        assertEquals(movie.getScreeningRoom().getScreeningRoomID(), movieRow.getScreeningRoomID());
    }

    @Test
    public void movieMapperFromMovieToMovieDocTestNegative() {
        ScreeningRoom screeningRoom = null;
        assertNull(screeningRoom);
        Movie movie = new Movie(UUID.randomUUID(), "SomeTitle", 25.00, screeningRoom);
        assertNotNull(movie);
        assertThrows(ScreeningRoomNullException.class, () -> {
            MovieRow movieRow = MovieConverter.toMovieRow(movie);
        });
    }

    @Test
    public void movieMapperFromMovieDocToMovieTestPositive() {
        ScreeningRoomRow screeningRoomRow = new ScreeningRoomRow(UUID.randomUUID(), 1, 4, 45, true);
        assertNotNull(screeningRoomRow);
        MovieRow movieRow = new MovieRow(UUID.randomUUID(), "SomeTitle", true, 45, screeningRoomRow.getScreeningRoomID());
        assertNotNull(movieRow);
        Movie movie = MovieConverter.toMovie(movieRow, screeningRoomRow);
        assertNotNull(movie);
        assertEquals(movieRow.getMovieID(), movie.getMovieID());
        assertEquals(movieRow.getMovieTitle(), movie.getMovieTitle());
        assertEquals(movieRow.getMovieBasePrice(), movie.getMovieBasePrice());
        assertEquals(movieRow.isMovieStatusActive(), movie.isMovieStatusActive());
        assertEquals(screeningRoomRow.getScreeningRoomID(), movie.getScreeningRoom().getScreeningRoomID());
        assertEquals(screeningRoomRow.getScreeningRoomFloor(), movie.getScreeningRoom().getScreeningRoomFloor());
        assertEquals(screeningRoomRow.getScreeningRoomNumber(), movie.getScreeningRoom().getScreeningRoomNumber());
        assertEquals(screeningRoomRow.getNumberOfAvailableSeats(), movie.getScreeningRoom().getNumberOfAvailableSeats());
        assertEquals(screeningRoomRow.isScreeningRoomStatusActive(), movie.getScreeningRoom().isScreeningRoomStatusActive());
    }
}
