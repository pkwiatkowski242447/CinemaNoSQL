package model.repositories.interfaces;

public interface ScreeningRoomRepositoryInterface extends RepositoryInterface<ScreeningRoom> {

    ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats);
}
