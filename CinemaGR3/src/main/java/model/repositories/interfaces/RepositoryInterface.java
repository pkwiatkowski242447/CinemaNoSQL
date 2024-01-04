package model.repositories.interfaces;

import model.exceptions.delete_exceptions.RepositoryDeleteException;
import model.exceptions.read_exceptions.RepositoryReadException;
import model.exceptions.update_exceptions.RepositoryUpdateException;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;

public interface RepositoryInterface<Type> extends Closeable {

    // R - read methods of repository interface

    Type findByUUID(UUID elementID) throws RepositoryReadException;
    List<Type> findAll() throws RepositoryReadException;

    // U - update methods of repository interface

    void update(Type element) throws RepositoryUpdateException;

    // D - delete methods of repository interface

    void delete(Type element) throws RepositoryDeleteException;
    void delete(UUID elementID) throws RepositoryDeleteException;

    // Close method


    @Override
    void close();
}
