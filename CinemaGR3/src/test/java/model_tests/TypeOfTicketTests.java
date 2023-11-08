package model_tests;

import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypeOfTicketTests {

    private TypeOfTicket testTypeOfTicket;
    private TypeOfTicket newTestTypeOfTicket;

    @BeforeEach
    public void init() {
        testTypeOfTicket = new Reduced(UUID.randomUUID());
        newTestTypeOfTicket = new Reduced(testTypeOfTicket.getTicketTypeID());
    }

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

    @Test
    public void typeOfTicketEqualsTestWithItself() {
        boolean result = testTypeOfTicket.equals(testTypeOfTicket);
        assertTrue(result);
    }

    @Test
    public void typeOfTicketEqualsTestWithNull() {
        boolean result = testTypeOfTicket.equals(null);
        assertFalse(result);
    }

    @Test
    public void typeOfTicketEqualsTestWithObjectFromOtherClass() {
        boolean result = testTypeOfTicket.equals(new Date());
        assertFalse(result);
    }

    @Test
    public void typeOfTicketEqualsTestWithTheSameObject() {
        boolean result = testTypeOfTicket.equals(newTestTypeOfTicket);
        assertTrue(result);
    }

    @Test
    public void typeOfTicketHashCodeTestPositive() {
        int hashCodeNo1 = testTypeOfTicket.hashCode();
        int hashCodeNo2 = newTestTypeOfTicket.hashCode();
        assertEquals(hashCodeNo1, hashCodeNo2);
    }

    @Test
    public void typeOfTicketHashCodeTestNegative() {
        int hashCodeNo1 = testTypeOfTicket.hashCode();
        int hashCodeNo2 = (new Reduced(UUID.randomUUID())).hashCode();
        assertNotEquals(hashCodeNo1, hashCodeNo2);
    }
}
