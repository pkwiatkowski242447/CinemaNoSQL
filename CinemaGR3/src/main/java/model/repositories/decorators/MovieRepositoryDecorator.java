package model.repositories.decorators;

import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.ScreeningRoom;
import model.repositories.interfaces.MovieRepositoryInterface;

import java.util.List;
import java.util.UUID;

public class MovieRepositoryDecorator implements MovieRepositoryInterface {

    protected MovieRepositoryInterface wrappedObject;

    public MovieRepositoryDecorator(MovieRepositoryInterface wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    @Override
    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        return wrappedObject.create(movieTitle, baseMoviePrice, screeningRoom);
    }

    @Override
    public Movie findByUUID(UUID movieID) {
        return wrappedObject.findByUUID(movieID);
    }

    @Override
    public List<Movie> findAll() {
        return wrappedObject.findAll();
    }

    @Override
    public List<Movie> findAllActive() {
        return wrappedObject.findAllActive();
    }

    @Override
    public List<UUID> findAllUUIDs() {
        return wrappedObject.findAllUUIDs();
    }

    @Override
    public void updateAllFields(Movie movie) {
        wrappedObject.updateAllFields(movie);
    }

    @Override
    public void delete(Movie movie) {
        wrappedObject.delete(movie);
    }

    @Override
    public void delete(UUID movieID) {
        wrappedObject.delete(movieID);
    }

    @Override
    public void expire(Movie movie) {
        wrappedObject.expire(movie);
    }

    @Override
    public ScreeningRoomDoc findScreeningRoomDoc(UUID screeningRoomDocId) {
        return wrappedObject.findScreeningRoomDoc(screeningRoomDocId);
    }

    @Override
    public MovieDoc findMovieDoc(UUID movieDocId) {
        return wrappedObject.findMovieDoc(movieDocId);
    }

    @Override
    public void close() {
        wrappedObject.close();
    }
}
