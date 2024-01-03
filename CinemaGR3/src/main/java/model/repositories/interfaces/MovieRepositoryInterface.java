package model.repositories.interfaces;

import model.Movie;

public interface MovieRepositoryInterface extends RepositoryInterface<Movie> {

    Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom);
}
