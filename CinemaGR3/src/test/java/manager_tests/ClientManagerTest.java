package manager_tests;

import model.Client;
import model.managers.ClientManager;
import model.managers.Manager;
import model.repositories.ClientRepository;
import model.repositories.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientManagerTest {

    @Test
    public void createClientManagerTest() {
        Repository<Client> clientRepository = new ClientRepository();
        assertNotNull(clientRepository);
        Manager<Client> clientManager = new ClientManager(clientRepository);
        assertNotNull(clientManager);
    }

    @Test
    public void setClientRepositoryForClientManagerTest() {
        Repository<Client> clientRepositoryNo1 = new ClientRepository();
        assertNotNull(clientRepositoryNo1);
        Repository<Client> clientRepositoryNo2 = new ClientRepository();
        assertNotNull(clientRepositoryNo2);
        Manager<Client> clientManager = new ClientManager(clientRepositoryNo1);
        assertNotNull(clientManager);
        clientManager.setObjectRepository(clientRepositoryNo2);
        assertNotEquals(clientRepositoryNo1, clientManager.getObjectRepository());
        assertEquals(clientRepositoryNo2, clientManager.getObjectRepository());
    }

    private final Repository<Client> clientRepositoryForTests = new ClientRepository();
    private final ClientManager clientManagerForTests = new ClientManager(clientRepositoryForTests);


    @BeforeEach
    public void populateClientRepositoryForTests() {
        Client clientNo1 = new Client(UUID.randomUUID(), "John", "Smith", 21);
        Client clientNo2 = new Client(UUID.randomUUID(), "Mary", "Jane", 18);
        Client clientNo3 = new Client(UUID.randomUUID(), "Vincent", "Vega", 40);

        clientRepositoryForTests.create(clientNo1);
        clientRepositoryForTests.create(clientNo2);
        clientRepositoryForTests.create(clientNo3);
    }

    @AfterEach
    public void depopulateClientRepositoryForTests() {
        List<Client> listOfClients = clientRepositoryForTests.findAll();
        for (Client client : listOfClients) {
            clientRepositoryForTests.delete(client);
        }
    }

    @Test
    public void registerNewClientTestTestPositive() {
        int numOfClientsBefore = clientManagerForTests.getObjectRepository().findAll().size();
        Client client = clientManagerForTests.register("Stefania", "Czarnecka", 80);
        assertNotNull(client);
        int numOfClientsAfter = clientManagerForTests.getObjectRepository().findAll().size();
        assertNotEquals(numOfClientsBefore, numOfClientsAfter);
    }

    @Test
    public void registerNewClientTestNegative() {
        int numOfClientsBefore = clientManagerForTests.getObjectRepository().findAll().size();
        Client client = clientManagerForTests.register(null, "Czarnecka", 80);
        assertNotNull(client);
        int numOfClientsAfter = clientManagerForTests.getObjectRepository().findAll().size();
        assertEquals(numOfClientsBefore, numOfClientsAfter);
    }

    @Test
    public void unregisterCertainClientTestPositive() {
        int numberOfClientsBefore = clientManagerForTests.getObjectRepository().findAll().size();
        Client someClientFromRepo = clientManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someClientFromRepo);
        UUID unregisteredClientID = someClientFromRepo.getClientID();
        assertDoesNotThrow(() -> clientManagerForTests.unregister(someClientFromRepo));
        int numberOfClientsAfter = clientManagerForTests.getObjectRepository().findAll().size();
        Client foundClient = clientManagerForTests.get(unregisteredClientID);
        assertNull(foundClient);
        assertNotEquals(numberOfClientsBefore, numberOfClientsAfter);
    }

    @Test
    public void unregisterCertainClientTestNegative() {
        int numOfClientsBefore = clientManagerForTests.getAll().size();
        Client client = new Client(UUID.randomUUID(), "Stefiania", "Czarnecka", 80);
        assertNotNull(client);
        clientManagerForTests.unregister(client);
        int numOfClientsAfter = clientManagerForTests.getAll().size();
        assertEquals(numOfClientsBefore, numOfClientsAfter);
    }

    @Test
    public void getCertainClientFromClientRepositoryTestPositive() {
        Client someClientFromRepo = clientManagerForTests.getObjectRepository().findAll().get(0);
        assertNotNull(someClientFromRepo);
        Client foundClient = clientManagerForTests.get(someClientFromRepo.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, someClientFromRepo);
    }

    @Test
    public void getCertainClientFromClientRepositoryTestNegative() {
        Client client = new Client(UUID.randomUUID(), "Stefiania", "Czarnecka", 80);
        assertNotNull(client);
        Client foundClient = clientManagerForTests.get(client.getClientID());
        assertNull(foundClient);
    }

    @Test
    public void getAllClientsFromRepositoryTest() {
        List<Client> listOfAllClients = clientManagerForTests.getObjectRepository().findAll();
        assertNotNull(listOfAllClients);
        assertEquals(3, listOfAllClients.size());
    }
}
