package model.repositories.access;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
import model.repositories.implementations.MongoRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoConnection {

    public static ConnectionString getMongoConnectionString() throws MongoConfigNotFoundException {
        ConnectionString connectionString;
        Properties properties = new Properties();
        try (InputStream inputStream = MongoRepository.class.getClassLoader().getResourceAsStream("mongo.properties")) {
            properties.load(inputStream);
            String socketNo1 = properties.getProperty("node1.name") + ":" + properties.getProperty("node1.port");
            String socketNo2 = properties.getProperty("node2.name") + ":" + properties.getProperty("node2.port");
            String socketNo3 = properties.getProperty("node3.name") + ":" + properties.getProperty("node3.port");
            connectionString = new ConnectionString(
                    "mongodb://" + socketNo1 + "," + socketNo2 + "," + socketNo3 +
                            "/?replicaSet=" + properties.getProperty("replicaSet.name")
            );
        } catch (IOException ioException) {
            throw new MongoConfigNotFoundException("Mongo config file not found!");
        }
        return connectionString;
    }

    public static MongoCredential getMongoCredentials() throws MongoConfigNotFoundException {
        MongoCredential mongoCredential;
        Properties properties = new Properties();
        try (InputStream inputStream = MongoRepository.class.getClassLoader().getResourceAsStream("mongo.properties")) {
            properties.load(inputStream);
            mongoCredential = MongoCredential.createCredential(
                    properties.getProperty("user.login"),
                    properties.getProperty("user.login"),
                    properties.getProperty("user.password").toCharArray()
            );
        } catch (IOException ioException) {
            throw new MongoConfigNotFoundException("Mongo config file not found!");
        }
        return mongoCredential;
    }
}
