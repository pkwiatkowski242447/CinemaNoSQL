package model.ticket_types;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Reduced extends TypeOfTicket {

    public Reduced() {
    }

    public Reduced(UUID ticketTypeID, double ticketBasePrice) {
        super(ticketTypeID, ticketBasePrice);
    }

    @Override
    public double applyDiscount() {
        return 0.75 * super.getTicketBasePrice();
    }

    @Override
    public String getTicketTypeInfo() {
        return " Typ biletu: Ulgowy";
    }
}
