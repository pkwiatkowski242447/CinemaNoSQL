package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import model.constants.*;
import utility.CassandraConnection;

import java.net.InetSocketAddress;
import java.util.List;

public abstract class CassandraClient implements AutoCloseable {

    private CqlSession session;
    private static final String CINEMA_KEYSPACE = "cinema";

    // Getters

    public CqlSession getSession() {
        return session;
    }

    public CqlSession initializeCassandraSession() {
        this.createSession();
        if (session.getKeyspace().isEmpty()) {
            this.createKeyspace();
            session.close();
            this.createSession();
        }
        return session;
    }

    private void createSession() {
        List<String> hostNames = CassandraConnection.socketData.keySet().stream().toList();
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(hostNames.get(0), CassandraConnection.socketData.get(hostNames.get(0))))
                .addContactPoint(new InetSocketAddress(hostNames.get(1), CassandraConnection.socketData.get(hostNames.get(1))))
                .addContactPoint(new InetSocketAddress(hostNames.get(2), CassandraConnection.socketData.get(hostNames.get(2))))
                .withLocalDatacenter(CassandraConnection.dataCenterName)
                .withAuthCredentials(CassandraConnection.cassandraUsername, CassandraConnection.cassandraPassword)
                .withKeyspace(CassandraConnection.cassandraKeySpace)
                .build();
    }

    public void createClients() {
        SimpleStatement createClients = SchemaBuilder.createTable(ClientConstants.CLIENTS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(ClientConstants.CLIENT_ID, DataTypes.UUID)
                .withClusteringColumn(ClientConstants.CLIENT_STATUS_ACTIVE, DataTypes.BOOLEAN)
                .withColumn(ClientConstants.CLIENT_NAME, DataTypes.TEXT)
                .withColumn(ClientConstants.CLIENT_SURNAME, DataTypes.TEXT)
                .withColumn(ClientConstants.CLIENT_AGE, DataTypes.INT)
                .build();
        session.execute(createClients);
    }

    public void createTickets() {
        SimpleStatement createTickets = SchemaBuilder.createTable(TicketConstants.TICKETS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(TicketConstants.TICKET_ID, DataTypes.UUID)
                .withClusteringColumn(TicketConstants.TICKET_STATUS_ACTIVE, DataTypes.BOOLEAN)
                .withColumn(TicketConstants.MOVIE_TIME, DataTypes.DATE)
                .withColumn(TicketConstants.RESERVATION_TIME, DataTypes.DATE)
                .withColumn(TicketConstants.TICKET_FINAL_PRICE, DataTypes.DOUBLE)
                .withColumn(TicketConstants.CLIENT_ID, DataTypes.UUID)
                .withColumn(TicketConstants.MOVIE_ID, DataTypes.UUID)
                .withColumn(TicketConstants.TYPE_OF_TICKET_ID, DataTypes.UUID)
                .build();
        session.execute(createTickets);
    }

    public void createMovies() {
        SimpleStatement createMovies = SchemaBuilder.createTable(MovieConstants.MOVIES_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(MovieConstants.MOVIE_ID, DataTypes.UUID)
                .withClusteringColumn(MovieConstants.MOVIE_STATUS_ACTIVE, DataTypes.BOOLEAN)
                .withColumn(MovieConstants.MOVIE_TITLE, DataTypes.TEXT)
                .withColumn(MovieConstants.MOVIE_BASE_PRICE, DataTypes.DOUBLE)
                .withColumn(MovieConstants.SCREENING_ROOM_ID, DataTypes.UUID)
                .build();
        session.execute(createMovies);
    }

    public void createScreeningRooms() {
        SimpleStatement createScreeningRooms = SchemaBuilder.createTable(ScreeningRoomConstants.SCREENING_ROOMS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(ScreeningRoomConstants.SCREENING_ROOM_ID, DataTypes.UUID)
                .withClusteringColumn(ScreeningRoomConstants.SCREENING_ROOM_STATUS_ACTIVE, DataTypes.BOOLEAN)
                .withColumn(ScreeningRoomConstants.SCREENING_ROOM_NUMBER, DataTypes.INT)
                .withColumn(ScreeningRoomConstants.SCREENING_ROOM_FLOOR, DataTypes.INT)
                .withColumn(ScreeningRoomConstants.NUMBER_OF_AVAILABLE_SEATS, DataTypes.INT)
                .build();
        session.execute(createScreeningRooms);
    }

    public void createTicketTypes() {
        SimpleStatement createTicketTypes = SchemaBuilder.createTable(TicketTypeConstants.TICKET_TYPES_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(TicketTypeConstants.TICKET_TYPE_ID, DataTypes.UUID)
                .withClusteringColumn(TicketTypeConstants.TICKET_TYPE_DISCRIMINATOR, DataTypes.TEXT)
                .build();
        session.execute(createTicketTypes);
    }

    public void createKeyspace() {
        CreateKeyspace keyspace = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql(CINEMA_KEYSPACE))
                .ifNotExists()
                .withSimpleStrategy(3)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    private void dropKeyspace() {
        Drop keyspace = SchemaBuilder.dropKeyspace(CqlIdentifier.fromCql(CINEMA_KEYSPACE))
                .ifExists();
        SimpleStatement dropKeyspace = keyspace.build();
        session.execute(dropKeyspace);
    }

    @Override
    public void close() {
        session.close();
    }
}
