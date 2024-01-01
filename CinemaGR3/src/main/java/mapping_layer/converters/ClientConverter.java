package mapping_layer.converters;

import mapping_layer.model_docs.ClientRow;
import model.Client;

public class ClientConverter {

    public static ClientRow toClientRow(Client client) {
        ClientRow clientRow = new ClientRow();
        clientRow.setClientID(client.getClientID());
        clientRow.setClientName(client.getClientName());
        clientRow.setClientSurname(client.getClientSurname());
        clientRow.setClientAge(client.getClientAge());
        clientRow.setClientStatusActive(client.isClientStatusActive());
        return clientRow;
    }

    public static Client toClient(ClientRow clientRow) {
        return new Client(clientRow.getClientID(),
                clientRow.getClientName(),
                clientRow.getClientSurname(),
                clientRow.getClientAge(),
                clientRow.isClientStatusActive());
    }
}
