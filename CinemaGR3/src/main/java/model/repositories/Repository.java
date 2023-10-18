package model.repositories;

import jakarta.persistence.*;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;

import java.util.List;
import java.util.UUID;

public abstract class Repository<Type> {

    private final EntityManager entityManager;

    // Constructor

    public Repository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    // Declaring CRUD methods.

    public void create(Type element) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(element);
            entityManager.getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            entityManager.getTransaction().rollback();
            throw new RepositoryCreateException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }
    public void update(Type element) {
        try {
            entityManager.getTransaction().begin();
            entityManager.lock(element, LockModeType.PESSIMISTIC_WRITE);
            entityManager.merge(element);
            entityManager.getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            entityManager.getTransaction().rollback();
            throw new RepositoryUpdateException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }
    public void delete(Type element) {
        try {
            entityManager.getTransaction().begin();
            entityManager.lock(element, LockModeType.PESSIMISTIC_WRITE);
            entityManager.remove(entityManager.merge(element));
            entityManager.getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            entityManager.getTransaction().rollback();
            throw new RepositoryDeleteException("Source: " + element.getClass() + "Repository ; " + exception.getMessage(), exception);
        }
    }

    // Other methods

    public abstract Type findByUUID(UUID identifier);
    public abstract List<Type> findAll();

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
