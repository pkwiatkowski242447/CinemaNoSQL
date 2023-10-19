package model.ticket_types;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Normal extends TypeOfTicket {

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
