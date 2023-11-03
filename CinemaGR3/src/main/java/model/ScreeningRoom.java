package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class ScreeningRoom {

    private final UUID screeningRoomID;
    private final int screeningRoomFloor;
    private final int screeningRoomNumber;
    private int numberOfAvailableSeats;
    private boolean screeningRoomStatusActive;

    // Constructors

    public ScreeningRoom(UUID screeningRoomID, int screeningRoomFloor, int screeningRoomNumber, int numberOfAvailableSeats, boolean screeningRoomStatusActive) {
        this.screeningRoomID = screeningRoomID;
        this.screeningRoomFloor = screeningRoomFloor;
        this.screeningRoomNumber = screeningRoomNumber;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.screeningRoomStatusActive = screeningRoomStatusActive;
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
        stringBuilder.append("Screening room ID: ")
                .append(this.screeningRoomID)
                .append(", number: ")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScreeningRoom that = (ScreeningRoom) o;

        return new EqualsBuilder()
                .append(screeningRoomID, that.screeningRoomID)
                .append(screeningRoomFloor, that.screeningRoomFloor)
                .append(screeningRoomNumber, that.screeningRoomNumber)
                .append(numberOfAvailableSeats, that.numberOfAvailableSeats)
                .append(screeningRoomStatusActive, that.screeningRoomStatusActive)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(screeningRoomID)
                .append(screeningRoomFloor)
                .append(screeningRoomNumber)
                .append(numberOfAvailableSeats)
                .append(screeningRoomStatusActive)
                .toHashCode();
    }
}
