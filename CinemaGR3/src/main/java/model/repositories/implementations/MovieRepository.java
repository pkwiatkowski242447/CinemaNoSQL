package model.repositories.implementations;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.converters.MovieConverter;
import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.null_reference_exceptions.ScreeningRoomNullException;
import model.exceptions.repository_exceptions.*;
import model.repositories.interfaces.MovieRepositoryInterface;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieRepository extends MongoRepository implements MovieRepositoryInterface {

    public MovieRepository(String databaseName) throws MongoConfigNotFoundException {
        super.initDatabaseConnection(databaseName);

        // Checking if collection "movies" exists.
        boolean collectionExists = false;
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if (collectionName.equals(this.movieCollectionName)) {
                collectionExists = true;
                break;
            }
        }

        // If collection does not exist - then create it with a specific JSON Schema.
        if (!collectionExists) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse("""
                            {
                                $jsonSchema: {
                                    "bsonType": "object",
                                    "required": ["_id", "movie_title", "movie_status_active", "movie_base_price", "screening_room_ref"],
                                    "properties": {
                                        "_id": {
                                            "description": "UUID identifier of a certain movie document.",
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "movie_title": {
                                            "description": "String containing certain movie title.",
                                            "bsonType": "string",
                                            "minLength": 1,
                                            "maxLength": 150
                                        }
                                        "movie_status_active": {
                                            "description": "Boolean flag indicating whether movie is active (still actively shown in the cinema) or not.",
                                            "bsonType": "bool"
                                        }
                                        "movie_base_price": {
                                            "description": "Double value holding base price of the movie - that is before any discounts.",
                                            "bsonType": "double",
                                            "minimum": 0,
                                            "maximum": 100
                                        }
                                        "screening_room_ref": {
                                            "description": "Screening room's ID in which this given movie will be shown.",
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                    }
                                }
                            }
                            """));
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            mongoDatabase.createCollection(this.movieCollectionName, createCollectionOptions);
        }
    }

    @Override
    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        Movie movie;
        try {
            movie = new Movie(UUID.randomUUID(), movieTitle, baseMoviePrice, screeningRoom);
            MovieRow movieRow = MovieConverter.toMovieRow(movie);
            getMovieCollection().insertOne(movieRow);
        } catch (MongoException | ScreeningRoomNullException exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
        return movie;
    }

    @Override
    public void updateAllFields(Movie movie) {
        try {
            MovieRow movieRow = MovieConverter.toMovieRow(movie);
            Bson filter = Filters.eq("_id", movie.getMovieID());
            MovieRow updatedMovieRow = getMovieCollection().findOneAndReplace(filter, movieRow);
            if (updatedMovieRow == null) {
                throw new MovieDocNotFoundException("Document for given movie object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Movie movie) {
        delete(movie.getMovieID());
    }

    @Override
    public void delete(UUID movieID) {
        try {
            Bson filter = Filters.eq("_id", movieID);
            MovieRow removedMovieRow = getMovieCollection().findOneAndDelete(filter);
            if (removedMovieRow == null) {
                throw new MovieDocNotFoundException("Document for given movie object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Movie movie) {
        movie.setMovieStatusActive(false);
        updateAllFields(movie);
    }

    @Override
    public Movie findByUUID(UUID identifier) {
        Movie movie;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            MovieRow foundMovieRow = findMovieDoc(identifier);
            if (foundMovieRow != null) {
                ScreeningRoomRow screeningRoomRow = this.findScreeningRoomDoc(foundMovieRow.getScreeningRoomID());
                movie = MovieConverter.toMovie(foundMovieRow, screeningRoomRow);
            } else {
                throw new MovieDocNotFoundException("Document for movie object with given UUID could not be read, since it is not in the database.");
            }
            clientSession.commitTransaction();
        } catch (MongoException | NullPointerException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return movie;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> listOfAllMovies;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson movieFilter = Filters.empty();
            listOfAllMovies = findMovies(movieFilter);
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllMovies;
    }

    @Override
    public List<Movie> findAllActive() {
        List<Movie> listOfAllActiveMovies;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson movieFilter = Filters.eq("movie_status_active", true);
            listOfAllActiveMovies = findMovies(movieFilter);
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveMovies;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfUUIDs;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson filter = Filters.empty();
            listOfUUIDs = new ArrayList<>();
            for (MovieRow movieRow : getMovieCollection().find(filter)) {
                listOfUUIDs.add(movieRow.getMovieID());
            }
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfUUIDs;
    }

    private List<Movie> findMovies(Bson movieFilter) {
        List<Movie> listOfFoundMovies = new ArrayList<>();
        for (MovieRow movieRow : getMovieCollection().find(movieFilter)) {
            ScreeningRoomRow screeningRoomRow = findScreeningRoomDoc(movieRow.getScreeningRoomID());
            listOfFoundMovies.add(MovieConverter.toMovie(movieRow, screeningRoomRow));
        }
        return listOfFoundMovies;
    }
}