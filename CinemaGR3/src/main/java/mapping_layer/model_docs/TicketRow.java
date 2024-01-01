package mapping_layer.model_docs;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.NamingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@Entity(defaultKeyspace = "cinema")
@CqlName("tickets")
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class TicketRow {

    @CqlName("ticket_id")
    private UUID ticketID;

    @CqlName("movie_time")
    private Date movieTime;

    @CqlName("reservation_time")
    private Date reservationTime;

    @CqlName("ticket_status_active")
    private boolean ticketStatusActive;

    @CqlName("ticket_final_price")
    private double ticketFinalPrice;

    @CqlName("movie_id")
    private UUID movieID;

    @CqlName("client_id")
    private UUID clientID;

    @CqlName("type_of_ticket_id")
    private UUID typeOfTicketID;

    // Constructor

    public TicketRow(UUID ticketID,
                     Date movieTime,
                     Date reservationTime,
                     boolean ticketStatusActive,
                     double ticketFinalPrice,
                     UUID movieID,
                     UUID clientID,
                     UUID typeOfTicketID) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketStatusActive = ticketStatusActive;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movieID = movieID;
        this.clientID = clientID;
        this.typeOfTicketID = typeOfTicketID;
    }
}
