package model.ticket_types;

import model.constants.TicketTypeConstants;

import java.util.UUID;

public class Reduced extends TypeOfTicket {

    // Constructors

    public Reduced() {
    }

    public Reduced(UUID ticketTypeID) {
        super(ticketTypeID, TicketTypeConstants.REDUCED_TICKET);
    }

    // Other methods

    @Override
    public double applyDiscount(double movieBasePrice) {
        return 0.75 * movieBasePrice;
    }
}
