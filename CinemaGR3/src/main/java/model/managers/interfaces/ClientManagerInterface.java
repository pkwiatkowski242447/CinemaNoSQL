package model.managers.interfaces;

import model.exceptions.managers.create_exceptions.ClientManagerCreateException;
import model.exceptions.managers.read_exceptions.ClientManagerReadException;
import model.exceptions.managers.update_exceptions.ClientManagerUpdateException;
import model.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientManagerInterface extends ManagerInterface<Client> {

    // C - create methods for client manager interface

    Client create(String clientName, String clientSurname, int clientAge) throws ClientManagerCreateException;

    // R - read methods for client manager interface

    List<Client> findAllActiveClients() throws ClientManagerReadException;

    // U - update methods for client manager interface

    void deactivate(UUID clientId) throws ClientManagerUpdateException;
    void activate(UUID clientId) throws ClientManagerUpdateException;
}
