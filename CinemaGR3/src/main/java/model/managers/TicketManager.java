package model.managers;

import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.repositories.Repository;

import java.util.Date;
import java.util.UUID;

public class TicketManager extends Manager<Ticket> {
    public TicketManager(Repository<Ticket> objectRepository) {
        super(objectRepository);
    }

    public Ticket register(Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket) {
        Ticket ticketToRepo = null;
        try{
            ticketToRepo = new Ticket(UUID.randomUUID(), movieTime, reservationTime, movie, client, typeOfTicket);
            getObjectRepository().create(ticketToRepo);
            ticketToRepo = getObjectRepository().findByUUID(ticketToRepo.getTicketID());
        } catch (RepositoryCreateException | TicketReservationException exception) {
            exception.printStackTrace();
        }
        return ticketToRepo;
    }
}
