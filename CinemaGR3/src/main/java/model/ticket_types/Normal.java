package model.ticket_types;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Normal extends TypeOfTicket {

    public Normal() {
    }

    @BsonCreator
    public Normal(@BsonProperty("_id") UUID ticketTypeID) {
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
