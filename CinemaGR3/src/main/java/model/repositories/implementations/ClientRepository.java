package model.repositories.implementations;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import jakarta.validation.*;
import model.constants.ClientConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.exceptions.repositories.object_not_found_exceptions.ClientObjectNotFoundException;
import model.model.Client;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.update_exceptions.ClientRepositoryUpdateException;
import model.exceptions.validation.ClientObjectNotValidException;
import model.repositories.interfaces.ClientRepositoryInterface;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClientRepository extends MongoRepository implements ClientRepositoryInterface {

    public ClientRepository(String databaseName) throws MongoConfigNotFoundException {
        super.initDatabaseConnection(databaseName);

        // Checking if collection "clients" exists.
        boolean collectionExists = false;
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if (collectionName.equals(this.clientCollectionName)) {
                collectionExists = true;
                break;
            }
        }

        // If it does not exist - then create it with a specific JSON Schema.
        if (!collectionExists) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse("""
                            {
                                $jsonSchema: {
                                    "bsonType": "object",
                                    "required": ["_id", "client_name", "client_surname", "client_age", "client_status_active"],
                                    "properties": {
                                        "_id": {
                                            "description": "UUID identifier of a certain clients document.",
                                            "bsonType": "binData",
                                            "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                        }
                                        "client_name": {
                                            "description": "String with clients name.",
                                            "bsonType": "string",
                                            "minLength": 1,
                                            "maxLength": 50
                                        }
                                        "client_surname": {
                                            "description": "String with clients surname.",
                                            "bsonType": "string",
                                            "minLength": 2,
                                            "maxLength": 100
                                        }
                                        "client_age": {
                                            "description": "Integer value representing clients age.",
                                            "bsonType": "int",
                                            "minimum": 18,
                                            "maximum": 120
                                        }
                                        "client_status_active": {
                                            "description": "Boolean flag indicating whether client is active (their account does exist [that is not deleted by the user]) or not.",
                                            "bsonType": "bool"
                                        }
                                    }
                                }
                            }
                            """));
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            mongoDatabase.createCollection(this.clientCollectionName, createCollectionOptions);
        }
    }

    // Create methods

    @Override
    public Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException {
        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        try {
            this.checkIfClientObjectIsValid(client);

            this.getClientCollection().insertOne(client);

            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, client.getClientID());
            Client foundClient = this.getClientCollection().find(clientFilter).first();

            if (foundClient == null) {
                throw new ClientObjectNotFoundException("Client object with id: " + client.getClientID() + " could not be found in the database.");
            } else {
                return foundClient;
            }
        } catch (MongoException | ClientObjectNotValidException | ClientObjectNotFoundException exception) {
            throw new ClientRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Client findByUUID(UUID clientId) throws ClientRepositoryReadException {
        try {
            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, clientId);
            Client foundClient = this.getClientCollection().find(clientFilter).first();

            if (foundClient == null) {
                throw new ClientObjectNotFoundException("Client object with id: " + clientId + " could not be found in the database.");
            } else {
                return foundClient;
            }
        } catch (MongoException | ClientObjectNotFoundException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Client> findAll() throws ClientRepositoryReadException {
        List<Client> listOfFoundClients = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson clientFilter = Filters.empty();
            listOfFoundClients.addAll(this.getClientCollection().find(clientFilter).into(new ArrayList<>()));
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundClients;
    }

    @Override
    public List<Client> findAllActive() throws ClientRepositoryReadException {
        List<Client> listOfFoundClients = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson clientFilter = Filters.eq(ClientConstants.CLIENT_STATUS_ACTIVE, true);
            listOfFoundClients.addAll(this.getClientCollection().find(clientFilter).into(new ArrayList<>()));
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfFoundClients;
    }

    // Update methods

    @Override
    public void update(Client client) throws ClientRepositoryUpdateException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            this.checkIfClientObjectIsValid(client);

            clientSession.startTransaction();

            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, client.getClientID());
            Client updatedClient = this.getClientCollection().findOneAndReplace(clientFilter, client);

            if (updatedClient == null) {
                throw new ClientObjectNotFoundException("Client object with id: " + client.getClientID() + " could not be updated, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (ClientObjectNotValidException | ClientObjectNotFoundException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(UUID clientId) throws ClientRepositoryUpdateException {
        Client client;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, clientId);
            client = this.getClientCollection().find(clientFilter).first();

            if (client != null) {
                client.setClientStatusActive(false);

                this.checkIfClientObjectIsValid(client);

                Client updatedClient = this.getClientCollection().findOneAndReplace(clientFilter, client);

                if (updatedClient == null) {
                    throw new ClientObjectNotFoundException("Client object with id: " + client.getClientID() + " could not be updated, since it is not in the database.");
                }
            } else {
                throw new ClientObjectNotFoundException("Client object with id: " + clientId + " could not be found in the database.");
            }
            clientSession.commitTransaction();
        } catch (ClientObjectNotValidException | ClientObjectNotFoundException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Client client) throws ClientRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, client.getClientID());
            Client removedClient = this.getClientCollection().findOneAndDelete(clientFilter);

            if (removedClient == null) {
                throw new ClientObjectNotFoundException("Client object with id: " + client.getClientID() + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (ClientObjectNotFoundException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID clientID) throws ClientRepositoryDeleteException {
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Bson clientFilter = Filters.eq(ClientConstants.DOCUMENT_ID, clientID);
            Client removedClient = this.getClientCollection().findOneAndDelete(clientFilter);

            if (removedClient == null) {
                throw new ClientObjectNotFoundException("Client object with id: " + clientID + " could not be deleted, since it is not in the database.");
            }

            clientSession.commitTransaction();
        } catch (ClientObjectNotFoundException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    private void checkIfClientObjectIsValid(Client client) throws ClientObjectNotValidException {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new ClientObjectNotValidException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}
