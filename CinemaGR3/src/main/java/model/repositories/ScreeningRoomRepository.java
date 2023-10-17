package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryReadException;

import java.util.List;
import java.util.UUID;

public class ScreeningRoomRepository extends Repository<ScreeningRoom> {
    @Override
    public ScreeningRoom findByUUID(UUID identifier) {
        ScreeningRoom screeningRoomToBeRead = null;
        try {
            getEntityManager().getTransaction().begin();
            screeningRoomToBeRead = getEntityManager().find(ScreeningRoom.class, identifier, LockModeType.PESSIMISTIC_READ);
            getEntityManager().getTransaction().commit();
        } catch (IllegalArgumentException | TransactionRequiredException | OptimisticLockException |
                 PessimisticLockException | LockTimeoutException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryReadException("Source: ScreeningRoomRepository ; " + exception.getMessage(), exception);
        }
        return screeningRoomToBeRead;
    }

    @Override
    public List<ScreeningRoom> findAll() {
        List<ScreeningRoom> listOfAllScreeningRooms = null;
        try {
            getEntityManager().getTransaction().begin();
            CriteriaQuery<ScreeningRoom> findAllScreeningRooms = getEntityManager().getCriteriaBuilder().createQuery(ScreeningRoom.class);
            Root<ScreeningRoom> screeningRoomRoot = findAllScreeningRooms.from(ScreeningRoom.class);
            findAllScreeningRooms.select(screeningRoomRoot);
            listOfAllScreeningRooms = getEntityManager().createQuery(findAllScreeningRooms).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
            getEntityManager().getTransaction().commit();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryReadException("Source: ScreeningRoomRepository ; " + exception.getMessage(), exception);
        }
        return listOfAllScreeningRooms;
    }
}
