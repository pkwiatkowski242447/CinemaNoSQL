package model.ticket_types;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class Normal extends TypeOfTicket {

    public Normal() {
    }

    public Normal(UUID ticketTypeID, double ticketBasePrice) {
        super(ticketTypeID, ticketBasePrice);
    }

    @Override
    public double applyDiscount() {
        return 1 * super.getTicketBasePrice();
    }

    @Override
    public String getTicketTypeInfo() {
        return " Typ biletu: Normalny";
    }
}
