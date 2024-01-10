package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import model.constants.GeneralConstants;
import model.dtos.TicketClientDTO;
import model.dtos.TicketMovieDTO;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.model.Ticket;
import model.repositories.providers.TicketQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface TicketDao {

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "createTicket")
    void createTicket(Ticket ticket);

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "findByUUID")
    Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "findAll")
    List<Ticket> findAll() throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "findAllForAGivenClientId")
    List<Ticket> findAllForAGivenClientId(UUID clientId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "findAllForAGivenMovieId")
    List<Ticket> findAllForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "update")
    void update(Ticket ticket);

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketClientDTO.class, TicketMovieDTO.class}, providerMethod = "delete")
    void delete(Ticket ticket);
}
