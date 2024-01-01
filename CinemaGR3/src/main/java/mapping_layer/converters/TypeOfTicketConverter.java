package mapping_layer.converters;

import mapping_layer.model_docs.ticket_types.NormalRow;
import mapping_layer.model_docs.ticket_types.ReducedRow;
import mapping_layer.model_docs.ticket_types.TypeOfTicketRow;
import model.constants.TicketTypeConstants;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;

public class TypeOfTicketConverter {

    public static TypeOfTicketRow toTypeOfTicketRow(TypeOfTicket typeOfTicket) {
        TypeOfTicketRow typeOfTicketRow;
        if (typeOfTicket.getClass().equals(Reduced.class)) {
            typeOfTicketRow = new ReducedRow();
        } else {
            typeOfTicketRow = new NormalRow();
        }
        typeOfTicketRow.setTypeOfTicketID(typeOfTicket.getTicketTypeID());
        return typeOfTicketRow;
    }

    public static TypeOfTicket toTypeOfTicket(TypeOfTicketRow typeOfTicketRow) throws TypeOfTicketNotFoundException {
        TypeOfTicket typeOfTicket;
        if (typeOfTicketRow.getTicketTypeDiscriminator().equals(TicketTypeConstants.REDUCED_TICKET)) {
            typeOfTicket = new Reduced(typeOfTicketRow.getTypeOfTicketID());
        } else if (typeOfTicketRow.getTicketTypeDiscriminator().equals(TicketTypeConstants.NORMAL_TICKET)) {
            typeOfTicket = new Normal(typeOfTicketRow.getTypeOfTicketID());
        } else {
            throw new TypeOfTicketNotFoundException("Given discriminator value does not correspond to any ticket type.");
        }
        return typeOfTicket;
    }
}
