package model.repositories.implementations;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import model.constants.GeneralConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;
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

    // Validator for bean validation

    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected static final Validator validator = validatorFactory.getValidator();

    // Collection names

    protected final String clientCollectionName = GeneralConstants.CLIENTS_COLLECTION_NAME;
    protected final String movieCollectionName = GeneralConstants.MOVIES_COLLECTION_NAME;
    protected final String ticketCollectionName = GeneralConstants.TICKETS_COLLECTION_NAME;

    // Collection types

    protected final Class<Client> clientCollectionType = Client.class;
    protected final Class<Movie> movieCollectionType = Movie.class;
    protected final Class<Ticket> ticketCollectionType = Ticket.class;

    // Mapping Java classes to Bson documents.
    private final ClassModel<Client> clientClassModel = ClassModel.builder(Client.class).build();
    private final ClassModel<Movie> movieClassModel = ClassModel.builder(Movie.class).build();
    private final ClassModel<Ticket> ticketClassModel = ClassModel.builder(Ticket.class).build();
    private final ClassModel<Normal> normalClassModel = ClassModel.builder(Normal.class).enableDiscriminator(true).build();
    private final ClassModel<Reduced> reducedClassModel = ClassModel.builder(Reduced.class).enableDiscriminator(true).build();

    private final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(
            clientClassModel, movieClassModel, ticketClassModel, normalClassModel, reducedClassModel
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

    protected MongoCollection<Client> getClientCollection() {
        return mongoDatabase.getCollection(clientCollectionName, clientCollectionType);
    }

    protected MongoCollection<Movie> getMovieCollection() {
        return mongoDatabase.getCollection(movieCollectionName, movieCollectionType);
    }

    protected MongoCollection<Ticket> getTicketCollection() {
        return mongoDatabase.getCollection(ticketCollectionName, ticketCollectionType);
    }

    // Close method

    @Override
    public void close() {
        mongoClient.close();
    }
}
