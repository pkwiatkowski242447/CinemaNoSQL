package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.validation.*;
import model.Client;
import model.constants.ClientConstants;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.update_exceptions.ClientRepositoryUpdateException;
import model.repositories.daos.ClientDao;
import model.repositories.interfaces.ClientRepositoryInterface;
import model.repositories.mappers.ClientMapper;
import model.repositories.mappers.ClientMapperBuilder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientRepository extends CassandraClient implements ClientRepositoryInterface {

    private final CqlSession session;
    private final ClientDao clientDao;

    public ClientRepository() throws CassandraConfigNotFound {
        this.session = this.initializeCassandraSession();
        ClientMapper clientMapper = new ClientMapperBuilder(session).build();
        clientDao = clientMapper.clientDao();

        createClientsTable();
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

    // Create methods

    @Override
    public Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException {
        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientRepositoryCreateException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        clientDao.create(client);
        try {
            return clientDao.findByUUID(client.getClientID());
        } catch (ClientRepositoryReadException exception) {
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
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientRepositoryUpdateException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        clientDao.update(client);
    }

    @Override
    public void expire(UUID clientId) throws ClientRepositoryUpdateException {
        Client client;
        try {
            client = clientDao.findByUUID(clientId);
            client.setClientStatusActive(false);
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
        client.setClientStatusActive(false);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientRepositoryUpdateException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        clientDao.update(client);
    }

    // Delete methods

    @Override
    public void delete(Client client) throws ClientRepositoryDeleteException {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientRepositoryDeleteException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        clientDao.delete(client);
    }

    @Override
    public void delete(UUID clientID) throws ClientRepositoryDeleteException {
        Client client;
        try {
            client = clientDao.findByUUID(clientID);
            client.setClientStatusActive(false);
        } catch (ClientRepositoryReadException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientRepositoryDeleteException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        clientDao.delete(client);
    }

    @Override
    public void close() {
        super.close();
    }
}
