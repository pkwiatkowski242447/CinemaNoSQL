package model.repositories.decorators;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import mapping_layer.mappers.MovieMapper;
import mapping_layer.mappers.ScreeningRoomMapper;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.interfaces.MovieRepositoryInterface;
import model.repositories.redis_access.RedisConnection;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.UUID;

public class RedisMovieRepositoryDecorator extends MovieRepositoryDecorator {

    private static JedisPool jedisPool;
    private final static int EXPIRE_TIME = 600;
    private final static String movieHashPrefix = "{movie}:";
    private final static String screeningRoomHashPrefix = "{screeningRoom}:";

    public RedisMovieRepositoryDecorator(MovieRepositoryInterface wrappedObject) throws RedisConfigNotFoundException {
        super(wrappedObject);
        jedisPool = RedisConnection.createConnection();
    }

    @Override
    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        Movie movie;
        try {
            movie = super.create(movieTitle, baseMoviePrice, screeningRoom);
            this.addToCache(movie);
        } catch (MovieRepositoryCreateException exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
        return movie;
    }

    @Override
    public Movie findByUUID(UUID movieID) {
        Movie movie;
        try(Jsonb jsonb = JsonbBuilder.create();
            Jedis jedis = jedisPool.getResource()) {
            String movieKey = movieHashPrefix + movieID.toString();
            MovieDoc movieValue = jsonb.fromJson(jedis.get(movieKey), MovieDoc.class);
            movie = MovieMapper.toMovie(movieValue, this.findScreeningRoomDoc(movieValue.getScreeningRoomID()));
        } catch (JedisConnectionException exception) {
            movie = wrappedObject.findByUUID(movieID);
        } catch (Exception exception) {
            movie = wrappedObject.findByUUID(movieID);
            this.addToCache(movie);
        }
        return movie;
    }

    @Override
    public void updateAllFields(Movie movie) {
        try {
            super.updateAllFields(movie);
            this.clearFromCache(movie.getMovieID());
            this.addToCache(movie);
        } catch (MovieRepositoryUpdateException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Movie movie) {
        try {
            super.delete(movie);
            this.clearFromCache(movie.getMovieID());
        } catch (MovieRepositoryDeleteException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID movieID) {
        try {
            this.clearFromCache(movieID);
            super.delete(movieID);
        } catch (MovieRepositoryDeleteException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Movie movie) {
        try {
            super.expire(movie);
            this.clearFromCache(movie.getMovieID());
            this.addToCache(movie);
        } catch (MovieRepositoryUpdateException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public ScreeningRoomDoc findScreeningRoomDoc(UUID screeningRoomDocId) {
        ScreeningRoomDoc screeningRoomDoc;
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            String screeningRoomKey = screeningRoomHashPrefix + screeningRoomDocId.toString();
            screeningRoomDoc = jsonb.fromJson(jedis.get(screeningRoomKey), ScreeningRoomDoc.class);
        } catch (JedisConnectionException ignored) {
            screeningRoomDoc = super.findScreeningRoomDoc(screeningRoomDocId);
        } catch (Exception exception) {
            screeningRoomDoc = super.findScreeningRoomDoc(screeningRoomDocId);
            this.addToCache(ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc));
        }
        return screeningRoomDoc;
    }

    public void addToCache(Movie movie) {
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            String movieKey = movieHashPrefix + movie.getMovieID().toString();
            String movieValue = jsonb.toJson(MovieMapper.toMovieDoc(movie));
            jedis.set(movieKey, movieValue);
            jedis.expire(movieKey, EXPIRE_TIME);
        } catch (JedisConnectionException ignored) {

        } catch (Exception exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    public void addToCache(ScreeningRoom screeningRoom) {
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            String screeningRoomKey = screeningRoomHashPrefix + screeningRoom.getScreeningRoomID().toString();
            String screeningRoomValue = jsonb.toJson(ScreeningRoomMapper.toScreeningRoomDoc(screeningRoom));
            jedis.set(screeningRoomKey, screeningRoomValue);
            jedis.expire(screeningRoomKey, EXPIRE_TIME);
        } catch (JedisConnectionException ignored) {

        } catch (Exception exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    public void clearFromCache(UUID movieID) {
        try (Jedis jedis = jedisPool.getResource()) {
            String movieKey = movieHashPrefix + movieID.toString();
            jedis.del(movieKey);
        } catch (JedisConnectionException ignored) {

        }
    }

    public Movie readMovieFromCache(UUID movieID) {
        Movie movie = null;
        try(Jsonb jsonb = JsonbBuilder.create();
            Jedis jedis = jedisPool.getResource()) {
            String movieKey = movieHashPrefix + movieID.toString();
            MovieDoc movieValue = jsonb.fromJson(jedis.get(movieKey), MovieDoc.class);
            movie = MovieMapper.toMovie(movieValue, this.findScreeningRoomDoc(movieValue.getScreeningRoomID()));
        } catch (JedisConnectionException ignored) {

        } catch(Exception exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return movie;
    }

    public void clearEntireCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
        } catch (JedisConnectionException ignored) {

        }
    }

    @Override
    public void close() {
        jedisPool.close();
        super.close();
    }
}
