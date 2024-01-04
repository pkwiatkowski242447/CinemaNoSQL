package repository_tests;

import model.Client;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.read_exceptions.ClientRepositoryReadException;
import model.exceptions.update_exceptions.ClientRepositoryUpdateException;
import model.exceptions.update_exceptions.RepositoryUpdateException;
import model.repositories.implementations.ClientRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;
    private static ClientRepository clientRepositoryForTests;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        clientRepositoryForTests = new ClientRepository();
    }

    @BeforeEach
    public void insertExampleClients() {
        String clientNo1Name = "John";
        String clientNo1Surname = "Smith";
        int clientNo1Age = 21;

        String clientNo2Name = "Mary";
        String clientNo2Surname = "Jane";
        int clientNo2Age = 18;

        String clientNo3Name = "Vincent";
        String clientNo3Surname = "Vega";
        int clientNo3Age = 40;

        try {
            clientNo1 = clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
            clientNo2 = clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
            clientNo3 = clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);
        } catch (ClientRepositoryCreateException exception) {
            throw new RuntimeException("Sample clients could not be created in the repository correctly.", exception);
        }
    }

    @AfterEach
    public void deleteExampleClients() {
        try {
            List<Client> listOfAllClients = clientRepositoryForTests.findAll();
            for (Client client : listOfAllClients) {
                clientRepositoryForTests.delete(client);
            }
        } catch (ClientRepositoryDeleteException exception) {
            throw new RuntimeException("Sample clients could not be deleted from the repository.", exception);
        } catch (ClientRepositoryReadException exception) {
            throw new RuntimeException("Sample clients could not be read the repository.", exception);
        }
    }

    @AfterAll
    public static void destroy() {
        clientRepositoryForTests.close();
    }

    @Test
    public void clientRepositoryConstructorTest() throws CassandraConfigNotFound {
        ClientRepository clientRepository = new ClientRepository();
        assertNotNull(clientRepository);
    }

    @Test
    public void createNewClientTestPositive() throws ClientRepositoryCreateException, ClientRepositoryReadException{
        Client newClient = clientRepositoryForTests.create("Stefania", "Czarnecka", 80);
        Client createdClient = clientRepositoryForTests.findByUUID(newClient.getClientID());
        assertEquals(createdClient, newClient);
    }

    @Test
    public void createNewClientTestNegativeNameNull() {
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(null, "Czarnecka", 80));
    }

    @Test
    public void createNewClientTestNegativeNameEmpty() {
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create("", "Czarnecka", 80));
    }

    @Test
    public void createNewClientTestNegativeNameTooLong() {
        String name = "ddddddddddddddddddddddddddddddddddddddddddddddddddd";
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(name, "Czarnecka", 80));
    }

    @Test
    public void createNewClientTestNegativeSurnameEmpty() {
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create("Stefania", "", 80));
    }

    @Test
    public void createNewClientTestNegativeSurnameTooLong() {
        String surname = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create("Stefania", surname, 80));
    }

    @Test
    public void createNewClientTestNegativeAgeTooLesserThan18() {
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create("Stefania", "Czarnecka", 17));
    }

    @Test
    public void createNewClientTestNegativeAgeGreaterThan120() {
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create("Stefania", "Czarnecka", 121));
    }

    @Test
    public void updateCertainClientTestPositive() throws ClientRepositoryReadException {
        String oldSurname = clientNo1.getClientSurname();
        String newSurname = "Doe";
        clientNo1.setClientSurname(newSurname);
        assertDoesNotThrow(() -> clientRepositoryForTests.update(clientNo1));
        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(clientNo1, foundClient);
        assertEquals(newSurname, foundClient.getClientSurname());
        assertNotEquals(oldSurname, foundClient.getClientSurname());
    }

    @Test
    public void updateCertainClientTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(newClient));
    }

    @Test
    public void updateCertainClientWithNullNameTestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName(null);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithEmptyNameTestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName("");
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithNameTooLongTestNegative() throws ClientRepositoryReadException {
        String newName = "ddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName(newName);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithNullSurnameTestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname(null);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithEmptySurnameTestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname("");
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithSurnameTooLongTestNegative() throws ClientRepositoryReadException {
        String newSurname = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname(newSurname);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithAgeLesserThan18TestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientAge(17);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithAgeGreaterThan120TestNegative() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientAge(121);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void deleteCertainClientTestPositive() throws ClientRepositoryReadException {
        UUID removedClientUUID = clientNo1.getClientID();
        int numberOfClientsBeforeDelete = clientRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> clientRepositoryForTests.delete(clientNo1));
        int numberOfClientsAfterDelete = clientRepositoryForTests.findAll().size();
        assertNotEquals(numberOfClientsBeforeDelete, numberOfClientsAfterDelete);
        assertEquals(numberOfClientsBeforeDelete - 1, numberOfClientsAfterDelete);
        assertThrows(ClientRepositoryReadException.class, () -> {
            Client foundClient = clientRepositoryForTests.findByUUID(removedClientUUID);
        });
    }

    @Test
    public void deleteCertainClientThatIsNotInTheDatabaseTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(ClientRepositoryDeleteException.class, () -> clientRepositoryForTests.delete(newClient));
    }

    @Test
    public void deleteCertainClientWithUUIDThatIsNotInTheDatabaseTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(ClientRepositoryDeleteException.class, () -> clientRepositoryForTests.delete(newClient.getClientID()));
    }

    @Test
    public void expireCertainClientTestPositive() throws ClientRepositoryReadException, ClientRepositoryUpdateException {
        UUID expiredClientUUID = clientNo1.getClientID();
        int beforeExpiringClient = clientRepositoryForTests.findAll().size();
        int numOfActiveClientsBefore = clientRepositoryForTests.findAllActive().size();
        clientRepositoryForTests.expire(clientNo1.getClientID());
        int afterExpiringClient = clientRepositoryForTests.findAll().size();
        int numOfActiveClientsAfter = clientRepositoryForTests.findAllActive().size();
        Client foundClient = clientRepositoryForTests.findByUUID(expiredClientUUID);
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
        assertFalse(clientNo1.isClientStatusActive());
        assertEquals(beforeExpiringClient, afterExpiringClient);
        assertNotEquals(numOfActiveClientsBefore, numOfActiveClientsAfter);
        assertEquals(numOfActiveClientsBefore - 1, numOfActiveClientsAfter);
    }

    @Test
    public void expireCertainClientTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(ClientRepositoryUpdateException.class, () -> {
            clientRepositoryForTests.expire(newClient.getClientID());
        });
    }

    @Test
    public void findCertainClientTestPositive() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void findCertainClientTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(ClientRepositoryReadException.class, () -> {
            Client foundClient = clientRepositoryForTests.findByUUID(newClient.getClientID());
        });
    }

    @Test
    public void findAllClientsTestPositive() throws ClientRepositoryReadException {
        List<Client> listOfAllClients = clientRepositoryForTests.findAll();
        assertNotNull(listOfAllClients);
        assertEquals(3, listOfAllClients.size());
    }

    @Test
    public void findAllActiveClientsTestPositive()  throws ClientRepositoryReadException, ClientRepositoryUpdateException {
        List<Client> startingListOfClients = clientRepositoryForTests.findAllActive();
        assertNotNull(startingListOfClients);
        clientRepositoryForTests.expire(startingListOfClients.get(0).getClientID());
        List<Client> endingListOfClients = clientRepositoryForTests.findAllActive();
        assertNotNull(endingListOfClients);
        assertEquals(startingListOfClients.size(), 3);
        assertEquals(endingListOfClients.size(), 2);
    }
}
