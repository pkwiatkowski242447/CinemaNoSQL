package model.repositories.decorators;

import mapping_layer.model_docs.ScreeningRoomRow;
import model.ScreeningRoom;
import model.repositories.interfaces.ScreeningRoomRepositoryInterface;

import java.util.List;
import java.util.UUID;

public class ScreeningRoomRepositoryDecorator implements ScreeningRoomRepositoryInterface {

    protected ScreeningRoomRepositoryInterface wrappedObject;

    public ScreeningRoomRepositoryDecorator(ScreeningRoomRepositoryInterface wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    @Override
    public ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        return wrappedObject.create(screeningRoomFloor, screeningRoomNumber, numberOfSeats);
    }

    @Override
    public ScreeningRoom findByUUID(UUID elementID) {
        return wrappedObject.findByUUID(elementID);
    }

    @Override
    public List<ScreeningRoom> findAll() {
        return wrappedObject.findAll();
    }

    @Override
    public List<ScreeningRoom> findAllActive() {
        return wrappedObject.findAllActive();
    }

    @Override
    public List<UUID> findAllUUIDs() {
        return wrappedObject.findAllUUIDs();
    }

    @Override
    public void updateAllFields(ScreeningRoom element) {
        wrappedObject.updateAllFields(element);
    }

    @Override
    public void delete(ScreeningRoom element) {
        wrappedObject.delete(element);
    }

    @Override
    public void delete(UUID elementID) {
        wrappedObject.delete(elementID);
    }

    @Override
    public void expire(ScreeningRoom element) {
        wrappedObject.expire(element);
    }

    @Override
    public ScreeningRoomRow findScreeningRoomDoc(UUID screeningRoomDocId) {
        return wrappedObject.findScreeningRoomDoc(screeningRoomDocId);
    }

    @Override
    public void close() {
        wrappedObject.close();
    }
}
