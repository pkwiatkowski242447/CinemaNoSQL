package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import jakarta.validation.constraints.*;
import model.constants.GeneralConstants;
import model.constants.TicketConstants;
import model.messages.TicketValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = TicketConstants.TICKETS_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class Ticket {

    @PartitionKey
    @CqlName(value = TicketConstants.TICKET_ID)
    @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.TICKET_ID_NOT_UUID)
    private UUID ticketID;

    @CqlName(value = TicketConstants.MOVIE_TIME)
    private Instant movieTime;

    @CqlName(value = TicketConstants.RESERVATION_TIME)
    private Instant reservationTime;

    @CqlName(value = TicketConstants.TICKET_BASE_PRICE)
    @Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
    @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH)
    private double ticketBasePrice;

    @CqlName(value = TicketConstants.TICKET_FINAL_PRICE)
    protected double ticketFinalPrice;

    @CqlName(value = TicketConstants.MOVIE_ID)
    @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.MOVIE_ID_NOT_UUID)
    private UUID movieId;

    @CqlName(value = TicketConstants.CLIENT_ID)
    @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.CLIENT_ID_NOT_UUID)
    private UUID clientId;

    @CqlName(value = TicketConstants.TICKET_TYPE_DISCRIMINATOR)
    @Pattern(regexp = TicketValidation.TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN, message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE)
    protected String ticketTypeDiscriminator;

    // Constructors

    public Ticket() {
    }

    public Ticket(@NotNull(message = TicketValidation.TICKET_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.TICKET_ID_NOT_UUID) UUID ticketID,
                  @NotNull(message = TicketValidation.MOVIE_TIME_NULL) Instant movieTime,
                  @NotNull(message = TicketValidation.RESERVATION_TIME_NULL) Instant reservationTime,
                  @Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
                  @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH) double ticketBasePrice,
                  @NotNull(message = TicketValidation.MOVIE_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.MOVIE_ID_NOT_UUID) UUID movieId,
                  @NotNull(message = TicketValidation.CLIENT_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.CLIENT_ID_NOT_UUID) UUID clientId) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.movieId = movieId;
        this.clientId = clientId;
    }

    public Ticket(@NotNull(message = TicketValidation.TICKET_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.TICKET_ID_NOT_UUID) UUID ticketID,
                  @NotNull(message = TicketValidation.MOVIE_TIME_NULL) Instant movieTime,
                  @NotNull(message = TicketValidation.RESERVATION_TIME_NULL) Instant reservationTime,
                  @Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
                  @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH) double ticketBasePrice,
                  @PositiveOrZero(message = TicketValidation.TICKET_FINAL_PRICE_NEGATIVE) double ticketFinalPrice,
                  @NotNull(message = TicketValidation.MOVIE_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.MOVIE_ID_NOT_UUID) UUID movieId,
                  @NotNull(message = TicketValidation.CLIENT_ID_NULL)
                  @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.CLIENT_ID_NOT_UUID) UUID clientId,
                  @NotBlank(message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_BLANK)
                  @Pattern(regexp = TicketValidation.TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN, message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE) String ticketTypeDiscriminator) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movieId = movieId;
        this.clientId = clientId;
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }

    // Getters

    public UUID getTicketID() {
        return ticketID;
    }

    public Instant getMovieTime() {
        return movieTime;
    }

    public Instant getReservationTime() {
        return reservationTime;
    }

    public double getTicketBasePrice() {
        return ticketBasePrice;
    }

    public double getTicketFinalPrice() {
        return ticketFinalPrice;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getTicketTypeDiscriminator() {
        return ticketTypeDiscriminator;
    }

    // Setters

    public void setTicketID(@NotNull(message = TicketValidation.TICKET_ID_NULL)
                            @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.TICKET_ID_NOT_UUID) UUID ticketID) {
        this.ticketID = ticketID;
    }

    public void setMovieTime(@NotNull(message = TicketValidation.MOVIE_TIME_NULL) Instant movieTime) {
        this.movieTime = movieTime;
    }

    public void setReservationTime(@NotNull(message = TicketValidation.RESERVATION_TIME_NULL) Instant reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setTicketBasePrice(@Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
                                   @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH) double ticketBasePrice) {
        this.ticketBasePrice = ticketBasePrice;
    }

    public void setTicketFinalPrice(@PositiveOrZero(message = TicketValidation.TICKET_FINAL_PRICE_NEGATIVE) double ticketFinalPrice) {
        this.ticketFinalPrice = ticketFinalPrice;
    }

    public void setMovieId(@NotNull(message = TicketValidation.MOVIE_ID_NULL)
                           @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.MOVIE_ID_NOT_UUID) UUID movieId) {
        this.movieId = movieId;
    }

    public void setClientId(@NotNull(message = TicketValidation.CLIENT_ID_NULL)
                            @Pattern(regexp = TicketValidation.UUID_REGEX_PATTERN, message = TicketValidation.CLIENT_ID_NOT_UUID) UUID clientId) {
        this.clientId = clientId;
    }

    public void setTicketTypeDiscriminator(@NotBlank(message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_BLANK)
                                           @Pattern(regexp = TicketValidation.TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN, message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE) String ticketTypeDiscriminator) {
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }

    // Other methods

    // ToString method

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ticketID: ", ticketID)
                .append("movieTime: ", movieTime)
                .append("reservationTime: ", reservationTime)
                .append("ticketFinalPrice: ", ticketFinalPrice)
                .append("movieId: ", movieId)
                .append("clientId: ", clientId)
                .append("ticketTypeDiscriminator: ", ticketTypeDiscriminator)
                .toString();
    }

    // Equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return new EqualsBuilder()
                .append(ticketFinalPrice, ticket.ticketFinalPrice)
                .append(ticketID, ticket.ticketID)
                .append(movieTime, ticket.movieTime)
                .append(reservationTime, ticket.reservationTime)
                .append(movieId, ticket.movieId)
                .append(clientId, ticket.clientId)
                .append(ticketTypeDiscriminator, ticket.ticketTypeDiscriminator)
                .isEquals();
    }

    // HashCode method

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ticketID)
                .append(movieTime)
                .append(reservationTime)
                .append(ticketFinalPrice)
                .append(movieId)
                .append(clientId)
                .append(ticketTypeDiscriminator)
                .toHashCode();
    }
}
