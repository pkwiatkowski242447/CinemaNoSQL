package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import model.constants.ClientConstants;
import model.constants.GeneralConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = ClientConstants.CLIENTS_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class Client {

    @PartitionKey
    @CqlName(value = ClientConstants.CLIENT_ID)
    private UUID clientID;

    @CqlName(value = ClientConstants.CLIENT_NAME)
    private String clientName;

    @CqlName(value = ClientConstants.CLIENT_SURNAME)
    private String clientSurname;

    @CqlName(value = ClientConstants.CLIENT_AGE)
    private int clientAge;

    @CqlName(value = ClientConstants.CLIENT_STATUS_ACTIVE)
    private boolean clientStatusActive;

    // Constructors

    public Client() {
    }

    public Client(UUID clientID, String clientName, String clientSurname, int clientAge, boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }

    public Client(UUID clientID, String clientName, String clientSurname, int clientAge) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = true;
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

    public void setClientID(UUID clientID) {
        this.clientID = clientID;
    }

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
