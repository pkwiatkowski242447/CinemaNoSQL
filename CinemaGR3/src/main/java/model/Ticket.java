package model;

import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

public class Ticket {

    @BsonCreator
    public Ticket(@BsonProperty("_id") UUID ticketID,
                  @BsonProperty("movie_time") Date movieTime,
                  @BsonProperty("reservation_time") Date reservationTime,
                  @BsonProperty("ticket_status_active") boolean ticketStatusActive,
                  @BsonProperty("ticket_final_price") double ticketFinalPrice,
                  @BsonProperty("movie_ref") Movie movie,
                  @BsonProperty("client_ref") Client client,
                  @BsonProperty("type_of_ticket_ref") TypeOfTicket typeOfTicket) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketStatusActive = ticketStatusActive;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movie = movie;
        this.client = client;
        this.typeOfTicket = typeOfTicket;
    }

    @BsonProperty("_id")
    private final UUID ticketID;

    @BsonProperty("movie_time")
    private final Date movieTime;

    @BsonProperty("reservation_time")
    private final Date reservationTime;

    @BsonProperty("ticket_status_active")
    private boolean ticketStatusActive;

    @BsonProperty("ticket_final_price")
    private final double ticketFinalPrice;

    @BsonProperty("movie_ref")
    private final Movie movie;

    @BsonProperty("client_ref")
    private final Client client;

    @BsonProperty("ticket_type_ref")
    private TypeOfTicket typeOfTicket;

    // Constructors

    public Ticket(UUID ticketID, Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket) throws TicketReservationException, NullPointerException{
        this.movie = movie;
        try {
            if (movie.getScreeningRoom().getNumberOfAvailableSeats() > 0) {
                movie.getScreeningRoom().setNumberOfAvailableSeats(movie.getScreeningRoom().getNumberOfAvailableSeats() - 1);
            } else {
                throw new TicketReservationException("Cannot create a new ticket - there are no available seats.");
            }
        } catch (NullPointerException exception) {
            throw new TicketReservationException("Reference to movie object is null.");
        }
        this.client = client;
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;

        TypeOfTicket ticketType;
        try {
            ticketType = switch (typeOfTicket) {
                case "reduced" -> new Reduced(UUID.randomUUID());
                default -> new Normal(UUID.randomUUID());
            };
        } catch (NullPointerException exception) {
            throw new TicketReservationException("Ticket type was not entered correctly.");
        }
        this.typeOfTicket = ticketType;
        this.ticketFinalPrice = ticketType.applyDiscount(movie.getMovieBasePrice());
        this.ticketStatusActive = true;
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

    public Movie getMovie() {
        return movie;
    }

    public Client getClient() {
        return client;
    }

    public TypeOfTicket getTicketType() {
        return typeOfTicket;
    }

    // Setters


    public void setTypeOfTicket(TypeOfTicket typeOfTicket) {
        this.typeOfTicket = typeOfTicket;
    }

    public void setTicketStatusActive(boolean ticketStatusActive) {
        this.ticketStatusActive = ticketStatusActive;
    }

    // Other methods

    public String getTicketInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ticket identifier: ")
                .append(this.ticketID)
                .append(" Date of making reservation: ")
                .append(this.reservationTime.toString())
                .append(" Date of the movie: ")
                .append(this.movieTime.toString());
        if (this.ticketStatusActive) {
            stringBuilder.append(" Reservation status: active");
        } else {
            stringBuilder.append(" Reservation status: not active");
        }
        stringBuilder.append(" Final price of the ticket: ").append(this.ticketFinalPrice);
        stringBuilder.append(this.typeOfTicket.getTicketTypeInfo());
        return stringBuilder.toString();
    }
}
