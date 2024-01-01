package model.ticket_types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public abstract class TypeOfTicket {

    public TypeOfTicket(UUID ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TypeOfTicket that = (TypeOfTicket) o;

        return new EqualsBuilder().append(ticketTypeID, that.ticketTypeID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(ticketTypeID).toHashCode();
    }
}
