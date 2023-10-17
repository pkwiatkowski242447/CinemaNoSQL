package model.ticket_types;

import jakarta.persistence.*;
import model.exceptions.model_exceptions.InvalidTicketBasePriceException;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ticket_type")
public abstract class TypeOfTicket {

    @Id
    private UUID ticketTypeID;
    private double ticketBasePrice;

    // Constructors

    public TypeOfTicket() {
    }

    public TypeOfTicket(UUID ticketTypeID, double ticketBasePrice) {
        this.ticketTypeID = ticketTypeID;
        if (ticketBasePrice > 0) {
            this.ticketBasePrice = ticketBasePrice;
        } else {
            throw new InvalidTicketBasePriceException("Podano nieprawidłową cenę biletu");
        }
    }

    // Other methods

    public abstract double applyDiscount();

    public abstract String getTicketTypeInfo();

    public UUID getTicketTypeID() {
        return ticketTypeID;
    }

    public double getTicketBasePrice() {
        return this.ticketBasePrice;
    }
}
