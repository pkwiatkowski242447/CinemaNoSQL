package model.managers.interfaces;

import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.update_exceptions.UpdateManagerException;

import java.util.List;
import java.util.UUID;

public interface ManagerInterface<Type> {

    // R - read methods for manager interface

    Type findByUUID(UUID elementId) throws ReadManagerException;
    List<Type> findAll() throws ReadManagerException;

    // D - delete methods for manager interface

    void delete(Type element) throws DeleteManagerException;
    void delete(UUID elementId) throws DeleteManagerException;

    // U - update methods for manager interface

    void update(Type element) throws UpdateManagerException;
}
