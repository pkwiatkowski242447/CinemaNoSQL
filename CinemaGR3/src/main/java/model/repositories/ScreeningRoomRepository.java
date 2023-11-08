package model.repositories;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.ScreeningRoomDocNotFoundException;
import model.exceptions.repository_exceptions.ScreeningRoomRepositoryCreateException;
import model.exceptions.repository_exceptions.ScreeningRoomRepositoryDeleteException;
import model.exceptions.repository_exceptions.ScreeningRoomRepositoryReadException;
import model.exceptions.repository_exceptions.ScreeningRoomRepositoryUpdateException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScreeningRoomRepository extends MongoRepository<ScreeningRoom> {

    public ScreeningRoomRepository(String databaseName) {
        super.initDatabaseConnection(databaseName);

        // Checking if collection "screeningRooms" exist.
        boolean collectionExists = false;
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if (collectionName.equals(this.screeningRoomCollectionName)) {
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
                            "required": ["_id", "screening_room_floor", "screening_room_number", "number_of_available_seats", "screening_room_status_active"],
                            "properties": {
                                "_id": {
                                    "description": "UUID identifier of a certain clients document.",
                                    "bsonType": "binData",
                                    "pattern": "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
                                }
                                "screening_room_floor": {
                                    "description": "Integer indicating the floor, which screening room is located on.",
                                    "bsonType": "int",
                                    "minimum": 0,
                                    "maximum": 3
                                }
                                "screening_room_number": {
                                    "description": "Integer indicating the number of the screening room.",
                                    "bsonType": "int",
                                    "minimum": 1,
                                    "maximum": 20
                                }
                                "number_of_available_seats": {
                                    "description": "Integer indicating number of available seats inside screening room.",
                                    "bsonType": "int",
                                    "minimum": 0,
                                    "maximum": 150
                                }
                                "screening_room_status_active": {
                                    "description": "Boolean flag indicating whether screening room is active (movies can be shown in it) or not."
                                    "bsonType": "bool"
                                }
                            }
                        }
                    }
                    """));
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            mongoDatabase.createCollection(this.screeningRoomCollectionName, createCollectionOptions);
        }
    }

    public ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        ScreeningRoom screeningRoom;
        try {
            screeningRoom = new ScreeningRoom(UUID.randomUUID(), screeningRoomFloor, screeningRoomNumber, numberOfSeats);
            ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc(screeningRoom);
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            screeningRoomDocCollection.insertOne(screeningRoomDoc);
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryCreateException(exception.getMessage(), exception);
        }
        return screeningRoom;
    }

    @Override
    public void updateAllFields(ScreeningRoom screeningRoom) {
        try {
            ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc(screeningRoom);
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter =  Filters.eq("_id", screeningRoom.getScreeningRoomID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("number_of_available_seats", screeningRoomDoc.getNumberOfAvailableSeats()),
                    Updates.set("screening_room_active_status", screeningRoom.isScreeningRoomStatusActive())
            );
            ScreeningRoomDoc updatedScreeningRoomDoc = screeningRoomDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (updatedScreeningRoomDoc == null) {
                throw new ScreeningRoomDocNotFoundException("Document for given screening room object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(ScreeningRoom screeningRoom) {
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.eq("_id", screeningRoom.getScreeningRoomID());
            ScreeningRoomDoc removedScreeningRoomDoc = screeningRoomDocCollection.findOneAndDelete(filter);
            if (removedScreeningRoomDoc == null) {
                throw new ScreeningRoomDocNotFoundException("Document for given screening room object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID screeningRoomID) {
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.eq("_id", screeningRoomID);
            screeningRoomDocCollection.findOneAndDelete(filter);
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(ScreeningRoom screeningRoom) {
        try {
            screeningRoom.setScreeningRoomStatusActive(false);
            ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc(screeningRoom);
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.eq("_id", screeningRoom.getScreeningRoomID());
            Bson updateAllFields = Updates.combine(
                    Updates.set("screening_room_floor", screeningRoomDoc.getScreeningRoomFloor()),
                    Updates.set("screening_room_number", screeningRoomDoc.getScreeningRoomNumber()),
                    Updates.set("number_of_available_seats", screeningRoomDoc.getNumberOfAvailableSeats()),
                    Updates.set("screening_room_status_active", screeningRoom.isScreeningRoomStatusActive())
            );
            ScreeningRoomDoc expiredScreeningRoomDoc = screeningRoomDocCollection.findOneAndUpdate(filter, updateAllFields);
            if (expiredScreeningRoomDoc == null) {
                throw new ScreeningRoomDocNotFoundException("Document for given screening room object could not be expired, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public ScreeningRoom findByUUID(UUID identifier) {
        ScreeningRoom screeningRoom;
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.eq("_id", identifier);
            ScreeningRoomDoc screeningRoomDoc = screeningRoomDocCollection.find(filter).first();
            if (screeningRoomDoc != null) {
                screeningRoom = new ScreeningRoom(screeningRoomDoc.getScreeningRoomID(),
                        screeningRoomDoc.getScreeningRoomFloor(),
                        screeningRoomDoc.getScreeningRoomNumber(),
                        screeningRoomDoc.getNumberOfAvailableSeats(),
                        screeningRoomDoc.isScreeningRoomStatusActive());
            } else {
                throw new ScreeningRoomDocNotFoundException("Document for screening room object with given UUID could not be read, since it is not in the database.");
            }
        } catch (MongoException | NullPointerException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return screeningRoom;
    }

    @Override
    public List<ScreeningRoom> findAll() {
        List<ScreeningRoom> listOfAllScreeningRooms;
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.empty();
            FindIterable<ScreeningRoomDoc> foundScreeningRoomDocs = screeningRoomDocCollection.find(filter);
            listOfAllScreeningRooms = new ArrayList<>();
            for (ScreeningRoomDoc screeningRoomDoc : foundScreeningRoomDocs) {
                ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomDoc.getScreeningRoomID(),
                        screeningRoomDoc.getScreeningRoomFloor(),
                        screeningRoomDoc.getScreeningRoomNumber(),
                        screeningRoomDoc.getNumberOfAvailableSeats(),
                        screeningRoomDoc.isScreeningRoomStatusActive());
                listOfAllScreeningRooms.add(screeningRoom);
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllScreeningRooms;
    }

    @Override
    public List<ScreeningRoom> findAllActive() {
        List<ScreeningRoom> listOfAllActiveScreeningRooms;
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.eq("screening_room_status_active", true);
            FindIterable<ScreeningRoomDoc> foundScreeningRoomDocs = screeningRoomDocCollection.find(filter);
            listOfAllActiveScreeningRooms = new ArrayList<>();
            for (ScreeningRoomDoc screeningRoomDoc : foundScreeningRoomDocs) {
                ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomDoc.getScreeningRoomID(),
                        screeningRoomDoc.getScreeningRoomFloor(),
                        screeningRoomDoc.getScreeningRoomNumber(),
                        screeningRoomDoc.getNumberOfAvailableSeats(),
                        screeningRoomDoc.isScreeningRoomStatusActive());
                listOfAllActiveScreeningRooms.add(screeningRoom);
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveScreeningRooms;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfScreeningRoomUUIDs = new ArrayList<>();
        try {
            MongoCollection<ScreeningRoomDoc> screeningRoomDocCollection = mongoDatabase.getCollection(this.screeningRoomCollectionName, this.screeningRoomCollectionType);
            Bson filter = Filters.empty();
            for (ScreeningRoomDoc screeningRoomDoc : screeningRoomDocCollection.find(filter)) {
                listOfScreeningRoomUUIDs.add(screeningRoomDoc.getScreeningRoomID());
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfScreeningRoomUUIDs;
    }
}
