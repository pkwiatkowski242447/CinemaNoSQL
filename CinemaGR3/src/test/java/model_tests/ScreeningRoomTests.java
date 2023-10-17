package model_tests;

import model.ScreeningRoom;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomTests {

    @Test
    public void screeningRoomConstructorTestPositive() {
        UUID screeningRoomIDNo1 = UUID.randomUUID();
        int screeningRoomFloorNo1 = 1;
        int screeningRoomNumberNo1 = 6;
        int numberOfAvailableSeatsNo1 = 90;

        ScreeningRoom screeningRoomNo1 = new ScreeningRoom(screeningRoomIDNo1, screeningRoomFloorNo1, screeningRoomNumberNo1, numberOfAvailableSeatsNo1);

        assertNotNull(screeningRoomNo1);

        assertEquals(screeningRoomIDNo1, screeningRoomNo1.getScreeningRoomID());
        assertEquals(screeningRoomFloorNo1, screeningRoomNo1.getScreeningRoomFloor());
        assertEquals(screeningRoomNumberNo1, screeningRoomNo1.getScreeningRoomNumber());
        assertEquals(numberOfAvailableSeatsNo1, screeningRoomNo1.getNumberOfAvailableSeats());
        assertTrue(screeningRoomNo1.isScreeningRoomStatusActive());
    }

    @Test
    public void screeningRoomConstructorPositiveWith0thFloor() {
        UUID screeningRoomID = UUID.randomUUID();
        int screeningRoomFloor = 0;
        int screeningRoomNumber = 3;
        int numberOfAvailableSeats = 50;

        ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomID, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats);

        assertNotNull(screeningRoom);
        assertEquals(screeningRoomFloor, screeningRoom.getScreeningRoomFloor());
    }

    @Test
    public void screeningRoomSetNumberOfAvailableSeatsTestPositive() {
        UUID screeningRoomID = UUID.randomUUID();
        int screeningRoomFloor = 2;
        int screeningRoomNumber = 3;
        int numberOfAvailableSeatsNo1 = 50;
        int numberOfAvailableSeatsNo2 = 100;

        ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomID, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeatsNo1);

        assertNotNull(screeningRoom);
        assertEquals(numberOfAvailableSeatsNo1, screeningRoom.getNumberOfAvailableSeats());

        screeningRoom.setNumberOfAvailableSeats(numberOfAvailableSeatsNo2);

        assertNotNull(screeningRoom);
        assertEquals(numberOfAvailableSeatsNo2, screeningRoom.getNumberOfAvailableSeats());
    }

    @Test
    public void screeningRoomSetScreeningRoomStatusActive() {
        UUID screeningRoomID = UUID.randomUUID();
        int screeningRoomFloor = 2;
        int screeningRoomNumber = 3;
        int numberOfAvailableSeats = 50;

        ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomID, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats);

        assertNotNull(screeningRoom);
        assertTrue(screeningRoom.isScreeningRoomStatusActive());

        screeningRoom.setScreeningRoomStatusActive(false);

        assertNotNull(screeningRoom);
        assertFalse(screeningRoom.isScreeningRoomStatusActive());
    }

    @Test
    public void getScreeningRoomInfoTestActiveStatus() {
        UUID screeningRoomID = UUID.randomUUID();
        int screeningRoomFloor = 2;
        int screeningRoomNumber = 3;
        int numberOfAvailableSeats = 50;

        ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomID, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats);

        assertNotNull(screeningRoom);
        assertNotNull(screeningRoom.getScreeningRoomInfo());
        assertNotEquals("", screeningRoom.getScreeningRoomInfo());
    }

    @Test
    public void getScreeningRoomInfoTestNotActiveStatus() {
        UUID screeningRoomID = UUID.randomUUID();
        int screeningRoomFloor = 2;
        int screeningRoomNumber = 3;
        int numberOfAvailableSeats = 50;

        ScreeningRoom screeningRoom = new ScreeningRoom(screeningRoomID, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats);
        screeningRoom.setScreeningRoomStatusActive(false);

        assertNotNull(screeningRoom);
        assertNotNull(screeningRoom.getScreeningRoomInfo());
        assertNotEquals("", screeningRoom.getScreeningRoomInfo());
    }
}
