package model.repositories;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import mapping_layer.mappers.ScreeningRoomMapper;
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
            ScreeningRoomDoc screeningRoomDoc = ScreeningRoomMapper.toScreeningRoomDoc(screeningRoom);
            getScreeningRoomCollection().insertOne(screeningRoomDoc);
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryCreateException(exception.getMessage(), exception);
        }
        return screeningRoom;
    }

    @Override
    public void updateAllFields(ScreeningRoom screeningRoom) {
        try {
            ScreeningRoomDoc screeningRoomDoc = ScreeningRoomMapper.toScreeningRoomDoc(screeningRoom);
            Bson filter =  Filters.eq("_id", screeningRoom.getScreeningRoomID());
            ScreeningRoomDoc updatedScreeningRoomDoc = getScreeningRoomCollection().findOneAndReplace(filter, screeningRoomDoc);
            if (updatedScreeningRoomDoc == null) {
                throw new ScreeningRoomDocNotFoundException("Document for given screening room object could not be updated, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(ScreeningRoom screeningRoom) {
        delete(screeningRoom.getScreeningRoomID());
    }

    @Override
    public void delete(UUID screeningRoomID) {
        try {
            Bson filter = Filters.eq("_id", screeningRoomID);
            ScreeningRoomDoc removedScreeningRoomDoc = getScreeningRoomCollection().findOneAndDelete(filter);
            if (removedScreeningRoomDoc == null) {
                throw new ScreeningRoomDocNotFoundException("Document for given screening room object could not be deleted, since it is not in the database.");
            }
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(ScreeningRoom screeningRoom) {
        screeningRoom.setScreeningRoomStatusActive(false);
        updateAllFields(screeningRoom);
    }

    @Override
    public ScreeningRoom findByUUID(UUID identifier) {
        ScreeningRoom screeningRoom;
        try {
            ScreeningRoomDoc screeningRoomDoc = findScreeningRoomDoc(identifier);
            if (screeningRoomDoc != null) {
                screeningRoom = ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc);
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
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson screeningRoomFilter = Filters.empty();
            listOfAllScreeningRooms = findScreeningRooms(screeningRoomFilter);
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllScreeningRooms;
    }

    @Override
    public List<ScreeningRoom> findAllActive() {
        List<ScreeningRoom> listOfAllActiveScreeningRooms;
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson screeningRoomFilter = Filters.eq("screening_room_status_active", true);
            listOfAllActiveScreeningRooms = findScreeningRooms(screeningRoomFilter);
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveScreeningRooms;
    }

    @Override
    public List<UUID> findAllUUIDs() {
        List<UUID> listOfScreeningRoomUUIDs = new ArrayList<>();
        try(ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            Bson filter = Filters.empty();
            for (ScreeningRoomDoc screeningRoomDoc : getScreeningRoomCollection().find(filter)) {
                listOfScreeningRoomUUIDs.add(screeningRoomDoc.getScreeningRoomID());
            }
            clientSession.commitTransaction();
        } catch (MongoException exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfScreeningRoomUUIDs;
    }

    private List<ScreeningRoom> findScreeningRooms(Bson screeningRoomFilter) {
        List<ScreeningRoom> listOfFoundScreeningRooms = new ArrayList<>();
        for (ScreeningRoomDoc screeningRoomDoc : getScreeningRoomCollection().find(screeningRoomFilter)) {
            listOfFoundScreeningRooms.add(ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc));
        }
        return listOfFoundScreeningRooms;
    }
}
