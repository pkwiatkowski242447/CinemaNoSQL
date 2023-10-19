package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.*;

@Entity
@Table(name = "screening_room")
public class ScreeningRoom {

    @Id
    @Column(name = "screening_room_id", nullable = false, unique = true)
    private UUID screeningRoomID;

    @Column(name = "screening_room_floor", nullable = false)
    @Min(0)
    @Max(3)
    private int screeningRoomFloor;

    @Column(name = "screening_room_number", nullable = false)
    @Min(1)
    @Max(20)
    private int screeningRoomNumber;

    @Column(name = "number_of_avail_seats", nullable = false)
    @Min(0)
    @Max(150)
    private int numberOfAvailableSeats;

    @Column(name = "screening_room_status_active", nullable = false)
    private boolean screeningRoomStatusActive;

    // Constructors

    public ScreeningRoom() {
    }

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
