package model.ticket_types;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.NamingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import model.constants.GeneralConstants;
import model.constants.TicketTypeConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@Entity(defaultKeyspace = GeneralConstants.KEY_SPACE)
@CqlName(value = TicketTypeConstants.TICKET_TYPES_TABLE_NAME)
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class TypeOfTicket {

    private UUID ticketTypeID;
    private String ticketTypeDiscriminator;

    // Constructors

    public TypeOfTicket() {
    }

    public TypeOfTicket(UUID ticketTypeID, String ticketTypeDiscriminator) {
        this.ticketTypeID = ticketTypeID;
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }

    // Getters

    public UUID getTicketTypeID() {
        return ticketTypeID;
    }

    public String getTicketTypeDiscriminator() {
        return ticketTypeDiscriminator;
    }

    // Setters

    public void setTicketTypeID(UUID ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

    public void setTicketTypeDiscriminator(String ticketTypeDiscriminator) {
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }

    // Other methods

    public double applyDiscount(double movieBasePrice) {
        return 0;
    }

    // ToString method

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ticketTypeID: ", ticketTypeID)
                .append("ticketTypeDiscriminator: ", ticketTypeDiscriminator)
                .toString();
    }

    // Hashcode and Equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TypeOfTicket that = (TypeOfTicket) o;

        return new EqualsBuilder()
                .append(ticketTypeID, that.ticketTypeID)
                .append(ticketTypeDiscriminator, that.ticketTypeDiscriminator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ticketTypeID)
                .append(ticketTypeDiscriminator)
                .toHashCode();
    }
}
