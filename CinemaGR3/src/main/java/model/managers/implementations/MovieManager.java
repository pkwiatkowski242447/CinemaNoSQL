package model.managers.implementations;

import lombok.Getter;
import lombok.Setter;
import model.exceptions.managers.create_exceptions.MovieManagerCreateException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.delete_exceptions.MovieManagerDeleteException;
import model.exceptions.managers.read_exceptions.MovieManagerReadException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.update_exceptions.MovieManagerUpdateException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.update_exceptions.MovieRepositoryUpdateException;
import model.managers.interfaces.MovieManagerInterface;
import model.model.Movie;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.repositories.implementations.MovieRepository;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class MovieManager implements MovieManagerInterface {

    private MovieRepository movieRepository;

    public MovieManager(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Create methods

    @Override
    public Movie create(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) throws MovieManagerCreateException {
        try {
            return this.movieRepository.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        } catch (MovieRepositoryCreateException exception) {
            throw new MovieManagerCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Movie findByUUID(UUID movieId) throws ReadManagerException {
        try {
            return this.movieRepository.findByUUID(movieId);
        } catch (MovieRepositoryReadException exception) {
            throw new MovieManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Movie> findAll() throws ReadManagerException {
        try {
            return this.movieRepository.findAll();
        } catch (MovieRepositoryReadException exception) {
            throw new MovieManagerReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Movie movie) throws MovieManagerUpdateException {
        try {
            this.movieRepository.update(movie);
        } catch (MovieRepositoryUpdateException exception) {
            throw new MovieManagerUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Movie movie) throws DeleteManagerException {
        try {
            this.movieRepository.delete(movie);
        } catch (MovieRepositoryDeleteException exception) {
            throw new MovieManagerDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID movieId) throws DeleteManagerException {
        try {
            this.movieRepository.delete(movieId);
        } catch (MovieRepositoryDeleteException exception) {
            throw new MovieManagerDeleteException(exception.getMessage(), exception);
        }
    }
}
