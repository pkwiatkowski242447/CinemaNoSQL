package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.*;

@Entity
public class ScreeningRoom {

    @Id
    @Column(nullable = false, unique = true)
    private UUID screeningRoomID;

    @Column(nullable = false)
    @Min(0)
    @Max(3)
    private int screeningRoomFloor;

    @Column(nullable = false)
    @Min(1)
    @Max(20)
    private int screeningRoomNumber;

    @Column(nullable = false)
    @Min(0)
    @Max(150)
    private int numberOfAvailableSeats;

    @Column(nullable = false)
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
        stringBuilder.append("Sala nr. ")
                .append(this.screeningRoomNumber)
                .append(", piętro: ")
                .append(this.screeningRoomFloor)
                .append(", liczba miejsc: ")
                .append(this.numberOfAvailableSeats);
        if (this.screeningRoomStatusActive) {
            stringBuilder.append(", status sali: aktywna");
        } else {
            stringBuilder.append(", status sali: aktywna");
        }
        return stringBuilder.toString();
    }
}
