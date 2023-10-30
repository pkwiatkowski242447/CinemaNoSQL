package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Client {

    @BsonCreator
    public Client(@BsonProperty("_id") UUID clientID,
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

    @BsonProperty("_id")
    private final UUID clientID;

    @BsonProperty("client_name")
    private String clientName;

    @BsonProperty("client_surname")
    private String clientSurname;

    @BsonProperty("client_surname")
    private int clientAge;

    @BsonProperty("client_status_active")
    private boolean clientStatusActive;

    // Constructors

    public Client(UUID clientID, String clientName, String clientSurname, int clientAge) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = true;
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

    // Setters

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setClientAge(int clientAge) {
        this.clientAge = clientAge;
    }

    public void setClientStatusActive(boolean clientStatusActive) {
        this.clientStatusActive = clientStatusActive;
    }

    // Other methods

    public String getClientInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Client identifier: ")
                .append(this.clientID)
                .append(", name and surname: ")
                .append(this.clientName)
                .append(" ")
                .append(this.clientSurname)
                .append(", age: ")
                .append(this.clientAge);
        if (this.clientStatusActive) {
            stringBuilder.append(", client status: active");
        } else {
            stringBuilder.append(", client status: not active");
        }
        return stringBuilder.toString();
    }
}
