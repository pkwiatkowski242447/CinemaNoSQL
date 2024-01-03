package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import mapping_layer.model_rows.ticket_types.NormalRow;
import mapping_layer.model_rows.ticket_types.ReducedRow;
import model.exceptions.repository_exceptions.create_exceptions.TypeOfTicketRepositoryCreateException;
import model.exceptions.repository_exceptions.delete_exceptions.TypeOfTicketRepositoryDeleteException;
import model.exceptions.repository_exceptions.read_exceptions.TypeOfTicketRepositoryReadException;
import model.repositories.providers.TypeOfTicketQueryProvider;
import model.ticket_types.TypeOfTicket;

import java.util.UUID;

@Dao
public interface TypeOfTicketDao {

    // Create methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TypeOfTicketQueryProvider.class, entityHelpers = {NormalRow.class, ReducedRow.class})
    TypeOfTicket createNormal() throws TypeOfTicketRepositoryCreateException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TypeOfTicketQueryProvider.class, entityHelpers = {NormalRow.class, ReducedRow.class})
    TypeOfTicket createReduced() throws TypeOfTicketRepositoryCreateException;

    // Read methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TypeOfTicketQueryProvider.class, entityHelpers = {NormalRow.class, ReducedRow.class})
    TypeOfTicket findByUUID(UUID typeOfTicketId) throws TypeOfTicketRepositoryReadException;

    // Delete methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TypeOfTicketQueryProvider.class, entityHelpers = {NormalRow.class, ReducedRow.class})
    void delete(TypeOfTicket ticketType) throws TypeOfTicketRepositoryDeleteException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = TypeOfTicketQueryProvider.class, entityHelpers = {NormalRow.class, ReducedRow.class})
    void deleteByUUID(UUID typeOfTicketId) throws TypeOfTicketRepositoryDeleteException;
}
