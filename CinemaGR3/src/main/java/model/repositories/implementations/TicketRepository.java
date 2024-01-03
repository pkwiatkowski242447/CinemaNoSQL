package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Client;
import model.Movie;
import model.Ticket;
import model.repositories.daos.TicketDao;
import model.repositories.interfaces.TicketRepositoryInterface;
import model.repositories.mappers.TicketMapper;
import model.repositories.mappers.TicketMapperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public class TicketRepository extends CassandraClient implements TicketRepositoryInterface {

    private final CqlSession session;
    private final TicketMapper ticketMapper;
    private final TicketDao ticketDao;

    public TicketRepository() {
        this.session = this.initializeCassandraSession();
        this.ticketMapper = new TicketMapperBuilder(session).build();
        this.ticketDao = ticketMapper.ticketDao();
    }

    // Create methods

    @Override
    public Ticket create(Instant movieTime, Instant reservationTime, Movie movie, Client client, String typeOfTicket) {
        return ticketDao.create(movieTime, reservationTime, movie, client, typeOfTicket);
    }

    // Read methods

    @Override
    public Ticket findByUUID(UUID ticketId) {
        return ticketDao.findByUUID(ticketId);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketDao.findAll();
    }

    @Override
    public List<Ticket> findAllActive() {
        return ticketDao.findAllActive();
    }

    // Update methods

    @Override
    public void update(Ticket ticket) {
        ticketDao.update(ticket);
    }

    @Override
    public void expire(Ticket ticket) {
        ticketDao.expire(ticket);
    }

    // Delete methods

    @Override
    public void delete(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    @Override
    public void delete(UUID ticketID) {
        ticketDao.deleteByUUID(ticketID);
    }
}
