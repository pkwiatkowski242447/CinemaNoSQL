package model.managers;

import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.repositories.MovieRepository;

import java.util.List;
import java.util.UUID;

public class MovieManager{

    private MovieRepository movieRepository;

    public MovieManager(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie register(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        Movie movieToRepo = null;
        try {
            movieToRepo = movieRepository.create(movieTitle, baseMoviePrice, screeningRoom);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return movieToRepo;
    }

    public void unregister(Movie movie) {
        try {
            movieRepository.expire(movie);
        } catch (RepositoryDeleteException exception) {
            exception.printStackTrace();
        }
    }

    public Movie get(UUID identifier) {
        Movie movie = null;
        try {
            movie = movieRepository.findByUUID(identifier);
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return movie;
    }

    public List<Movie> getAll() {
        List<Movie> listOfMovies = null;
        try {
            listOfMovies = movieRepository.findAll();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfMovies;
    }

    public List<Movie> getAllActive() {
        List<Movie> listOfMovies = null;
        try {
            listOfMovies = movieRepository.findAllActive();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfMovies;
    }

    public MovieRepository getMovieRepository() {
        return movieRepository;
    }

    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
