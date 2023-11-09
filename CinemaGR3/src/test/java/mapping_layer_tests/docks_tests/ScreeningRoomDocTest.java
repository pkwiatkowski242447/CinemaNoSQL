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
}
