package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import model.Client;
import model.constants.ClientConstants;
import model.exceptions.read_exceptions.ClientRepositoryReadException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Client> clientEntityHelper;

    public ClientQueryProvider(MapperContext mapperContext, EntityHelper<Client> clientEntityHelper) {
        this.session = mapperContext.getSession();
        this.clientEntityHelper = clientEntityHelper;
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

    // Additional methods

    private Client convertRowToClient(Row clientRow) {
        return new Client(
                clientRow.getUuid(ClientConstants.CLIENT_ID),
                clientRow.getString(ClientConstants.CLIENT_NAME),
                clientRow.getString(ClientConstants.CLIENT_SURNAME),
                clientRow.getInt(ClientConstants.CLIENT_AGE),
                clientRow.getBoolean(ClientConstants.CLIENT_STATUS_ACTIVE)
        );
    }

    private List<Client> convertRowsToClients(List<Row> clientRows) {
        List<Client> clientList = new ArrayList<>();
        for (Row clientRow : clientRows) {
            clientList.add(this.convertRowToClient(clientRow));
        }
        return clientList;
    }
}
