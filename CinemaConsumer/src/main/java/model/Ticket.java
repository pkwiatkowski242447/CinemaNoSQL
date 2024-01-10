package model;

import constants.TicketConstants;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;

@Data
public class Ticket {

    @BsonProperty(TicketConstants.DOCUMENT_ID)
    @JsonbProperty(TicketConstants.TICKET_ID)
    protected UUID ticketId;

    @BsonProperty(TicketConstants.MOVIE_TIME)
    @JsonbProperty(TicketConstants.MOVIE_TIME)
    protected Instant movieTime;

    @BsonProperty(TicketConstants.RESERVATION_TIME)
    @JsonbProperty(TicketConstants.RESERVATION_TIME)
    protected Instant reservationTime;

    @BsonProperty(TicketConstants.TICKET_BASE_PRICE)
    @JsonbProperty(TicketConstants.TICKET_BASE_PRICE)
    protected double ticketBasePrice;

    @BsonProperty(TicketConstants.TICKET_FINAL_PRICE)
    @JsonbProperty(TicketConstants.TICKET_FINAL_PRICE)
    protected double ticketFinalPrice;

    @BsonProperty(TicketConstants.MOVIE_ID)
    @JsonbProperty(TicketConstants.MOVIE_ID)
    protected UUID movieId;

    @BsonProperty(TicketConstants.CLIENT_ID)
    @JsonbProperty(TicketConstants.CLIENT_ID)
    protected UUID clientId;

    // Constructors

    @JsonbCreator
    @BsonCreator
    public Ticket(@JsonbProperty(TicketConstants.TICKET_ID)
                  @BsonProperty(TicketConstants.DOCUMENT_ID) UUID ticketId,
                  @JsonbProperty(TicketConstants.MOVIE_TIME)
                  @BsonProperty(TicketConstants.MOVIE_TIME) Instant movieTime,
                  @JsonbProperty(TicketConstants.RESERVATION_TIME)
                  @BsonProperty(TicketConstants.RESERVATION_TIME)Instant reservationTime,
                  @JsonbProperty(TicketConstants.TICKET_BASE_PRICE)
                  @BsonProperty(TicketConstants.TICKET_BASE_PRICE) double ticketBasePrice,
                  @JsonbProperty(TicketConstants.TICKET_FINAL_PRICE)
                  @BsonProperty(TicketConstants.TICKET_FINAL_PRICE) double ticketFinalPrice,
                  @JsonbProperty(TicketConstants.MOVIE_ID)
                  @BsonProperty(TicketConstants.MOVIE_ID) UUID movieId,
                  @JsonbProperty(TicketConstants.CLIENT_ID)
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
