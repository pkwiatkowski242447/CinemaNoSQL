package mapping_layer_tests.mappers_tests;

import mapping_layer.mappers.ScreeningRoomMapper;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScreeningRoomMapperTest {

    @Test
    public void screeningRoomMapperFromScreeningRoomToScreeningRoomDoc() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 4, 40);
        assertNotNull(screeningRoom);
        ScreeningRoomDoc screeningRoomDoc = ScreeningRoomMapper.toScreeningRoomDoc(screeningRoom);
        assertNotNull(screeningRoomDoc);
        assertEquals(screeningRoom.getScreeningRoomID(), screeningRoomDoc.getScreeningRoomID());
        assertEquals(screeningRoom.getScreeningRoomFloor(), screeningRoomDoc.getScreeningRoomFloor());
        assertEquals(screeningRoom.getScreeningRoomNumber(), screeningRoomDoc.getScreeningRoomNumber());
        assertEquals(screeningRoom.getNumberOfAvailableSeats(), screeningRoomDoc.getNumberOfAvailableSeats());
        assertEquals(screeningRoom.isScreeningRoomStatusActive(), screeningRoomDoc.isScreeningRoomStatusActive());
    }

    @Test
    public void screeningRoomMapperFromScreeningRoomDocToScreeningRoom() {
        ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc(UUID.randomUUID(), 1, 4, 40, true);
        assertNotNull(screeningRoomDoc);
        ScreeningRoom screeningRoom = ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc);
        assertNotNull(screeningRoom);
        assertEquals(screeningRoomDoc.getScreeningRoomID(), screeningRoom.getScreeningRoomID());
        assertEquals(screeningRoomDoc.getScreeningRoomFloor(), screeningRoom.getScreeningRoomFloor());
        assertEquals(screeningRoomDoc.getScreeningRoomNumber(), screeningRoom.getScreeningRoomNumber());
        assertEquals(screeningRoomDoc.getNumberOfAvailableSeats(), screeningRoom.getNumberOfAvailableSeats());
        assertEquals(screeningRoomDoc.isScreeningRoomStatusActive(), screeningRoom.isScreeningRoomStatusActive());
    }
}
