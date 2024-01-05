package model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import jakarta.validation.constraints.*;
import model.constants.ClientConstants;
import model.constants.GeneralConstants;
import model.messages.ClientValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    @NotBlank(message = ClientValidation.CLIENT_NAME_BLANK)
    @Size(min = 1, message = ClientValidation.CLIENT_NAME_TOO_SHORT)
    @Size(max = 50, message = ClientValidation.CLIENT_NAME_TOO_LONG)
    private String clientName;

    @CqlName(value = ClientConstants.CLIENT_SURNAME)
    @NotBlank(message = ClientValidation.CLIENT_SURNAME_BLANK)
    @Size(min = 2, message = ClientValidation.CLIENT_SURNAME_TOO_SHORT)
    @Size(max = 100, message = ClientValidation.CLIENT_SURNAME_TOO_LONG)
    private String clientSurname;

    @CqlName(value = ClientConstants.CLIENT_AGE)
    @Min(value = 18, message = ClientValidation.CLIENT_AGE_TOO_LOW)
    @Max(value = 120, message = ClientValidation.CLIENT_AGE_TOO_HIGH)
    private int clientAge;

    @CqlName(value = ClientConstants.CLIENT_STATUS_ACTIVE)
    private boolean clientStatusActive;

    // Constructors

    public Client() {
    }

    public Client(@NotNull(message = ClientValidation.CLIENT_ID_NAME) UUID clientID,
                  @NotBlank(message = ClientValidation.CLIENT_NAME_BLANK)
                  @Size(min = 1, message = ClientValidation.CLIENT_NAME_TOO_SHORT)
                  @Size(max = 50, message = ClientValidation.CLIENT_NAME_TOO_LONG) String clientName,
                  @NotBlank(message = ClientValidation.CLIENT_SURNAME_BLANK)
                  @Size(min = 2, message = ClientValidation.CLIENT_SURNAME_TOO_SHORT)
                  @Size(max = 100, message = ClientValidation.CLIENT_SURNAME_TOO_LONG) String clientSurname,
                  @Min(value = 18, message = ClientValidation.CLIENT_AGE_TOO_LOW)
                  @Max(value = 120, message = ClientValidation.CLIENT_AGE_TOO_HIGH) int clientAge,
                  boolean clientStatusActive) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAge = clientAge;
        this.clientStatusActive = clientStatusActive;
    }

    public Client(@NotNull(message = ClientValidation.CLIENT_ID_NAME) UUID clientID,
                  @NotBlank(message = ClientValidation.CLIENT_NAME_BLANK)
                  @Size(min = 1, message = ClientValidation.CLIENT_NAME_TOO_SHORT)
                  @Size(max = 50, message = ClientValidation.CLIENT_NAME_TOO_LONG) String clientName,
                  @NotBlank(message = ClientValidation.CLIENT_SURNAME_BLANK)
                  @Size(min = 2, message = ClientValidation.CLIENT_SURNAME_TOO_SHORT)
                  @Size(max = 100, message = ClientValidation.CLIENT_SURNAME_TOO_LONG) String clientSurname,
                  @Min(value = 18, message = ClientValidation.CLIENT_AGE_TOO_LOW)
                  @Max(value = 120, message = ClientValidation.CLIENT_AGE_TOO_HIGH) int clientAge) {
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

    public void setClientID(@NotNull(message = ClientValidation.CLIENT_ID_NAME) UUID clientID) {
        this.clientID = clientID;
    }

    public void setClientName(@NotBlank(message = ClientValidation.CLIENT_NAME_BLANK)
                              @Size(min = 1, message = ClientValidation.CLIENT_NAME_TOO_SHORT)
                              @Size(max = 50, message = ClientValidation.CLIENT_NAME_TOO_LONG) String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(@NotBlank(message = ClientValidation.CLIENT_SURNAME_BLANK)
                                 @Size(min = 2, message = ClientValidation.CLIENT_SURNAME_TOO_SHORT)
                                 @Size(max = 100, message = ClientValidation.CLIENT_SURNAME_TOO_LONG) String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setClientAge(@Min(value = 18, message = ClientValidation.CLIENT_AGE_TOO_LOW)
                             @Max(value = 120, message = ClientValidation.CLIENT_AGE_TOO_HIGH) int clientAge) {
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
