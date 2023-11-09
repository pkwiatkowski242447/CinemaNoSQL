package mapping_layer_tests.docks_tests.ticket_types;

import mapping_layer.model_docs.ticket_types.ReducedDoc;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReducedDocTest {

    @Test
    public void reducedDocNoArgsConstructorTestPositive() {
        ReducedDoc reducedDoc = new ReducedDoc();
        assertNotNull(reducedDoc);
    }

    @Test
    public void normalDocAllArgsConstructorAngGettersTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        ReducedDoc reducedDoc = new ReducedDoc(uuidValue);
        assertNotNull(reducedDoc);
        assertEquals(uuidValue, reducedDoc.getTypeOfTicketID());
    }

    @Test
    public void normalDocIdSetterTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        ReducedDoc reducedDoc = new ReducedDoc(uuidValue);
        UUID idBefore = reducedDoc.getTypeOfTicketID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        assertNotNull(newId);
        reducedDoc.setTypeOfTicketID(newId);
        UUID idAfter = reducedDoc.getTypeOfTicketID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }
}
