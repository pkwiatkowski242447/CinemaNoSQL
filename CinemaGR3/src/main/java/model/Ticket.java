package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import lombok.AccessLevel;
import lombok.Setter;
import model.constants.GeneralConstants;
import model.constants.TicketConstants;
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
    private UUID ticketID;

    @CqlName(value = TicketConstants.MOVIE_TIME)
    @Setter(AccessLevel.NONE)
    private Instant movieTime;

    @CqlName(value = TicketConstants.RESERVATION_TIME)
    @Setter(AccessLevel.NONE)
    private Instant reservationTime;

    @CqlName(value = TicketConstants.TICKET_FINAL_PRICE)
    @Setter(AccessLevel.NONE)
    private double ticketFinalPrice;

    @CqlName(value = TicketConstants.MOVIE_ID)
    @Setter(AccessLevel.NONE)
    private UUID movieId;

    @CqlName(value = TicketConstants.CLIENT_ID)
    @Setter(AccessLevel.NONE)
    private UUID clientId;

    @CqlName(value = TicketConstants.TYPE_OF_TICKET_ID)
    private UUID typeOfTicketId;

    // Constructors

    public Ticket() {
    }

    public Ticket(UUID ticketID, Instant movieTime, Instant reservationTime, double ticketFinalPrice, UUID movieId, UUID clientId, UUID typeOfTicketId) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movieId = movieId;
        this.clientId = clientId;
        this.typeOfTicketId = typeOfTicketId;
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

    public double getTicketFinalPrice() {
        return ticketFinalPrice;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getTypeOfTicketId() {
        return typeOfTicketId;
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

    public void setTicketFinalPrice(double ticketFinalPrice) {
        this.ticketFinalPrice = ticketFinalPrice;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setTypeOfTicketId(UUID typeOfTicketId) {
        this.typeOfTicketId = typeOfTicketId;
    }

    // Other methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ticketID: ", ticketID)
                .append("movieTime: ", movieTime)
                .append("reservationTime: ", reservationTime)
                .append("ticketFinalPrice: ", ticketFinalPrice)
                .append("movieId: ", movieId)
                .append("clientId: ", clientId)
                .append("typeOfTicketId: ", typeOfTicketId)
                .toString();
    }

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
                .append(typeOfTicketId, ticket.typeOfTicketId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ticketID)
                .append(movieTime)
                .append(reservationTime)
                .append(ticketFinalPrice)
                .append(movieId)
                .append(clientId)
                .append(typeOfTicketId)
                .toHashCode();
    }
}
