package mapping_layer_tests.docks_tests.ticket_types;

import mapping_layer.model_docs.ticket_types.NormalDoc;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NormalDocTest {

    @Test
    public void normalDocNoArgsConstructorTestPositive() {
        NormalDoc normalDoc = new NormalDoc();
        assertNotNull(normalDoc);
    }

    @Test
    public void normalDocAllArgsConstructorAngGettersTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        NormalDoc normalDoc = new NormalDoc(uuidValue);
        assertNotNull(normalDoc);
        assertEquals(uuidValue, normalDoc.getTypeOfTicketID());
    }

    @Test
    public void normalDocIdSetterTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        NormalDoc normalDoc = new NormalDoc(uuidValue);
        UUID idBefore = normalDoc.getTypeOfTicketID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        assertNotNull(newId);
        normalDoc.setTypeOfTicketID(newId);
        UUID idAfter = normalDoc.getTypeOfTicketID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }
}
