package model.repositories.implementations;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import mapping_layer.model_docs.ClientRow;
import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import mapping_layer.model_docs.TicketRow;
import mapping_layer.model_docs.ticket_types.NormalRow;
import mapping_layer.model_docs.ticket_types.ReducedRow;
import mapping_layer.model_docs.ticket_types.TypeOfTicketRow;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
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

public abstract class MongoRepository implements Closeable {

    // Database connection required information.
    // private final ConnectionString connectionString = new ConnectionString("mongodb://mongonode1:27020,mongonode2:27021,mongonode3:27022/?replicaSet=replicaSet0");
    // private final MongoCredential mongoCredential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    // Collection names

    protected final String clientCollectionName = "clients";
    protected final String screeningRoomCollectionName = "screeningRooms";
    protected final String movieCollectionName = "movies";
    protected final String ticketCollectionName = "tickets";
    protected final String typeOfTicketCollectionName = "typesOfTickets";

    // Collection types

    protected final Class<ClientRow> clientCollectionType = ClientRow.class;
    protected final Class<ScreeningRoomRow> screeningRoomCollectionType = ScreeningRoomRow.class;
    protected final Class<MovieRow> movieCollectionType = MovieRow.class;
    protected final Class<TicketRow> ticketCollectionType = TicketRow.class;
    protected final Class<TypeOfTicketRow> typeOfTicketCollectionType = TypeOfTicketRow.class;

    // Mapping Java classes to Bson documents.
    private final ClassModel<ClientRow> clientDocClassModel = ClassModel.builder(ClientRow.class).build();
    private final ClassModel<MovieRow> movieDocClassModel = ClassModel.builder(MovieRow.class).build();
    private final ClassModel<ScreeningRoomRow> screeningRoomDocClassModel = ClassModel.builder(ScreeningRoomRow.class).build();
    private final ClassModel<TicketRow> ticketDocClassModel = ClassModel.builder(TicketRow.class).build();
    private final ClassModel<TypeOfTicketRow> typeOfTicketDocClassModel = ClassModel.builder(TypeOfTicketRow.class).enableDiscriminator(true).build();
    private final ClassModel<NormalRow> normalDocClassModel = ClassModel.builder(NormalRow.class).enableDiscriminator(true).build();
    private final ClassModel<ReducedRow> reducedDocClassModel = ClassModel.builder(ReducedRow.class).enableDiscriminator(true).build();

    private final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(
            clientDocClassModel, movieDocClassModel, screeningRoomDocClassModel, ticketDocClassModel, typeOfTicketDocClassModel, normalDocClassModel, reducedDocClassModel
    ).build();
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            pojoCodecProvider, PojoCodecProvider.builder().automatic(true).conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());

    // MongoClient & MongoDatabase variables
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;


    protected void initDatabaseConnection(String databaseName) throws MongoConfigNotFoundException  {
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

    // Other methods

    public ScreeningRoomRow findScreeningRoomDoc(UUID screeningRoomID) {
        Bson screeningRoomFilter = Filters.eq("_id", screeningRoomID);
        ScreeningRoomRow foundScreeningRoomRow = getScreeningRoomCollection().find(screeningRoomFilter).first();
        if (foundScreeningRoomRow != null) {
            return foundScreeningRoomRow;
        } else {
            throw new ScreeningRoomDocNotFoundException("Document for screening room object with given UUID could not be found in the database.");
        }
    }

    public MovieRow findMovieDoc(UUID movieID) {
        Bson movieFilter = Filters.eq("_id", movieID);
        MovieRow foundMovieRow = getMovieCollection().find(movieFilter).first();
        if (foundMovieRow != null) {
            return foundMovieRow;
        } else {
            throw new MovieDocNotFoundException("Document for movie object with given UUID could not be found in the database.");
        }
    }

    public ClientRow findClientDoc(UUID clientID) {
        Bson clientFilter = Filters.eq("_id", clientID);
        ClientRow foundClientRow = getClientCollection().find(clientFilter).first();
        if (foundClientRow != null) {
            return foundClientRow;
        } else {
            throw new ClientDocNotFoundException("Document for client object with given UUID could not be found in the database.");
        }
    }

    public TicketRow findTicketDoc(UUID ticketID) {
        Bson ticketFilter = Filters.eq("_id", ticketID);
        TicketRow foundTicketRow = getTicketCollection().find(ticketFilter).first();
        if (foundTicketRow != null) {
            return foundTicketRow;
        } else {
            throw new TicketDocNotFoundException("Document for ticket object with given UUID could not be found in the database.");
        }
    }

    public TypeOfTicketRow findTypeOfTicketDoc(UUID typeOfTicketID) {
        Bson typeOfTicketFilter = Filters.eq("_id", typeOfTicketID);
        TypeOfTicketRow foundTypeOfTicketRow = getTypeOfTicketCollection().find(typeOfTicketFilter).first();
        if (foundTypeOfTicketRow != null) {
            return foundTypeOfTicketRow;
        } else {
            throw new TypeOfTicketDocNotFoundException("Document for type of ticket object with given UUID could not be found in the database.");
        }
    }

    public String findTypeOfTicket(TypeOfTicketRow typeOfTicketRow) {
        MongoCollection<Document> documentMongoCollection = mongoDatabase.getCollection(typeOfTicketCollectionName);
        Bson filter = Filters.eq("_id", typeOfTicketRow.getTypeOfTicketID());
        Document foundTypeOfTicket = documentMongoCollection.find(filter).first();
        if (foundTypeOfTicket != null) {
            return (String) foundTypeOfTicket.get("_clazz");
        } else {
            throw new TypeOfTicketNotFoundException("Type of ticket object could not be found in the database.");
        }
    }

    protected MongoCollection<ScreeningRoomRow> getScreeningRoomCollection() {
        return mongoDatabase.getCollection(screeningRoomCollectionName, screeningRoomCollectionType);
    }

    protected MongoCollection<ClientRow> getClientCollection() {
        return mongoDatabase.getCollection(clientCollectionName, clientCollectionType);
    }

    protected MongoCollection<MovieRow> getMovieCollection() {
        return mongoDatabase.getCollection(movieCollectionName, movieCollectionType);
    }

    protected MongoCollection<TicketRow> getTicketCollection() {
        return mongoDatabase.getCollection(ticketCollectionName, ticketCollectionType);
    }

    protected MongoCollection<TypeOfTicketRow> getTypeOfTicketCollection() {
        return mongoDatabase.getCollection(typeOfTicketCollectionName, typeOfTicketCollectionType);
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
