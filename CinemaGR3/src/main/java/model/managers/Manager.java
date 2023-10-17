package model.managers;

import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.repositories.Repository;

import java.util.List;
import java.util.UUID;

public abstract class Manager<Type> {

    private Repository<Type> objectRepository;

    // Constructor

    public Manager(Repository<Type> objectRepository) {
        this.objectRepository = objectRepository;
    }

    public Repository<Type> getObjectRepository() {
        return objectRepository;
    }

    public void setObjectRepository(Repository<Type> objectRepository) {
        this.objectRepository = objectRepository;
    }

    public Type get(UUID identifier) {
        Type element = null;
        try {
            element = objectRepository.findByUUID(identifier);
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return element;
    }
    public void unregister(Type element) {
        try {
            objectRepository.delete(element);
        } catch (RepositoryDeleteException exception) {
            exception.printStackTrace();
        }
    }
    public List<Type> getAll() {
        List<Type> listOfElements = null;
        try {
            listOfElements = objectRepository.findAll();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfElements;
    }
}
