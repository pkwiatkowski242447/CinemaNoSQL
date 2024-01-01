package mapping_layer_tests.mappers_tests;

import mapping_layer.converters.ClientConverter;
import mapping_layer.model_docs.ClientRow;
import model.Client;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientConverterTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void clientMapperConstructorTest() {
        ClientConverter clientConverter = new ClientConverter();
        assertNotNull(clientConverter);
    }

    @Test
    public void clientMapperFromClientToClientDocTestPositive() {
        Client client = new Client(UUID.randomUUID(), "SomeName", "SomeSurname", 20, true);
        assertNotNull(client);
        ClientRow clientRow = ClientConverter.toClientRow(client);
        assertNotNull(clientRow);
        assertEquals(client.getClientID(), clientRow.getClientID());
        assertEquals(client.getClientName(), clientRow.getClientName());
        assertEquals(client.getClientSurname(), clientRow.getClientSurname());
        assertEquals(client.getClientAge(), clientRow.getClientAge());
        assertEquals(client.isClientStatusActive(), clientRow.isClientStatusActive());
    }

    @Test
    public void clientMapperFromClientDocToClientTestPositive() {
        ClientRow clientRow = new ClientRow(UUID.randomUUID(), "SomeName", "SomeSurname", 20, true);
        assertNotNull(clientRow);
        Client client = ClientConverter.toClient(clientRow);
        assertNotNull(client);
        assertEquals(clientRow.getClientID(), client.getClientID());
        assertEquals(clientRow.getClientName(), client.getClientName());
        assertEquals(clientRow.getClientSurname(), client.getClientSurname());
        assertEquals(clientRow.getClientAge(), client.getClientAge());
        assertEquals(clientRow.isClientStatusActive(), client.isClientStatusActive());
    }
}
