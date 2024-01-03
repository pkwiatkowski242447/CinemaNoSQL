package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import mapping_layer.model_rows.TicketRow;
import model.Client;
import model.Movie;
import model.Ticket;
import model.repositories.providers.TicketQueryProvider;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Dao
public interface TicketDao {

    // Create methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    Ticket create(Instant movieTime, Instant reservationTime, Movie movie, Client client, String typeOfTicket);

    // Read methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    Ticket findByUUID(UUID ticketId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    List<Ticket> findAll();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    List<Ticket> findAllActive();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    List<UUID> findAllUUIDs();

    // Update methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    void update(Ticket ticket);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    void expire(Ticket ticket);

    // Delete methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    void delete(Ticket ticket);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TicketQueryProvider.class, entityHelpers = {TicketRow.class})
    void deleteByUUID(UUID ticketId);
}
