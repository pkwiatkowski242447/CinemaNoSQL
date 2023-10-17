package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import model.ticket_types.TypeOfTicket;

import java.util.Date;
import java.util.UUID;

@Entity
public class Ticket {

    @Id
    @Column(nullable = false, unique = true)
    private UUID ticketID;

    @Column(nullable = false)
    private Date movieTime;

    @Column(nullable = false)
    private Date reservationTime;

    @Column(nullable = false)
    private boolean ticketStatusActive;

    @Column(nullable = false)
    private double ticketFinalPrice;

    @ManyToOne
    @NotNull
    private Movie movie;

    @ManyToOne
    @NotNull
    private Client client;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private TypeOfTicket typeOfTicket;

    // Constructors

    public Ticket() {
    }

    public Ticket(UUID ticketID, Date movieTime, Date reservationTime, Movie movie, Client client, TypeOfTicket typeOfTicket) {
        this.ticketID = ticketID;
        this.movieTime = movieTime;
        this.reservationTime = reservationTime;
        this.movie = movie;
        this.client = client;
        this.typeOfTicket = typeOfTicket;
        this.ticketFinalPrice = typeOfTicket.applyDiscount();
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

    public void setTicketStatusActive(boolean ticketStatusActive) {
        this.ticketStatusActive = ticketStatusActive;
    }

    // Other methods

    public String getTicketInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bilet: ")
                .append(this.ticketID)
                .append(" Czas złożenia rezerwacji: ")
                .append(this.reservationTime.toString())
                .append(" Czas seansu: ")
                .append(this.movieTime.toString());
        if (this.ticketStatusActive) {
            stringBuilder.append(" Staus rezerwacji: aktywna");
        } else {
            stringBuilder.append(" Staus rezerwacji: nieaktywna");
        }
        stringBuilder.append(" Cena końcowa: ").append(this.ticketFinalPrice);
        stringBuilder.append(this.typeOfTicket.getTicketTypeInfo());
        return stringBuilder.toString();
    }
}
