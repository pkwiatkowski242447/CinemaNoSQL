package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;
import model.repositories.ClientRepository;
import model.repositories.Repository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private final Client clientNo1 = new Client(UUID.randomUUID(), "John", "Smith", 21);
    private final Client clientNo2 = new Client(UUID.randomUUID(), "Mary", "Jane", 18);
    private final Client clientNo3 = new Client(UUID.randomUUID(), "Vincent", "Vega", 40);
    private static Repository<Client> clientRepositoryForTests;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        clientRepositoryForTests = new ClientRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void insertExampleClients() {
        clientRepositoryForTests.create(clientNo1);
        clientRepositoryForTests.create(clientNo2);
        clientRepositoryForTests.create(clientNo3);
    }

    @AfterEach
    public void deleteExampleClients() {
        List<Client> listOfAllClients = clientRepositoryForTests.findAll();
        for (Client client : listOfAllClients) {
            clientRepositoryForTests.delete(client);
        }
    }

    @Test
    public void clientRepositoryConstructorTest() {
        Repository<Client> clientRepository = new ClientRepository(entityManager);
        assertNotNull(clientRepository);
    }

    @Test
    public void createNewClientTestPositive() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertDoesNotThrow(() -> clientRepositoryForTests.create(newClient));
        Client createdClient = clientRepositoryForTests.findByUUID(newClient.getClientID());
        assertEquals(createdClient, newClient);
    }

    @Test
    public void createNewClientTestNegativeNullId() {
        Client newClient = new Client(null, "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeNameNull() {
        Client newClient = new Client(UUID.randomUUID(), null, "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeNameEmpty() {
        Client newClient = new Client(UUID.randomUUID(), "", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeNameTooLong() {
        String name = "ddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client newClient = new Client(UUID.randomUUID(), name, "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeSurnameEmpty() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeSurnameTooLong() {
        String surname = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client newClient = new Client(UUID.randomUUID(), "Stefania", surname, 80);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeAgeTooLesserThan18() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 17);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegativeAgeGreaterThan120() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 121);
        assertNotNull(newClient);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void createNewClientTestNegative() {
        // Creating new client with UUID from different one already in the db is required - since when
        // if is exactly the same object - this test didn't pass.
        Client newClient = new Client(clientNo1.getClientID(), "Stefania", "Czarnecka", 80);
        assertThrows(RepositoryCreateException.class, () -> clientRepositoryForTests.create(newClient));
    }

    @Test
    public void updateCertainClientTestPositive() {
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
    public void updateCertainClientWithNullNameTestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName(null);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithEmptyNameTestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName("");
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithNameTooLongTestNegative() {
        String newName = "ddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientName(newName);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithNullSurnameTestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname(null);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithEmptySurnameTestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname("");
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithSurnameTooLongTestNegative() {
        String newSurname = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientSurname(newSurname);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithAgeLesserThan18TestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientAge(17);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void updateCertainClientWithAgeGreaterThan120TestNegative() {
        Client foundClient = clientRepositoryForTests.findAll().get(0);
        assertNotNull(foundClient);
        foundClient.setClientAge(121);
        assertThrows(RepositoryUpdateException.class, () -> clientRepositoryForTests.update(foundClient));
    }

    @Test
    public void deleteCertainClientTestPositive() {
        UUID removedClientUUID = clientNo1.getClientID();
        int numberOfClientsBeforeDelete = clientRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> clientRepositoryForTests.delete(clientNo1));
        int numberOfClientsAfterDelete = clientRepositoryForTests.findAll().size();
        assertNotEquals(numberOfClientsBeforeDelete, numberOfClientsAfterDelete);
        assertEquals(numberOfClientsBeforeDelete - 1, numberOfClientsAfterDelete);
        Client foundClient = clientRepositoryForTests.findByUUID(removedClientUUID);
        assertNull(foundClient);
    }

    @Test
    public void deleteCertainClientTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        assertThrows(RepositoryDeleteException.class, () -> clientRepositoryForTests.delete(newClient));
    }

    @Test
    public void findCertainClientTestPositive() {
        Client foundClient = clientRepositoryForTests.findByUUID(clientNo1.getClientID());
        assertNotNull(foundClient);
        assertEquals(foundClient, clientNo1);
    }

    @Test
    public void findCertainClientTestNegative() {
        Client newClient = new Client(UUID.randomUUID(), "Stefania", "Czarnecka", 80);
        assertNotNull(newClient);
        Client foundClient = clientRepositoryForTests.findByUUID(newClient.getClientID());
        assertNull(foundClient);
    }

    @Test
    public void findAllClientsTestPositive() {
        List<Client> listOfAllClients = clientRepositoryForTests.findAll();
        assertNotNull(listOfAllClients);
        assertEquals(3, listOfAllClients.size());
    }
}
