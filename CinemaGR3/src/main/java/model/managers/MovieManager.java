package model.managers;

import model.Movie;
import model.exceptions.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.repositories.implementations.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieManager{

    private MovieRepository movieRepository;

    public MovieManager(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie register(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) {
        Movie movie = null;
        try {
            movie = movieRepository.create(movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        } catch (MovieRepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return movie;
    }

    public Movie get(UUID identifier) {
        Movie movie = null;
        try {
            movie = movieRepository.findByUUID(identifier);
        } catch (MovieRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return movie;
    }

    public List<Movie> getAll() {
        List<Movie> listOfAllMovies = new ArrayList<>();
        try {
            listOfAllMovies = movieRepository.findAll();
        } catch (MovieRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfAllMovies;
    }

    public MovieRepository getMovieRepository() {
        return movieRepository;
    }

    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
