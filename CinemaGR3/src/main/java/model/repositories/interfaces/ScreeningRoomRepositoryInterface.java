package model.repositories.interfaces;

import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;

import java.util.UUID;

public interface ScreeningRoomRepositoryInterface extends RepositoryInterface<ScreeningRoom> {

    ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats);
    ScreeningRoomDoc findScreeningRoomDoc(UUID screeningRoomDocId);
}
