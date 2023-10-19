package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client implements Serializable {

    @Id
    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientID;

    @Column(name = "client_name", nullable = false)
    @Length(min = 1, max = 50)
    private String clientName;

    @Column(name = "client_surname", nullable = false)
    @Length(min = 2, max = 100)
    private String clientSurname;

    @Column(name = "client_age", nullable = false)
    @Min(18)
    @Max(120)
    private int clientAge;

    @Column(name = "client_status_active", nullable = false)
    private boolean clientStatusActive;

    // Constructors

    public Client() {
    }

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
