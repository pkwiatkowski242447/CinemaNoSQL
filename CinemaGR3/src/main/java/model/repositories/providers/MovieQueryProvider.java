package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import mapping_layer.model_rows.MovieRow;
import model.Movie;

import java.util.List;
import java.util.UUID;

public class MovieQueryProvider {

    private final CqlSession session;
    private final EntityHelper<MovieRow> movieEntityHelper;

    public MovieQueryProvider(MapperContext mapperContext, EntityHelper<MovieRow> movieEntityHelper) {
        this.session = mapperContext.getSession();
        this.movieEntityHelper = movieEntityHelper;
    }

    // Create methods

    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        return null;
    }

    // Read methods

    public Movie findByUUID(UUID movieId) {
        return null;
    }

    public List<Movie> findAll() {
        return null;
    }

    public List<Movie> findAllActive() {
        return null;
    }

    public List<UUID> findAllUUIDs() {
        return null;
    }

    // Update methods

    public void update(Movie movie) {

    }

    public void expire(Movie movie) {

    }

    // Delete methods

    public void delete(Movie movie) {

    }

    public void deleteByUUID(UUID movieId) {

    }
}
