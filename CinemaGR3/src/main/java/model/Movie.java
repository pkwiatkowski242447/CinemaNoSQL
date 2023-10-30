package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Movie {

    @BsonCreator
    public Movie(@BsonProperty("_id") UUID movieID,
                 @BsonProperty("movie_title") String movieTitle,
                 @BsonProperty("movie_status_active") boolean movieStatusActive,
                 @BsonProperty("movie_base_price") double movieBasePrice) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieStatusActive = movieStatusActive;
        this.movieBasePrice = movieBasePrice;
    }

    @BsonProperty("_id")
    private final UUID movieID;

    @BsonProperty("movie_title")
    private String movieTitle;

    @BsonProperty("movie_status_active")
    private boolean movieStatusActive;

    @BsonProperty("movie_base_price")
    private double movieBasePrice;

    @BsonProperty("screening_room_ref")
    private ScreeningRoom screeningRoom;

    // Constructors

    public Movie(UUID movieID, String movieTitle, double movieBasePrice, ScreeningRoom screeningRoom) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieBasePrice = movieBasePrice;
        this.movieStatusActive = true;
        this.screeningRoom = screeningRoom;
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

    public ScreeningRoom getScreeningRoom() {
        return screeningRoom;
    }

    public double getMovieBasePrice() {
        return movieBasePrice;
    }

    // Setters

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieBasePrice(double movieBasePrice) {
        this.movieBasePrice = movieBasePrice;
    }

    public void setMovieStatusActive(boolean movieStatusActive) {
        this.movieStatusActive = movieStatusActive;
    }

    // Other methods

    public String getMovieInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Movie title: ")
                .append(this.movieTitle)
                .append(", ")
                .append(" location of the spectacle: ")
                .append(this.screeningRoom.getScreeningRoomInfo());
        return stringBuilder.toString();
    }
}
