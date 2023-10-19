package model.ticket_types;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class TypeOfTicket {

    @Id
    @Column(name = "ticket_type_id", nullable = false, unique = true)
    private UUID ticketTypeID;

    // Constructors

    public TypeOfTicket() {
    }

    public TypeOfTicket(UUID ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

    // Other methods

    public abstract double applyDiscount(double movieBasePrice);

    public abstract String getTicketTypeInfo();

    public UUID getTicketTypeID() {
        return ticketTypeID;
    }
}
