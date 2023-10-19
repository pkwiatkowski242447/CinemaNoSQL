package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "ticket_id", nullable = false, unique = true)
    private UUID ticketID;

    @Column(name = "movie_time", nullable = false)
    private Date movieTime;

    @Column(name = "reservation_time", nullable = false)
    private Date reservationTime;

    @Column(name = "ticket_status_active", nullable = false)
    private boolean ticketStatusActive;

    @Column(name = "ticket_final_price", nullable = false)
    private double ticketFinalPrice;

    @ManyToOne
    @NotNull
    private Movie movie;

    @ManyToOne
    @NotNull
    private Client client;

    @ManyToOne
    @NotNull
    private TypeOfTicket typeOfTicket;

    // Constructors

    public Ticket() {
    }

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
            switch (typeOfTicket) {
                case "reduced":
                    ticketType = new Reduced(UUID.randomUUID());
                    break;
                default:
                    ticketType = new Normal(UUID.randomUUID());
            }
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
