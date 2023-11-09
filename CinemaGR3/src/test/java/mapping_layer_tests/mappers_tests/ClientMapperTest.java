package mapping_layer_tests.mappers_tests;

import mapping_layer.mappers.ClientMapper;
import mapping_layer.model_docs.ClientDoc;
import model.Client;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientMapperTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void clientMapperConstructorTest() {
        ClientMapper clientMapper = new ClientMapper();
        assertNotNull(clientMapper);
    }

    @Test
    public void clientMapperFromClientToClientDocTestPositive() {
        Client client = new Client(UUID.randomUUID(), "SomeName", "SomeSurname", 20, true);
        assertNotNull(client);
        ClientDoc clientDoc = ClientMapper.toClientDoc(client);
        assertNotNull(clientDoc);
        assertEquals(client.getClientID(), clientDoc.getClientID());
        assertEquals(client.getClientName(), clientDoc.getClientName());
        assertEquals(client.getClientSurname(), clientDoc.getClientSurname());
        assertEquals(client.getClientAge(), clientDoc.getClientAge());
        assertEquals(client.isClientStatusActive(), clientDoc.isClientStatusActive());
    }

    @Test
    public void clientMapperFromClientDocToClientTestPositive() {
        ClientDoc clientDoc = new ClientDoc(UUID.randomUUID(), "SomeName", "SomeSurname", 20, true);
        assertNotNull(clientDoc);
        Client client = ClientMapper.toClient(clientDoc);
        assertNotNull(client);
        assertEquals(clientDoc.getClientID(), client.getClientID());
        assertEquals(clientDoc.getClientName(), client.getClientName());
        assertEquals(clientDoc.getClientSurname(), client.getClientSurname());
        assertEquals(clientDoc.getClientAge(), client.getClientAge());
        assertEquals(clientDoc.isClientStatusActive(), client.isClientStatusActive());
    }
}
