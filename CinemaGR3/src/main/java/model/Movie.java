package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import jakarta.validation.constraints.*;
import model.constants.GeneralConstants;
import model.constants.MovieConstants;
import model.messages.MovieValidation;
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
    @NotBlank(message = MovieValidation.MOVIE_TITLE_BLANK)
    @Size(min = 1, message = MovieValidation.MOVIE_TITLE_TOO_SHORT)
    @Size(max = 150, message = MovieValidation.MOVIE_TITLE_TOO_LONG)
    private String movieTitle;

    @CqlName(value = MovieConstants.MOVIE_BASE_PRICE)
    @Min(value = 0, message = MovieValidation.MOVIE_BASE_PRICE_NEGATIVE)
    @Max(value = 100, message = MovieValidation.MOVIE_BASE_PRICE_TOO_HIGH)
    private double movieBasePrice;

    @CqlName(value = MovieConstants.NUMBER_OF_AVAILABLE_SEATS)
    @Min(value = 0, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_NEGATIVE)
    @Max(value = 150, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_TOO_HIGH)
    private int numberOfAvailableSeats;

    @CqlName(value = MovieConstants.SCREENING_ROOM_NUMBER)
    @Min(value = 1, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_LOW)
    @Max(value = 20, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_HIGH)
    private int screeningRoomNumber;

    // Constructors

    public Movie() {
    }

    public Movie(@NotNull(message = MovieValidation.MOVIE_ID_NULL) UUID movieID,
                 @NotBlank(message = MovieValidation.MOVIE_TITLE_BLANK)
                 @Size(min = 1, message = MovieValidation.MOVIE_TITLE_TOO_SHORT)
                 @Size(max = 150, message = MovieValidation.MOVIE_TITLE_TOO_LONG) String movieTitle,
                 @Min(value = 0, message = MovieValidation.MOVIE_BASE_PRICE_NEGATIVE)
                 @Max(value = 100, message = MovieValidation.MOVIE_BASE_PRICE_TOO_HIGH) double movieBasePrice,
                 @Min(value = 0, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_NEGATIVE)
                 @Max(value = 150, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_TOO_HIGH) int numberOfAvailableSeats,
                 @Min(value = 1, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_LOW)
                 @Max(value = 20, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_HIGH) int screeningRoomNumber) {
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

    public void setMovieID(@NotNull(message = MovieValidation.MOVIE_ID_NULL) UUID movieID) {
        this.movieID = movieID;
    }

    public void setMovieTitle(@NotBlank(message = MovieValidation.MOVIE_TITLE_BLANK)
                              @Size(min = 1, message = MovieValidation.MOVIE_TITLE_TOO_SHORT)
                              @Size(max = 150, message = MovieValidation.MOVIE_TITLE_TOO_LONG) String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieBasePrice(@Min(value = 0, message = MovieValidation.MOVIE_BASE_PRICE_NEGATIVE)
                                  @Max(value = 100, message = MovieValidation.MOVIE_BASE_PRICE_TOO_HIGH) double movieBasePrice) {
        this.movieBasePrice = movieBasePrice;
    }

    public void setNumberOfAvailableSeats(@Min(value = 0, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_NEGATIVE)
                                          @Max(value = 150, message = MovieValidation.NUMBER_OF_AVAILABLE_SEATS_TOO_HIGH) int numberOfAvailableSeats) {
        this.numberOfAvailableSeats = numberOfAvailableSeats;
    }

    public void setScreeningRoomNumber(@Min(value = 1, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_LOW)
                                       @Max(value = 20, message = MovieValidation.SCREENING_ROOM_NUMBER_TOO_HIGH) int screeningRoomNumber) {
        this.screeningRoomNumber = screeningRoomNumber;
    }

    // Other methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("movieID: ", movieID)
                .append("movieTitle: ", movieTitle)
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
                .append(numberOfAvailableSeats)
                .append(screeningRoomNumber)
                .toHashCode();
    }
}
