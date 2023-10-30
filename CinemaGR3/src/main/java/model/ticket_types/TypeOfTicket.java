package model.ticket_types;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public abstract class TypeOfTicket {

    @BsonCreator
    public TypeOfTicket(@BsonProperty("_id") UUID ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

    @BsonProperty("_id")
    private UUID ticketTypeID;

    // Constructors

    public TypeOfTicket() {
    }

    // Other methods

    public abstract double applyDiscount(double movieBasePrice);

    public abstract String getTicketTypeInfo();

    public UUID getTicketTypeID() {
        return ticketTypeID;
    }
}
