package model.repositories;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import mapping_layer.model_docs.ClientDoc;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import mapping_layer.model_docs.TicketDoc;
import mapping_layer.model_docs.ticket_types.NormalDoc;
import mapping_layer.model_docs.ticket_types.ReducedDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.exceptions.model_docs_exceptions.*;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;

public abstract class MongoRepository<Type> implements Closeable {

    // Database connection required information.
    private final ConnectionString connectionString = new ConnectionString("mongodb://mongonode1:27020,mongonode2:27021,mongonode3:27022/?replicaSet=replicaSet0");
    private final MongoCredential mongoCredential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    // Collection names

    protected final String clientCollectionName = "clients";
    protected final String screeningRoomCollectionName = "screeningRooms";
    protected final String movieCollectionName = "movies";
    protected final String ticketCollectionName = "tickets";
    protected final String typeOfTicketCollectionName = "typesOfTickets";

    // Collection types

    protected final Class<ClientDoc> clientCollectionType = ClientDoc.class;
    protected final Class<ScreeningRoomDoc> screeningRoomCollectionType = ScreeningRoomDoc.class;
    protected final Class<MovieDoc> movieCollectionType = MovieDoc.class;
    protected final Class<TicketDoc> ticketCollectionType = TicketDoc.class;
    protected final Class<TypeOfTicketDoc> typeOfTicketCollectionType = TypeOfTicketDoc.class;

    // Mapping Java classes to Bson documents.
    private final ClassModel<ClientDoc> clientDocClassModel = ClassModel.builder(ClientDoc.class).build();
    private final ClassModel<MovieDoc> movieDocClassModel = ClassModel.builder(MovieDoc.class).build();
    private final ClassModel<ScreeningRoomDoc> screeningRoomDocClassModel = ClassModel.builder(ScreeningRoomDoc.class).build();
    private final ClassModel<TicketDoc> ticketDocClassModel = ClassModel.builder(TicketDoc.class).build();
    private final ClassModel<TypeOfTicketDoc> typeOfTicketDocClassModel = ClassModel.builder(TypeOfTicketDoc.class).enableDiscriminator(true).build();
    private final ClassModel<NormalDoc> normalDocClassModel = ClassModel.builder(NormalDoc.class).enableDiscriminator(true).build();
    private final ClassModel<ReducedDoc> reducedDocClassModel = ClassModel.builder(ReducedDoc.class).enableDiscriminator(true).build();

    private final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(
            clientDocClassModel, movieDocClassModel, screeningRoomDocClassModel, ticketDocClassModel, typeOfTicketDocClassModel, normalDocClassModel, reducedDocClassModel
    ).build();
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            pojoCodecProvider, PojoCodecProvider.builder().automatic(true).conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());

    // MongoClient & MongoDatabase variables
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;


    protected void initDatabaseConnection(String databaseName) {
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

    // Defining methods that later will be overwritten in other repositories.

    // R - Methods for finding object representation in the DB

    public abstract Type findByUUID(UUID elementUUID);

    public abstract List<Type> findAll();

    public abstract List<Type> findAllActive();

    public abstract List<UUID> findAllUUIDs();

    // U - Methods for updating object representation in the DB

    public abstract void updateAllFields(Type element);

    // D - Methods for deleting object representation from DB

    public abstract void delete(Type element);

    public abstract void delete(UUID elementID);

    public abstract void expire(Type element);

    // Other methods

    public ScreeningRoomDoc findScreeningRoomDoc(UUID screeningRoomID) {
        MongoCollection<ScreeningRoomDoc> screeningRoomDocMongoCollection = mongoDatabase.getCollection(screeningRoomCollectionName, screeningRoomCollectionType);
        Bson screeningRoomFilter = Filters.eq("_id", screeningRoomID);
        ScreeningRoomDoc foundScreeningRoomDoc = screeningRoomDocMongoCollection.find(screeningRoomFilter).first();
        if (foundScreeningRoomDoc != null) {
            return foundScreeningRoomDoc;
        } else {
            throw new ScreeningRoomDocNotFoundException("Document for screening room object with given UUID could not be found in the database.");
        }
    }

    public MovieDoc findMovieDoc(UUID movieID) {
        MongoCollection<MovieDoc> movieDocMongoCollection = mongoDatabase.getCollection(movieCollectionName, movieCollectionType);
        Bson movieFilter = Filters.eq("_id", movieID);
        MovieDoc foundMovieDoc = movieDocMongoCollection.find(movieFilter).first();
        if (foundMovieDoc != null) {
            return foundMovieDoc;
        } else {
            throw new MovieDocNotFoundException("Document for movie object with given UUID could not be found in the database.");
        }
    }

    public ClientDoc findClientDoc(UUID clientID) {
        MongoCollection<ClientDoc> clientDocMongoCollection = mongoDatabase.getCollection(clientCollectionName, clientCollectionType);
        Bson clientFilter = Filters.eq("_id", clientID);
        ClientDoc foundClientDoc = clientDocMongoCollection.find(clientFilter).first();
        if (foundClientDoc != null) {
            return foundClientDoc;
        } else {
            throw new ClientDocNotFoundException("Document for client object with given UUID could not be found in the database.");
        }
    }

    public TypeOfTicketDoc findTypeOfTicketDoc(UUID typeOfTicketID) {
        MongoCollection<TypeOfTicketDoc> typeOfTicketDocMongoCollection = mongoDatabase.getCollection(typeOfTicketCollectionName, typeOfTicketCollectionType);
        Bson typeOfTicketFilter = Filters.eq("_id", typeOfTicketID);
        TypeOfTicketDoc foundTypeOfTicketDoc = typeOfTicketDocMongoCollection.find(typeOfTicketFilter).first();
        if (foundTypeOfTicketDoc != null) {
            return foundTypeOfTicketDoc;
        } else {
            throw new TypeOfTicketDocNotFoundException("Document for type of ticket object with given UUID could not be found in the database.");
        }
    }

    public String findTypeOfTicket(TypeOfTicketDoc typeOfTicketDoc) {
        MongoCollection<Document> documentMongoCollection = mongoDatabase.getCollection(typeOfTicketCollectionName);
        Bson filter = Filters.eq("_id", typeOfTicketDoc.getTypeOfTicketID());
        Document foundTypeOfTicket = documentMongoCollection.find(filter).first();
        if (foundTypeOfTicket != null) {
            return (String) foundTypeOfTicket.get("_clazz");
        } else {
            throw new TypeOfTicketNotFoundException("Type of ticket object could not be found in the database.");
        }
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
