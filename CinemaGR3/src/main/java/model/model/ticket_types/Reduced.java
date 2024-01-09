package model.model.ticket_types;

import model.model.Ticket;
import model.constants.TicketConstants;

import java.time.Instant;
import java.util.UUID;


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
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketBasePrice = ticketBasePrice;
        this.movieId = movieId;
        this.clientId = clientId;
        this.ticketFinalPrice = ticketFinalPrice;
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }
}
