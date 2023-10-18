package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;

import java.util.List;
import java.util.UUID;

public class ScreeningRoomRepository extends Repository<ScreeningRoom> {

    public ScreeningRoomRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void delete(ScreeningRoom screeningRoom) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(screeningRoom, LockModeType.PESSIMISTIC_WRITE);

            CriteriaQuery<Movie> findAllMoviesForAGivenScreeningRoom = getEntityManager().getCriteriaBuilder().createQuery(Movie.class);
            Root<Movie> movieRoot = findAllMoviesForAGivenScreeningRoom.from(Movie.class);
            findAllMoviesForAGivenScreeningRoom.select(movieRoot).where(getEntityManager().getCriteriaBuilder().equal(movieRoot.get("screeningRoom"), screeningRoom));
            List<Movie> listOfMovies = getEntityManager().createQuery(findAllMoviesForAGivenScreeningRoom).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();

            for (Movie movie : listOfMovies) {

                CriteriaQuery<Ticket> findAllTicketsForAGivenMovie = getEntityManager().getCriteriaBuilder().createQuery(Ticket.class);
                Root<Ticket> ticketRoot = findAllTicketsForAGivenMovie.from(Ticket.class);
                findAllTicketsForAGivenMovie.select(ticketRoot).where(getEntityManager().getCriteriaBuilder().equal(ticketRoot.get("movie"), movie));
                List<Ticket> listOfTickets = getEntityManager().createQuery(findAllTicketsForAGivenMovie).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();

                for (Ticket ticket : listOfTickets) {
                    getEntityManager().remove(ticket);
                }

                getEntityManager().remove(movie);
            }

            getEntityManager().remove(getEntityManager().merge(screeningRoom));
            getEntityManager().getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryDeleteException("Source: ScreeningRoomRepository ; " + exception.getMessage(), exception);
        }
    }

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
