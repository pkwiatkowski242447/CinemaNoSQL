package mapping_layer_tests.docks_tests.ticket_types;

import mapping_layer.model_docs.ticket_types.NormalRow;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NormalRowTest {

    @Test
    public void normalDocNoArgsConstructorTestPositive() {
        NormalRow normalRow = new NormalRow();
        assertNotNull(normalRow);
    }

    @Test
    public void normalDocAllArgsConstructorAngGettersTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        NormalRow normalRow = new NormalRow(uuidValue);
        assertNotNull(normalRow);
        assertEquals(uuidValue, normalRow.getTypeOfTicketID());
    }

    @Test
    public void normalDocIdSetterTestPositive() {
        UUID uuidValue = UUID.randomUUID();
        NormalRow normalRow = new NormalRow(uuidValue);
        UUID idBefore = normalRow.getTypeOfTicketID();
        assertNotNull(idBefore);
        UUID newId = UUID.randomUUID();
        assertNotNull(newId);
        normalRow.setTypeOfTicketID(newId);
        UUID idAfter = normalRow.getTypeOfTicketID();
        assertNotNull(idAfter);
        assertEquals(newId, idAfter);
        assertNotEquals(idBefore, idAfter);
    }
}
