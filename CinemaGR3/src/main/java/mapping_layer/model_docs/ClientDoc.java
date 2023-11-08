package mapping_layer.model_docs;

import model.Client;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class ClientDoc {

    @BsonProperty("_id")
    private final UUID clientID;

    @BsonProperty("client_name")
    private final String clientName;

    @BsonProperty("client_surname")
    private final String clientSurname;

    @BsonProperty("client_age")
    private final int clientAge;

    @BsonProperty("client_status_active")
    private final boolean clientStatusActive;

    // Constructor

    @BsonCreator
    public ClientDoc(@BsonProperty("_id") UUID clientID,
                     @BsonProperty("client_name") String clientName,
                     @BsonProperty("client_surname") String clientSurname,
                     @BsonProperty("client_age") int clientAge,
                     @BsonProperty("client_status_active") boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }

    public ClientDoc(Client client) {
        this.clientID = client.getClientID();
        this.clientName = client.getClientName();
        this.clientSurname = client.getClientSurname();
        this.clientAge = client.getClientAge();
        this.clientStatusActive = client.isClientStatusActive();
    }

    // Getters

    public UUID getClientID() {
        return clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public int getClientAge() {
        return clientAge;
    }

    public boolean isClientStatusActive() {
        return clientStatusActive;
    }
}
