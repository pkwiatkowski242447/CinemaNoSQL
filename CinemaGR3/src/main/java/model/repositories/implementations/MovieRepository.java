package model.repositories.implementations;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import jakarta.validation.ConstraintViolation;
import model.constants.MovieConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.exceptions.repositories.object_not_found_exceptions.MovieObjectNotFoundException;
import model.model.Movie;
import model.exceptions.repositories.create_exceptions.MovieRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.MovieRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.exceptions.repositories.update_exceptions.MovieRepositoryUpdateException;
import model.exceptions.validation.MovieObjectNotValidException;
import model.repositories.interfaces.MovieRepositoryInterface;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
                                    "required": ["_id", "movie_title", "movie_base_price", "number_of_available_seats", "screening_room_number"],
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
                                        "movie_base_price": {
                                            "description": "Double value holding base price of the movie - that is before any discounts.",
                                            "bsonType": "double",
                                            "minimum": 0,
                                            "maximum": 100
                                        }
                                        "number_of_available_seats": {
                                            "description": "Integer indicating number of available seats inside screening room.",
                                            "bsonType": "int",
                                            "minimum": 0,
                                            "maximum": 150
                                        }
                                        "screening_room_number": {
                                             "description": "Integer indicating the number of the screening room.",
                                             "bsonType": "int",
                                             "minimum": 1,
                                             "maximum": 20
                                        }
                                    }
                                }
                            }
                            """));
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            mongoDatabase.createCollection(this.movieCollectionName, createCollectionOptions);
        }
    }

    // Create methods

    @Override
    public Movie create(String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) throws MovieRepositoryCreateException {
        Movie movie = new Movie(UUID.randomUUID(), movieTitle, movieBasePrice, numberOfAvailableSeats, screeningRoomNumber);
        try {
            this.checkIfMovieObjectIsValid(movie);

            this.getMovieCollection().insertOne(movie);

            Bson movieFilter = Filters.eq(MovieConstants.DOCUMENT_ID, movie.getMovieID());
            Movie foundMovie = this.getMovieCollection().find(movieFilter).first();

            if (foundMovie == null) {
                throw new MovieObjectNotFoundException("Client object with id: " + movie.getMovieID() + " could not be found in the database.");
            } else {
                return foundMovie;
            }
        } catch (MongoException | MovieObjectNotValidException | MovieObjectNotFoundException exception) {
            throw new MovieRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Movie findByUUID(UUID movieId) throws MovieRepositoryReadException {
        try {
            Bson movieFilter = Filters.eq(MovieConstants.DOCUMENT_ID, movieId);
            Movie foundMovie = this.getMovieCollection().find(movieFilter).first();

            if (foundMovie == null) {
                throw new MovieObjectNotFoundException("Client object with id: " + movieId + " could not be found in the database.");
            } else {
                return foundMovie;
            }
        } catch (MongoException | MovieObjectNotFoundException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Movie> findAll() throws MovieRepositoryReadException {
        List<Movie> listOfFoundMovies = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson movieFilter = Filters.empty();
            listOfFoundMovies.addAll(this.getMovieCollection().find(movieFilter).into(new ArrayList<>()));
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new MovieRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundMovies;
    }

    // Update methods

    @Override
    public void update(Movie movie) throws MovieRepositoryUpdateException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            this.checkIfMovieObjectIsValid(movie);

            clientSession.startTransaction();

            Bson movieFilter = Filters.eq(MovieConstants.DOCUMENT_ID, movie.getMovieID());
            Movie updatedMovie = this.getMovieCollection().findOneAndReplace(movieFilter, movie);

            if (updatedMovie == null) {
                throw new MovieObjectNotFoundException("Movie object with id: " + movie.getMovieID() + " could not be updated, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | MovieObjectNotValidException | MovieObjectNotFoundException exception) {
            throw new MovieRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Movie movie) throws MovieRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson movieFilter = Filters.eq(MovieConstants.DOCUMENT_ID, movie.getMovieID());
            Movie removedMovie = this.getMovieCollection().findOneAndDelete(movieFilter);

            if (removedMovie == null) {
                throw new MovieObjectNotFoundException("Movie object with id: " + movie.getMovieID() + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | MovieObjectNotFoundException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID movieID) throws MovieRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson movieFilter = Filters.eq(MovieConstants.DOCUMENT_ID, movieID);
            Movie removedMovie = this.getMovieCollection().findOneAndDelete(movieFilter);

            if (removedMovie == null) {
                throw new MovieObjectNotFoundException("Movie object with id: " + movieID + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | MovieObjectNotFoundException exception) {
            throw new MovieRepositoryDeleteException(exception.getMessage(), exception);
        }
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