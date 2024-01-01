package model.repositories.interfaces;

import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import model.Movie;
import model.ScreeningRoom;

import java.util.UUID;

public interface MovieRepositoryInterface extends RepositoryInterface<Movie> {

    Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom);
    ScreeningRoomRow findScreeningRoomDoc(UUID screeningRoomDocId);
    MovieRow findMovieDoc(UUID movieDocId);
}
