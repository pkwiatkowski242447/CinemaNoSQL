package model_tests;

import model.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTests {

    private Client testClient;
    private Client otherTestClient;

    @BeforeEach
    public void init() {
        testClient = new Client(UUID.randomUUID(), "Gustavo", "Fring", 40);
        otherTestClient = new Client(testClient.getClientID(),
                testClient.getClientName(),
                testClient.getClientSurname(),
                testClient.getClientAge(),
                testClient.isClientStatusActive());
    }

    @Test
    public void clientConstructorAndGettersTestPositive() {
        UUID clientIDNo1 = UUID.randomUUID();
        String clientNameNo1 = "SomeName";
        String clientSurnameNo1 = "SomeSurname";
        int clientAgeNo1 = 20;
        Client someClientNo1 = new Client(clientIDNo1, clientNameNo1, clientSurnameNo1, clientAgeNo1);

        assertNotNull(someClientNo1);

        assertEquals(clientIDNo1, someClientNo1.getClientID());
        assertEquals(clientNameNo1, someClientNo1.getClientName());
        assertEquals(clientSurnameNo1, someClientNo1.getClientSurname());
        assertEquals(clientAgeNo1, someClientNo1.getClientAge());
        assertTrue(someClientNo1.isClientStatusActive());
    }

    @Test
    public void clientNameSetterTest() {
        UUID clientID = UUID.randomUUID();
        String clientNameNo1 = "SomeName";
        String clientNameNo2 = "OtherName";
        String clientSurname = "SomeSurname";
        int clientAge = 20;
        Client someClient = new Client(clientID, clientNameNo1, clientSurname, clientAge);

        assertNotNull(someClient);
        assertEquals(clientNameNo1, someClient.getClientName());

        someClient.setClientName(clientNameNo2);

        assertNotNull(someClient);
        assertEquals(clientNameNo2, someClient.getClientName());
    }

    @Test
    public void clientSurnameSetterTest() {
        UUID clientID = UUID.randomUUID();
        String clientName = "SomeName";
        String clientSurnameNo1 = "SomeSurname";
        String clientSurnameNo2 = "OtherSurname";
        int clientAge = 20;
        Client someClient = new Client(clientID, clientName, clientSurnameNo1, clientAge);

        assertNotNull(someClient);
        assertEquals(clientSurnameNo1, someClient.getClientSurname());

        someClient.setClientSurname(clientSurnameNo2);

        assertNotNull(someClient);
        assertEquals(clientSurnameNo2, someClient.getClientSurname());
    }

    @Test
    public void clientAgeSetterTest() {
        UUID clientID = UUID.randomUUID();
        String clientName = "SomeName";
        String clientSurname = "SomeSurname";
        int clientAgeNo1 = 20;
        int clientAgeNo2 = 10;
        Client someClient = new Client(clientID, clientName, clientSurname, clientAgeNo1);

        assertNotNull(someClient);
        assertEquals(clientAgeNo1, someClient.getClientAge());

        someClient.setClientAge(clientAgeNo2);

        assertNotNull(someClient);
        assertEquals(clientAgeNo2, someClient.getClientAge());
    }

    @Test
    public void clientStatusActiveSetterTest() {
        UUID clientID = UUID.randomUUID();
        String clientName = "SomeName";
        String clientSurname = "SomeSurname";
        int clientAge= 20;
        Client someClient = new Client(clientID, clientName, clientSurname, clientAge);

        assertNotNull(someClient);
        assertTrue(someClient.isClientStatusActive());

        someClient.setClientStatusActive(false);

        assertNotNull(someClient);
        assertFalse(someClient.isClientStatusActive());
    }

    @Test
    public void getClientInfoTestStatusActive() {
        UUID clientID = UUID.randomUUID();
        String clientName = "SomeName";
        String clientSurname = "SomeSurname";
        int clientAge= 20;
        Client client = new Client(clientID, clientName, clientSurname, clientAge);

        assertNotNull(client);
        assertNotNull(client.getClientInfo());
        assertNotEquals("", client.getClientInfo());
    }

    @Test
    public void getClientInfoTestStatusNotActive() {
        UUID clientID = UUID.randomUUID();
        String clientName = "SomeName";
        String clientSurname = "SomeSurname";
        int clientAge= 20;
        Client client = new Client(clientID, clientName, clientSurname, clientAge);
        client.setClientStatusActive(false);

        assertNotNull(client);
        assertNotNull(client.getClientInfo());
        assertNotEquals("", client.getClientInfo());
    }

    @Test
    public void clientEqualsTestWithItself() {
        boolean result = testClient.equals(testClient);
        assertTrue(result);
    }

    @Test
    public void clientEqualsTestWithNull() {
        boolean result = testClient.equals(null);
        assertFalse(result);
    }

    @Test
    public void clientEqualsTestWithObjectFromDifferentClass() {
        boolean result = testClient.equals(new Date());
        assertFalse(result);
    }

    @Test
    public void clientEqualsTestWithTheSameObject() {
        boolean result = testClient.equals(otherTestClient);
        assertTrue(result);
    }

    @Test
    public void clientHashCodeTestPositive() {
        int hashCodeNo1 = testClient.hashCode();
        int hashCodeNo2 = otherTestClient.hashCode();
        assertEquals(hashCodeNo1, hashCodeNo2);
    }

    @Test
    public void clientHashCodeTestNegative() {
        testClient.setClientName("Scott");
        int hashCodeNo1 = testClient.hashCode();
        int hashCodeNo2 = otherTestClient.hashCode();
        assertNotEquals(hashCodeNo1, hashCodeNo2);
    }
}
