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
    private static CqlSession session;
    private static final String CINEMA_KEYSPACE = "cinema";

    public static CqlSession initializeCassandraSession() throws CassandraConfigNotFound {
        createSession();
        createKeyspace();
        return session;
    }

    private static void createSession() throws CassandraConfigNotFound {
        CassandraConnection.getDataFromPropertyFile();
        List<String> hostNames = CassandraConnection.socketData.keySet().stream().toList();
        InetSocketAddress node1 = new InetSocketAddress(hostNames.get(0), CassandraConnection.socketData.get(hostNames.get(0)));
        InetSocketAddress node2 = new InetSocketAddress(hostNames.get(1), CassandraConnection.socketData.get(hostNames.get(1)));
        InetSocketAddress node3 = new InetSocketAddress(hostNames.get(2), CassandraConnection.socketData.get(hostNames.get(2)));
        session = CqlSession.builder()
                .addContactPoint(node1)
                .addContactPoint(node2)
                .addContactPoint(node3)
                .withLocalDatacenter(CassandraConnection.dataCenterName)
                .withAuthCredentials(CassandraConnection.cassandraUsername, CassandraConnection.cassandraPassword)
                .withKeyspace(CqlIdentifier.fromCql(CINEMA_KEYSPACE))
                .build();
    }

    public static void createKeyspace() {
        CreateKeyspace keyspace = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql(CINEMA_KEYSPACE))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    public static void dropKeyspace() {
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
