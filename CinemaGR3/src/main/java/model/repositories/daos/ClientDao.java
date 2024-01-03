package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import mapping_layer.model_rows.ClientRow;
import model.Client;
import model.exceptions.repository_exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repository_exceptions.read_exceptions.ClientRepositoryReadException;
import model.repositories.providers.ClientQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface ClientDao {

    // Create methods

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException;

    // Read methods

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    Client findByUUID(UUID clientId) throws ClientRepositoryReadException;

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    List<Client> findAll() throws ClientRepositoryReadException;

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    List<Client> findAllActive() throws ClientRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    void update(Client client);

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    void expire(Client client);

    // Delete methods

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    void delete(Client client);

    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {ClientRow.class})
    void deleteByUUID(UUID clientId);
}
