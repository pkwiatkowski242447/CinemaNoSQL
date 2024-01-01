package model.repositories.implementations;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.converters.TicketConverter;
import mapping_layer.converters.TypeOfTicketConverter;
import mapping_layer.model_docs.ClientRow;
import mapping_layer.model_docs.MovieRow;
import mapping_layer.model_docs.ScreeningRoomRow;
import mapping_layer.model_docs.TicketRow;
import mapping_layer.model_docs.ticket_types.TypeOfTicketRow;
import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.exceptions.model_exceptions.TicketReservationException;
import model.exceptions.repository_exceptions.*;
import model.repositories.interfaces.TicketRepositoryInterface;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class TicketRepository extends MongoRepository implements TicketRepositoryInterface {

    public TicketRepository(String databaseName) throws MongoConfigNotFoundException {
        super.initDatabaseConnection(databaseName);

        // Checking if collection "tickets" exists.
        boolean collectionExists = false;
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if (collectionName.equals(this.ticketCollectionName)) {
                collectionExists = true;
                break;
            }
        }

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


    @Override
    public Ticket create(Date movieTime, Date reservationTime, Movie movie, Client client, String typeOfTicket) {
        Ticket ticket;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            // Creating new ticket object.
            ticket = new Ticket(UUID.randomUUID(), movieTime, reservationTime, movie, client, typeOfTicket);

            String className;
            TypeOfTicketRow typeOfTicketRow;

            if (ticket.getTicketType().getClass().equals(Reduced.class)) {
                className = "reduced";
            } else {
                className = "normal";
            }
            typeOfTicketRow = TypeOfTicketConverter.toTypeOfTicketRow(ticket.getTicketType());

            Bson filter = Filters.eq("_clazz", className);

            try {
                TypeOfTicketRow foundTypeOfTicketRow = getTypeOfTicketCollection().find(filter).first();
                if (foundTypeOfTicketRow != null) {
                    TypeOfTicket ticketType = TypeOfTicketConverter.toTypeOfTicket(foundTypeOfTicketRow, this.findTypeOfTicket(foundTypeOfTicketRow));
                    ticket.setTypeOfTicket(ticketType);
                } else {
                    getTypeOfTicketCollection().insertOne(typeOfTicketRow);
                }
            } catch (TypeOfTicketNotFoundException exception) {
                 throw new TypeOfTicketNotFoundException(exception.getMessage());
            }

            // From ticket object ticketDoc object is created (basically it represents ticket object in form of a document).
            TicketRow ticketRow = TicketConverter.toTicketRow(ticket);
            getTicketCollection().insertOne(ticketRow);

            filter = Filters.eq("_id", ticket.getMovie().getScreeningRoom().getScreeningRoomID());
            Bson update = Updates.inc("number_of_available_seats", -1);
            getScreeningRoomCollection().findOneAndUpdate(filter, update);
            clientSession.commitTransaction();
        } catch (MongoException | TicketReservationException | NullReferenceException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
        return ticket;
    }

    @Override
    public void updateAllFields(Ticket ticket) {
        try {
            TicketRow ticketRow = TicketConverter.toTicketRow(ticket);
            Bson filter = Filters.eq("_id", ticket.getTicketID());
            TicketRow updatedTicketRow = getTicketCollection().findOneAndReplace(filter, ticketRow);
            if (updatedTicketRow == null) {
                throw new TicketDocNotFoundException("Document for given ticket object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new TicketRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Ticket ticket) {
        this.delete(ticket.getTicketID());
    }

    @Override
    public void delete(UUID ticketID) {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson filter = Filters.eq("_id", ticketID);
            TicketRow removedTicketRow = getTicketCollection().findOneAndDelete(filter);
            if (removedTicketRow == null) {
                throw new TicketDocNotFoundException("Document for given ticket object could not be deleted, since it is not in the database.");
            }
            MovieRow foundMovieRow = findMovieDoc(removedTicketRow.getMovieID());
            Bson screeningRoomFilter = Filters.eq("_id", foundMovieRow.getScreeningRoomID());
            Bson updates = Updates.inc("number_of_available_seats", 1);
            ScreeningRoomRow updatedScreeningRoom = getScreeningRoomCollection().findOneAndUpdate(screeningRoomFilter, updates);
            if (updatedScreeningRoom == null) {
                throw new ScreeningRoomDocNotFoundException("Document for screening room object for given ticket object could not be found in the database.");
            }
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Ticket ticket) {
        ticket.setTicketStatusActive(false);
        updateAllFields(ticket);
    }

    @Override
    public Ticket findByUUID(UUID identifier) {
        Ticket ticket;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            TicketRow ticketRow = findTicketDoc(identifier);
            if (ticketRow != null) {
                ticket = this.getTicket(ticketRow);
            } else {
                throw new TicketDocNotFoundException("Document for given ticket object could not be read, since it is not in the database.");
            }
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return ticket;
    }

    @Override
    public List<Ticket> findAll() {
       List<Ticket> listOfAllTickets;
       try(ClientSession clientSession = mongoClient.startSession()) {
           clientSession.startTransaction();
           Bson ticketFilter = Filters.empty();
           listOfAllTickets = findTickets(ticketFilter);
           clientSession.commitTransaction();
       } catch (MongoException exception) {
           throw new TicketRepositoryReadException(exception.getMessage(), exception);
       }
       return listOfAllTickets;
    }

    @Override
    public List<Ticket> findAllActive() {
        List<Ticket> listOfAllActiveTickets;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson ticketFilter = Filters.eq("ticket_status_active", true);
            listOfAllActiveTickets = findTickets(ticketFilter);
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveTickets;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfTicketUUIDs;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            MongoCollection<TicketRow> ticketDocCollection = mongoDatabase.getCollection(this.ticketCollectionName, this.ticketCollectionType);
            Bson filter = Filters.empty();
            listOfTicketUUIDs = new ArrayList<>();
            for (TicketRow ticketRow : ticketDocCollection.find(filter)) {
                listOfTicketUUIDs.add(ticketRow.getTicketID());
            }
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfTicketUUIDs;
    }

    private List<Ticket> findTickets(Bson ticketFilter) {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        for (TicketRow ticketRow : getTicketCollection().find(ticketFilter)) {
            listOfFoundTickets.add(getTicket(ticketRow));
        }
        return listOfFoundTickets;
    }

    private Ticket getTicket(TicketRow ticketRow) {
        ClientRow clientRow = this.findClientDoc(ticketRow.getClientID());
        MovieRow movieRow = this.findMovieDoc(ticketRow.getMovieID());
        ScreeningRoomRow screeningRoomRow = this.findScreeningRoomDoc(movieRow.getScreeningRoomID());
        TypeOfTicketRow ticketTypeDoc = this.findTypeOfTicketDoc(ticketRow.getTypeOfTicketID());
        return TicketConverter.toTicket(ticketRow,
                movieRow,
                screeningRoomRow,
                clientRow,
                ticketTypeDoc,
                this.findTypeOfTicket(ticketTypeDoc));
    }
}
