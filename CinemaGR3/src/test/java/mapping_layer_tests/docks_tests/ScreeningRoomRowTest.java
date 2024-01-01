package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.ScreeningRoomRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomRowTest {

    private UUID screeningRoomId;
    private int screeningRoomFloor;
    private int screeningRoomNumber;
    private int numberOfAvailableSeats;
    private boolean screeningRoomActiveStatus;

    private ScreeningRoomRow screeningRoomRow;

    @BeforeEach
    public void init() {
        screeningRoomId = UUID.randomUUID();
        screeningRoomFloor = 1;
        screeningRoomNumber = 2;
        numberOfAvailableSeats = 30;
        screeningRoomActiveStatus = true;

        screeningRoomRow = new ScreeningRoomRow(screeningRoomId, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats, screeningRoomActiveStatus);
    }

    @Test
    public void screeningRoomDocNoArgsTestPositive() {
        ScreeningRoomRow testScreeningRoomRow = new ScreeningRoomRow();
        assertNotNull(testScreeningRoomRow);
    }

    @Test
    public void screeningRoomDocAllArgsAndGettersTestPositive() {
        ScreeningRoomRow testScreeningRoomRow = new ScreeningRoomRow(screeningRoomId, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats, screeningRoomActiveStatus);
        assertNotNull(testScreeningRoomRow);
        assertEquals(screeningRoomId, testScreeningRoomRow.getScreeningRoomID());
        assertEquals(screeningRoomFloor, testScreeningRoomRow.getScreeningRoomFloor());
        assertEquals(screeningRoomNumber, testScreeningRoomRow.getScreeningRoomNumber());
        assertEquals(numberOfAvailableSeats, testScreeningRoomRow.getNumberOfAvailableSeats());
        assertEquals(screeningRoomActiveStatus, testScreeningRoomRow.isScreeningRoomStatusActive());
    }

    @Test
    public void screeningRoomIdSetterTestPositive() {
        UUID idBefore = screeningRoomRow.getScreeningRoomID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        screeningRoomRow.setScreeningRoomID(newId);
        UUID idAfter = screeningRoomRow.getScreeningRoomID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void screeningRoomFloorSetterTestPositive() {
        int screeningRoomFloorBefore = screeningRoomRow.getScreeningRoomFloor();
        int newScreeningRoomFloor = 3;
        screeningRoomRow.setScreeningRoomFloor(newScreeningRoomFloor);
        int screeningRoomFloorAfter = screeningRoomRow.getScreeningRoomFloor();
        assertEquals(newScreeningRoomFloor, screeningRoomFloorAfter);
        assertNotEquals(screeningRoomFloorBefore, screeningRoomFloorAfter);
    }

    @Test
    public void screeningRoomNumberSetterTestPositive() {
        int screeningRoomNumberBefore = screeningRoomRow.getScreeningRoomNumber();
        int newScreeningRoomNumber = 20;
        screeningRoomRow.setScreeningRoomNumber(newScreeningRoomNumber);
        int screeningRoomNumberAfter = screeningRoomRow.getScreeningRoomNumber();
        assertEquals(newScreeningRoomNumber, screeningRoomNumberAfter);
        assertNotEquals(screeningRoomNumberBefore, screeningRoomNumberAfter);
    }

    @Test
    public void screeningRoomNumberOfAvailableSeatsSetterTestPositive() {
        int screeningRoomNumOfSeatsBefore = screeningRoomRow.getNumberOfAvailableSeats();
        int newScreeningRoomNumOfSeats = 130;
        screeningRoomRow.setNumberOfAvailableSeats(newScreeningRoomNumOfSeats);
        int screeningRoomNumOfSeatsAfter = screeningRoomRow.getNumberOfAvailableSeats();
        assertEquals(newScreeningRoomNumOfSeats, screeningRoomNumOfSeatsAfter);
        assertNotEquals(screeningRoomNumOfSeatsBefore, screeningRoomNumOfSeatsAfter);
    }

    @Test
    public void screeningRoomStatusActiveSetterTestPositive() {
        boolean screeningRoomStatusActiveBefore = screeningRoomRow.isScreeningRoomStatusActive();
        boolean newScreeningRoomStatusActive = false;
        screeningRoomRow.setScreeningRoomStatusActive(newScreeningRoomStatusActive);
        boolean screeningRoomStatusActiveAfter = screeningRoomRow.isScreeningRoomStatusActive();
        assertEquals(newScreeningRoomStatusActive, screeningRoomStatusActiveAfter);
        assertNotEquals(screeningRoomStatusActiveBefore, screeningRoomStatusActiveAfter);
    }
}
