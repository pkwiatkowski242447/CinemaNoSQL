package model.repositories;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.model_docs.ClientDoc;
import mapping_layer.model_docs.MovieDoc;
import mapping_layer.model_docs.ScreeningRoomDoc;
import mapping_layer.model_docs.TicketDoc;
import mapping_layer.model_docs.ticket_types.NormalDoc;
import mapping_layer.model_docs.ticket_types.ReducedDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_docs_exceptions.*;
import model.exceptions.model_exceptions.TicketReservationException;
import model.exceptions.repository_exceptions.TicketRepositoryCreateException;
import model.exceptions.repository_exceptions.TicketRepositoryDeleteException;
import model.exceptions.repository_exceptions.TicketRepositoryReadException;
import model.exceptions.repository_exceptions.TicketRepositoryUpdateException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class TicketRepository extends MongoRepository<Ticket> {

    public TicketRepository(String databaseName) {
        super.initDatabaseConnection(databaseName);

        // Checking if collection "tickets" exists.
        boolean collectionExists = false;
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if (collectionName.equals(this.ticketCollectionName)) {
                collectionExists = true;
                break;
            }
        }

        mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType).drop();

        // If it does not exit - then create it with a specific JSON Schema.
        if (!collectionExists) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse("""
                            {
                                $jsonSchema: {
                                    "bsonType": "object",
                                    "required": ["_id", "movie_time", "reservation_time", "ticket_status_active", "ticket_final_price", "movie_ref", "client_ref", "type_of_ticket_ref"],
                                    "properties": {
                                        "_id": {
                                            "description": "UUID identifier of a certain ticket document.",
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "movie_time": {
                                            "description": "Date of the movie spectacle in the cinema (the date when the movie is on).",
                                            "bsonType": "date"
                                        }
                                        "reservation_time": {
                                            "description": "Date of the reservation of the ticket - exactly when the reservation was made.",
                                            "bsonType": "date"
                                        }
                                        "ticket_status_active": {
                                            "description": "Boolean flag indicating status of the ticket - whether it is active (before the spectacle) - or not (after the spectacle - when it is no longer valid).",
                                            "bsonType": "bool"
                                        }
                                        "ticket_final_price": {
                                            "description": "Double value holding final price of the ticket - after counting in every discount."
                                            "bsonType": "double"
                                        }
                                        "movie_ref": {
                                            "description": "ID of the object representing movie which this ticket is for."
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "client_ref": {
                                            "description": "ID of the object representing the client that bought this particular ticket for the movie."
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "type_of_ticket_ref": {
                                            "description": "ID of the object representing type of ticket that client chose.",
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                    }
                                }
                            }
                            """));
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            mongoDatabase.createCollection(this.ticketCollectionName, createCollectionOptions);
        }
    }


    public Ticket create(Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket) {
        Ticket ticket;
        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();
            // Creating new ticket object.
            ticket = new Ticket(UUID.randomUUID(), movieTime, reservationTime, movie, client, typeOfTicket);

            MongoCollection<TypeOfTicketDoc> typeOfTicketDocCollection = mongoDatabase.getCollection(this.typeOfTicketCollectionName, this.typeOfTicketCollectionType);

            String className;
            TypeOfTicketDoc typeOfTicketDoc;

            if (ticket.getTicketType().getClass().equals(Reduced.class)) {
                className = "reduced";
                typeOfTicketDoc = new ReducedDoc(ticket.getTicketType().getTicketTypeID());
            } else {
                className = "normal";
                typeOfTicketDoc = new NormalDoc(ticket.getTicketType().getTicketTypeID());
            }

            Bson filter = Filters.eq("_clazz", className);

            try {
                TypeOfTicketDoc foundTypeOfTicketDoc = typeOfTicketDocCollection.find(filter).first();
                if (foundTypeOfTicketDoc != null) {
                    TypeOfTicket ticketType = this.findTypeOfTicket(foundTypeOfTicketDoc.getTypeOfTicketID());
                    ticket.setTypeOfTicket(ticketType);
                } else {
                    typeOfTicketDocCollection.insertOne(typeOfTicketDoc);
                }
            } catch (TypeOfTicketNotFoundException exception) {
                 throw new TypeOfTicketNotFoundException(exception.getMessage());
            }

            // From ticket object ticketDoc object is created (basically it represents ticket object in form of a document).
            TicketDoc ticketDoc = new TicketDoc(ticket);
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            ticketDocCollection.insertOne(ticketDoc);

            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            filter = Filters.eq("_id", ticket.getMovie().getScreeningRoom().getScreeningRoomID());
            Bson updateNumOfSeats = Updates.inc("number_of_available_seats", -1);
            screeningRoomDocCollection.findOneAndUpdate(filter, updateNumOfSeats);
            session.commitTransaction();
        } catch (MongoException | TicketReservationException | NullReferenceException exception) {
            session.abortTransaction();
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        } finally {
            session.close();
        }
        return ticket;
    }

    @Override
    public void updateAllFields(Ticket ticket) {
        try {
            TicketDoc ticketDoc = new TicketDoc(ticket);
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("_id", ticket.getTicketID());
            Bson updateAlLFields = Updates.set("ticket_status_active", ticketDoc.isTicketStatusActive());
            TicketDoc updatedTicketDoc = ticketDocCollection.findOneAndUpdate(filter, updateAlLFields);
            if (updatedTicketDoc == null) {
                throw new TicketDocNotFoundException("Document for given ticket object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Ticket ticket) {
        try {
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("_id", ticket.getTicketID());
            TicketDoc removedTicketDoc = ticketDocCollection.findOneAndDelete(filter);
            if (removedTicketDoc == null) {
                throw new TicketDocNotFoundException("Document for given ticket object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID ticketID) {
        try {
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("_id", ticketID);
            ticketDocCollection.findOneAndDelete(filter);
        } catch (MongoException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Ticket ticket) {
        try {
            ticket.setTicketStatusActive(false);
            TicketDoc ticketDoc = new TicketDoc(ticket);
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("_id", ticketDoc.getTicketID());
            Bson updateAllFields = Updates.set("ticket_status_active", false);
            TicketDoc expiredTicketDoc = ticketDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (expiredTicketDoc == null) {
                throw new TicketDocNotFoundException("Document for given ticket object could not be expired, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public Ticket findByUUID(UUID identifier) {
        Ticket ticket;
        try {
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("_id", identifier);
            TicketDoc ticketDoc = ticketDocCollection.find(filter).first();
            if (ticketDoc != null) {
                Client foundClient = this.findClient(ticketDoc.getClientID());
                Movie foundMovie = this.findMovie(ticketDoc.getMovieID());
                TypeOfTicket ticketType = this.findTypeOfTicket(ticketDoc.getTypeOfTicketID());

                ticket = new Ticket(ticketDoc.getTicketID(),
                        ticketDoc.getMovieTime(),
                        ticketDoc.getReservationTime(),
                        ticketDoc.isTicketStatusActive(),
                        ticketDoc.getTicketFinalPrice(),
                        foundMovie,
                        foundClient,
                        ticketType);
            } else {
                throw new TicketDocNotFoundException("Document for given ticket object could not be read, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
       List<Ticket> listOfAllTickets;
       try {
           MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
           Bson filter = Filters.empty();
           listOfAllTickets = getTickets(ticketDocCollection, filter);
       } catch (MongoException exception) {
           throw new TicketRepositoryReadException(exception.getMessage(), exception);
       }
       return listOfAllTickets;
    }

    @Override
    public List<Ticket> findAllActive() {
        List<Ticket> listOfAllTickets;
        try {
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.eq("ticket_status_active", true);
            listOfAllTickets = getTickets(ticketDocCollection, filter);
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllTickets;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfTicketUUIDs;
        try {
            MongoCollection<TicketDoc> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.empty();
            listOfTicketUUIDs = new ArrayList<>();
            for (TicketDoc ticketDoc : ticketDocCollection.find(filter)) {
                listOfTicketUUIDs.add(ticketDoc.getTicketID());
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfTicketUUIDs;
    }

    private List<Ticket> getTickets(MongoCollection<TicketDoc> ticketDocCollection, Bson filter) {
        List<Ticket> listOfAllTickets;
        FindIterable<TicketDoc> foundTicketDocs = ticketDocCollection.find(filter);
        listOfAllTickets = new ArrayList<>();
        for (TicketDoc ticketDoc : foundTicketDocs) {
            Client client = this.findClient(ticketDoc.getClientID());
            Movie movie = this.findMovie(ticketDoc.getMovieID());
            TypeOfTicket ticketType = this.findTypeOfTicket(ticketDoc.getTypeOfTicketID());
            Ticket ticket = new Ticket(ticketDoc.getTicketID(),
                    ticketDoc.getMovieTime(),
                    ticketDoc.getReservationTime(),
                    ticketDoc.isTicketStatusActive(),
                    ticketDoc.getTicketFinalPrice(),
                    movie,
                    client,
                    ticketType);
            listOfAllTickets.add(ticket);
        }
        return listOfAllTickets;
    }

    private Client findClient(UUID clientUUID) throws NullPointerException {
        MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
        Bson filter = Filters.eq("_id", clientUUID);
        ClientDoc foundClientDoc = clientDocCollection.find(filter).first();
        Client client;
        if (foundClientDoc != null) {
            client = new Client(foundClientDoc.getClientID(),
                    foundClientDoc.getClientName(),
                    foundClientDoc.getClientSurname(),
                    foundClientDoc.getClientAge(),
                    foundClientDoc.isClientStatusActive());
        } else {
            throw new ClientDocNotFoundException("Document for client object with given UUID could not be found in the database.");
        }
        return client;
    }

    private Movie findMovie(UUID movieUUID) throws NullPointerException {
        MongoCollection<MovieDoc> movieDocCollection = mongoDatabase.getCollection(this.movieCollectionName, this.movieCollectionType);
        Bson filter = Filters.eq("_id", movieUUID);
        MovieDoc foundMovieDoc = movieDocCollection.find(filter).first();
        Movie movie;
        try {
            if (foundMovieDoc != null) {
                ScreeningRoom foundScreeningRoom = this.findScreeningRoom(foundMovieDoc.getScreeningRoomID());
                movie = new Movie(foundMovieDoc.getMovieID(),
                        foundMovieDoc.getMovieTitle(),
                        foundMovieDoc.isMovieStatusActive(),
                        foundMovieDoc.getMovieBasePrice(),
                        foundScreeningRoom);
            } else {
                throw new MovieDocNotFoundException("Document for movie object with given UUID could not be found.");
            }
        } catch (ScreeningRoomDocNotFoundException exception) {
            throw new MovieDocNotFoundException("Document for screening room object with UUID from foundMovieDoc could not be found in the database.");
        }
        return movie;
    }

    private ScreeningRoom findScreeningRoom(UUID screeningRoomUUID) throws ScreeningRoomDocNotFoundException {
        MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
        Bson filter = Filters.eq("_id", screeningRoomUUID);
        ScreeningRoomDoc foundScreeningRoomDoc = screeningRoomDocCollection.find(filter).first();
        ScreeningRoom screeningRoom;
        if (foundScreeningRoomDoc != null) {
            screeningRoom = new ScreeningRoom(foundScreeningRoomDoc.getScreeningRoomID(),
                    foundScreeningRoomDoc.getScreeningRoomFloor(),
                    foundScreeningRoomDoc.getScreeningRoomNumber(),
                    foundScreeningRoomDoc.getNumberOfAvailableSeats(),
                    foundScreeningRoomDoc.isScreeningRoomStatusActive());
        } else {
            throw new ScreeningRoomDocNotFoundException("Document for screening room object with given UUID could not be found.");
        }
        return screeningRoom;
    }

    private TypeOfTicket findTypeOfTicket(UUID typeOfTicketUUID) {
        MongoCollection<Document> typeOfTicketDocCollection = mongoDatabase.getCollection(this.typeOfTicketCollectionName);
        Bson filter = Filters.eq("_id", typeOfTicketUUID);
        Document foundTypeOfTicketDoc = typeOfTicketDocCollection.find(filter).first();
        TypeOfTicket typeOfTicket;
        if (foundTypeOfTicketDoc != null) {
            if (foundTypeOfTicketDoc.get("_clazz").toString().equals("reduced")) {
                typeOfTicket = new Reduced((UUID)foundTypeOfTicketDoc.get("_id"));
            } else {
                typeOfTicket = new Normal((UUID)foundTypeOfTicketDoc.get("_id"));
            }
        } else {
            throw new TypeOfTicketNotFoundException("Document for type of ticket object with given UUID could not be found in the database.");
        }
        return typeOfTicket;
    }
}
