package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.dtos.TicketClientDTO;
import model.constants.GeneralConstants;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.repositories.providers.TicketClientQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface TicketClientDao {

    // Create methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Insert
    void create(TicketClientDTO ticketClientDTO);

    // Read methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketClientQueryProvider.class, entityHelpers = {TicketClientDTO.class}, providerMethod = "findByUUID")
    TicketClientDTO findByUUID(UUID ticketClientDTOId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketClientQueryProvider.class, entityHelpers = {TicketClientDTO.class}, providerMethod = "findAll")
    List<TicketClientDTO> findAll() throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketClientQueryProvider.class, entityHelpers = {TicketClientDTO.class}, providerMethod = "findAllForAGivenClientId")
    List<TicketClientDTO> findAllForAGivenClientId(UUID clientId) throws TicketRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Update
    void update(TicketClientDTO ticketClientDTO);

    // Delete methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Delete
    void delete(TicketClientDTO ticketClientDTO);
}
