package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.*;

public class ScreeningRoom {

    @BsonCreator
    public ScreeningRoom(@BsonProperty("_id") UUID screeningRoomID,
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

    @BsonProperty("_id")
    private final UUID screeningRoomID;

    @BsonProperty("screening_room_floor")
    private final int screeningRoomFloor;

    @BsonProperty("screening_room_number")
    private final int screeningRoomNumber;

    @BsonProperty("number_of_available_seats")
    private int numberOfAvailableSeats;

    @BsonProperty("screening_room_status_active")
    private boolean screeningRoomStatusActive;

    // Constructors

    public ScreeningRoom(UUID screeningRoomID, int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        this.screeningRoomID = screeningRoomID;
        this.screeningRoomFloor = screeningRoomFloor;
        this.screeningRoomNumber = screeningRoomNumber;
        this.numberOfAvailableSeats = numberOfSeats;
        this.screeningRoomStatusActive = true;
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

    // Setters

    public void setNumberOfAvailableSeats(int numberOfAvailableSeats) {
        this.numberOfAvailableSeats = numberOfAvailableSeats;
    }

    public void setScreeningRoomStatusActive(boolean screeningRoomActiveStatus) {
        this.screeningRoomStatusActive = screeningRoomActiveStatus;
    }

    // Other methods

    public String getScreeningRoomInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" Number of the screening room: ")
                .append(this.screeningRoomNumber)
                .append(", floor: ")
                .append(this.screeningRoomFloor)
                .append(", number of available seats: ")
                .append(this.numberOfAvailableSeats);
        if (this.screeningRoomStatusActive) {
            stringBuilder.append(", screening room status: active");
        } else {
            stringBuilder.append(", screening room status: not active");
        }
        return stringBuilder.toString();
    }
}
