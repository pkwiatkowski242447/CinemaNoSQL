package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.ScreeningRoomDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomDocTest {

    private UUID screeningRoomId;
    private int screeningRoomFloor;
    private int screeningRoomNumber;
    private int numberOfAvailableSeats;
    private boolean screeningRoomActiveStatus;

    private ScreeningRoomDoc screeningRoomDoc;

    @BeforeEach
    public void init() {
        screeningRoomId = UUID.randomUUID();
        screeningRoomFloor = 1;
        screeningRoomNumber = 2;
        numberOfAvailableSeats = 30;
        screeningRoomActiveStatus = true;

        screeningRoomDoc = new ScreeningRoomDoc(screeningRoomId, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats, screeningRoomActiveStatus);
    }

    @Test
    public void screeningRoomDocNoArgsTestPositive() {
        ScreeningRoomDoc testScreeningRoomDoc = new ScreeningRoomDoc();
        assertNotNull(testScreeningRoomDoc);
    }

    @Test
    public void screeningRoomDocAllArgsAndGettersTestPositive() {
        ScreeningRoomDoc testScreeningRoomDoc = new ScreeningRoomDoc(screeningRoomId, screeningRoomFloor, screeningRoomNumber, numberOfAvailableSeats, screeningRoomActiveStatus);
        assertNotNull(testScreeningRoomDoc);
        assertEquals(screeningRoomId, testScreeningRoomDoc.getScreeningRoomID());
        assertEquals(screeningRoomFloor, testScreeningRoomDoc.getScreeningRoomFloor());
        assertEquals(screeningRoomNumber, testScreeningRoomDoc.getScreeningRoomNumber());
        assertEquals(numberOfAvailableSeats, testScreeningRoomDoc.getNumberOfAvailableSeats());
        assertEquals(screeningRoomActiveStatus, testScreeningRoomDoc.isScreeningRoomStatusActive());
    }

    @Test
    public void screeningRoomIdSetterTestPositive() {
        UUID idBefore = screeningRoomDoc.getScreeningRoomID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        screeningRoomDoc.setScreeningRoomID(newId);
        UUID idAfter = screeningRoomDoc.getScreeningRoomID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void screeningRoomFloorSetterTestPositive() {
        int screeningRoomFloorBefore = screeningRoomDoc.getScreeningRoomFloor();
        int newScreeningRoomFloor = 3;
        screeningRoomDoc.setScreeningRoomFloor(newScreeningRoomFloor);
        int screeningRoomFloorAfter = screeningRoomDoc.getScreeningRoomFloor();
        assertEquals(newScreeningRoomFloor, screeningRoomFloorAfter);
        assertNotEquals(screeningRoomFloorBefore, screeningRoomFloorAfter);
    }

    @Test
    public void screeningRoomNumberSetterTestPositive() {
        int screeningRoomNumberBefore = screeningRoomDoc.getScreeningRoomNumber();
        int newScreeningRoomNumber = 20;
        screeningRoomDoc.setScreeningRoomNumber(newScreeningRoomNumber);
        int screeningRoomNumberAfter = screeningRoomDoc.getScreeningRoomNumber();
        assertEquals(newScreeningRoomNumber, screeningRoomNumberAfter);
        assertNotEquals(screeningRoomNumberBefore, screeningRoomNumberAfter);
    }

    @Test
    public void screeningRoomNumberOfAvailableSeatsSetterTestPositive() {
        int screeningRoomNumOfSeatsBefore = screeningRoomDoc.getNumberOfAvailableSeats();
        int newScreeningRoomNumOfSeats = 130;
        screeningRoomDoc.setNumberOfAvailableSeats(newScreeningRoomNumOfSeats);
        int screeningRoomNumOfSeatsAfter = screeningRoomDoc.getNumberOfAvailableSeats();
        assertEquals(newScreeningRoomNumOfSeats, screeningRoomNumOfSeatsAfter);
        assertNotEquals(screeningRoomNumOfSeatsBefore, screeningRoomNumOfSeatsAfter);
    }

    @Test
    public void screeningRoomStatusActiveSetterTestPositive() {
        boolean screeningRoomStatusActiveBefore = screeningRoomDoc.isScreeningRoomStatusActive();
        boolean newScreeningRoomStatusActive = false;
        screeningRoomDoc.setScreeningRoomStatusActive(newScreeningRoomStatusActive);
        boolean screeningRoomStatusActiveAfter = screeningRoomDoc.isScreeningRoomStatusActive();
        assertEquals(newScreeningRoomStatusActive, screeningRoomStatusActiveAfter);
        assertNotEquals(screeningRoomStatusActiveBefore, screeningRoomStatusActiveAfter);
    }
}
