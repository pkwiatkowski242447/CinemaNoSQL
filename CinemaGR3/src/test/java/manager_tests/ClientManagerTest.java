package manager_tests;

import model.Client;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
import model.managers.ClientManager;
import model.repositories.implementations.ClientRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientManagerTest {

    private final static String databaseName = "test";
    private static ClientRepository clientRepositoryForTests;
    private static ClientManager clientManagerForTests;

    @BeforeAll
    public static void init() throws MongoConfigNotFoundException  {
        clientRepositoryForTests = new ClientRepository(databaseName);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        clientRepositoryForTests.close();
    }

    @BeforeEach
    public void populateClientRepositoryForTests() {
        String clientNo1Name = "John";
        String clientNo1Surname = "Smith";
        int clientNo1Age = 21;
        String clientNo2Name = "Mary";
        String clientNo2Surname = "Jane";
        int clientNo2Age = 18;
        String clientNo3Name = "Vincent";
        String clientNo3Surname = "Vega";
        int clientNo3Age = 40;

        clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
        clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
        clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);
    }

    @AfterEach
    public void depopulateClientRepositoryForTests() {
        List<Client> listOfClients = clientRepositoryForTests.findAll();
        for (Client client : listOfClients) {
            clientRepositoryForTests.delete(client);
        }
    }

    @Test
    public void createClientManagerTest() throws MongoConfigNotFoundException {
        ClientRepository clientRepository = new ClientRepository(databaseName);
        assertNotNull(clientRepository);
        ClientManager clientManager = new ClientManager(clientRepository);
        assertNotNull(clientManager);
        clientRepository.close();
    }

    @Test
    public void setClientRepositoryForClientManagerTest() throws MongoConfigNotFoundException {
        ClientRepository clientRepositoryNo1 = new ClientRepository(databaseName);
        assertNotNull(clientRepositoryNo1);
        ClientRepository clientRepositoryNo2 = new ClientRepository(databaseName);
        assertNotNull(clientRepositoryNo2);
        ClientManager clientManager = new ClientManager(clientRepositoryNo1);
        assertNotNull(clientManager);
        clientManager.setClientRepository(clientRepositoryNo2);
        assertNotEquals(clientRepositoryNo1, clientManager.getClientRepository());
        assertEquals(clientRepositoryNo2, clientManager.getClientRepository());
        clientRepositoryNo1.close();
        clientRepositoryNo2.close();
    }

    @Test
    public void registerNewClientTestTestPositive() {
        int numOfClientsBefore = clientManagerForTests.getAll().size();
        Client client = clientManagerForTests.register("Stefania", "Czarnecka", 80);
        assertNotNull(client);
        int numOfClientsAfter = clientManagerForTests.getAll().size();
        assertNotEquals(numOfClientsBefore, numOfClientsAfter);
    }

    @Test
    public void registerNewClientTestNegative() {
        Client client = clientManagerForTests.register(null, "Czarnecka", 80);
        assertNull(client);
    }

    @Test
    public void unregisterCertainClientTestPositive() {
        int numberOfClientsBefore = clientManagerForTests.getAllActive().size();
        Client someClientFromRepo = clientManagerForTests.getAllActive().get(0);
        assertNotNull(someClientFromRepo);
        assertTrue(someClientFromRepo.isClientStatusActive());
        UUID unregisteredClientID = someClientFromRepo.getClientID();
        assertDoesNotThrow(() -> clientManagerForTests.unregister(someClientFromRepo));
        int numberOfClientsAfter = clientManagerForTests.getAllActive().size();
        Client foundClient = clientManagerForTests.get(unregisteredClientID);
        assertNotNull(foundClient);
        assertFalse(foundClient.isClientStatusActive());
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
        Client someClientFromRepo = clientManagerForTests.getAll().get(0);
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
        List<Client> listOfAllClientsNo1 = clientManagerForTests.getClientRepository().findAll();
        List<Client> listOfAllClientsNo2 = clientManagerForTests.getAll();
        assertNotNull(listOfAllClientsNo1);
        assertNotNull(listOfAllClientsNo2);
        assertEquals(listOfAllClientsNo1.size(), listOfAllClientsNo2.size());
    }
}
