package model.ticket_types;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.NamingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import model.Ticket;
import model.constants.GeneralConstants;
import model.constants.TicketConstants;

import java.time.Instant;
import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = TicketConstants.TICKETS_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class Reduced extends Ticket {

    // Constructors

    public Reduced() {
    }

    public Reduced(UUID ticketID, Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) {
        super(ticketID, movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        this.ticketFinalPrice = this.getTicketBasePrice() * 0.75;
        this.ticketTypeDiscriminator = TicketConstants.REDUCED_TICKET;
    }

    public Reduced(UUID ticketID,
                   Instant movieTime,
                   Instant reservationTime,
                   double ticketBasePrice,
                   double ticketFinalPrice,
                   UUID movieId,
                   UUID clientId,
                   String ticketTypeDiscriminator) {
        super(ticketID, movieTime, reservationTime, ticketBasePrice, ticketFinalPrice, movieId, clientId, ticketTypeDiscriminator);
    }
}
