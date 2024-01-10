package repositories;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import constants.GeneralConstants;
import exceptions.MongoConfigNotFoundException;
import model.Ticket;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import utility.MongoConnection;

import java.io.Closeable;
import java.util.List;

public abstract class MongoRepository implements Closeable {

    // Collection names

    protected final String ticketCollectionName = GeneralConstants.TICKETS_COLLECTION_NAME;

    // Collection types
    protected final Class<Ticket> ticketCollectionType = Ticket.class;

    // Mapping Java classes to Bson documents.
    private final ClassModel<Ticket> ticketClassModel = ClassModel.builder(Ticket.class).build();

    private final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(
            ticketClassModel
    ).build();
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            pojoCodecProvider, PojoCodecProvider.builder().automatic(true).conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());

    // MongoClient & MongoDatabase variables
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;


    protected void initDatabaseConnection(String databaseName) throws MongoConfigNotFoundException {
        ConnectionString connectionString = MongoConnection.getMongoConnectionString();
        MongoCredential mongoCredential = MongoConnection.getMongoCredentials();

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .credential(mongoCredential)
                .readConcern(ReadConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .writeConcern(WriteConcern.MAJORITY)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                )).build();

        mongoClient = MongoClients.create(mongoClientSettings);
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    // Get collection methods
    protected MongoCollection<Ticket> getTicketCollection() {
        return mongoDatabase.getCollection(ticketCollectionName, ticketCollectionType);
    }

    // Close method

    @Override
    public void close() {
        mongoClient.close();
    }
}
