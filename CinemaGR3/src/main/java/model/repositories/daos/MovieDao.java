package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import mapping_layer.model_rows.MovieRow;
import model.Movie;
import model.repositories.providers.MovieQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface MovieDao {

    // Create methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom);

    // Read methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    Movie findByUUID(UUID movieId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    List<Movie> findAll();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    List<Movie> findAllActive();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    List<UUID> findAllUUIDs();

    // Update methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    void update(Movie movie);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    void expire(Movie movie);

    // Delete methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    void delete(Movie movie);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = MovieQueryProvider.class, entityHelpers = {MovieRow.class})
    void deleteByUUID(UUID movieId);
}
