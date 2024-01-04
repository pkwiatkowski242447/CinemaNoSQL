package model.managers;

import model.Ticket;
import model.exceptions.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.repositories.implementations.TicketRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TicketManager {

    private TicketRepository ticketRepository;

    public TicketManager(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket registerNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.createNormalTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        } catch (TicketRepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    public Ticket registerReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.createReducedTicket(movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        } catch (TicketRepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    public Ticket get(UUID identifier) {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findByUUID(identifier);
        } catch (TicketRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    public List<Ticket> getAll() {
        List<Ticket> listOfAllTickets = null;
        try {
            listOfAllTickets = ticketRepository.findAll();
        } catch (TicketRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfAllTickets;
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
