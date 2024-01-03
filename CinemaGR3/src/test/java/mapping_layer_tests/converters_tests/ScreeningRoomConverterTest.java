package mapping_layer_tests.converters_tests;

import mapping_layer.converters.ScreeningRoomConverter;
import mapping_layer.model_rows.ScreeningRoomRow;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScreeningRoomConverterTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void screeningRoomMapperConstructorTest() {
        ScreeningRoomConverter screeningRoomConverter = new ScreeningRoomConverter();
        assertNotNull(screeningRoomConverter);
    }

    @Test
    public void screeningRoomMapperFromScreeningRoomToScreeningRoomDoc() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 4, 40);
        assertNotNull(screeningRoom);
        ScreeningRoomRow screeningRoomRow = ScreeningRoomConverter.toScreeningRoomRow(screeningRoom);
        assertNotNull(screeningRoomRow);
        assertEquals(screeningRoom.getScreeningRoomID(), screeningRoomRow.getScreeningRoomID());
        assertEquals(screeningRoom.getScreeningRoomFloor(), screeningRoomRow.getScreeningRoomFloor());
        assertEquals(screeningRoom.getScreeningRoomNumber(), screeningRoomRow.getScreeningRoomNumber());
        assertEquals(screeningRoom.getNumberOfAvailableSeats(), screeningRoomRow.getNumberOfAvailableSeats());
        assertEquals(screeningRoom.isScreeningRoomStatusActive(), screeningRoomRow.isScreeningRoomStatusActive());
    }

    @Test
    public void screeningRoomMapperFromScreeningRoomDocToScreeningRoom() {
        ScreeningRoomRow screeningRoomRow = new ScreeningRoomRow(UUID.randomUUID(), true, 1, 4, 40);
        assertNotNull(screeningRoomRow);
        ScreeningRoom screeningRoom = ScreeningRoomConverter.toScreeningRoom(screeningRoomRow);
        assertNotNull(screeningRoom);
        assertEquals(screeningRoomRow.getScreeningRoomID(), screeningRoom.getScreeningRoomID());
        assertEquals(screeningRoomRow.getScreeningRoomFloor(), screeningRoom.getScreeningRoomFloor());
        assertEquals(screeningRoomRow.getScreeningRoomNumber(), screeningRoom.getScreeningRoomNumber());
        assertEquals(screeningRoomRow.getNumberOfAvailableSeats(), screeningRoom.getNumberOfAvailableSeats());
        assertEquals(screeningRoomRow.isScreeningRoomStatusActive(), screeningRoom.isScreeningRoomStatusActive());
    }
}
