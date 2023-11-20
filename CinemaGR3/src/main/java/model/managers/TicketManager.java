package model.managers;

import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;
import model.repositories.implementations.TicketRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TicketManager {

    private TicketRepository ticketRepository;

    public TicketManager(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket register(Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket) {
        Ticket ticketToRepo = null;
        try{
            ticketToRepo = ticketRepository.create(movieTime, reservationTime, movie, client, typeOfTicket);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return ticketToRepo;
    }

    public void unregister(Ticket ticket) {
        try {
            ticketRepository.expire(ticket);
        } catch (RepositoryUpdateException exception) {
            exception.printStackTrace();
        }
    }

    public Ticket get(UUID identifier) {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findByUUID(identifier);
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return ticket;
    }

    public List<Ticket> getAll() {
        List<Ticket> listOfTickets = null;
        try {
            listOfTickets = ticketRepository.findAll();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfTickets;
    }

    public List<Ticket> getAllActive() {
        List<Ticket> listOfTickets = null;
        try {
            listOfTickets = ticketRepository.findAllActive();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfTickets;
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
