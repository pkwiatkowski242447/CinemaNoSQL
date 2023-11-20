package model.repositories.interfaces;

import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.ScreeningRoom;

import java.util.UUID;

public interface MovieRepositoryInterface extends RepositoryInterface<Movie> {

    Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom);
    ScreeningRoomDoc findScreeningRoomDoc(UUID screeningRoomDocId);
    MovieDoc findMovieDoc(UUID movieDocId);
}
