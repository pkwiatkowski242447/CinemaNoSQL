package model.repositories;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.model_docs.ClientDoc;
import model.Client;
import model.exceptions.model_docs_exceptions.ClientDocNotFoundException;
import model.exceptions.repository_exceptions.ClientRepositoryCreateException;
import model.exceptions.repository_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repository_exceptions.ClientRepositoryReadException;
import model.exceptions.repository_exceptions.ClientRepositoryUpdateException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepository extends MongoRepository<Client> {

    public ClientRepository() {
        super.initDatabaseConnection();

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

    // C - Methods for creating object representation in the DB

    public Client create(String clientName, String clientSurname, int clientAge) {
        Client client;
        try {
            client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
            ClientDoc clientDoc = new ClientDoc(client);
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            clientDocCollection.insertOne(clientDoc);
        } catch (MongoException exception) {
            throw new ClientRepositoryCreateException(exception.getMessage(), exception);
        }
        return client;
    }

    @Override
    public void updateAllFields(Client client) {
        try {
            ClientDoc clientDoc = new ClientDoc(client);
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.eq("_id", clientDoc.getClientID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("client_name", clientDoc.getClientName()),
                    Updates.set("client_surname", clientDoc.getClientSurname()),
                    Updates.set("client_age", clientDoc.getClientAge()),
                    Updates.set("client_status_active", clientDoc.isClientStatusActive())
            );
            ClientDoc updatedClientDoc = clientDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (updatedClientDoc == null) {
                throw new ClientDocNotFoundException("Document for given client object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Client client) {
        try {
            ClientDoc clientDoc = new ClientDoc(client);
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson clientDocToBeRemoved = Filters.eq("_id", clientDoc.getClientID());
            ClientDoc removedClientDoc = clientDocCollection.findOneAndDelete(clientDocToBeRemoved);
            if (removedClientDoc == null) {
                throw new ClientDocNotFoundException("Document for given client object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID clientID) {
        try {
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.eq("_id", clientID);
            clientDocCollection.findOneAndDelete(filter);
        } catch (MongoException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Client client) {
        try {
            client.setClientStatusActive(false);
            ClientDoc clientDoc = new ClientDoc(client);
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson clientDocToBeExpired = Filters.eq("_id", clientDoc.getClientID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("client_name", clientDoc.getClientName()),
                    Updates.set("client_surname", clientDoc.getClientSurname()),
                    Updates.set("client_age", clientDoc.getClientAge()),
                    Updates.set("client_status_active", clientDoc.isClientStatusActive())
            );
            ClientDoc updatedClientDoc = clientDocCollection.findOneAndUpdate(clientDocToBeExpired, updateAllFields);
            if (updatedClientDoc == null) {
                throw new ClientDocNotFoundException("Document for given client object could not be expired, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public Client findByUUID(UUID identifier) {
        Client client;
        try {
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.eq("_id", identifier);
            ClientDoc foundClientDoc = clientDocCollection.find(filter).first();
            if (foundClientDoc != null) {
                client = this.createClientFromClientDoc(foundClientDoc);
            } else {
                throw new ClientDocNotFoundException("Document for client object with given UUID could not be read, since it is not in the database.");
            }
        } catch (MongoException | NullPointerException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        List<Client> listOfAllClients;
        try {
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.empty();
            FindIterable<ClientDoc> foundClientDocs = clientDocCollection.find(filter);
            listOfAllClients = new ArrayList<>();
            for (ClientDoc clientDoc : foundClientDocs) {
                Client client = this.createClientFromClientDoc(clientDoc);
                listOfAllClients.add(client);
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllClients;
    }

    @Override
    public List<Client> findAllActive() {
        List<Client> listOfAllActiveClients;
        try {
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.eq("client_status_active", true);
            FindIterable<ClientDoc> foundClientDocs = clientDocCollection.find(filter);
            listOfAllActiveClients = new ArrayList<>();
            for (ClientDoc clientDoc : foundClientDocs) {
                Client client = this.createClientFromClientDoc(clientDoc);
                listOfAllActiveClients.add(client);
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveClients;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfClientsUUIDs = new ArrayList<>();
        try {
            MongoCollection<ClientDoc> clientDocCollection = mongoDatabase.getCollection(this.clientCollectionName, this.clientCollectionType);
            Bson filter = Filters.empty();
            for (ClientDoc clientDoc : clientDocCollection.find(filter)) {
                listOfClientsUUIDs.add(clientDoc.getClientID());
            }
        } catch (MongoException exception) {
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfClientsUUIDs;
    }

    private Client createClientFromClientDoc(ClientDoc clientDoc) {
            return new Client(clientDoc.getClientID(),
                    clientDoc.getClientName(),
                    clientDoc.getClientSurname(),
                    clientDoc.getClientAge(),
                    clientDoc.isClientStatusActive());
    }
}
