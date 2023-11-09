package mapping_layer_tests.mappers_tests;

import mapping_layer.mappers.TicketMapper;
import mapping_layer.mappers.TypeOfTicketMapper;
import mapping_layer.model_docs.ticket_types.NormalDoc;
import mapping_layer.model_docs.ticket_types.ReducedDoc;
import mapping_layer.model_docs.ticket_types.TypeOfTicketDoc;
import model.exceptions.model_docs_exceptions.TypeOfTicketNotFoundException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;
import model.ticket_types.TypeOfTicket;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypeOfTicketMapperTest {

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Test
    public void typeOfTicketMapperConstructorTest() {
        TypeOfTicketMapper typeOfTicketMapper = new TypeOfTicketMapper();
        assertNotNull(typeOfTicketMapper);
    }

    @Test
    public void typeOfTicketMapperFromReducedToTypeOfTicketDocTestPositive() {
        TypeOfTicket typeOfTicket = new Reduced(UUID.randomUUID());
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Reduced.class);
        TypeOfTicketDoc typeOfTicketDoc = TypeOfTicketMapper.toTypeOfTicketDoc(typeOfTicket);
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), ReducedDoc.class);
        assertEquals(typeOfTicket.getTicketTypeID(), typeOfTicketDoc.getTypeOfTicketID());
    }

    @Test
    public void typeOfTicketMapperFromNormalToTypeOfTicketDocTestPositive() {
        TypeOfTicket typeOfTicket = new Normal(UUID.randomUUID());
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Normal.class);
        TypeOfTicketDoc typeOfTicketDoc = TypeOfTicketMapper.toTypeOfTicketDoc(typeOfTicket);
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), NormalDoc.class);
        assertEquals(typeOfTicket.getTicketTypeID(), typeOfTicketDoc.getTypeOfTicketID());
    }

    @Test
    public void typeOfTicketMapperFromReducedDocToTypeOfTicketTestPositive() {
        TypeOfTicketDoc typeOfTicketDoc = new ReducedDoc(UUID.randomUUID());
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), ReducedDoc.class);
        TypeOfTicket typeOfTicket = TypeOfTicketMapper.toTypeOfTicket(typeOfTicketDoc, "reduced");
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Reduced.class);
        assertEquals(typeOfTicketDoc.getTypeOfTicketID(), typeOfTicket.getTicketTypeID());
    }

    @Test
    public void typeOfTicketMapperFromNormalDocToTypeOfTicketTestPositive() {
        TypeOfTicketDoc typeOfTicketDoc = new NormalDoc(UUID.randomUUID());
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), NormalDoc.class);
        TypeOfTicket typeOfTicket = TypeOfTicketMapper.toTypeOfTicket(typeOfTicketDoc, "normal");
        assertNotNull(typeOfTicket);
        assertEquals(typeOfTicket.getClass(), Normal.class);
        assertEquals(typeOfTicketDoc.getTypeOfTicketID(), typeOfTicket.getTicketTypeID());
    }

    @Test
    public void typeOfTicketMapperFromReducedDocToTypeOfTicketTestNegative() {
        TypeOfTicketDoc typeOfTicketDoc = new ReducedDoc(UUID.randomUUID());
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), ReducedDoc.class);
        assertThrows(TypeOfTicketNotFoundException.class, () -> {
            TypeOfTicket typeOfTicket = TypeOfTicketMapper.toTypeOfTicket(typeOfTicketDoc, "someThing");
        });
    }

    @Test
    public void typeOfTicketMapperFromNormalDocToTypeOfTicketTestNegative() {
        TypeOfTicketDoc typeOfTicketDoc = new NormalDoc(UUID.randomUUID());
        assertNotNull(typeOfTicketDoc);
        assertEquals(typeOfTicketDoc.getClass(), NormalDoc.class);
        assertThrows(TypeOfTicketNotFoundException.class, () -> {
            TypeOfTicket typeOfTicket = TypeOfTicketMapper.toTypeOfTicket(typeOfTicketDoc, "someThing");
        });
    }
}
