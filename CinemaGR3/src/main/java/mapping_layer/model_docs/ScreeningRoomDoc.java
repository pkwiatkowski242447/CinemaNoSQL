package mapping_layer.model_docs;

import model.ScreeningRoom;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class ScreeningRoomDoc {

    @BsonProperty("_id")
    private final UUID screeningRoomID;

    @BsonProperty("screening_room_floor")
    private final int screeningRoomFloor;

    @BsonProperty("screening_room_number")
    private final int screeningRoomNumber;

    @BsonProperty("number_of_available_seats")
    private final int numberOfAvailableSeats;

    @BsonProperty("screening_room_status_active")
    private final boolean screeningRoomStatusActive;

    // Constructor

    @BsonCreator
    public ScreeningRoomDoc(@BsonProperty("_id") UUID screeningRoomID,
                            @BsonProperty("screening_room_floor") int screeningRoomFloor,
                            @BsonProperty("screening_room_number") int screeningRoomNumber,
                            @BsonProperty("number_of_available_seats") int numberOfAvailableSeats,
                            @BsonProperty("screening_room_status_active") boolean screeningRoomStatusActive) {
        this.screeningRoomID = screeningRoomID;
        this.screeningRoomFloor = screeningRoomFloor;
        this.screeningRoomNumber = screeningRoomNumber;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.screeningRoomStatusActive = screeningRoomStatusActive;
    }

    public ScreeningRoomDoc(ScreeningRoom screeningRoom) {
        this.screeningRoomID = screeningRoom.getScreeningRoomID();
        this.screeningRoomFloor = screeningRoom.getScreeningRoomFloor();
        this.screeningRoomNumber = screeningRoom.getScreeningRoomNumber();
        this.numberOfAvailableSeats = screeningRoom.getNumberOfAvailableSeats();
        this.screeningRoomStatusActive = screeningRoom.isScreeningRoomStatusActive();
    }

    // Getters

    public UUID getScreeningRoomID() {
        return screeningRoomID;
    }

    public int getScreeningRoomFloor() {
        return screeningRoomFloor;
    }

    public int getScreeningRoomNumber() {
        return screeningRoomNumber;
    }

    public int getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }

    public boolean isScreeningRoomStatusActive() {
        return screeningRoomStatusActive;
    }
}
