package model.repositories.interfaces;

import model.Ticket;
import model.exceptions.create_exceptions.TicketRepositoryCreateException;

import java.time.Instant;
import java.util.UUID;

public interface TicketRepositoryInterface extends RepositoryInterface<Ticket> {

    // C - create method for ticket repository interface

    Ticket createNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException;
    Ticket createReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException;
}
