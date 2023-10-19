package model.ticket_types;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Reduced extends TypeOfTicket {

    @Id
    @Column(name = "ticket_type_id")
    private UUID ticketTypeID;

    public Reduced() {
    }

    public Reduced(UUID ticketTypeID) {
        super(ticketTypeID);
    }

    @Override
    public double applyDiscount(double movieBasePrice) {
        return 0.75 * movieBasePrice;
    }

    @Override
    public String getTicketTypeInfo() {
        return " Ticket type: Reduced";
    }
}
