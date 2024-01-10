package model.model;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import model.constants.TicketConstants;
import model.messages.TicketValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;

@Getter
@BsonDiscriminator(key = TicketConstants.TICKET_DISCRIMINATOR)
public abstract class Ticket {

    @BsonProperty(TicketConstants.DOCUMENT_ID)
    @JsonbProperty(TicketConstants.TICKET_ID)
    @NotNull(message = TicketValidation.TICKET_ID_NULL)
    protected UUID ticketID;

    @BsonProperty(TicketConstants.MOVIE_TIME)
    @JsonbProperty(TicketConstants.MOVIE_TIME)
    @Setter
    @NotNull(message = TicketValidation.MOVIE_TIME_NULL)
    protected Instant movieTime;

    @BsonProperty(TicketConstants.RESERVATION_TIME)
    @JsonbProperty(TicketConstants.RESERVATION_TIME)
    @NotNull(message = TicketValidation.RESERVATION_TIME_NULL)
    protected Instant reservationTime;

    @BsonProperty(TicketConstants.TICKET_BASE_PRICE)
    @JsonbProperty(TicketConstants.TICKET_BASE_PRICE)
    @Setter
    @Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
    @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH)
    protected double ticketBasePrice;

    @BsonProperty(TicketConstants.TICKET_FINAL_PRICE)
    @JsonbProperty(TicketConstants.TICKET_FINAL_PRICE)
    @PositiveOrZero(message = TicketValidation.TICKET_FINAL_PRICE_NEGATIVE)
    protected double ticketFinalPrice;

    @BsonProperty(TicketConstants.MOVIE_ID)
    @JsonbProperty(TicketConstants.MOVIE_ID)
    @NotNull(message = TicketValidation.MOVIE_ID_NULL)
    protected UUID movieId;

    @BsonProperty(TicketConstants.CLIENT_ID)
    @JsonbProperty(TicketConstants.CLIENT_ID)
    @NotNull(message = TicketValidation.CLIENT_ID_NULL)
    protected UUID clientId;

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
                .toString();
    }

    // Equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return new EqualsBuilder()
                .append(ticketBasePrice, ticket.ticketBasePrice)
                .append(ticketFinalPrice, ticket.ticketFinalPrice)
                .append(ticketID, ticket.ticketID)
                .append(movieTime, ticket.movieTime)
                .append(reservationTime, ticket.reservationTime)
                .append(movieId, ticket.movieId)
                .append(clientId, ticket.clientId)
                .isEquals();
    }

    // HashCode method

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ticketID)
                .append(movieTime)
                .append(reservationTime)
                .append(ticketBasePrice)
                .append(ticketFinalPrice)
                .append(movieId)
                .append(clientId)
                .toHashCode();
    }
}
