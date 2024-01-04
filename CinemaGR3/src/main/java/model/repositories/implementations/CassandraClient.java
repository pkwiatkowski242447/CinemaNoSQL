package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import model.exceptions.CassandraConfigNotFound;
import utility.CassandraConnection;

import java.net.InetSocketAddress;
import java.util.List;

public abstract class CassandraClient implements AutoCloseable {

    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected static final Validator validator = validatorFactory.getValidator();
    private CqlSession session;
    private static final String CINEMA_KEYSPACE = "cinema";

    // Getters

    public CqlSession getSession() {
        return session;
    }

    public CqlSession initializeCassandraSession() throws CassandraConfigNotFound {
        this.createSession();
        if (session.getKeyspace().isEmpty()) {
            this.createKeyspace();
            session.close();
            this.createSession();
        }
        return session;
    }

    private void createSession() throws CassandraConfigNotFound {
        CassandraConnection.getDataFromPropertyFile();
        List<String> hostNames = CassandraConnection.socketData.keySet().stream().toList();
        session = CqlSession.builder()
                // .addContactPoint(new InetSocketAddress(hostNames.get(0), CassandraConnection.socketData.get(hostNames.get(0))))
                // .addContactPoint(new InetSocketAddress(hostNames.get(1), CassandraConnection.socketData.get(hostNames.get(1))))
                // .addContactPoint(new InetSocketAddress(hostNames.get(2), CassandraConnection.socketData.get(hostNames.get(2))))
                .addContactPoint(new InetSocketAddress("cassandranode1", 9042))
                .addContactPoint(new InetSocketAddress("cassandranode2", 9043))
                .addContactPoint(new InetSocketAddress("cassandranode3", 9044))
                .withLocalDatacenter(CassandraConnection.dataCenterName)
                .withAuthCredentials(CassandraConnection.cassandraUsername, CassandraConnection.cassandraPassword)
                .withKeyspace(CqlIdentifier.fromCql(CINEMA_KEYSPACE))
                .build();
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
