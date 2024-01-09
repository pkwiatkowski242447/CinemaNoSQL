package model.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import model.messages.TicketValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Ticket {

    @NotNull(message = TicketValidation.TICKET_ID_NULL)
    protected UUID ticketID;

    @Setter
    @NotNull(message = TicketValidation.MOVIE_TIME_NULL)
    protected Instant movieTime;

    @NotNull(message = TicketValidation.RESERVATION_TIME_NULL)
    protected Instant reservationTime;

    @Setter
    @Min(value = 0, message = TicketValidation.TICKET_BASE_PRICE_NEGATIVE)
    @Max(value = 100, message = TicketValidation.TICKET_BASE_PRICE_TOO_HIGH)
    protected double ticketBasePrice;

    @PositiveOrZero(message = TicketValidation.TICKET_FINAL_PRICE_NEGATIVE)
    protected double ticketFinalPrice;

    @NotNull(message = TicketValidation.MOVIE_ID_NULL)
    protected UUID movieId;

    @NotNull(message = TicketValidation.CLIENT_ID_NULL)
    protected UUID clientId;

    @NotEmpty(message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_BLANK)
    @Pattern(regexp = TicketValidation.TICKET_TYPE_DISCRIMINATOR_REGEX_PATTERN, message = TicketValidation.TICKET_TYPE_DISCRIMINATOR_INCORRECT_VALUE)
    protected String ticketTypeDiscriminator;

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
                .append(ticketBasePrice, ticket.ticketBasePrice)
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
                .append(ticketBasePrice)
                .append(ticketFinalPrice)
                .append(movieId)
                .append(clientId)
                .append(ticketTypeDiscriminator)
                .toHashCode();
    }
}
