package model.managers;

import model.Client;
import model.exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.update_exceptions.ClientRepositoryUpdateException;
import model.repositories.implementations.ClientRepository;

import java.util.List;
import java.util.UUID;

public class ClientManager {

    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client register(String clientName, String clientSurname, int clientAge) {
        Client client = null;
        try {
            client = clientRepository.create(clientName, clientSurname, clientAge);
        } catch (ClientRepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    public void unregister(Client client) {
        try {
            clientRepository.expire(client.getClientID());
        } catch (ClientRepositoryUpdateException exception) {
            exception.printStackTrace();
        }
    }

    public Client get(UUID identifier) {
        Client client = null;
        try {
            client = clientRepository.findByUUID(identifier);
        } catch (ClientRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    public List<Client> getAll() {
        List<Client> listOfAllClients = null;
        try {
            listOfAllClients = clientRepository.findAll();
        } catch (ClientRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfAllClients;
    }

    public List<Client> getAllActive() {
        List<Client> listOfAllActiveClients = null;
        try {
            listOfAllActiveClients = clientRepository.findAllActive();
        } catch (ClientRepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfAllActiveClients;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
