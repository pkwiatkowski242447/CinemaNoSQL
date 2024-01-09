package model.dtos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import model.constants.GeneralConstants;
import model.constants.TicketConstants;
import model.messages.TicketValidation;

import java.time.Instant;
import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class TicketClientDTO {

    @PartitionKey
    @CqlName(value = TicketConstants.CLIENT_ID)
    private UUID clientId;

    @ClusteringColumn
    @CqlName(value = TicketConstants.TICKET_ID)
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
    private UUID movieId;

    @CqlName(value = TicketConstants.TICKET_TYPE_DISCRIMINATOR)
    protected String ticketTypeDiscriminator;

    // Constructors

    public TicketClientDTO() {
    }

    public TicketClientDTO(UUID clientId,
                           UUID ticketID,
                           Instant movieTime,
                           Instant reservationTime,
                           double ticketBasePrice,
                           double ticketFinalPrice,
                           UUID movieId,
                           String ticketTypeDiscriminator) {
        this.clientId = clientId;
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movieId = movieId;
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }

    // Getters

    public UUID getClientId() {
        return clientId;
    }

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

    public String getTicketTypeDiscriminator() {
        return ticketTypeDiscriminator;
    }

    // Setters

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setTicketID(UUID ticketID) {
        this.ticketID = ticketID;
    }

    public void setMovieTime(Instant movieTime) {
        this.movieTime = movieTime;
    }

    public void setReservationTime(Instant reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setTicketBasePrice(double ticketBasePrice) {
        this.ticketBasePrice = ticketBasePrice;
    }

    public void setTicketFinalPrice(double ticketFinalPrice) {
        this.ticketFinalPrice = ticketFinalPrice;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public void setTicketTypeDiscriminator(String ticketTypeDiscriminator) {
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }
}
