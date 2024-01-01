package mapping_layer_tests.docks_tests;

import mapping_layer.model_docs.ClientRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRowTest {

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

    private ClientRow clientRowNo1;
    private ClientRow clientRowNo2;

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

        clientRowNo1 = new ClientRow(uuidNo1, nameNo1, surnameNo1, ageNo1, statusActiveNo1);
        clientRowNo2 = new ClientRow(uuidNo2, nameNo2, surnameNo2, ageNo2, statusActiveNo2);
    }

    @Test
    public void clientDocNoArgsConstructorTestPositive() {
        ClientRow clientRow = new ClientRow();
        assertNotNull(clientRow);
    }

    @Test
    public void clientDocAllArgsConstructorAndGettersTestPositive() {
        ClientRow testClientRow = new ClientRow(uuidNo1, nameNo1, surnameNo1, ageNo1, statusActiveNo1);
        assertNotNull(testClientRow);
        assertEquals(uuidNo1, testClientRow.getClientID());
        assertEquals(nameNo1, testClientRow.getClientName());
        assertEquals(surnameNo1, testClientRow.getClientSurname());
        assertEquals(ageNo1, testClientRow.getClientAge());
        assertEquals(statusActiveNo1, testClientRow.isClientStatusActive());
    }

    @Test
    public void clientDocClientIDSetterTest() {
        UUID idBefore = clientRowNo1.getClientID();
        assertNotNull(idBefore);
        UUID newUUID = UUID.randomUUID();
        assertNotNull(newUUID);
        clientRowNo1.setClientID(newUUID);
        UUID idAfter = clientRowNo1.getClientID();
        assertNotNull(idAfter);
        assertEquals(newUUID, idAfter);
        assertNotEquals(idBefore, idAfter);
    }

    @Test
    public void clientDocClientNameSetterTest() {
        String nameBefore = clientRowNo1.getClientName();
        assertNotNull(nameBefore);
        String newName = "NewName";
        assertNotNull(newName);
        clientRowNo1.setClientName(newName);
        String nameAfter = clientRowNo1.getClientName();
        assertNotNull(nameAfter);
        assertEquals(newName, nameAfter);
        assertNotEquals(nameBefore, nameAfter);
    }

    public void clientDocClientSurnameSetterTest() {
        String surnameBefore = clientRowNo1.getClientSurname();
        assertNotNull(surnameBefore);
        String newSurname = "NewSurname";
        assertNotNull(newSurname);
        clientRowNo1.setClientSurname(newSurname);
        String surnameAfter = clientRowNo1.getClientSurname();
        assertNotNull(surnameAfter);
        assertEquals(newSurname, surnameAfter);
        assertNotEquals(surnameBefore, surnameAfter);
    }

    public void clientDocClientAgeSetterTest() {
        int ageBefore = clientRowNo1.getClientAge();
        int newAge = 22;
        clientRowNo1.setClientAge(newAge);
        int ageAfter = clientRowNo1.getClientAge();
        assertEquals(newAge, ageAfter);
        assertNotEquals(ageBefore, ageAfter);
    }

    public void clientDocClientStatusActiveSetterTest() {
        boolean statusBefore = clientRowNo1.isClientStatusActive();
        boolean newStatus = false;
        clientRowNo1.setClientStatusActive(newStatus);
        boolean statusAfter = clientRowNo1.isClientStatusActive();
        assertEquals(newStatus, statusAfter);
        assertNotEquals(statusBefore, statusAfter);
    }


}
