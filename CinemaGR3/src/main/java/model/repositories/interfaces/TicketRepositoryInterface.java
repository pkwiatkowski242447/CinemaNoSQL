package model.repositories.interfaces;

import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.model.Ticket;
import model.exceptions.repositories.create_exceptions.TicketRepositoryCreateException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TicketRepositoryInterface extends RepositoryInterface<Ticket> {

    // C - create method for ticket repository interface

    Ticket createNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException;
    Ticket createReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException;

    // R - read methods for ticket repository interface

    List<Ticket> findAllTicketsForAGivenClientId(UUID clientId) throws TicketRepositoryReadException;
    List<Ticket> findAllTicketsForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException;
}