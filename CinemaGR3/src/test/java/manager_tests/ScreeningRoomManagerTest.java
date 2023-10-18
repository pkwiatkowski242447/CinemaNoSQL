package manager_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.ScreeningRoom;
import model.managers.Manager;
import model.managers.ScreeningRoomManager;
import model.repositories.Repository;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomManagerTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTest;
    private static ScreeningRoomManager screeningRoomManagerForTest;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        screeningRoomRepositoryForTest = new ScreeningRoomRepository(entityManager);
        screeningRoomManagerForTest = new ScreeningRoomManager(screeningRoomRepositoryForTest);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void populateScreeningRoomRepositoryForTests() {
        ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
        ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
        ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

        screeningRoomRepositoryForTest.create(screeningRoomNo1);
        screeningRoomRepositoryForTest.create(screeningRoomNo2);
        screeningRoomRepositoryForTest.create(screeningRoomNo3);
    }

    @AfterEach
    public void depopulateScreeningRoomRepositoryForTests() {
        List<ScreeningRoom> listOfScreeningRooms = screeningRoomRepositoryForTest.findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            screeningRoomRepositoryForTest.delete(screeningRoom);
        }
    }

    @Test
    public void createScreeningRoomManagerTest() {
        Repository<ScreeningRoom> screeningRoomRepository = new ScreeningRoomRepository(entityManager);
        assertNotNull(screeningRoomRepository);
        Manager<ScreeningRoom> screeningRoomManager = new ScreeningRoomManager(screeningRoomRepository);
        assertNotNull(screeningRoomManager);
    }

    @Test
    public void setScreeningRoomRepositoryForScreeningRoomManagerTest() {
        Repository<ScreeningRoom> screeningRoomRepositoryNo1 = new ScreeningRoomRepository(entityManager);
        assertNotNull(screeningRoomRepositoryNo1);
        Repository<ScreeningRoom> screeningRoomRepositoryNo2 = new ScreeningRoomRepository(entityManager);
        assertNotNull(screeningRoomRepositoryNo2);
        Manager<ScreeningRoom> screeningRoomManager = new ScreeningRoomManager(screeningRoomRepositoryNo1);
        assertNotNull(screeningRoomManager);
        screeningRoomManager.setObjectRepository(screeningRoomRepositoryNo2);
        assertNotEquals(screeningRoomRepositoryNo1, screeningRoomManager.getObjectRepository());
        assertEquals(screeningRoomRepositoryNo2, screeningRoomManager.getObjectRepository());
    }

    @Test
    public void registerNewScreeningRoomTestPositive() {
        int numOfScreeningRoomsBefore = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTest.register(1, 6, 70);
        assertNotNull(screeningRoom);
        int numOfScreeningRoomsAfter = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        assertNotEquals(numOfScreeningRoomsBefore, numOfScreeningRoomsAfter);
    }

    @Test
    public void registerNewScreeningRoomTestNegative() {
        int numOfScreeningRoomsBefore = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTest.register(-1, 6, 70);
        assertNotNull(screeningRoom);
        int numOfScreeningRoomsAfter = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        assertEquals(numOfScreeningRoomsBefore, numOfScreeningRoomsAfter);
    }

    @Test
    public void unregisterCertainScreeningRoomTestPositive() {
        int numberOfScreeningRoomsBefore = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = screeningRoomManagerForTest.getObjectRepository().findAll().get(0);
        assertNotNull(screeningRoom);
        UUID removedScreeningRoomID = screeningRoom.getScreeningRoomID();
        assertDoesNotThrow(() -> screeningRoomManagerForTest.unregister(screeningRoom));
        int numberOfScreeningRoomsAfter = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTest.get(removedScreeningRoomID);
        assertNull(foundScreeningRoom);
        assertNotEquals(numberOfScreeningRoomsBefore, numberOfScreeningRoomsAfter);
    }

    @Test
    public void unregisterCertainScreeningRoomTestNegative() {
        int numberOfScreeningRoomsBefore = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 70);
        assertNotNull(screeningRoom);
        screeningRoomManagerForTest.unregister(screeningRoom);
        int numberOfScreeningRoomsAfter = screeningRoomManagerForTest.getObjectRepository().findAll().size();
        assertEquals(numberOfScreeningRoomsBefore, numberOfScreeningRoomsAfter);
    }

    @Test
    public void getCertainScreeningRoomFromScreeningRoomRepositoryTestPositive() {
        ScreeningRoom screeningRoom = screeningRoomManagerForTest.getObjectRepository().findAll().get(0);
        assertNotNull(screeningRoom);
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTest.get(screeningRoom.getScreeningRoomID());
        assertNotNull(foundScreeningRoom);
        assertEquals(screeningRoom, foundScreeningRoom);
    }

    @Test
    public void getCertainScreeningRoomFromScreeningRoomRepositoryTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 70);
        assertNotNull(screeningRoom);
        ScreeningRoom foundScreeningRoom = screeningRoomManagerForTest.get(screeningRoom.getScreeningRoomID());
        assertNull(foundScreeningRoom);
    }

    @Test
    public void getAllScreeningRoomsFromRepositoryTest() {
        List<ScreeningRoom> listOfScreeningRooms = screeningRoomManagerForTest.getObjectRepository().findAll();
        assertNotNull(listOfScreeningRooms);
        assertEquals(3, listOfScreeningRooms.size());
    }
}
