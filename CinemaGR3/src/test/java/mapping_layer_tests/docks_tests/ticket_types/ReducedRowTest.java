package mapping_layer_tests.docks_tests.ticket_types;

import mapping_layer.model_docs.ticket_types.ReducedRow;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReducedRowTest {

    @Test
    public void reducedDocNoArgsConstructorTestPositive() {
        ReducedRow reducedRow = new ReducedRow();
        assertNotNull(reducedRow);
    }

    @Test
    public void normalDocAllArgsConstructorAngGettersTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        ReducedRow reducedRow = new ReducedRow(uuidValue);
        assertNotNull(reducedRow);
        assertEquals(uuidValue, reducedRow.getTypeOfTicketID());
    }

    @Test
    public void normalDocIdSetterTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        ReducedRow reducedRow = new ReducedRow(uuidValue);
        UUID idBefore = reducedRow.getTypeOfTicketID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        assertNotNull(newId);
        reducedRow.setTypeOfTicketID(newId);
        UUID idAfter = reducedRow.getTypeOfTicketID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }
}
