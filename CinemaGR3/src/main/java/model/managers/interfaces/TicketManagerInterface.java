package model.managers.interfaces;

import model.exceptions.managers.create_exceptions.TicketManagerCreateException;
import model.exceptions.managers.read_exceptions.TicketManagerReadException;
import model.exceptions.managers.update_exceptions.TicketManagerUpdateException;
import model.model.Ticket;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TicketManagerInterface extends ManagerInterface<Ticket> {

    // C - create methods for ticket manager interface

    Ticket createNormalTicket(Instant movieTime, Instant reservationTime, UUID movieId, UUID clientId) throws TicketManagerCreateException;
    Ticket createReducedTicket(Instant movieTime, Instant reservationTime, UUID movieId, UUID clientId) throws TicketManagerCreateException;

    // R - read methods for ticket manager interface

    List<Ticket> findAllForGivenClientId(UUID clientId) throws TicketManagerReadException;
    List<Ticket> findAllForGivenMovieId(UUID movieId) throws TicketManagerReadException;
}
