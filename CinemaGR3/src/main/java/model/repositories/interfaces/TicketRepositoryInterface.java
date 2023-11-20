package model.repositories.interfaces;

import model.Client;
import model.Movie;
import model.Ticket;

import java.util.Date;

public interface TicketRepositoryInterface extends RepositoryInterface<Ticket> {

    Ticket create(Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket);
}
