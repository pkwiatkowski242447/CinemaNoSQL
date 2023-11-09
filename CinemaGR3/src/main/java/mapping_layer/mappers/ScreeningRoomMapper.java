package mapping_layer.mappers;

import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;

public class ScreeningRoomMapper {

    public static ScreeningRoomDoc toScreeningRoomDoc(ScreeningRoom screeningRoom) {
        ScreeningRoomDoc screeningRoomDoc = new ScreeningRoomDoc();
        screeningRoomDoc.setScreeningRoomID(screeningRoom.getScreeningRoomID());
        screeningRoomDoc.setScreeningRoomFloor(screeningRoom.getScreeningRoomFloor());
        screeningRoomDoc.setScreeningRoomNumber(screeningRoom.getScreeningRoomNumber());
        screeningRoomDoc.setNumberOfAvailableSeats(screeningRoom.getNumberOfAvailableSeats());
        screeningRoomDoc.setScreeningRoomStatusActive(screeningRoom.isScreeningRoomStatusActive());
        return screeningRoomDoc;
    }

    public static ScreeningRoom toScreeningRoom(ScreeningRoomDoc screeningRoomDoc) {
        return new ScreeningRoom(screeningRoomDoc.getScreeningRoomID(),
                screeningRoomDoc.getScreeningRoomFloor(),
                screeningRoomDoc.getScreeningRoomNumber(),
                screeningRoomDoc.getNumberOfAvailableSeats(),
                screeningRoomDoc.isScreeningRoomStatusActive());
    }
}
