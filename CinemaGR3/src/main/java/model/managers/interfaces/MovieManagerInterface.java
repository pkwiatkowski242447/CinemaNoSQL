package model.managers.interfaces;

import model.exceptions.managers.create_exceptions.MovieManagerCreateException;
import model.model.Movie;

public interface MovieManagerInterface extends ManagerInterface<Movie> {

    // C - create method for movie manager interface

    Movie create(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) throws MovieManagerCreateException;
}
