package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import model.constants.GeneralConstants;
import model.constants.MovieConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = MovieConstants.MOVIES_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class Movie {

    @PartitionKey
    @CqlName(value = MovieConstants.MOVIE_ID)
    private UUID movieID;

    @CqlName(value = MovieConstants.MOVIE_TITLE)
    private String movieTitle;

    @CqlName(value = MovieConstants.MOVIE_BASE_PRICE)
    private double movieBasePrice;

    @CqlName(value = MovieConstants.NUMBER_OF_AVAILABLE_SEATS)
    private int numberOfAvailableSeats;

    @CqlName(value = MovieConstants.SCREENING_ROOM_NUMBER)
    private int screeningRoomNumber;

    // Constructors

    public Movie() {
    }

    public Movie(UUID movieID, String movieTitle, double movieBasePrice, int numberOfAvailableSeats, int screeningRoomNumber) {
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

    public void setMovieID(UUID movieID) {
        this.movieID = movieID;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("movieID: ", movieID)
                .append("movieTitle: ", movieTitle)
                .append("movieBasePrice: ", movieBasePrice)
                .append("numberOfAvailableSeats: ", numberOfAvailableSeats)
                .append("screeningRoomNumber: ", screeningRoomNumber)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return new EqualsBuilder()
                .append(movieBasePrice, movie.movieBasePrice)
                .append(numberOfAvailableSeats, movie.numberOfAvailableSeats)
                .append(screeningRoomNumber, movie.screeningRoomNumber)
                .append(movieID, movie.movieID)
                .append(movieTitle, movie.movieTitle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(movieID)
                .append(movieTitle)
                .append(movieBasePrice)
                .append(numberOfAvailableSeats)
                .append(screeningRoomNumber)
                .toHashCode();
    }
}
