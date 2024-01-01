package mapping_layer.model_docs.ticket_types;

import lombok.*;
import model.constants.TicketTypeConstants;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class ReducedRow extends TypeOfTicketRow {

    public ReducedRow(UUID typeOfTicketID) {
        super(typeOfTicketID, TicketTypeConstants.REDUCED_TICKET);
    }
}
