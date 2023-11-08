package mapping_layer.model_docs;

import model.Ticket;
import model.exceptions.model_docs_exceptions.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

public class TicketDoc {

    @BsonProperty("_id")
    private final UUID ticketID;

    @BsonProperty("movie_time")
    private final Date movieTime;

    @BsonProperty("reservation_time")
    private final Date reservationTime;

    @BsonProperty("ticket_status_active")
    private final boolean ticketStatusActive;

    @BsonProperty("ticket_final_price")
    private final double ticketFinalPrice;

    @BsonProperty("movie_ref")
    private final UUID movieID;

    @BsonProperty("client_ref")
    private final UUID clientID;

    @BsonProperty("type_of_ticket_ref")
    private final UUID typeOfTicketID;

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

    public TicketDoc(Ticket ticket) {
        this.ticketID = ticket.getTicketID();
        if (ticket.getMovieTime() != null) {
            this.movieTime = ticket.getMovieTime();
        } else {
            throw new NullMovieTimeException("Reference to movie time date object is null");
        }
        if (ticket.getReservationTime() != null) {
            this.reservationTime = ticket.getReservationTime();
        } else {
            throw new NullReservationTimeException("Reference to reservation date object is null");
        }
        this.ticketStatusActive = ticket.isTicketStatusActive();
        this.ticketFinalPrice = ticket.getTicketFinalPrice();
        if (ticket.getClient() != null) {
            this.clientID = ticket.getClient().getClientID();
        } else {
            throw new ClientNullException("Reference to client object is null.");
        }
        if (ticket.getMovie() != null) {
            this.movieID = ticket.getMovie().getMovieID();
        } else {
            throw new MovieNullException("Reference to movie object is null.");
        }
        if (ticket.getTicketType() != null) {
            this.typeOfTicketID = ticket.getTicketType().getTicketTypeID();
        } else {
            throw new TypeOfTicketNullException("Reference to type of ticket object is null.");
        }
    }

    // Getters

    public UUID getTicketID() {
        return ticketID;
    }

    public Date getMovieTime() {
        return movieTime;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public boolean isTicketStatusActive() {
        return ticketStatusActive;
    }

    public double getTicketFinalPrice() {
        return ticketFinalPrice;
    }

    public UUID getMovieID() {
        return movieID;
    }

    public UUID getClientID() {
        return clientID;
    }

    public UUID getTypeOfTicketID() {
        return typeOfTicketID;
    }
}
