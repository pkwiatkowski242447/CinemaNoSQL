package model.repositories.interfaces;

import model.Client;
import model.Movie;
import model.Ticket;

import java.time.Instant;

public interface TicketRepositoryInterface extends RepositoryInterface<Ticket> {

    Ticket create(Instant movieTime, Instant reservationTime, Movie movie, Client client, String typeOfTicket);
}
