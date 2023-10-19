package model.ticket_types;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Normal extends TypeOfTicket {

    @Id
    @Column(name = "ticket_type_id")
    private UUID ticketTypeID;

    public Normal() {
    }

    public Normal(UUID ticketTypeID) {
        super(ticketTypeID);
    }

    @Override
    public double applyDiscount(double movieBasePrice) {
        return movieBasePrice;
    }

    @Override
    public String getTicketTypeInfo() {
        return " Ticket type: Normal";
    }
}
