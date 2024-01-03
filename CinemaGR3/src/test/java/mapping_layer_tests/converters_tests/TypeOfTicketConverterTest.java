package mapping_layer_tests.converters_tests;

import mapping_layer.converters.TypeOfTicketConverter;
import mapping_layer.model_rows.ticket_types.NormalRow;
import mapping_layer.model_rows.ticket_types.ReducedRow;
import mapping_layer.model_rows.ticket_types.TypeOfTicketRow;
import model.exceptions.model_docs_exceptions.not_found_exceptions.TypeOfTicketNotFoundException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypeOfTicketConverterTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void typeOfTicketMapperConstructorTest() {
        TypeOfTicketConverter typeOfTicketConverter = new TypeOfTicketConverter();
        assertNotNull(typeOfTicketConverter);
    }

    @Test
    public void typeOfTicketMapperFromReducedToTypeOfTicketDocTestPositive() {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID());
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Reduced.class);
        TypeOfTicketRow typeOfTicketRow = TypeOfTicketConverter.toTypeOfTicketRow(typeOfTicket);
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), ReducedRow.class);
        assertEquals(typeOfTicket.getTicketTypeID(), typeOfTicketRow.getTypeOfTicketID());
    }

    @Test
    public void typeOfTicketMapperFromNormalToTypeOfTicketDocTestPositive() {
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID());
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Normal.class);
        TypeOfTicketRow typeOfTicketRow = TypeOfTicketConverter.toTypeOfTicketRow(typeOfTicket);
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), NormalRow.class);
        assertEquals(typeOfTicket.getTicketTypeID(), typeOfTicketRow.getTypeOfTicketID());
    }

    @Test
    public void typeOfTicketMapperFromReducedDocToTypeOfTicketTestPositive() {
        TypeOfTicketRow typeOfTicketRow = new ReducedRow(UUID.randomUUID());
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), ReducedRow.class);
        TypeOfTicket typeOfTicket;
        try {
            typeOfTicket = TypeOfTicketConverter.toTypeOfTicket(typeOfTicketRow);
        } catch (TypeOfTicketNotFoundException exception) {
            throw new RuntimeException("TypeOfTicketObject was created with unknown ticket type.");
        }
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Reduced.class);
        assertEquals(typeOfTicketRow.getTypeOfTicketID(), typeOfTicket.getTicketTypeID());
    }

    @Test
    public void typeOfTicketMapperFromNormalDocToTypeOfTicketTestPositive() {
        TypeOfTicketRow typeOfTicketRow = new NormalRow(UUID.randomUUID());
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), NormalRow.class);
        TypeOfTicket typeOfTicket;
        try {
            typeOfTicket = TypeOfTicketConverter.toTypeOfTicket(typeOfTicketRow);
        } catch(TypeOfTicketNotFoundException exception) {
            throw new RuntimeException("TypeOfTicketObject was created with unknown ticket type.");
        }
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Normal.class);
        assertEquals(typeOfTicketRow.getTypeOfTicketID(), typeOfTicket.getTicketTypeID());
    }

    @Test
    public void typeOfTicketMapperFromReducedDocToTypeOfTicketTestNegative() {
        TypeOfTicketRow typeOfTicketRow = new ReducedRow(UUID.randomUUID(), "someThing");
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), ReducedRow.class);
        assertThrows(TypeOfTicketNotFoundException.class, () -> {
            TypeOfTicket typeOfTicket = TypeOfTicketConverter.toTypeOfTicket(typeOfTicketRow);
        });
    }

    @Test
    public void typeOfTicketMapperFromNormalDocToTypeOfTicketTestNegative() {
        TypeOfTicketRow typeOfTicketRow = new NormalRow(UUID.randomUUID(), "someThing");
        assertNotNull(typeOfTicketRow);
        assertEquals(typeOfTicketRow.getClass(), NormalRow.class);
        assertThrows(TypeOfTicketNotFoundException.class, () -> {
            TypeOfTicket typeOfTicket = TypeOfTicketConverter.toTypeOfTicket(typeOfTicketRow);
        });
    }
}
