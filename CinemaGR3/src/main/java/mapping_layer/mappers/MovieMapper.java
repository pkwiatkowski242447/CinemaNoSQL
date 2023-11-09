package mapping_layer.mappers;

import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.exceptions.model_docs_exceptions.ScreeningRoomNullException;

public class MovieMapper {

    public static MovieDoc toMovieDoc(Movie movie) {
        MovieDoc movieDoc = new MovieDoc();
        movieDoc.setMovieID(movie.getMovieID());
        movieDoc.setMovieTitle(movie.getMovieTitle());
        movieDoc.setMovieBasePrice(movie.getMovieBasePrice());
        movieDoc.setMovieStatusActive(movie.isMovieStatusActive());
        if (movie.getScreeningRoom() != null) {
            movieDoc.setScreeningRoomID(movie.getScreeningRoom().getScreeningRoomID());
        } else {
            throw  new ScreeningRoomNullException("Reference to screening room object is null.");
        }
        return movieDoc;
    }

    public static Movie toMovie(MovieDoc movieDoc, ScreeningRoomDoc screeningRoomDoc) {
        return new Movie(movieDoc.getMovieID(),
                movieDoc.getMovieTitle(),
                movieDoc.isMovieStatusActive(),
                movieDoc.getMovieBasePrice(),
                ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc));
    }
}
