package mapping_layer.model_docs.ticket_types;

import lombok.*;
import model.constants.TicketTypeConstants;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class NormalRow extends TypeOfTicketRow {

    public NormalRow(UUID typeOfTicketID) {
        super(typeOfTicketID, TicketTypeConstants.NORMAL_TICKET);
    }
}
