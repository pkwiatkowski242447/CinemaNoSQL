package manager_tests;

import model.ScreeningRoom;
import model.managers.ScreeningRoomManager;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomManagerTest {

    private final static String databaseName = "test";
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;
    private static ScreeningRoomManager screeningRoomManagerForTests;

    @BeforeAll
    public static void init() {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        screeningRoomManagerForTests = new ScreeningRoomManager(screeningRoomRepositoryForTests);
    }

    @AfterAll
    public static void destroy() {
        screeningRoomRepositoryForTests.close();
    }

    @BeforeEach
    public void populateScreeningRoomRepositoryForTests() {
        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        screeningRoomRepositoryForTests.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        screeningRoomRepositoryForTests.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        screeningRoomRepositoryForTests.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);
    }

    @AfterEach
    public void depopulateScreeningRoomRepositoryForTests() {
        List<ScreeningRoom> listOfScreeningRooms = screeningRoomRepositoryForTests.findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            screeningRoomRepositoryForTests.delete(screeningRoom);
        }
    }

    @Test
    public void createScreeningRoomManagerTest() {
        ScreeningRoomRepository screeningRoomRepository = new ScreeningRoomRepository(databaseName);
        assertNotNull(screeningRoomRepository);
        ScreeningRoomManager screeningRoomManager = new ScreeningRoomManager(screeningRoomRepository);
        assertNotNull(screeningRoomManager);
        screeningRoomRepository.close();
    }

    @Test
    public void setScreeningRoomRepositoryForScreeningRoomManagerTest() {
        ScreeningRoomRepository screeningRoomRepositoryNo1 = new ScreeningRoomRepository(databaseName);
        assertNotNull(screeningRoomRepositoryNo1);
        ScreeningRoomRepository screeningRoomRepositoryNo2 = new ScreeningRoomRepository(databaseName);
        assertNotNull(screeningRoomRepositoryNo2);
        ScreeningRoomManager screeningRoomManager = new ScreeningRoomManager(screeningRoomRepositoryNo1);
        assertNotNull(screeningRoomManager);
        screeningRoomManager.setScreeningRoomRepository(screeningRoomRepositoryNo2);
        assertNotEquals(screeningRoomRepositoryNo1, screeningRoomManager.getScreeningRoomRepository());
        assertEquals(screeningRoomRepositoryNo2, screeningRoomManager.getScreeningRoomRepository());
        screeningRoomRepositoryNo1.close();
        screeningRoomRepositoryNo2.close();
    }

    @Test
    public void registerNewScreeningRoomTestPositive() {
        int numOfScreeningRoomsBefore = screeningRoomManagerForTests.getAll().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.register(1, 6, 70);
        assertNotNull(screeningRoom);
        int numOfScreeningRoomsAfter = screeningRoomManagerForTests.getAll().size();
        assertNotEquals(numOfScreeningRoomsBefore, numOfScreeningRoomsAfter);
    }

    @Test
    public void registerNewScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.register(-1, 6, 70);
        assertNull(screeningRoom);
    }

    @Test
    public void unregisterCertainScreeningRoomTestPositive() {
        int numberOfScreeningRoomsBefore = screeningRoomManagerForTests.getAllActive().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.getAllActive().get(0);
        assertNotNull(screeningRoom);
        assertTrue(screeningRoom.isScreeningRoomStatusActive());
        UUID removedScreeningRoomID = screeningRoom.getScreeningRoomID();
        assertDoesNotThrow(() -> screeningRoomManagerForTests.unregister(screeningRoom));
        int numberOfScreeningRoomsAfter = screeningRoomManagerForTests.getAllActive().size();
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTests.get(removedScreeningRoomID);
        assertNotNull(foundScreeningRoom);
        assertFalse(foundScreeningRoom.isScreeningRoomStatusActive());
        assertNotEquals(numberOfScreeningRoomsBefore, numberOfScreeningRoomsAfter);
    }

    @Test
    public void unregisterCertainScreeningRoomTestNegative() {
        int numberOfScreeningRoomsBefore = screeningRoomManagerForTests.getAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 70);
        assertNotNull(screeningRoom);
        screeningRoomManagerForTests.unregister(screeningRoom);
        int numberOfScreeningRoomsAfter = screeningRoomManagerForTests.getAll().size();
        assertEquals(numberOfScreeningRoomsBefore, numberOfScreeningRoomsAfter);
    }

    @Test
    public void getCertainScreeningRoomFromScreeningRoomRepositoryTestPositive() {
        ScreeningRoom screeningRoom = screeningRoomManagerForTests.getAll().get(0);
        assertNotNull(screeningRoom);
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTests.get(screeningRoom.getScreeningRoomID());
        assertNotNull(foundScreeningRoom);
        assertEquals(screeningRoom, foundScreeningRoom);
    }

    @Test
    public void getCertainScreeningRoomFromScreeningRoomRepositoryTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 70);
        assertNotNull(screeningRoom);
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTests.get(screeningRoom.getScreeningRoomID());
        assertNull(foundScreeningRoom);
    }

    @Test
    public void getAllScreeningRoomsFromRepositoryTest() {
        List<ScreeningRoom> listOfScreeningRoomsNo1 = screeningRoomManagerForTests.getScreeningRoomRepository().findAll();
        List<ScreeningRoom> listOfScreeningRoomsNo2 = screeningRoomManagerForTests.getAll();
        assertNotNull(listOfScreeningRoomsNo1);
        assertNotNull(listOfScreeningRoomsNo2);
        assertEquals(listOfScreeningRoomsNo1.size(), listOfScreeningRoomsNo2.size());
    }
}
