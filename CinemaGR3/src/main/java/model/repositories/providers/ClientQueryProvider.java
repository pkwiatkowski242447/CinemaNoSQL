package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import mapping_layer.converters.ClientConverter;
import mapping_layer.model_rows.ClientRow;
import model.Client;
import model.constants.ClientConstants;
import model.exceptions.repository_exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repository_exceptions.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repository_exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repository_exceptions.update_exceptions.ClientRepositoryUpdateException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientQueryProvider {

    private final CqlSession session;
    private final EntityHelper<ClientRow> clientEntityHelper;

    public ClientQueryProvider(MapperContext mapperContext, EntityHelper<ClientRow> clientEntityHelper) {
        this.session = mapperContext.getSession();
        this.clientEntityHelper = clientEntityHelper;
    }

    // Create methods

    public Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException {
        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge, true);
        ClientRow clientModelRow = ClientConverter.toClientRow(client);

        BoundStatement createClient = session.prepare(clientEntityHelper.insert().build())
                .bind()
                .setUuid(ClientConstants.CLIENT_ID, clientModelRow.getClientID())
                .setString(ClientConstants.CLIENT_NAME, clientModelRow.getClientName())
                .setString(ClientConstants.CLIENT_SURNAME, clientModelRow.getClientSurname())
                .setInt(ClientConstants.CLIENT_AGE, clientModelRow.getClientAge())
                .setBoolean(ClientConstants.CLIENT_STATUS_ACTIVE, clientModelRow.isClientStatusActive());
        Row clientRow = session.execute(createClient).one();

        if (clientRow != null) {
            return this.convertRowToClient(clientRow);
        } else {
            throw new ClientRepositoryCreateException("Client object with given parameters could not be created in database.");
        }
    }

    // Read methods

    public Client findByUUID(UUID clientId) throws ClientRepositoryReadException {
        Select findByUUID = QueryBuilder
                .selectFrom(ClientConstants.CLIENTS_TABLE_NAME)
                .all()
                .where(Relation.column(ClientConstants.CLIENT_ID).isEqualTo(QueryBuilder.literal(clientId)));
        Row clientRow = session.execute(findByUUID.build()).one();

        if (clientRow != null) {
            return this.convertRowToClient(clientRow);
        } else {
            throw new ClientRepositoryReadException("Client object with given UUID could not be found in the database.");
        }
    }

    public List<Client> findAll() throws ClientRepositoryReadException {
        Select findAll = QueryBuilder
                .selectFrom(ClientConstants.CLIENTS_TABLE_NAME)
                .all();
        List<Row> clientRows = session.execute(findAll.build()).all();

        if (!clientRows.isEmpty()) {
            return this.convertRowsToClients(clientRows);
        } else {
            throw new ClientRepositoryReadException("No client objects were found in the database.");
        }
    }

    public List<Client> findAllActive() throws ClientRepositoryReadException {
        Select findAll = QueryBuilder
                .selectFrom(ClientConstants.CLIENTS_TABLE_NAME)
                .all()
                .where(Relation.column(ClientConstants.CLIENT_STATUS_ACTIVE).isEqualTo(QueryBuilder.literal(true)));
        List<Row> clientRows = session.execute(findAll.build()).all();

        if (!clientRows.isEmpty()) {
            return this.convertRowsToClients(clientRows);
        } else {
            throw new ClientRepositoryReadException("No client objects were found in the database.");
        }
    }

    // Update methods


    public void update(Client client) throws ClientRepositoryUpdateException {
        ClientRow clientModelRow = ClientConverter.toClientRow(client);

        Update updateById = QueryBuilder
                .update(ClientConstants.CLIENTS_TABLE_NAME)
                .setColumn(ClientConstants.CLIENT_NAME, QueryBuilder.literal(clientModelRow.getClientName()))
                .setColumn(ClientConstants.CLIENT_SURNAME, QueryBuilder.literal(clientModelRow.getClientSurname()))
                .setColumn(ClientConstants.CLIENT_AGE, QueryBuilder.literal(clientModelRow.getClientAge()))
                .setColumn(ClientConstants.CLIENT_STATUS_ACTIVE, QueryBuilder.literal(clientModelRow.isClientStatusActive()))
                .where(Relation.column(ClientConstants.CLIENT_ID).isEqualTo(QueryBuilder.literal(clientModelRow.getClientID())));
        Row clientRow = session.execute(updateById.build()).one();

        if (clientRow == null) {
            throw new ClientRepositoryUpdateException("Client object could not be updated.");
        }
    }

    public void expire(Client client) throws ClientRepositoryUpdateException {
        client.setClientStatusActive(false);
        this.update(client);
    }

    // Delete methods

    public void delete(Client client) throws ClientRepositoryDeleteException {
        this.deleteByUUID(client.getClientID());
    }

    public void deleteByUUID(UUID clientId) throws ClientRepositoryDeleteException {
        Delete deleteById = QueryBuilder
                .deleteFrom(ClientConstants.CLIENTS_TABLE_NAME)
                .where(Relation.column(ClientConstants.CLIENT_ID).isEqualTo(QueryBuilder.literal(clientId)));
        Row clientRow = session.execute(deleteById.build()).one();

        if (clientRow != null) {
            throw new ClientRepositoryDeleteException("Client object with given id could not be deleted from the database.");
        }
    }

    // Additional methods

    private Client convertRowToClient(Row clientRow) {
        ClientRow clientModelRow = new ClientRow(
                clientRow.getUuid(ClientConstants.CLIENT_ID),
                clientRow.getBoolean(ClientConstants.CLIENT_STATUS_ACTIVE),
                clientRow.getString(ClientConstants.CLIENT_NAME),
                clientRow.getString(ClientConstants.CLIENT_SURNAME),
                clientRow.getInt(ClientConstants.CLIENT_AGE)
        );

        return ClientConverter.toClient(clientModelRow);
    }

    private List<Client> convertRowsToClients(List<Row> clientRows) {
        List<Client> clientList = new ArrayList<>();
        for (Row clientRow : clientRows) {
            clientList.add(this.convertRowToClient(clientRow));
        }
        return clientList;
    }
}
