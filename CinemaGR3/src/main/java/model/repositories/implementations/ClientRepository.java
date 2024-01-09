package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.validation.*;
import model.model.Client;
import model.constants.ClientConstants;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.update_exceptions.ClientRepositoryUpdateException;
import model.exceptions.validation.ClientObjectNotValidException;
import model.repositories.daos.ClientDao;
import model.repositories.interfaces.ClientRepositoryInterface;
import model.repositories.mappers.ClientMapper;
import model.repositories.mappers.ClientMapperBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientRepository extends CassandraClient implements ClientRepositoryInterface {

    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;

    public ClientRepository(CqlSession cqlSession) {
        this.session = cqlSession;
        this.createClientsTable();

        this.clientMapper = new ClientMapperBuilder(session).build();
        clientDao = clientMapper.clientDao();
    }

    private void createClientsTable() {
        SimpleStatement createClientsTable = SchemaBuilder
                .createTable(ClientConstants.CLIENTS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(ClientConstants.CLIENT_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(ClientConstants.CLIENT_NAME), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(ClientConstants.CLIENT_SURNAME), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(ClientConstants.CLIENT_AGE), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(ClientConstants.CLIENT_STATUS_ACTIVE), DataTypes.BOOLEAN)
                .build();
        session.execute(createClientsTable);
    }

    private void dropClientsTable() {
        SimpleStatement dropClientsTable = SchemaBuilder
                .dropTable(ClientConstants.CLIENTS_TABLE_NAME)
                .ifExists()
                .build();
        session.execute(dropClientsTable);
    }

    // Create methods

    @Override
    public Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException {
        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        try {
            this.checkIfClientObjectIsValid(client);
            clientDao.create(client);
            return clientDao.findByUUID(client.getClientID());
        } catch (ClientObjectNotValidException | ClientRepositoryReadException exception) {
            throw new ClientRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Client findByUUID(UUID clientId) throws ClientRepositoryReadException {
        try {
            return clientDao.findByUUID(clientId);
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Client> findAll() throws ClientRepositoryReadException {
        try {
            return clientDao.findAll();
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Client> findAllActive() throws ClientRepositoryReadException {
        try {
            return clientDao.findAllActive();
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Client client) throws ClientRepositoryUpdateException {
        try {
            clientDao.findByUUID(client.getClientID());
            this.checkIfClientObjectIsValid(client);
        } catch (ClientRepositoryReadException | ClientObjectNotValidException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
        clientDao.update(client);
    }

    @Override
    public void expire(UUID clientId) throws ClientRepositoryUpdateException {
        Client client;
        try {
            client = clientDao.findByUUID(clientId);
            client.setClientStatusActive(false);
            this.checkIfClientObjectIsValid(client);
        } catch (ClientRepositoryReadException | ClientObjectNotValidException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
        clientDao.update(client);
    }

    // Delete methods

    @Override
    public void delete(Client client) throws ClientRepositoryDeleteException {
        try {
            clientDao.findByUUID(client.getClientID());
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
        clientDao.delete(client);
    }

    @Override
    public void delete(UUID clientID) throws ClientRepositoryDeleteException {
        Client client;
        try {
            client = clientDao.findByUUID(clientID);
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
        clientDao.delete(client);
    }

    private void checkIfClientObjectIsValid(Client client) throws ClientObjectNotValidException {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientObjectNotValidException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}
