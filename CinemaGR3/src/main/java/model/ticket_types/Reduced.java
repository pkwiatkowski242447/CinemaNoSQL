package model.ticket_types;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Reduced extends TypeOfTicket {

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
