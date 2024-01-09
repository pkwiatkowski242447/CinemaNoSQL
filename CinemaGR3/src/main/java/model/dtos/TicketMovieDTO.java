package model.dtos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import model.constants.GeneralConstants;
import model.constants.TicketConstants;

import java.time.Instant;
import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = TicketConstants.TICKETS_MOVIES_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class TicketMovieDTO {

    @PartitionKey
    @CqlName(value = TicketConstants.MOVIE_ID)
    private UUID movieId;

    @ClusteringColumn
    @CqlName(value = TicketConstants.TICKET_ID)
    private UUID ticketID;

    @CqlName(value = TicketConstants.MOVIE_TIME)
    private Instant movieTime;

    @CqlName(value = TicketConstants.RESERVATION_TIME)
    private Instant reservationTime;

    @CqlName(value = TicketConstants.TICKET_BASE_PRICE)
    private double ticketBasePrice;

    @CqlName(value = TicketConstants.TICKET_FINAL_PRICE)
    protected double ticketFinalPrice;

    @CqlName(value = TicketConstants.CLIENT_ID)
    private UUID clientId;

    @CqlName(value = TicketConstants.TICKET_TYPE_DISCRIMINATOR)
    protected String ticketTypeDiscriminator;

    // Constructors

    public TicketMovieDTO() {
    }

    public TicketMovieDTO(UUID movieId,
                          UUID ticketID,
                          Instant movieTime,
                          Instant reservationTime,
                          double ticketBasePrice,
                          double ticketFinalPrice,
                          UUID clientId,
                          String ticketTypeDiscriminator) {
        this.movieId = movieId;
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.ticketFinalPrice = ticketFinalPrice;
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

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setTicketTypeDiscriminator(String ticketTypeDiscriminator) {
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }
}
