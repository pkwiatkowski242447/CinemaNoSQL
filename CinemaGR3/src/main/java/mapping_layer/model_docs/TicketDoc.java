package mapping_layer.model_docs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class TicketDoc {

    @BsonProperty("_id")
    private UUID ticketID;

    @BsonProperty("movie_time")
    private Date movieTime;

    @BsonProperty("reservation_time")
    private Date reservationTime;

    @BsonProperty("ticket_status_active")
    private boolean ticketStatusActive;

    @BsonProperty("ticket_final_price")
    private double ticketFinalPrice;

    @BsonProperty("movie_ref")
    private UUID movieID;

    @BsonProperty("client_ref")
    private UUID clientID;

    @BsonProperty("type_of_ticket_ref")
    private UUID typeOfTicketID;

    // Constructor

    @BsonCreator
    public TicketDoc(@BsonProperty("_id") UUID ticketID,
                     @BsonProperty("movie_time") Date movieTime,
                     @BsonProperty("reservation_time") Date reservationTime,
                     @BsonProperty("ticket_status_active") boolean ticketStatusActive,
                     @BsonProperty("ticket_final_price") double ticketFinalPrice,
                     @BsonProperty("movie_ref") UUID movieID,
                     @BsonProperty("client_ref") UUID clientID,
                     @BsonProperty("type_of_ticket_ref") UUID typeOfTicketID) {
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
