package model.repositories.interfaces;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;

public interface RepositoryInterface<Type> extends Closeable {

    // R - read methods of repository interface

    Type findByUUID(UUID elementID);
    List<Type> findAll();
    List<Type> findAllActive();

    // U - update methods of repository interface

    void update(Type element);

    // D - delete methods of repository interface

    void delete(Type element);
    void delete(UUID elementID);
    void expire(Type element);

    // Close method


    @Override
    void close();
}
