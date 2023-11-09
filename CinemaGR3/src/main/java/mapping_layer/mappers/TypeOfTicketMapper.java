package mapping_layer.mappers;

import mapping_layer.model_docs.ticket_types.NormalDoc;
import mapping_layer.model_docs.ticket_types.ReducedDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.exceptions.model_docs_exceptions.TypeOfTicketNotFoundException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;

public class TypeOfTicketMapper {

    public static TypeOfTicketDoc toTypeOfTicketDoc(TypeOfTicket typeOfTicket) {
        TypeOfTicketDoc typeOfTicketDoc;
        if (typeOfTicket.getClass().equals(Reduced.class)) {
            typeOfTicketDoc = new ReducedDoc();
        } else {
            typeOfTicketDoc = new NormalDoc();
        }
        typeOfTicketDoc.setTypeOfTicketID(typeOfTicket.getTicketTypeID());
        return typeOfTicketDoc;
    }

    public static TypeOfTicket toTypeOfTicket(TypeOfTicketDoc typeOfTicketDoc, String clazz) {
        TypeOfTicket typeOfTicket;
        if (clazz.equals("reduced")) {
            typeOfTicket = new Reduced(typeOfTicketDoc.getTypeOfTicketID());
        } else if (clazz.equals("normal")) {
            typeOfTicket = new Normal(typeOfTicketDoc.getTypeOfTicketID());
        } else {
            throw new TypeOfTicketNotFoundException("Given clazz value does not correspond to any ticket type.");
        }
        return typeOfTicket;
    }
}
