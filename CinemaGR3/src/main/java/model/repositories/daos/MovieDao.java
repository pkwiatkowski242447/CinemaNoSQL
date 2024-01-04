package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Movie;
import model.constants.GeneralConstants;
import model.exceptions.read_exceptions.MovieRepositoryReadException;
import model.repositories.providers.MovieQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface MovieDao {

    // Create methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Insert
    void create(Movie movie);

    // Read methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {Movie.class}, providerMethod = "findByUUID")
    Movie findByUUID(UUID movieId) throws MovieRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {Movie.class}, providerMethod = "findAll")
    List<Movie> findAll() throws MovieRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Update
    void update(Movie movie);

    // Delete methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Delete
    void delete(Movie movie);
}
