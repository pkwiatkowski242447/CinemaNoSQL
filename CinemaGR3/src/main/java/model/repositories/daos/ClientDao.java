package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.model.Client;
import model.constants.GeneralConstants;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.repositories.providers.ClientQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface ClientDao {

    // Create methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Insert
    void create(Client client);

    // Read methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Client.class}, providerMethod = "findByUUID")
    Client findByUUID(UUID clientId) throws ClientRepositoryReadException;

    @StatementAttributes(consistencyLevel =GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Client.class}, providerMethod = "findAll")
    List<Client> findAll() throws ClientRepositoryReadException;

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_READ_CONSISTENCY_LEVEL)
    @QueryProvider(providerClass = ClientQueryProvider.class, entityHelpers = {Client.class}, providerMethod = "findAllActive")
    List<Client> findAllActive() throws ClientRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Update
    void update(Client client);

    // Delete methods

    @StatementAttributes(consistencyLevel = GeneralConstants.CASSANDRA_WRITE_CONSISTENCY_LEVEL)
    @Delete
    void delete(Client client);
}
