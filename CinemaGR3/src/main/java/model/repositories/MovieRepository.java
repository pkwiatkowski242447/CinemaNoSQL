package model.repositories;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.Movie;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.DocNotFoundException;
import model.exceptions.model_docs_exceptions.MovieDocNotFoundException;
import model.exceptions.model_docs_exceptions.ScreeningRoomDocNotFoundException;
import model.exceptions.model_docs_exceptions.ScreeningRoomNullException;
import model.exceptions.repository_exceptions.MovieRepositoryCreateException;
import model.exceptions.repository_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repository_exceptions.MovieRepositoryReadException;
import model.exceptions.repository_exceptions.MovieRepositoryUpdateException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieRepository extends MongoRepository<Movie> {

    private final static Logger logger = LoggerFactory.getLogger(MovieRepository.class);

    public MovieRepository() {
        super.initDatabaseConnection();

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

    public Movie create(String movieTitle, double baseMoviePrice, ScreeningRoom screeningRoom) {
        Movie movie;
        try {
            movie = new Movie(UUID.randomUUID(), movieTitle, baseMoviePrice, screeningRoom);
            MovieDoc movieDoc = new MovieDoc(movie);
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            movieDocCollection.insertOne(movieDoc);
        } catch (MongoException | ScreeningRoomNullException exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
        return movie;
    }

    @Override
    public void updateAllFields(Movie movie) {
        try {
            MovieDoc movieDoc = new MovieDoc(movie);
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson filter = Filters.eq("_id", movie.getMovieID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("movie_title", movieDoc.getMovieTitle()),
                    Updates.set("movie_base_price", movie.getMovieBasePrice()),
                    Updates.set("movie_status_active", movie.isMovieStatusActive())
            );
            MovieDoc updatedMovieDoc = movieDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (updatedMovieDoc == null) {
                throw new MovieDocNotFoundException("Document for given movie object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Movie movie) {
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson filter = Filters.eq("_id", movie.getMovieID());
            MovieDoc removedMovieDoc = movieDocCollection.findOneAndDelete(filter);
            if (removedMovieDoc == null) {
                throw new MovieDocNotFoundException("Document for given movie object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID movieID) {
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson filter = Filters.eq("_id", movieID);
            movieDocCollection.findOneAndDelete(filter);
        } catch (MongoException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Movie movie) {
        try {
            movie.setMovieStatusActive(false);
            MovieDoc movieDoc = new MovieDoc(movie);
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson filter = Filters.eq("_id", movieDoc.getMovieID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("movie_title", movieDoc.getMovieTitle()),
                    Updates.set("movie_base_price", movieDoc.getMovieBasePrice()),
                    Updates.set("movie_status_active", movieDoc.isMovieStatusActive())
            );
            MovieDoc expiredMovieDoc = movieDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (expiredMovieDoc == null) {
                throw new MovieDocNotFoundException("Document for given movie object could not be expired, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public Movie findByUUID(UUID identifier) {
        Movie movie;
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson movieFilter = Filters.eq("_id", identifier);
            MovieDoc foundMovieDoc = movieDocCollection.find(movieFilter).first();
            if (foundMovieDoc != null) {
                ScreeningRoom screeningRoom = this.findScreeningRoom(foundMovieDoc.getScreeningRoomID());
                movie = this.getMovie(foundMovieDoc, screeningRoom);
            } else {
                throw new MovieDocNotFoundException("Document for movie object with given UUID could not be read, since it is not in the database.");
            }
        } catch (MongoException | NullPointerException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return movie;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> listOfAllMovies;
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson movieFilter = Filters.empty();
            listOfAllMovies = this.getMovies(movieDocCollection, movieFilter);
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllMovies;
    }

    @Override
    public List<Movie> findAllActive() {
        List<Movie> listOfAllActiveMovies;
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson movieFilter = Filters.eq("movie_status_active", true);
            listOfAllActiveMovies = getMovies(movieDocCollection, movieFilter);
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveMovies;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfUUIDs;
        try {
            MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
            Bson filter = Filters.empty();
            listOfUUIDs = new ArrayList<>();
            for (MovieDoc movieDoc : movieDocCollection.find(filter)) {
                listOfUUIDs.add(movieDoc.getMovieID());
            }
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfUUIDs;
    }
    
    private List<Movie> getMovies(MongoCollection<MovieDoc> movieDocCollection, Bson movieFilter) {
        List<Movie> listOfMovies = new ArrayList<>();
        FindIterable<MovieDoc> foundMovieDocs = movieDocCollection.find(movieFilter);
        try {
            for (MovieDoc movieDoc : foundMovieDocs) {
                logger.debug("MovieDod ID: " + movieDoc.getMovieID());
                logger.debug("GetMovies, movieDoc scrID: " + movieDoc.getScreeningRoomID());
                ScreeningRoom screeningRoom = this.findScreeningRoom(movieDoc.getScreeningRoomID());
                Movie movie = getMovie(movieDoc, screeningRoom);
                listOfMovies.add(movie);
            }
        } catch (ScreeningRoomDocNotFoundException exception) {
            throw new DocNotFoundException(exception.getMessage(), exception);
        }
        return listOfMovies;
    }

    private Movie getMovie(MovieDoc movieDoc, ScreeningRoom screeningRoom) {
        return new Movie(movieDoc.getMovieID(),
                movieDoc.getMovieTitle(),
                movieDoc.isMovieStatusActive(),
                movieDoc.getMovieBasePrice(),
                screeningRoom);
    }

    private ScreeningRoom findScreeningRoom(UUID screeningRoomUUID) throws ScreeningRoomDocNotFoundException {
        MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
        Bson filter = Filters.eq("_id", screeningRoomUUID);
        ScreeningRoomDoc foundScreeningRoomDoc = screeningRoomDocCollection.find(filter).first();
        ScreeningRoom screeningRoom;
        logger.debug("Screening room ID: " + screeningRoomUUID);
        if (foundScreeningRoomDoc != null) {
            screeningRoom = new ScreeningRoom(foundScreeningRoomDoc.getScreeningRoomID(),
                    foundScreeningRoomDoc.getScreeningRoomFloor(),
                    foundScreeningRoomDoc.getScreeningRoomNumber(),
                    foundScreeningRoomDoc.getNumberOfAvailableSeats(),
                    foundScreeningRoomDoc.isScreeningRoomStatusActive());
        } else {
            throw new ScreeningRoomDocNotFoundException("Document for screening room object with given UUID could not be found in the database.");
        }
        return screeningRoom;
    }
}