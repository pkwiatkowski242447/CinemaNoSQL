package utility;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import model.constants.GeneralConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.repositories.implementations.MongoRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class MongoConnection {

    public static ConnectionString getMongoConnectionString() throws MongoConfigNotFoundException {
        ConnectionString connectionString;
        Properties properties = new Properties();
        try (InputStream inputStream = MongoRepository.class.getClassLoader().getResourceAsStream(GeneralConstants.PROPERTY_FILE_NAME)) {
            properties.load(inputStream);
            String socketNo1 = properties.getProperty(GeneralConstants.NODE1_NAME) + ":" + properties.getProperty(GeneralConstants.NODE1_PORT);
            String socketNo2 = properties.getProperty(GeneralConstants.NODE2_NAME) + ":" + properties.getProperty(GeneralConstants.NODE2_PORT);
            String socketNo3 = properties.getProperty(GeneralConstants.NODE3_NAME) + ":" + properties.getProperty(GeneralConstants.NODE3_PORT);
            String replicaSetName = properties.getProperty(GeneralConstants.REPLICA_SET_NAME);
            connectionString = new ConnectionString(String.format("mongodb://%s,%s,%s/?replicaSet=%s", socketNo1, socketNo2, socketNo3, replicaSetName));
        } catch (IOException ioException) {
            throw new MongoConfigNotFoundException("Mongo config file not found!");
        }
        return connectionString;
    }

    public static MongoCredential getMongoCredentials() throws MongoConfigNotFoundException {
        MongoCredential mongoCredential;
        Properties properties = new Properties();
        try (InputStream inputStream = MongoRepository.class.getClassLoader().getResourceAsStream(GeneralConstants.PROPERTY_FILE_NAME)) {
            properties.load(inputStream);
            mongoCredential = MongoCredential.createCredential(
                    properties.getProperty(GeneralConstants.USER_LOGIN),
                    properties.getProperty(GeneralConstants.USER_LOGIN),
                    properties.getProperty(GeneralConstants.USER_PASSWORD).toCharArray()
            );
        } catch (IOException ioException) {
            throw new MongoConfigNotFoundException("Mongo config file not found!");
        }
        return mongoCredential;
    }
}
