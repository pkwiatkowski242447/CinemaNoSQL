package model.repositories.interfaces;

import model.Movie;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;

public interface MovieRepositoryInterface extends RepositoryInterface<Movie> {

    // C - create method for movie repository interface

    Movie create(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) throws MovieRepositoryCreateException;
}
