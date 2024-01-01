package mapping_layer.converters;

import mapping_layer.model_docs.ScreeningRoomRow;
import model.ScreeningRoom;

public class ScreeningRoomConverter {

    public static ScreeningRoomRow toScreeningRoomRow(ScreeningRoom screeningRoom) {
        ScreeningRoomRow screeningRoomRow = new ScreeningRoomRow();
        screeningRoomRow.setScreeningRoomID(screeningRoom.getScreeningRoomID());
        screeningRoomRow.setScreeningRoomFloor(screeningRoom.getScreeningRoomFloor());
        screeningRoomRow.setScreeningRoomNumber(screeningRoom.getScreeningRoomNumber());
        screeningRoomRow.setNumberOfAvailableSeats(screeningRoom.getNumberOfAvailableSeats());
        screeningRoomRow.setScreeningRoomStatusActive(screeningRoom.isScreeningRoomStatusActive());
        return screeningRoomRow;
    }

    public static ScreeningRoom toScreeningRoom(ScreeningRoomRow screeningRoomRow) {
        return new ScreeningRoom(screeningRoomRow.getScreeningRoomID(),
                screeningRoomRow.getScreeningRoomFloor(),
                screeningRoomRow.getScreeningRoomNumber(),
                screeningRoomRow.getNumberOfAvailableSeats(),
                screeningRoomRow.isScreeningRoomStatusActive());
    }
}
