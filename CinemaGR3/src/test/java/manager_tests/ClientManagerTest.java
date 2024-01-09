package manager_tests;

import com.datastax.oss.driver.api.core.CqlSession;
import model.exceptions.managers.create_exceptions.ClientManagerCreateException;
import model.exceptions.managers.create_exceptions.CreateManagerException;
import model.exceptions.managers.delete_exceptions.ClientManagerDeleteException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.read_exceptions.ClientManagerReadException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.update_exceptions.ClientManagerUpdateException;
import model.exceptions.managers.update_exceptions.UpdateManagerException;
import model.model.Client;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.repositories.create_exceptions.ClientRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.ClientRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.ClientRepositoryReadException;
import model.managers.implementations.ClientManager;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.ClientRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientManagerTest {

    private static ClientRepository clientRepositoryForTests;
    private static ClientManager clientManagerForTests;
    private static CqlSession cqlSession;

    private Client clientNo1;
    private Client clientNo2;
    private Client clientNo3;

    @BeforeAll
    public static void init() throws CassandraConfigNotFound {
        cqlSession = CassandraClient.initializeCassandraSession();
        clientRepositoryForTests = new ClientRepository(cqlSession);
        clientManagerForTests = new ClientManager(clientRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        cqlSession.close();
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

        try {
            clientNo1 = clientRepositoryForTests.create(clientNo1Name, clientNo1Surname, clientNo1Age);
            clientNo2 = clientRepositoryForTests.create(clientNo2Name, clientNo2Surname, clientNo2Age);
            clientNo3 = clientRepositoryForTests.create(clientNo3Name, clientNo3Surname, clientNo3Age);
        } catch (ClientRepositoryCreateException exception) {
            throw new RuntimeException("Sample clients could not be created in the repository.", exception);
        }
    }

    @AfterEach
    public void depopulateClientRepositoryForTests() {
        try {
            List<Client> listOfClients = clientRepositoryForTests.findAll();
            for (Client client : listOfClients) {
                clientRepositoryForTests.delete(client);
            }
        } catch (ClientRepositoryDeleteException exception) {
            throw new RuntimeException("Sample clients could not be deleted from repository." ,exception);
        } catch (ClientRepositoryReadException exception) {
            throw new RuntimeException("Sample clients could not be read from repository." ,exception);
        }
    }

    @Test
    public void clientManagerCreateClientManagerTestPositive() {
        ClientRepository clientRepository = new ClientRepository(cqlSession);
        assertNotNull(clientRepository);
        ClientManager clientManager = new ClientManager(clientRepository);
        assertNotNull(clientManager);
    }

    @Test
    public void clientManagerSetClientRepositoryForClientManagerTestPositive() {
        ClientRepository clientRepositoryNo1 = new ClientRepository(cqlSession);
        assertNotNull(clientRepositoryNo1);

        ClientRepository clientRepositoryNo2 = new ClientRepository(cqlSession);
        assertNotNull(clientRepositoryNo2);

        ClientManager clientManager = new ClientManager(clientRepositoryNo1);
        assertNotNull(clientManager);

        clientManager.setClientRepository(clientRepositoryNo2);

        assertNotEquals(clientRepositoryNo1, clientManager.getClientRepository());
        assertEquals(clientRepositoryNo2, clientManager.getClientRepository());
    }

    @Test
    public void clientManagerCreateClientTestPositive() throws ReadManagerException, CreateManagerException {
        int numOfClientsBefore = clientManagerForTests.findAll().size();

        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client client = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(client);

        int numOfClientsAfter = clientManagerForTests.findAll().size();

        assertNotEquals(numOfClientsBefore, numOfClientsAfter);
    }

    @Test
    public void clientManagerCreateClientWithNullNameTestNegative() {
        String clientName = null;
        String clientSurname = "Czarnecka";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithEmptyNameTestNegative() {
        String clientName = "";
        String clientSurname = "Czarnecka";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithNameTooShortTestNegative() {
        String clientName = "";
        String clientSurname = "Czarnecka";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithNameTooLongTestNegative() {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        String clientSurname = "Czarnecka";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithNameLengthEqualTo1TestPositive() throws ClientManagerCreateException {
        String clientName = "d";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());    }

    @Test
    public void clientManagerCreateClientWithNameLengthEqualTo50TestPositive() throws ClientManagerCreateException {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientManagerCreateClientWithNullSurnameTestNegative() {
        String clientName = "Stefania";
        String clientSurname = null;
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithEmptySurnameTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithSurnameTooShortTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "d";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithSurnameTooLongTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        int clientAge = 35;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithSurnameLengthEqualTo2estPositive() throws ClientManagerCreateException {
        String clientName = "Stefania";
        String clientSurname = "df";
        int clientAge = 35;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());    }

    @Test
    public void clientManagerCreateClientWithSurnameLengthEqualTo50TestPositive() throws ClientManagerCreateException {
        String clientName = "Stefania";
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        int clientAge = 35;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientManagerCreateClientWithAgeTooLowTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 17;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithAgeTooHighTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 121;
        assertThrows(ClientManagerCreateException.class, () -> clientManagerForTests.create(clientName, clientSurname, clientAge));
    }

    @Test
    public void clientManagerCreateClientWithAgeEqualTo18TestPositive() throws ClientManagerCreateException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 18;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientManagerCreateClientWithAgeEqualTo120TestPositive() throws ClientManagerCreateException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 120;

        Client testClient = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(testClient);

        assertEquals(clientName, testClient.getClientName());
        assertEquals(clientSurname, testClient.getClientSurname());
        assertEquals(clientAge, testClient.getClientAge());
    }

    @Test
    public void clientManagerUpdateClientThatIsNotInTheRepositoryTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(client);

        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(client));
    }

    @Test
    public void clientManagerUpdateClientWithNullNameTestNegative() {
        String clientName = null;
        clientNo1.setClientName(clientName);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithEmptyNameTestNegative() {
        String clientName = "";
        clientNo1.setClientName(clientName);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithNameTooShortTestNegative() {
        String clientName = "";
        clientNo1.setClientName(clientName);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithNameTooLongTestNegative() {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfd";
        clientNo1.setClientName(clientName);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithNameLengthEqualTo1TestNegative() throws UpdateManagerException, ReadManagerException {
        String clientName = "d";
        clientNo1.setClientName(clientName);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerUpdateClientWithNameLengthEqualTo50TestNegative() throws UpdateManagerException, ReadManagerException {
        String clientName = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        clientNo1.setClientName(clientName);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerUpdateClientWithNullSurnameTestNegative() {
        String clientSurname = null;
        clientNo1.setClientSurname(clientSurname);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithEmptySurnameTestNegative() {
        String clientSurname = "";
        clientNo1.setClientSurname(clientSurname);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithSurnameTooShortTestNegative() {
        String clientSurname = "d";
        clientNo1.setClientSurname(clientSurname);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithSurnameTooLongTestNegative() {
        String clientSurname = "d";
        clientNo1.setClientSurname(clientSurname);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithSurnameLengthEqualTo2TestNegative() throws UpdateManagerException, ReadManagerException {
        String clientSurname = "df";
        clientNo1.setClientSurname(clientSurname);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerUpdateClientWithSurnameLengthEqualTo100TestNegative() throws UpdateManagerException, ReadManagerException {
        String clientSurname = "ddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddfddddf";
        clientNo1.setClientSurname(clientSurname);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerUpdateClientWithClientAgeTooLowTestNegative() {
        int clientAge = 17;
        clientNo1.setClientAge(clientAge);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithClientAgeTooHighTestNegative() {
        int clientAge = 121;
        clientNo1.setClientAge(clientAge);
        assertThrows(ClientManagerUpdateException.class, () -> clientManagerForTests.update(clientNo1));
    }

    @Test
    public void clientManagerUpdateClientWithClientAgeEqualTo18TestNegative() throws UpdateManagerException, ReadManagerException {
        int clientAge = 18;
        clientNo1.setClientAge(clientAge);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerUpdateClientWithClientAgeEqualTo120TestNegative() throws UpdateManagerException, ReadManagerException {
        int clientAge = 120;
        clientNo1.setClientAge(clientAge);

        clientManagerForTests.update(clientNo1);

        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);

        assertEquals(clientNo1, foundClient);
    }

    @Test
    public void clientManagerDeleteClientTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfClientsBefore = clientManagerForTests.findAll().size();
        
        UUID removedClientUUID = clientNo1.getClientID();
        
        clientManagerForTests.delete(clientNo1);
        
        int numberOfClientsAfter = clientManagerForTests.findAll().size();

        assertNotEquals(numberOfClientsBefore, numberOfClientsAfter);
        
        assertThrows(ClientManagerReadException.class, () -> clientManagerForTests.findByUUID(removedClientUUID));
    }

    @Test
    public void clientManagerDeleteClientThatIsNotInTheRepositoryTestNegative() throws ReadManagerException, DeleteManagerException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;
        
        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(client);
        
        assertThrows(ClientManagerDeleteException.class, () -> clientManagerForTests.delete(client));
    }

    @Test
    public void clientManagerDeleteClientByIdTestPositive() throws ReadManagerException, DeleteManagerException {
        int numberOfClientsBefore = clientManagerForTests.findAll().size();

        UUID removedClientUUID = clientNo1.getClientID();

        clientManagerForTests.delete(clientNo1.getClientID());

        int numberOfClientsAfter = clientManagerForTests.findAll().size();

        assertNotEquals(numberOfClientsBefore, numberOfClientsAfter);

        assertThrows(ClientManagerReadException.class, () -> clientManagerForTests.findByUUID(removedClientUUID));
    }

    @Test
    public void clientManagerDeleteClientByIdThatIsNotInTheRepositoryTestNegative() throws ReadManagerException, DeleteManagerException {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(client);

        assertThrows(ClientManagerDeleteException.class, () -> clientManagerForTests.delete(client.getClientID()));
    }

    @Test
    public void clientManagerFindClientTestPositive() throws ReadManagerException {
        Client foundClient = clientManagerForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void clientManagerFindClientThatIsNotInTheRepositoryTestNegative() {
        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
        assertNotNull(client);
        
        assertThrows(ClientManagerReadException.class, () -> clientManagerForTests.findByUUID(client.getClientID()));
    }

    @Test
    public void clientManagerFindAllActiveClientsTestPositive() throws ReadManagerException, UpdateManagerException {
        List<Client> startingListOfClients = clientManagerForTests.findAllActiveClients();
        assertNotNull(startingListOfClients);

        clientManagerForTests.deactivate(clientNo1.getClientID());

        List<Client> finalListOfClients = clientManagerForTests.findAllActiveClients();
        assertNotNull(finalListOfClients);

        assertEquals(startingListOfClients.size(), 3);
        assertEquals(finalListOfClients.size(), 2);
    }

    @Test
    public void clientManagerFindAllClientsTestPositive() throws ReadManagerException, CreateManagerException {
        List<Client> startingListOfClients = clientManagerForTests.findAll();
        assertNotNull(startingListOfClients);

        String clientName = "Stefania";
        String clientSurname = "Czarnecka";
        int clientAge = 35;

        Client client = clientManagerForTests.create(clientName, clientSurname, clientAge);
        assertNotNull(client);

        List<Client> finalListOfClients = clientManagerForTests.findAll();
        assertNotNull(finalListOfClients);

        assertEquals(startingListOfClients.size(), 3);
        assertEquals(finalListOfClients.size(), 4);
    }
}
