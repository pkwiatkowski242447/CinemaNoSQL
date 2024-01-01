package repository_tests.mongo;

import mapping_layer.model_docs.ScreeningRoomRow;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.implementations.ScreeningRoomRepository;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomRepositoryTest {

    private final static String databaseName = "test";
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;
    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    @BeforeAll
    public static void init() throws MongoConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
    }

    @BeforeEach
    public void insertExampleScreeningRooms() {
        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        screeningRoomNo1 = screeningRoomRepositoryForTests.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        screeningRoomNo2 = screeningRoomRepositoryForTests.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        screeningRoomNo3 = screeningRoomRepositoryForTests.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);
    }

    @AfterEach
    public void deleteExampleScreeningRooms() {
        List<UUID> listOfScreeningRoomsUUIDs = screeningRoomRepositoryForTests.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            screeningRoomRepositoryForTests.delete(screeningRoomID);
        }
    }

    @AfterAll
    public static void destroy() {
        screeningRoomRepositoryForTests.close();
    }

    @Test
    public void screeningRoomRepositoryConstructorTest() throws MongoConfigNotFoundException {
        ScreeningRoomRepository screeningRoomRepository = new ScreeningRoomRepository(databaseName);
        assertNotNull(screeningRoomRepository);
    }

    @Test
    public void createScreeningRoomTestPositive() {
        final ScreeningRoom[] newScreeningRoom = new ScreeningRoom[1];
        assertDoesNotThrow(() -> {
            newScreeningRoom[0] = screeningRoomRepositoryForTests.create(1, 6, 70);
        });
        ScreeningRoom newlyCreatedScreeningRoom = screeningRoomRepositoryForTests.findByUUID(newScreeningRoom[0].getScreeningRoomID());
        assertNotNull(newlyCreatedScreeningRoom);
        assertEquals(newScreeningRoom[0], newlyCreatedScreeningRoom);
    }

    @Test
    public void createScreeningRoomWithFloorNumberLesserThan0() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(-1, 8, 30));
    }

    @Test
    public void createScreeningRoomWithFloorNumberGreaterThan3() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(4, 8, 30));
    }

    @Test
    public void createScreeningRoomWithRoomNumberLesserThan1() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(0, 0, 30));
    }

    @Test
    public void createScreeningRoomWithRoomNumberGreaterThan20() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(0, 21, 30));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(0, 8, -1));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(0, 8, 151));
    }

    @Test
    public void updateCertainScreeningRoomTestPositive() {
        int numOfAvailableSeatsBefore = screeningRoomNo1.getNumberOfAvailableSeats();
        screeningRoomNo1.setNumberOfAvailableSeats(numOfAvailableSeatsBefore - 1);
        assertDoesNotThrow(() -> screeningRoomRepositoryForTests.updateAllFields(screeningRoomNo1));
        int numOfAvailableSeatsAfter = screeningRoomRepositoryForTests.findByUUID(screeningRoomNo1.getScreeningRoomID()).getNumberOfAvailableSeats();
        assertNotEquals(numOfAvailableSeatsBefore, numOfAvailableSeatsAfter);
    }

    @Test
    public void updateCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 2, 50);
        assertNotNull(screeningRoom);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.updateAllFields(screeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(-1);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.updateAllFields(foundScreeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(151);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.updateAllFields(foundScreeningRoom));
    }

    @Test
    public void deleteCertainScreeningRoomTestPositive() {
        UUID removedScreeningRoomID = screeningRoomNo1.getScreeningRoomID();
        int numOfScreeningRoomsBeforeDelete = screeningRoomRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> screeningRoomRepositoryForTests.delete(screeningRoomNo1));
        int numOfScreeningRoomsAfterDelete = screeningRoomRepositoryForTests.findAll().size();
        assertNotEquals(numOfScreeningRoomsBeforeDelete, numOfScreeningRoomsAfterDelete);
        assertEquals(numOfScreeningRoomsBeforeDelete - 1, numOfScreeningRoomsAfterDelete);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(removedScreeningRoomID);
        });
    }

    @Test
    public void deleteCertainScreeningRoomThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryDeleteException.class, () -> screeningRoomRepositoryForTests.delete(screeningRoom));
    }

    @Test
    public void deleteCertainScreeningRoomWithUUIDThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryDeleteException.class, () -> screeningRoomRepositoryForTests.delete(screeningRoom.getScreeningRoomID()));
    }

    @Test
    public void expireCertainScreeningRoomTestPositive() {
        UUID expiredScreeningRoomUUID = screeningRoomNo1.getScreeningRoomID();
        int beforeExpiringScreeningRoom = screeningRoomRepositoryForTests.findAll().size();
        int numOfActiveScreeningRoomsBefore = screeningRoomRepositoryForTests.findAllActive().size();
        screeningRoomRepositoryForTests.expire(screeningRoomNo1);
        int afterExpiringScreeningRoom = screeningRoomRepositoryForTests.findAll().size();
        int numOfActiveScreeningRoomsAfter = screeningRoomRepositoryForTests.findAllActive().size();
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(expiredScreeningRoomUUID);
        assertNotNull(foundScreeningRoom);
        assertEquals(foundScreeningRoom, screeningRoomNo1);
        assertFalse(screeningRoomNo1.isScreeningRoomStatusActive());
        assertEquals(beforeExpiringScreeningRoom, afterExpiringScreeningRoom);
        assertNotEquals(numOfActiveScreeningRoomsBefore, numOfActiveScreeningRoomsAfter);
        assertEquals(numOfActiveScreeningRoomsBefore - 1, numOfActiveScreeningRoomsAfter);
    }

    @Test
    public void expireCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryUpdateException.class, () -> {
            screeningRoomRepositoryForTests.expire(screeningRoom);
        });
    }

    @Test
    public void findCertainScreeningRoomTestPositive() {
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(foundScreeningRoom);
        assertEquals(foundScreeningRoom, screeningRoomNo1);
    }

    @Test
    public void findCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 2, 50);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(screeningRoom.getScreeningRoomID());
        });
    }

    @Test
    public void findAllScreeningRoomsTestPositive() {
        List<ScreeningRoom> listOfAllScreeningRooms = screeningRoomRepositoryForTests.findAll();
        assertNotNull(listOfAllScreeningRooms);
        assertEquals(3, listOfAllScreeningRooms.size());
    }

    @Test
    public void findAllActiveScreeningRoomTestPositive() {
        List<ScreeningRoom> startingListOfScreeningRooms = screeningRoomRepositoryForTests.findAllActive();
        assertNotNull(startingListOfScreeningRooms);
        screeningRoomRepositoryForTests.expire(startingListOfScreeningRooms.get(0));
        List<ScreeningRoom> endingListOfScreeningRooms = screeningRoomRepositoryForTests.findAllActive();
        assertNotNull(endingListOfScreeningRooms);
        assertEquals(startingListOfScreeningRooms.size(), 3);
        assertEquals(endingListOfScreeningRooms.size(), 2);
    }

    // Other

    @Test
    public void mongoRepositoryFindScreeningRoomDocTestPositive() {
        ScreeningRoomRow screeningRoomRow = screeningRoomRepositoryForTests.findScreeningRoomDoc(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(screeningRoomRow);
        assertEquals(screeningRoomRow.getScreeningRoomID(), screeningRoomNo1.getScreeningRoomID());
        assertEquals(screeningRoomRow.getScreeningRoomFloor(), screeningRoomNo1.getScreeningRoomFloor());
        assertEquals(screeningRoomRow.getScreeningRoomNumber(), screeningRoomNo1.getScreeningRoomNumber());
        assertEquals(screeningRoomRow.getNumberOfAvailableSeats(), screeningRoomNo1.getNumberOfAvailableSeats());
        assertEquals(screeningRoomRow.isScreeningRoomStatusActive(), screeningRoomNo1.isScreeningRoomStatusActive());
    }

    @Test
    public void mongoRepositoryFindScreeningRoomDocTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 45);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomDocNotFoundException.class, () -> {
            screeningRoomRepositoryForTests.findScreeningRoomDoc(screeningRoom.getScreeningRoomID());
        });
    }
}
