package model.managers.implementations;

import lombok.Getter;
import lombok.Setter;
import model.exceptions.managers.create_exceptions.ClientManagerCreateException;
import model.exceptions.managers.delete_exceptions.ClientManagerDeleteException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.read_exceptions.ClientManagerReadException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.update_exceptions.ClientManagerUpdateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.update_exceptions.ClientRepositoryUpdateException;
import model.managers.interfaces.ClientManagerInterface;
import model.model.Client;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.repositories.implementations.ClientRepository;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class ClientManager implements ClientManagerInterface {

    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Create methods

    @Override
    public Client create(String clientName, String clientSurname, int clientAge) throws ClientManagerCreateException {
        try {
            return this.clientRepository.create(clientName, clientSurname, clientAge);
        } catch (ClientRepositoryCreateException exception) {
            throw new ClientManagerCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Client findByUUID(UUID clientId) throws ReadManagerException {
        try {
            return this.clientRepository.findByUUID(clientId);
        } catch (ClientRepositoryReadException exception) {
            throw new ClientManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Client> findAll() throws ReadManagerException {
        try {
            return this.clientRepository.findAll();
        } catch (ClientRepositoryReadException exception) {
            throw new ClientManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Client> findAllActiveClients() throws ClientManagerReadException {
        try {
            return this.clientRepository.findAllActive();
        } catch (ClientRepositoryReadException exception) {
            throw new ClientManagerReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Client client) throws ClientManagerUpdateException {
        try {
            this.clientRepository.update(client);
        } catch (ClientRepositoryUpdateException exception) {
            throw new ClientManagerUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void deactivate(UUID clientId) throws ClientManagerUpdateException {
        try {
            this.clientRepository.expire(clientId);
        } catch (ClientRepositoryUpdateException exception) {
            throw new ClientManagerUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void activate(UUID clientId) throws ClientManagerUpdateException {
        try {
            Client client = this.clientRepository.findByUUID(clientId);
            client.setClientStatusActive(true);
            this.clientRepository.update(client);
        } catch (ClientRepositoryUpdateException | ClientRepositoryReadException exception) {
            throw new ClientManagerUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Client client) throws DeleteManagerException {
        try {
            this.clientRepository.delete(client);
        } catch (ClientRepositoryDeleteException exception) {
            throw new ClientManagerDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID clientId) throws DeleteManagerException {
        try {
            this.clientRepository.delete(clientId);
        } catch (ClientRepositoryDeleteException exception) {
            throw new ClientManagerDeleteException(exception.getMessage(), exception);
        }
    }
}
