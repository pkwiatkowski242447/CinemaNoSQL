package model.managers;

import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.repositories.ScreeningRoomRepository;

import java.util.List;
import java.util.UUID;

public class ScreeningRoomManager {

    private ScreeningRoomRepository screeningRoomRepository;

    public ScreeningRoomManager(ScreeningRoomRepository screeningRoomRepository) {
        this.screeningRoomRepository = screeningRoomRepository;
    }

    public ScreeningRoom register(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        ScreeningRoom screeningRoomToRepo = null;
        try{
            screeningRoomToRepo = screeningRoomRepository.create(screeningRoomFloor, screeningRoomNumber, numberOfSeats);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return screeningRoomToRepo;
    }

    public void unregister(ScreeningRoom screeningRoom) {
        try {
            screeningRoomRepository.delete(screeningRoom);
        } catch (RepositoryDeleteException exception) {
            exception.printStackTrace();
        }
    }

    public ScreeningRoom get(UUID identifier) {
        ScreeningRoom screeningRoom = null;
        try {
            screeningRoom = screeningRoomRepository.findByUUID(identifier);
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return screeningRoom;
    }

    public List<ScreeningRoom> getAll() {
        List<ScreeningRoom> listOfScreeningRooms = null;
        try {
            listOfScreeningRooms = screeningRoomRepository.findAll();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfScreeningRooms;
    }

    public ScreeningRoomRepository getScreeningRoomRepository() {
        return screeningRoomRepository;
    }

    public void setScreeningRoomRepository(ScreeningRoomRepository screeningRoomRepository) {
        this.screeningRoomRepository = screeningRoomRepository;
    }
}
