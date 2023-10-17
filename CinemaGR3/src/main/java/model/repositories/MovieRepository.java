package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Movie;
import model.exceptions.repository_exceptions.RepositoryReadException;

import java.util.List;
import java.util.UUID;

public class MovieRepository extends Repository<Movie> {

    @Override
    public Movie findByUUID(UUID identifier) {
        Movie movieToBeRead = null;
        try {
            getEntityManager().getTransaction().begin();
            movieToBeRead = getEntityManager().find(Movie.class, identifier, LockModeType.PESSIMISTIC_READ);
            getEntityManager().getTransaction().commit();
        } catch (IllegalArgumentException | TransactionRequiredException | OptimisticLockException |
                 PessimisticLockException | LockTimeoutException exception) {
            getEntityManager().getTransaction().rollback();
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
            getEntityManager().getTransaction().rollback();
            throw new RepositoryReadException("Source: MovieRepository ; " + exception.getMessage(), exception);
        }
        return listOfAllMovie;
    }
}
