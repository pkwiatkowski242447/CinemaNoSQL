package mapping_layer.model_docs;

import model.Movie;
import model.exceptions.model_docs_exceptions.ScreeningRoomNullException;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class MovieDoc {

    @BsonProperty("_id")
    private final UUID movieID;

    @BsonProperty("movie_title")
    private final String movieTitle;

    @BsonProperty("movie_status_active")
    private final boolean movieStatusActive;

    @BsonProperty("movie_base_price")
    private final double movieBasePrice;

    @BsonProperty("screening_room_ref")
    private final UUID screeningRoomID;

    // Constructor

    @BsonCreator
    public MovieDoc(@BsonProperty("_id") UUID movieID,
                    @BsonProperty("movie_title") String movieTitle,
                    @BsonProperty("movie_status_active") boolean movieStatusActive,
                    @BsonProperty("movie_base_price") double movieBasePrice,
                    @BsonProperty("screening_room_ref") UUID screeningRoomID) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieStatusActive = movieStatusActive;
        this.movieBasePrice = movieBasePrice;
        this.screeningRoomID = screeningRoomID;
    }

    public MovieDoc(Movie movie) {
        this.movieID = movie.getMovieID();
        this.movieTitle = movie.getMovieTitle();
        this.movieBasePrice = movie.getMovieBasePrice();
        this.movieStatusActive = movie.isMovieStatusActive();
        if (movie.getScreeningRoom() != null) {
            this.screeningRoomID = movie.getScreeningRoom().getScreeningRoomID();
        } else {
            throw new ScreeningRoomNullException("Reference to the screening room object is null.");
        }
    }

    // Getters

    public UUID getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public boolean isMovieStatusActive() {
        return movieStatusActive;
    }

    public double getMovieBasePrice() {
        return movieBasePrice;
    }

    public UUID getScreeningRoomID() {
        return screeningRoomID;
    }
}
