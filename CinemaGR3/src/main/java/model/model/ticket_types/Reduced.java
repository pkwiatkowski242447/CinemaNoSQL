package model.model.ticket_types;

import model.model.Ticket;
import model.constants.TicketConstants;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;


@BsonDiscriminator(key = TicketConstants.TICKET_DISCRIMINATOR, value = TicketConstants.REDUCED_TICKET)
public class Reduced extends Ticket {

    // Constructors

    public Reduced(UUID ticketID,
                   Instant movieTime,
                   Instant reservationTime,
                   double ticketBasePrice,
                   UUID movieId,
                   UUID clientId) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.movieId = movieId;
        this.clientId = clientId;
        this.ticketFinalPrice = this.getTicketBasePrice() * 0.75;
    }

    @BsonCreator
    public Reduced(@BsonProperty(TicketConstants.DOCUMENT_ID) UUID ticketID,
                   @BsonProperty(TicketConstants.MOVIE_TIME) Instant movieTime,
                   @BsonProperty(TicketConstants.RESERVATION_TIME) Instant reservationTime,
                   @BsonProperty(TicketConstants.TICKET_BASE_PRICE) double ticketBasePrice,
                   @BsonProperty(TicketConstants.TICKET_FINAL_PRICE) double ticketFinalPrice,
                   @BsonProperty(TicketConstants.MOVIE_ID) UUID movieId,
                   @BsonProperty(TicketConstants.CLIENT_ID) UUID clientId) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.movieId = movieId;
        this.clientId = clientId;
        this.ticketFinalPrice = ticketFinalPrice;
    }
}
