package model.managers;

import model.Client;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.repositories.ClientRepository;

import java.util.List;
import java.util.UUID;

public class ClientManager {

    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client register(String clientName, String clientSurname, int clientAge) {
        Client clientToRepo = null;
        try {
            clientToRepo = clientRepository.create(clientName, clientSurname, clientAge);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return clientToRepo;
    }

    public void unregister(Client client) {
        try {
            clientRepository.expire(client);
        } catch (RepositoryDeleteException exception) {
            exception.printStackTrace();
        }
    }

    public Client get(UUID identifier) {
        Client client = null;
        try {
            client = clientRepository.findByUUID(identifier);
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    public List<Client> getAll() {
        List<Client> listOfClients = null;
        try {
            listOfClients = clientRepository.findAll();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfClients;
    }

    public List<Client> getAllActive() {
        List<Client> listOfClients = null;
        try {
            listOfClients = clientRepository.findAllActive();
        } catch (RepositoryReadException exception) {
            exception.printStackTrace();
        }
        return listOfClients;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
