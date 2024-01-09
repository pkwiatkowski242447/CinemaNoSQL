package model.repositories.implementations;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import jakarta.validation.ConstraintViolation;
import model.constants.TicketConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.exceptions.repositories.object_not_found_exceptions.TicketObjectNotFoundException;
import model.model.Ticket;
import model.exceptions.repositories.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.exceptions.repositories.update_exceptions.TicketRepositoryUpdateException;
import model.exceptions.validation.TicketObjectNotValidException;
import model.repositories.interfaces.TicketRepositoryInterface;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
                                    "required": ["_id", "movie_time", "reservation_time", "ticket_base_price", "ticket_final_price", "movie_id", "client_id"],
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
                                        "ticket_base_price": {
                                            "description": "Base price of a ticket (before any discounts)",
                                            "bsonType": "double",
                                            "minimum": 0,
                                            "maximum": 100
                                        }
                                        "ticket_final_price": {
                                            "description": "Double value holding final price of the ticket - after counting in every discount."
                                            "bsonType": "double"
                                        }
                                        "movie_id": {
                                            "description": "ID of the object representing movie which this ticket is for."
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "client_id": {
                                            "description": "ID of the object representing the client that bought this particular ticket for the movie."
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

    // Create methods

    @Override
    public Ticket createNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket normalTicket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        try {
            this.checkIfTicketObjectIsValid(normalTicket);

            this.getTicketCollection().insertOne(normalTicket);

            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, normalTicket.getTicketID());
            Ticket foundTicket = this.getTicketCollection().find(ticketFilter).first();

            if (foundTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + normalTicket.getTicketID() + " could not be found in the database.");
            } else {
                return foundTicket;
            }

        } catch (MongoException | TicketObjectNotValidException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    @Override
    public Ticket createReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket reducedTicket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        try {
            this.checkIfTicketObjectIsValid(reducedTicket);

            this.getTicketCollection().insertOne(reducedTicket);

            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, reducedTicket.getTicketID());
            Ticket foundTicket = this.getTicketCollection().find(ticketFilter).first();

            if (foundTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + reducedTicket.getTicketID() + " could not be found in the database.");
            } else {
                return foundTicket;
            }

        } catch (MongoException | TicketObjectNotValidException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException {
        try {
            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, ticketId);
            Ticket foundTicket = this.getTicketCollection().find(ticketFilter).first();

            if (foundTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + ticketId + " could not be found in the database.");
            } else {
                return foundTicket;
            }
        } catch (MongoException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAll() throws TicketRepositoryReadException {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson ticketFilter = Filters.empty();
            listOfFoundTickets.addAll(this.getTicketCollection().find(ticketFilter).into(new ArrayList<>()));

            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundTickets;
    }

    @Override
    public List<Ticket> findAllTicketsForAGivenClientId(UUID clientId) throws TicketRepositoryReadException{
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson ticketFilter = Filters.eq(TicketConstants.CLIENT_ID, clientId);
            listOfFoundTickets.addAll(this.getTicketCollection().find(ticketFilter).into(new ArrayList<>()));

            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundTickets;
    }

    @Override
    public List<Ticket> findAllTicketsForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson ticketFilter = Filters.eq(TicketConstants.MOVIE_ID, movieId);
            listOfFoundTickets.addAll(this.getTicketCollection().find(ticketFilter).into(new ArrayList<>()));

            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundTickets;
    }

    // Update methods

    @Override
    public void update(Ticket ticket) throws TicketRepositoryUpdateException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            this.checkIfTicketObjectIsValid(ticket);

            clientSession.startTransaction();

            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, ticket.getTicketID());
            Ticket updatedTicket = this.getTicketCollection().findOneAndReplace(ticketFilter, ticket);

            if (updatedTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + ticket.getTicketID() + " could not be updated, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | TicketObjectNotValidException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Ticket ticket) throws TicketRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, ticket.getTicketID());
            Ticket removedTicket = this.getTicketCollection().findOneAndDelete(ticketFilter);

            if (removedTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + ticket.getTicketID() + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID ticketID) throws TicketRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson ticketFilter = Filters.eq(TicketConstants.DOCUMENT_ID, ticketID);
            Ticket removedTicket = this.getTicketCollection().findOneAndDelete(ticketFilter);

            if (removedTicket == null) {
                throw new TicketObjectNotFoundException("Ticket object with id: " + ticketID + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (MongoException | TicketObjectNotFoundException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    public void checkIfTicketObjectIsValid(Ticket ticket) throws TicketObjectNotValidException {
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketObjectNotValidException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}
