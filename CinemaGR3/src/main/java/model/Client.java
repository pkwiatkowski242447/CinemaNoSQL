package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Client implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    private UUID clientID;

    @Column(nullable = false)
    @Length(min = 1, max = 50)
    private String clientName;

    @Column(nullable = false)
    @Length(min = 2, max = 100)
    private String clientSurname;

    @Column(nullable = false)
    @Min(18)
    @Max(120)
    private int clientAge;

    @Column(nullable = false)
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
        stringBuilder.append("Klient: ")
                .append(this.clientID)
                .append(", imię i nazwisko: ")
                .append(this.clientName)
                .append(" ")
                .append(this.clientSurname)
                .append(", wiek: ")
                .append(this.clientAge);
        if (this.clientStatusActive) {
            stringBuilder.append(", status klienta: aktywny");
        } else {
            stringBuilder.append(", status klienta: nieaktywny");
        }
        return stringBuilder.toString();
    }
}
