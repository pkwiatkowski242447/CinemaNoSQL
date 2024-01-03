package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Movie;
import model.repositories.daos.MovieDao;
import model.repositories.interfaces.MovieRepositoryInterface;
import model.repositories.mappers.MovieMapper;
import model.repositories.mappers.MovieMapperBuilder;

import java.util.List;
import java.util.UUID;

public class MovieRepository extends CassandraClient implements MovieRepositoryInterface {

    private final CqlSession session;
    private final MovieMapper movieMapper;
    private final MovieDao movieDao;

    public MovieRepository() {
        this.session = this.initializeCassandraSession();
        this.movieMapper = new MovieMapperBuilder(session).build();
        this.movieDao = movieMapper.movieDao();
    }

    // Create methods

    @Override
    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        return movieDao.create(movieTitle, baseMoviePrice, screeningRoom);
    }

    // Read methods

    @Override
    public Movie findByUUID(UUID movieId) {
        return movieDao.findByUUID(movieId);
    }

    @Override
    public List<Movie> findAll() {
        return movieDao.findAll();
    }

    @Override
    public List<Movie> findAllActive() {
        return movieDao.findAllActive();
    }

    // Update methods

    @Override
    public void update(Movie movie) {
        movieDao.update(movie);
    }

    @Override
    public void expire(Movie movie) {
        movieDao.expire(movie);
    }

    // Delete methods

    @Override
    public void delete(Movie movie) {
        movieDao.delete(movie);
    }

    @Override
    public void delete(UUID movieID) {
        movieDao.deleteByUUID(movieID);
    }

    @Override
    public void close() {
        super.close();
    }
}