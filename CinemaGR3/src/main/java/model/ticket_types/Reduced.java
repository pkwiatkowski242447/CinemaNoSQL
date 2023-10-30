package model.ticket_types;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Reduced extends TypeOfTicket {

    public Reduced() {
    }

    @BsonCreator
    public Reduced(@BsonProperty("_id") UUID ticketTypeID) {
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
