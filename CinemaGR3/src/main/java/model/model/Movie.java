package model.model;

import jakarta.validation.constraints.*;
import model.constants.MovieConstants;
import model.messages.MovieValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Movie {

    @BsonProperty(MovieConstants.DOCUMENT_ID)
    private final UUID movieID;

    @BsonProperty(MovieConstants.MOVIE_TITLE)
    @NotBlank(message = MovieValidation.MOVIE_TITLE_BLANK)
    @Size(min = 1, message = MovieValidation.MOVIE_TITLE_TOO_SHORT)
    @Size(max = 150, message = MovieValidation.MOVIE_TITLE_TOO_LONG)
    private String movieTitle;

    @BsonProperty(MovieConstants.MOVIE_BASE_PRICE)
    @Min(value = 0, message = MovieValidation.MOVIE_BASE_PRICE_NEGATIVE)
    @Max(value = 100, message = MovieValidation.MOVIE_BASE_PRICE_TOO_HIGH)
    private double movieBasePrice;

    @BsonProperty(MovieConstants.NUMBER_OF_AVAILABLE_SEATS)
    @Min(value = 0, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_NEGATIVE)
    @Max(value = 150, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_TOO_HIGH)
    private int numberOfAvailableSeats;

    @BsonProperty(MovieConstants.SCREENING_ROOM_NUMBER)
    @Min(value = 1, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_LOW)
    @Max(value = 20, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_HIGH)
    private int screeningRoomNumber;

    // Constructors

    @BsonCreator
    public Movie(@BsonProperty(MovieConstants.DOCUMENT_ID) UUID movieID,
                 @BsonProperty(MovieConstants.MOVIE_TITLE) String movieTitle,
                 @BsonProperty(MovieConstants.MOVIE_BASE_PRICE) double movieBasePrice,
                 @BsonProperty(MovieConstants.NUMBER_OF_AVAILABLE_SEATS) int numberOfAvailableSeats,
                 @BsonProperty(MovieConstants.SCREENING_ROOM_NUMBER) int screeningRoomNumber) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieBasePrice = movieBasePrice;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.screeningRoomNumber = screeningRoomNumber;
    }

    // Getters

    public UUID getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public double getMovieBasePrice() {
        return movieBasePrice;
    }

    public int getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }

    public int getScreeningRoomNumber() {
        return screeningRoomNumber;
    }

    // Setters

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieBasePrice(double movieBasePrice) {
        this.movieBasePrice = movieBasePrice;
    }

    public void setNumberOfAvailableSeats(int numberOfAvailableSeats) {
        this.numberOfAvailableSeats = numberOfAvailableSeats;
    }

    public void setScreeningRoomNumber(int screeningRoomNumber) {
        this.screeningRoomNumber = screeningRoomNumber;
    }

    // Other methods

    // To String method

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("movieID: ", movieID)
                .append("movieTitle: ", movieTitle)
                .append("numberOfAvailableSeats: ", numberOfAvailableSeats)
                .append("screeningRoomNumber: ", screeningRoomNumber)
                .toString();
    }

    // Equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return new EqualsBuilder()
                .append(numberOfAvailableSeats, movie.numberOfAvailableSeats)
                .append(screeningRoomNumber, movie.screeningRoomNumber)
                .append(movieID, movie.movieID)
                .append(movieTitle, movie.movieTitle)
                .isEquals();
    }

    // Hash Code method

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(movieID)
                .append(movieTitle)
                .append(numberOfAvailableSeats)
                .append(screeningRoomNumber)
                .toHashCode();
    }
}
