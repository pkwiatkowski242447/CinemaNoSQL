package model.model.ticket_types;

import model.model.Ticket;
import model.constants.TicketConstants;

import java.time.Instant;
import java.util.UUID;


public class Normal extends Ticket {

    // Constructors

    public Normal(UUID ticketID,
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
        this.ticketFinalPrice = this.getTicketBasePrice();
        this.ticketTypeDiscriminator = TicketConstants.NORMAL_TICKET;
    }

    public Normal(UUID ticketID,
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
