package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlSession;
import utility.CassandraConnection;

import java.net.InetSocketAddress;
import java.util.List;

public abstract class CassandraRepository implements AutoCloseable {

    private static CqlSession session;

    // Getters

    public static CqlSession getSession() {
        return session;
    }

    public void initializeCassandraSession() {
        List<String> hostNames = CassandraConnection.socketData.keySet().stream().toList();
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(hostNames.get(0), CassandraConnection.socketData.get(hostNames.get(0))))
                .addContactPoint(new InetSocketAddress(hostNames.get(1), CassandraConnection.socketData.get(hostNames.get(1))))
                .withLocalDatacenter(CassandraConnection.dataCenterName)
                .withAuthCredentials(CassandraConnection.cassandraUsername, CassandraConnection.cassandraPassword)
                .withKeyspace(CassandraConnection.cassandraKeySpace)
                .build();
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
