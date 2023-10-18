package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Movie;
import model.Ticket;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;

import java.util.List;
import java.util.UUID;

public class MovieRepository extends Repository<Movie> {

    public MovieRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void delete(Movie movie) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(movie, LockModeType.PESSIMISTIC_WRITE);

            CriteriaQuery<Ticket> findAllTicketsForAGivenMovie = getEntityManager().getCriteriaBuilder().createQuery(Ticket.class);
            Root<Ticket> ticketRoot = findAllTicketsForAGivenMovie.from(Ticket.class);
            findAllTicketsForAGivenMovie.select(ticketRoot).where(getEntityManager().getCriteriaBuilder().equal(ticketRoot.get("movie"), movie));
            List<Ticket> listOfTickets = getEntityManager().createQuery(findAllTicketsForAGivenMovie).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();

            for (Ticket ticket : listOfTickets) {
                getEntityManager().remove(ticket);
            }

            getEntityManager().remove(getEntityManager().merge(movie));
            getEntityManager().getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryDeleteException("Source: MovieRepository ; " + exception.getMessage(), exception);
        }
    }

    @Override
    public Movie findByUUID(UUID identifier) {
        Movie movieToBeRead = null;
        try {
            getEntityManager().getTransaction().begin();
            movieToBeRead = getEntityManager().find(Movie.class, identifier, LockModeType.PESSIMISTIC_READ);
            getEntityManager().getTransaction().commit();
        } catch (IllegalArgumentException | TransactionRequiredException | OptimisticLockException |
                 PessimisticLockException | LockTimeoutException exception) {
            throw new RepositoryReadException("Source: MovieRepository ; " + exception.getMessage(), exception);
        }
        return movieToBeRead;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> listOfAllMovie = null;
        try {
            getEntityManager().getTransaction().begin();
            CriteriaQuery<Movie> findAllMovies = getEntityManager().getCriteriaBuilder().createQuery(Movie.class);
            Root<Movie> movieRoot = findAllMovies.from(Movie.class);
            findAllMovies.select(movieRoot);
            listOfAllMovie = getEntityManager().createQuery(findAllMovies).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
            getEntityManager().getTransaction().commit();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            throw new RepositoryReadException("Source: MovieRepository ; " + exception.getMessage(), exception);
        }
        return listOfAllMovie;
    }
}
