package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import model.model.Movie;
import model.constants.MovieConstants;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Movie> movieEntityHelper;

    public MovieQueryProvider(MapperContext mapperContext, EntityHelper<Movie> movieEntityHelper) {
        this.session = mapperContext.getSession();
        this.movieEntityHelper = movieEntityHelper;
    }

    // Read methods

    public Movie findByUUID(UUID movieId) throws MovieRepositoryReadException{
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(MovieConstants.MOVIES_TABLE_NAME)
                .all()
                .where(Relation.column(CqlIdentifier.fromCql(MovieConstants.MOVIE_ID)).isEqualTo(QueryBuilder.literal(movieId)))
                .build();
        Row movieRow = session.execute(findByUUID).one();

        if (movieRow != null) {
            return this.convertRowToMovie(movieRow);
        } else {
            throw new MovieRepositoryReadException("Movie object with given Id could not be found in the database.");
        }
    }

    public List<Movie> findAll() throws MovieRepositoryReadException {
        List<Movie> listOfFoundMovies = new ArrayList<>();
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(MovieConstants.MOVIES_TABLE_NAME)
                .all()
                .allowFiltering()
                .build();
        List<Row> movieRows = session.execute(findByUUID).all();

        if (!movieRows.isEmpty()) {
            listOfFoundMovies.addAll(this.convertRowsToMovies(movieRows));
        }
        return listOfFoundMovies;
    }

    // Additional methods

    private Movie convertRowToMovie(Row movieRow) {
        return new Movie(
                movieRow.getUuid(MovieConstants.MOVIE_ID),
                movieRow.getString(MovieConstants.MOVIE_TITLE),
                movieRow.getDouble(MovieConstants.MOVIE_BASE_PRICE),
                movieRow.getInt(MovieConstants.NUMBER_OF_AVAILABLE_SEATS),
                movieRow.getInt(MovieConstants.SCREENING_ROOM_NUMBER)
        );
    }

    private List<Movie> convertRowsToMovies(List<Row> movieRows) {
        List<Movie> movieList = new ArrayList<>();
        for (Row movieRow : movieRows) {
            movieList.add(this.convertRowToMovie(movieRow));
        }
        return movieList;
    }
}
