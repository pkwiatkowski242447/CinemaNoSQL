package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Ticket;
import model.constants.GeneralConstants;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.repositories.providers.TicketQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface TicketDao {

    // Create methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Insert
    void create(Ticket ticket);

    // Read methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {Ticket.class}, providerMethod = "findByUUID")
    Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {Ticket.class}, providerMethod = "findAll")
    List<Ticket> findAll() throws TicketRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Update
    void update(Ticket ticket);

    // Delete methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Delete
    void delete(Ticket ticket);
}
