package mapping_layer.model_docs.ticket_types;

import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@Entity(defaultKeyspace = "cinema")
@CqlName("ticket_types")
@PropertyStrategy(mutable = false)
@NamingStrategy(convention = NamingConvention.SNAKE_CASE_INSENSITIVE)
public class TypeOfTicketRow {

    @PartitionKey
    @CqlName("ticket_type_id")
    private UUID typeOfTicketID;

    @CqlName("ticket_type_discriminator")
    private String ticketTypeDiscriminator;

    public TypeOfTicketRow(UUID typeOfTicketID, String ticketTypeDiscriminator) {
        this.typeOfTicketID = typeOfTicketID;
        this.ticketTypeDiscriminator = ticketTypeDiscriminator;
    }
}
