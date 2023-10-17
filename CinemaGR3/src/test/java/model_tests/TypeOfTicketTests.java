package model_tests;

import model.exceptions.model_exceptions.InvalidTicketBasePriceException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypeOfTicketTests {

    @Test
    public void ticketTypeTestPositive() {
        TypeOfTicket typeOfTicketNo1 = new Normal(UUID.randomUUID(), 25);
        assertNotNull(typeOfTicketNo1);
    }

    @Test
    public void ticketTypeIdGetterTest() {
        UUID ticketTypeId = UUID.randomUUID();
        TypeOfTicket typeOfTicketNo1 = new Normal(ticketTypeId, 25);
        assertNotNull(typeOfTicketNo1);
        assertEquals(ticketTypeId, typeOfTicketNo1.getTicketTypeID());
    }

    @Test
    public void ticketTypeTestNegative() {
        assertThrows(InvalidTicketBasePriceException.class, () -> {
            TypeOfTicket typeOfTicketNo1 = new Normal(UUID.randomUUID(),-25);
        });
    }

    @Test
    public void getTicketTypeInfoForNormal() {
        TypeOfTicket typeOfTicketNo1 = new Normal(UUID.randomUUID(),25);

        assertNotNull(typeOfTicketNo1);
        assertNotNull(typeOfTicketNo1.getTicketTypeInfo());
        assertNotEquals("", typeOfTicketNo1.getTicketTypeInfo());
    }

    @Test
    public void getTicketTypeInfoForReduced() {
        TypeOfTicket typeOfTicketNo1 = new Reduced(UUID.randomUUID(),25);

        assertNotNull(typeOfTicketNo1);
        assertNotNull(typeOfTicketNo1.getTicketTypeInfo());
        assertNotEquals("", typeOfTicketNo1.getTicketTypeInfo());
    }
}
