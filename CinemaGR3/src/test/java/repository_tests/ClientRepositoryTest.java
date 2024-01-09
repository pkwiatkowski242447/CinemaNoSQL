package repository_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.model.Client;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.exceptions.repositories.update_exceptions.ClientRepositoryUpdateException;
import model.exceptions.repositories.update_exceptions.RepositoryUpdateException;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.ClientRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private static CqlSession cqlSession;
    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;
    private static ClientRepository clientRepositoryForTests;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();
        clientRepositoryForTests = new ClientRepository(cqlSession);
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
        cqlSession.close();
    }

    @Test
    public void clientRepositoryConstructorTest() {
        ClientRepository clientRepository = new ClientRepository(cqlSession);
        assertNotNull(clientRepository);
    }

    @Test
    public void clientRepositoryCreateNewClientTestPositive() throws ClientRepositoryCreateException, ClientRepositoryReadException {
        String clientName = "Stefania";
        String clientSurname = "Czanecka";
        int clientAge = 35;

        Client newClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        Client createdClient = clientRepositoryForTests.findByUUID(newClient.getClientID());
        assertNotNull(createdClient);

        assertEquals(createdClient, newClient);
    }

    @Test
    public void clientRepositoryCreateNewClientWithNullNameTestNegative() {
        String clientName = null;
        String clientSurname = "Czanecka";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithEmptyNameTestNegative() {
        String clientName = "";
        String clientSurname = "Czanecka";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithNameTooShortTestNegative() {
        String clientName = "";
        String clientSurname = "Czanecka";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithNameTooLongTestNegative() {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        String clientSurname = "Czanecka";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithNameLengthEqualTo1TestPositive() throws ClientRepositoryCreateException {
        String clientName = "d";
        String clientSurname = "Czanecka";
        int clientAge = 35;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryCreateNewClientWithNameLengthEqualTo50TestPositive() throws ClientRepositoryCreateException {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        String clientSurname = "Czanecka";
        int clientAge = 35;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryCreateNewClientWithNullSurnameTestNegative() {
        String clientName = "Stefania";
        String clientSurname = null;
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithEmptySurnameTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithSurnameTooShortTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "d";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithSurnameTooLongTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        int clientAge = 35;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithSurnameLengthEqualTo2TestPositive() throws ClientRepositoryCreateException {
        String clientName = "Stefania";
        String clientSurname = "df";
        int clientAge = 35;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryCreateNewClientWithSurnameLengthEqualTo100TestPositive() throws ClientRepositoryCreateException {
        String clientName = "Stefania";
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        int clientAge = 35;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryCreateNewClientWithAgeLowerThan18TestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 17;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithAgeGreaterThan120TestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 121;
        assertThrows(ClientRepositoryCreateException.class, () -> clientRepositoryForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientRepositoryCreateNewClientWithAgeGreaterEqualTo18TestPositive() throws ClientRepositoryCreateException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 18;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryCreateNewClientWithAgeGreaterEqualTo120TestPositive() throws ClientRepositoryCreateException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 120;

        Client testClient = clientRepositoryForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientRepositoryUpdateCertainClientTestPositive() throws ClientRepositoryReadException {
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
    public void clientRepositoryUpdateClientThatIsNotInTheDatabaseTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client newClient = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(newClient));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNullNameTestNegative() {
        String clientName = null;
        clientNo1.setClientName(clientName);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithEmptyNameTestNegative() {
        String clientName = "";
        clientNo1.setClientName(clientName);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNameTooShortTestNegative() {
        String clientName = "";
        clientNo1.setClientName(clientName);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNameTooLongTestNegative() {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        clientNo1.setClientName(clientName);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNameLengthEqualTo1TestPositive() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        String clientName = "d";
        clientNo1.setClientName(clientName);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNameLengthEqualTo50TestPositive() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        clientNo1.setClientName(clientName);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithNullSurnameTestNegative() {
        String clientSurname = null;
        clientNo1.setClientSurname(clientSurname);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithEmptySurnameTestNegative() {
        String clientSurname = "";
        clientNo1.setClientSurname(clientSurname);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithSurnameTooShortTestNegative() {
        String clientSurname = "d";
        clientNo1.setClientSurname(clientSurname);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithSurnameTooLongTestNegative() {
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        clientNo1.setClientSurname(clientSurname);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithSurnameLengthEqualTo2TestPositive() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        String clientSurname = "df";
        clientNo1.setClientSurname(clientSurname);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithSurnameLengthEqualTo100TestPositive() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        clientNo1.setClientSurname(clientSurname);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithAgeLowerThan18TestNegative() {
        int clientAge = 17;
        clientNo1.setClientAge(clientAge);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithAgeGreaterThan120TestNegative() {
        int clientAge = 121;
        clientNo1.setClientAge(clientAge);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.update(clientNo1));
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithAgeEqualTo18TestPositive() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        int clientAge = 18;
        clientNo1.setClientAge(clientAge);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryUpdateCertainClientWithAgeEqualTo120TestNegative() throws ClientRepositoryUpdateException, ClientRepositoryReadException {
        int clientAge = 120;
        clientNo1.setClientAge(clientAge);

        clientRepositoryForTests.update(clientNo1);

        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryDeleteCertainClientTestPositive() throws ClientRepositoryReadException {
        UUID removedClientUUID = clientNo1.getClientID();
        int numberOfClientsBeforeDelete = clientRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> clientRepositoryForTests.delete(clientNo1));

        int numberOfClientsAfterDelete = clientRepositoryForTests.findAll().size();

        assertNotEquals(numberOfClientsBeforeDelete, numberOfClientsAfterDelete);
        assertEquals(numberOfClientsBeforeDelete - 1, numberOfClientsAfterDelete);

        assertThrows(ClientRepositoryReadException.class, () -> clientRepositoryForTests.findByUUID(removedClientUUID));
    }

    @Test
    public void clientRepositoryDeleteClientThatIsNotInTheDatabaseTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client newClient = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        assertThrows(ClientRepositoryDeleteException.class, () -> clientRepositoryForTests.delete(newClient));
    }

    @Test
    public void clientRepositoryDeleteCertainClientByIdTestPositive() throws ClientRepositoryReadException {
        UUID removedClientUUID = clientNo1.getClientID();
        int numberOfClientsBeforeDelete = clientRepositoryForTests.findAll().size();

        assertDoesNotThrow(() -> clientRepositoryForTests.delete(clientNo1.getClientID()));

        int numberOfClientsAfterDelete = clientRepositoryForTests.findAll().size();

        assertNotEquals(numberOfClientsBeforeDelete, numberOfClientsAfterDelete);
        assertEquals(numberOfClientsBeforeDelete - 1, numberOfClientsAfterDelete);

        assertThrows(ClientRepositoryReadException.class, () -> clientRepositoryForTests.findByUUID(removedClientUUID));
    }

    @Test
    public void clientRepositoryDeleteClientByIdThatIsNotInTheDatabaseTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client newClient = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        assertThrows(ClientRepositoryDeleteException.class, () -> clientRepositoryForTests.delete(newClient.getClientID()));
    }

    @Test
    public void clientRepositoryExpireCertainClientTestPositive() throws ClientRepositoryReadException, ClientRepositoryUpdateException {
        UUID expiredClientUUID = clientNo1.getClientID();

        int beforeExpiringClient = clientRepositoryForTests.findAll().size();
        int numOfActiveClientsBefore = clientRepositoryForTests.findAllActive().size();

        clientRepositoryForTests.expire(clientNo1.getClientID());

        int afterExpiringClient = clientRepositoryForTests.findAll().size();
        int numOfActiveClientsAfter = clientRepositoryForTests.findAllActive().size();

        Client foundClient = clientRepositoryForTests.findByUUID(expiredClientUUID);

        assertNotNull(foundClient);

        assertNotEquals(foundClient, clientNo1);
        assertTrue(clientNo1.isClientStatusActive());

        clientNo1.setClientStatusActive(false);

        assertEquals(foundClient, clientNo1);
        assertFalse(clientNo1.isClientStatusActive());

        assertEquals(beforeExpiringClient, afterExpiringClient);
        assertNotEquals(numOfActiveClientsBefore, numOfActiveClientsAfter);
        assertEquals(numOfActiveClientsBefore - 1, numOfActiveClientsAfter);
    }

    @Test
    public void clientRepositoryExpireClientThatIsNotInTheDatabaseTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client newClient = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        assertThrows(ClientRepositoryUpdateException.class, () -> clientRepositoryForTests.expire(newClient.getClientID()));
    }

    @Test
    public void clientRepositoryFindCertainClientTestPositive() throws ClientRepositoryReadException {
        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientRepositoryFindClientThatIsNotInTheDatabaseTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client newClient = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(newClient);

        assertThrows(ClientRepositoryReadException.class, () -> clientRepositoryForTests.findByUUID(newClient.getClientID()));
    }

    @Test
    public void clientRepositoryFindAllClientsTestPositive() throws ClientRepositoryReadException, ClientRepositoryDeleteException {
        List<Client> startingListOfAllClients = clientRepositoryForTests.findAll();
        assertNotNull(startingListOfAllClients);

        clientRepositoryForTests.delete(startingListOfAllClients.get(0).getClientID());

        List<Client> finalListOfAllClients = clientRepositoryForTests.findAll();
        assertNotNull(finalListOfAllClients);

        assertEquals(startingListOfAllClients.size(), 3);
        assertEquals(finalListOfAllClients.size(), 2);
    }

    @Test
    public void clientRepositoryFindAllActiveClientsTestPositive()  throws ClientRepositoryReadException, ClientRepositoryUpdateException {
        List<Client> startingListOfActiveClients = clientRepositoryForTests.findAllActive();
        assertNotNull(startingListOfActiveClients);

        clientRepositoryForTests.expire(startingListOfActiveClients.get(0).getClientID());

        List<Client> finalListOfActiveClients = clientRepositoryForTests.findAllActive();
        assertNotNull(finalListOfActiveClients);

        assertEquals(startingListOfActiveClients.size(), 3);
        assertEquals(finalListOfActiveClients.size(), 2);
    }
}
