package model.model;

import jakarta.validation.constraints.*;
import model.constants.ClientConstants;
import model.messages.ClientValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Client {

    @BsonProperty(ClientConstants.DOCUMENT_ID)
    private final UUID clientID;

    @BsonProperty(ClientConstants.CLIENT_NAME)
    @NotBlank(message = ClientValidation.CLIENT_NAME_BLANK)
    @Size(min = 1, message = ClientValidation.CLIENT_NAME_TOO_SHORT)
    @Size(max = 50, message = ClientValidation.CLIENT_NAME_TOO_LONG)
    private String clientName;

    @BsonProperty(ClientConstants.CLIENT_SURNAME)
    @NotBlank(message = ClientValidation.CLIENT_SURNAME_BLANK)
    @Size(min = 2, message = ClientValidation.CLIENT_SURNAME_TOO_SHORT)
    @Size(max = 100, message = ClientValidation.CLIENT_SURNAME_TOO_LONG)
    private String clientSurname;

    @BsonProperty(ClientConstants.CLIENT_AGE)
    @Min(value = 18, message = ClientValidation.CLIENT_AGE_TOO_LOW)
    @Max(value = 120, message = ClientValidation.CLIENT_AGE_TOO_HIGH)
    private int clientAge;

    @BsonProperty(ClientConstants.CLIENT_STATUS_ACTIVE)
    private boolean clientStatusActive;

    // Constructors

    public Client(UUID clientID,
                  String clientName,
                  String clientSurname,
                  int clientAge) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = true;
    }

    @BsonCreator
    public Client(@BsonProperty(ClientConstants.DOCUMENT_ID) UUID clientID,
                  @BsonProperty(ClientConstants.CLIENT_NAME) String clientName,
                  @BsonProperty(ClientConstants.CLIENT_SURNAME) String clientSurname,
                  @BsonProperty(ClientConstants.CLIENT_AGE) int clientAge,
                  @BsonProperty(ClientConstants.CLIENT_STATUS_ACTIVE) boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }

    // Getter

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

    // Setter

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

    // ToString method

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientID: ", clientID)
                .append("clientName: ", clientName)
                .append("clientSurname: ", clientSurname)
                .append("clientAge: ", clientAge)
                .append("clientStatusActive: ", clientStatusActive)
                .toString();
    }

    // Equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return new EqualsBuilder()
                .append(clientAge, client.clientAge)
                .append(clientStatusActive, client.clientStatusActive)
                .append(clientID, client.clientID)
                .append(clientName, client.clientName)
                .append(clientSurname, client.clientSurname)
                .isEquals();
    }

    // HashCode method

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clientID)
                .append(clientName)
                .append(clientSurname)
                .append(clientAge)
                .append(clientStatusActive)
                .toHashCode();
    }
}
