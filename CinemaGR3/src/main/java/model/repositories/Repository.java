package model.repositories;

import jakarta.persistence.*;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;

import java.util.List;
import java.util.UUID;

public abstract class Repository<Type> {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    // Declaring CRUD methods.

    public void create(Type element) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(element);
            getEntityManager().getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryCreateException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }
    public void update(Type element) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(element, LockModeType.PESSIMISTIC_WRITE);
            getEntityManager().merge(element);
            getEntityManager().getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryUpdateException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }
    public void delete(Type element) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(element, LockModeType.PESSIMISTIC_WRITE);
            getEntityManager().remove(getEntityManager().merge(element));
            getEntityManager().getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryDeleteException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }

    // Other methods

    public abstract Type findByUUID(UUID identifier);
    public abstract List<Type> findAll();

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
