package model.repositories.interfaces;

import mapping_layer.model_docs.ScreeningRoomRow;
import model.ScreeningRoom;

import java.util.UUID;

public interface ScreeningRoomRepositoryInterface extends RepositoryInterface<ScreeningRoom> {

    ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats);
    ScreeningRoomRow findScreeningRoomDoc(UUID screeningRoomDocId);
}
