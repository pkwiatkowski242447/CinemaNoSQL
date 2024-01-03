package model.ticket_types;

import model.constants.TicketTypeConstants;

import java.util.UUID;

public class Normal extends TypeOfTicket {

    // Constructors

    public Normal() {
    }

    public Normal(UUID ticketTypeID) {
        super(ticketTypeID, TicketTypeConstants.NORMAL_TICKET);
    }

    // Other methods

    @Override
    public double applyDiscount(double movieBasePrice) {
        return movieBasePrice;
    }
}
