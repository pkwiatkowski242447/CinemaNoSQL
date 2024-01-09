package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.constants.GeneralConstants;
import model.dtos.TicketMovieDTO;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.repositories.providers.TicketMovieQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface TicketMovieDao {

    // Create methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Insert
    void create(TicketMovieDTO ticketMovieDTO);

    // Read methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketMovieQueryProvider.class, entityHelpers = {TicketMovieDTO.class}, providerMethod = "findByUUID")
    TicketMovieDTO findByUUID(UUID ticketClientDTOId) throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketMovieQueryProvider.class, entityHelpers = {TicketMovieDTO.class}, providerMethod = "findAll")
    List<TicketMovieDTO> findAll() throws TicketRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = TicketMovieQueryProvider.class, entityHelpers = {TicketMovieDTO.class}, providerMethod = "findAllForAGivenMovieId")
    List<TicketMovieDTO> findAllForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Update
    void update(TicketMovieDTO ticketMovieDTO);

    // Delete methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Delete
    void delete(TicketMovieDTO ticketMovieDTO);
}
