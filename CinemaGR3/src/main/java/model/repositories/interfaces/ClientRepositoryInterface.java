package model.repositories.interfaces;

import model.model.Client;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.update_exceptions.ClientRepositoryUpdateException;

import java.util.List;
import java.util.UUID;

public interface ClientRepositoryInterface extends RepositoryInterface<Client> {

    // C - create methods of client repository interface

    Client create(String clientName, String clientSurname, int clientAge) throws ClientRepositoryCreateException;

    // R - read method for client repository interface

    List<Client> findAllActive() throws ClientRepositoryReadException;

    // U - update method for client repository interface

    void expire(UUID clientId) throws ClientRepositoryUpdateException;
}
