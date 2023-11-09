package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.ClientDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDocTest {

    private UUID uuidNo1;
    private UUID uuidNo2;
    private String nameNo1;
    private String nameNo2;
    private String surnameNo1;
    private String surnameNo2;
    private int ageNo1;
    private int ageNo2;
    private boolean statusActiveNo1;
    private boolean statusActiveNo2;

    private ClientDoc clientDocNo1;
    private ClientDoc clientDocNo2;

    @BeforeEach
    public void init() {
        uuidNo1 = UUID.randomUUID();
        uuidNo2 = UUID.randomUUID();
        nameNo1 = "SomeName";
        nameNo2 = "SomeOtherName";
        surnameNo1 = "SomeSurname";
        surnameNo2 = "SomeOtherSurname";
        ageNo1 = 25;
        ageNo2 = 30;
        statusActiveNo1 = true;
        statusActiveNo2 = true;

        clientDocNo1 = new ClientDoc(uuidNo1, nameNo1, surnameNo1, ageNo1, statusActiveNo1);
        clientDocNo2 = new ClientDoc(uuidNo2, nameNo2, surnameNo2, ageNo2, statusActiveNo2);
    }

    @Test
    public void clientDocNoArgsConstructorTestPositive() {
        ClientDoc clientDoc = new ClientDoc();
        assertNotNull(clientDoc);
    }

    @Test
    public void clientDocAllArgsConstructorAndGettersTestPositive() {
        ClientDoc testClientDoc = new ClientDoc(uuidNo1, nameNo1, surnameNo1, ageNo1, statusActiveNo1);
        assertNotNull(testClientDoc);
        assertEquals(uuidNo1, testClientDoc.getClientID());
        assertEquals(nameNo1, testClientDoc.getClientName());
        assertEquals(surnameNo1, testClientDoc.getClientSurname());
        assertEquals(ageNo1, testClientDoc.getClientAge());
        assertEquals(statusActiveNo1, testClientDoc.isClientStatusActive());
    }

    @Test
    public void clientDocClientIDSetterTest() {
        UUID idBefore = clientDocNo1.getClientID();
        assertNotNull(idBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        clientDocNo1.setClientID(newUUID);
        UUID idAfter = clientDocNo1.getClientID();
        assertNotNull(idAfter);
        assertEquals(newUUID, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void clientDocClientNameSetterTest() {
        String nameBefore = clientDocNo1.getClientName();
        assertNotNull(nameBefore);
        String newName = "NewName";
        assertNotNull(newName);
        clientDocNo1.setClientName(newName);
        String nameAfter = clientDocNo1.getClientName();
        assertNotNull(nameAfter);
        assertEquals(newName, nameAfter);
        assertNotEquals(nameBefore, nameAfter);
    }

    public void clientDocClientSurnameSetterTest() {
        String surnameBefore = clientDocNo1.getClientSurname();
        assertNotNull(surnameBefore);
        String newSurname = "NewSurname";
        assertNotNull(newSurname);
        clientDocNo1.setClientSurname(newSurname);
        String surnameAfter = clientDocNo1.getClientSurname();
        assertNotNull(surnameAfter);
        assertEquals(newSurname, surnameAfter);
        assertNotEquals(surnameBefore, surnameAfter);
    }

    public void clientDocClientAgeSetterTest() {
        int ageBefore = clientDocNo1.getClientAge();
        int newAge = 22;
        clientDocNo1.setClientAge(newAge);
        int ageAfter = clientDocNo1.getClientAge();
        assertEquals(newAge, ageAfter);
        assertNotEquals(ageBefore, ageAfter);
    }

    public void clientDocClientStatusActiveSetterTest() {
        boolean statusBefore = clientDocNo1.isClientStatusActive();
        boolean newStatus = false;
        clientDocNo1.setClientStatusActive(newStatus);
        boolean statusAfter = clientDocNo1.isClientStatusActive();
        assertEquals(newStatus, statusAfter);
        assertNotEquals(statusBefore, statusAfter);
    }


}
