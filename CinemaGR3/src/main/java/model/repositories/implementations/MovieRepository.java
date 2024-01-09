package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.validation.ConstraintViolation;
import model.model.Movie;
import model.constants.MovieConstants;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.exceptions.repositories.update_exceptions.MovieRepositoryUpdateException;
import model.exceptions.validation.MovieObjectNotValidException;
import model.repositories.daos.MovieDao;
import model.repositories.interfaces.MovieRepositoryInterface;
import model.repositories.mappers.MovieMapper;
import model.repositories.mappers.MovieMapperBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MovieRepository extends CassandraClient implements MovieRepositoryInterface {

    private final CqlSession session;
    private final MovieMapper movieMapper;
    private final MovieDao movieDao;

    public MovieRepository(CqlSession cqlSession) {
        this.session = cqlSession;
        this.createMoviesTable();

        this.movieMapper = new MovieMapperBuilder(session).build();
        this.movieDao = movieMapper.movieDao();
    }

    private void createMoviesTable() {
        SimpleStatement createMoviesTable = SchemaBuilder
                .createTable(MovieConstants.MOVIES_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(MovieConstants.MOVIE_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(MovieConstants.MOVIE_TITLE), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(MovieConstants.MOVIE_BASE_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(MovieConstants.NUMBER_OF_AVAILABLE_SEATS), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(MovieConstants.SCREENING_ROOM_NUMBER), DataTypes.INT)
                .build();
        session.execute(createMoviesTable);
    }

    private void dropMoviesTable() {
        SimpleStatement dropMoviesTable = SchemaBuilder
                .dropTable(MovieConstants.MOVIES_TABLE_NAME)
                .ifExists()
                .build();
        session.execute(dropMoviesTable);
    }

    // Create methods

    @Override
    public Movie create(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) throws MovieRepositoryCreateException {
        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        try {
            this.checkIfMovieObjectIsValid(movie);
            movieDao.create(movie);
            return movieDao.findByUUID(movie.getMovieID());
        } catch (MovieObjectNotValidException | MovieRepositoryReadException exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Movie findByUUID(UUID movieId) throws MovieRepositoryReadException {
        try {
            return movieDao.findByUUID(movieId);
        } catch (MovieRepositoryReadException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Movie> findAll() throws MovieRepositoryReadException {
        try {
            return movieDao.findAll();
        } catch (MovieRepositoryReadException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Movie movie) throws MovieRepositoryUpdateException {
        try {
            movieDao.findByUUID(movie.getMovieID());
            this.checkIfMovieObjectIsValid(movie);
        } catch (MovieRepositoryReadException | MovieObjectNotValidException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
        movieDao.update(movie);
    }

    // Delete methods

    @Override
    public void delete(Movie movie) throws MovieRepositoryDeleteException {
        try {
            movieDao.findByUUID(movie.getMovieID());
        } catch (MovieRepositoryReadException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
        movieDao.delete(movie);
    }

    @Override
    public void delete(UUID movieID) throws MovieRepositoryDeleteException {
        Movie movie;
        try {
            movie = movieDao.findByUUID(movieID);
        } catch (MovieRepositoryReadException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
        movieDao.delete(movie);
    }

    private void checkIfMovieObjectIsValid(Movie movie) throws MovieObjectNotValidException {
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Movie> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new MovieObjectNotValidException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}