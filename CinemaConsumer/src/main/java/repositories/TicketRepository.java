package repositories;

import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import constants.GeneralConstants;
import exceptions.MongoConfigNotFoundException;
import model.Ticket;
import org.bson.Document;

public class TicketRepository extends MongoRepository {

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

    // C - create methods for ticket repository

    public void create(Ticket ticket) {
        this.getTicketCollection().insertOne(ticket);
    }
}
