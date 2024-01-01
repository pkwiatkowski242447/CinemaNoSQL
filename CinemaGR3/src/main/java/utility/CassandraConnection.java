package utility;

import lombok.Getter;
import lombok.Setter;
import model.exceptions.repository_exceptions.CassandraConfigNotFound;
import model.repositories.implementations.CassandraRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Getter @Setter
public class CassandraConnection {

    public static final Map<String, Integer> socketData = new HashMap<>();
    public static String dataCenterName;
    public static String cassandraUsername;
    public static String cassandraPassword;
    public static String cassandraKeySpace;

    public static void getDataFromPropertyFile() throws CassandraConfigNotFound {
        Properties properties = new Properties();
        try (InputStream inputStream = CassandraRepository.class.getResourceAsStream("cassandra.properties")) {
            properties.load(inputStream);
            dataCenterName = properties.getProperty("datacenter.name");
            cassandraUsername = properties.getProperty("cassandra.username");
            cassandraPassword = properties.getProperty("cassandra.password");
            cassandraKeySpace = properties.getProperty("cassandra.keyspace");

            socketData.put(properties.getProperty("cluster.hostname.node.first"), Integer.parseInt(properties.getProperty("cluster.port.node.first")));
            socketData.put(properties.getProperty("cluster.hostname.node.second"), Integer.parseInt(properties.getProperty("cluster.port.node.second")));
        } catch (IOException exception) {
            throw new CassandraConfigNotFound("File containing Apache Cassandra's config was not found.");
        }
    }
}
