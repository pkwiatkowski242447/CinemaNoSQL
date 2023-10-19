package model.managers;

import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.repositories.Repository;

import java.util.UUID;

public class MovieManager extends Manager<Movie> {
    public MovieManager(Repository<Movie> objectRepository) {
        super(objectRepository);
    }

    public Movie register(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        Movie movieToRepo = null;
        try {
            movieToRepo = new Movie(UUID.randomUUID(), movieTitle, baseMoviePrice, screeningRoom);
            getObjectRepository().create(movieToRepo);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return movieToRepo;
    }
}
