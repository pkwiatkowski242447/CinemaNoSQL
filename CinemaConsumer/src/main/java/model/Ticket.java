package model;

import constants.TicketConstants;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;

@Data
public class Ticket {

    @BsonProperty(TicketConstants.DOCUMENT_ID)
    private final UUID ticketId;

    @BsonProperty(TicketConstants.MOVIE_TIME)
    private final Instant movieTime;

    @BsonProperty(TicketConstants.RESERVATION_TIME)
    private final Instant reservationTime;

    @BsonProperty(TicketConstants.TICKET_BASE_PRICE)
    private final double ticketBasePrice;

    @BsonProperty(TicketConstants.TICKET_FINAL_PRICE)
    private final double ticketFinalPrice;

    @BsonProperty(TicketConstants.MOVIE_ID)
    private final UUID movieId;

    @BsonProperty(TicketConstants.CLIENT_ID)
    private final UUID clientId;

    @BsonCreator
    public Ticket(@BsonProperty(TicketConstants.DOCUMENT_ID) UUID ticketId,
                  @BsonProperty(TicketConstants.MOVIE_TIME) Instant movieTime,
                  @BsonProperty(TicketConstants.RESERVATION_TIME) Instant reservationTime,
                  @BsonProperty(TicketConstants.TICKET_BASE_PRICE) double ticketBasePrice,
                  @BsonProperty(TicketConstants.TICKET_FINAL_PRICE) double ticketFinalPrice,
                  @BsonProperty(TicketConstants.MOVIE_ID) UUID movieId,
                  @BsonProperty(TicketConstants.CLIENT_ID) UUID clientId) {
        this.ticketId = ticketId;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movieId = movieId;
        this.clientId = clientId;
    }
}
