package mapping_layer.mappers;

import mapping_layer.model_docs.ClientDoc;
import model.Client;

public class ClientMapper {

    public static ClientDoc toClientDoc(Client client) {
        ClientDoc clientDoc = new ClientDoc();
        clientDoc.setClientID(client.getClientID());
        clientDoc.setClientName(client.getClientName());
        clientDoc.setClientSurname(client.getClientSurname());
        clientDoc.setClientAge(client.getClientAge());
        clientDoc.setClientStatusActive(client.isClientStatusActive());
        return clientDoc;
    }

    public static Client toClient(ClientDoc clientDoc) {
        return new Client(clientDoc.getClientID(),
                clientDoc.getClientName(),
                clientDoc.getClientSurname(),
                clientDoc.getClientAge(),
                clientDoc.isClientStatusActive());
    }
}
