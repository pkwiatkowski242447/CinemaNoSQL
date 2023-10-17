package model.managers;

import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.repositories.Repository;

import java.util.UUID;

public class ScreeningRoomManager extends Manager<ScreeningRoom> {
    public ScreeningRoomManager(Repository<ScreeningRoom> objectRepository) {
        super(objectRepository);
    }

    public ScreeningRoom register(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        ScreeningRoom screeningRoomToRepo = null;
        try{
            screeningRoomToRepo = new ScreeningRoom(UUID.randomUUID(), screeningRoomFloor, screeningRoomNumber, numberOfSeats);
            getObjectRepository().create(screeningRoomToRepo);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return screeningRoomToRepo;
    }
}
