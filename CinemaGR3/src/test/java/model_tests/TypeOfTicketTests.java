package model_tests;

import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypeOfTicketTests {

    @Test
    public void ticketTypeConstructorsTestPositive() {
        TypeOfTicket typeOfTicketNo1 = new Normal();
        assertNotNull(typeOfTicketNo1);

        TypeOfTicket typeOfTicketNo2 = new Normal(UUID.randomUUID());
        assertNotNull(typeOfTicketNo2);

        TypeOfTicket typeOfTicketNo3 = new Reduced();
        assertNotNull(typeOfTicketNo3);

        TypeOfTicket typeOfTicketNo4 = new Reduced(UUID.randomUUID());
        assertNotNull(typeOfTicketNo4);
    }

    @Test
    public void ticketTypeIdGetterTest() {
        UUID ticketTypeId = UUID.randomUUID();
        TypeOfTicket typeOfTicketNo1 = new Normal(ticketTypeId);
        assertNotNull(typeOfTicketNo1);
        assertEquals(ticketTypeId, typeOfTicketNo1.getTicketTypeID());
    }

    @Test
    public void getTicketTypeInfoForNormal() {
        TypeOfTicket typeOfTicketNo1 = new Normal(UUID.randomUUID());

        assertNotNull(typeOfTicketNo1);
        assertNotNull(typeOfTicketNo1.getTicketTypeInfo());
        assertNotEquals("", typeOfTicketNo1.getTicketTypeInfo());
    }

    @Test
    public void getTicketTypeInfoForReduced() {
        TypeOfTicket typeOfTicketNo1 = new Reduced(UUID.randomUUID());

        assertNotNull(typeOfTicketNo1);
        assertNotNull(typeOfTicketNo1.getTicketTypeInfo());
        assertNotEquals("", typeOfTicketNo1.getTicketTypeInfo());
    }
}
