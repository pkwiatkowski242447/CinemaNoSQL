package model;

import model.exceptions.model_exceptions.TicketReservationException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

public class Ticket {

    private final UUID ticketID;
    private final Date movieTime;
    private final Date reservationTime;
    private boolean ticketStatusActive;
    private final double ticketFinalPrice;

    private final Movie movie;
    private final Client client;
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

    public Ticket(UUID ticketID, Date movieTime, Date reservationTime, boolean ticketStatusActive, double ticketFinalPrice, Movie movie, Client client, TypeOfTicket typeOfTicket) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.ticketStatusActive = ticketStatusActive;
        this.ticketFinalPrice = ticketFinalPrice;
        this.movie = movie;
        this.client = client;
        this.typeOfTicket = typeOfTicket;
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

    public void setTicketStatusActive(boolean ticketStatusActive) {
        this.ticketStatusActive = ticketStatusActive;
    }

    public void setTypeOfTicket(TypeOfTicket typeOfTicket) {
        this.typeOfTicket = typeOfTicket;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return new EqualsBuilder()
                .append(ticketID, ticket.ticketID)
                .append(movieTime, ticket.movieTime)
                .append(reservationTime, ticket.reservationTime)
                .append(ticketStatusActive, ticket.ticketStatusActive)
                .append(ticketFinalPrice, ticket.ticketFinalPrice)
                .append(movie.getMovieID(), ticket.movie.getMovieID())
                .append(movie.getScreeningRoom().getNumberOfAvailableSeats(), ticket.movie.getScreeningRoom().getNumberOfAvailableSeats())
                .append(client, ticket.client)
                .append(typeOfTicket, ticket.typeOfTicket)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ticketID)
                .append(movieTime)
                .append(reservationTime)
                .append(ticketStatusActive)
                .append(ticketFinalPrice)
                .append(movie)
                .append(client)
                .append(typeOfTicket.getClass())
                .toHashCode();
    }
}
