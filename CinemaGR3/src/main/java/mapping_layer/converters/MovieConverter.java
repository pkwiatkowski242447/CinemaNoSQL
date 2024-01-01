package mapping_layer.converters;

import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import model.Movie;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ScreeningRoomNullException;

public class MovieConverter {

    public static MovieRow toMovieRow(Movie movie) throws ScreeningRoomNullException {
        MovieRow movieRow = new MovieRow();
        movieRow.setMovieID(movie.getMovieID());
        movieRow.setMovieTitle(movie.getMovieTitle());
        movieRow.setMovieBasePrice(movie.getMovieBasePrice());
        movieRow.setMovieStatusActive(movie.isMovieStatusActive());
        if (movie.getScreeningRoom() != null) {
            movieRow.setScreeningRoomID(movie.getScreeningRoom().getScreeningRoomID());
        } else {
            throw new ScreeningRoomNullException("Reference to screening room object is null.");
        }
        return movieRow;
    }

    public static Movie toMovie(MovieRow movieRow, ScreeningRoomRow screeningRoomRow) {
        return new Movie(movieRow.getMovieID(),
                movieRow.getMovieTitle(),
                movieRow.isMovieStatusActive(),
                movieRow.getMovieBasePrice(),
                ScreeningRoomConverter.toScreeningRoom(screeningRoomRow));
    }
}
