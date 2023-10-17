package model.managers;

import model.Client;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.repositories.Repository;

import java.util.UUID;

public class ClientManager extends Manager<Client> {
    public ClientManager(Repository<Client> objectRepository) {
        super(objectRepository);
    }

    public Client register(String clientName, String clientSurname, int clientAge) {
        Client clientToRepo = null;
        try {
            clientToRepo = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
            getObjectRepository().create(clientToRepo);
        } catch (RepositoryCreateException exception) {
            exception.printStackTrace();
        }
        return clientToRepo;
    }
}
