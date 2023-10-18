package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryUpdateException;
import model.repositories.Repository;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static Repository<ScreeningRoom> screeningRoomRepositoryForTests;
    private final ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
    private final ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
    private final ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        // entityManager.getTransaction().commit();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void insertExampleScreeningRooms() {
        screeningRoomRepositoryForTests.create(screeningRoomNo1);
        screeningRoomRepositoryForTests.create(screeningRoomNo2);
        screeningRoomRepositoryForTests.create(screeningRoomNo3);
    }

    @AfterEach
    public void deleteExampleScreeningRooms() {
        List<ScreeningRoom> listOfScreeningRooms = screeningRoomRepositoryForTests.findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            screeningRoomRepositoryForTests.delete(screeningRoom);
        }
    }

    @Test
    public void screeningRoomRepositoryConstructorTest() {
        Repository<ScreeningRoom> screeningRoomRepository = new ScreeningRoomRepository(entityManager);
        assertNotNull(screeningRoomRepository);
    }

    @Test
    public void createScreeningRoomTestPositive() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 6, 70);
        assertNotNull(newScreeningRoom);
        assertDoesNotThrow(() -> screeningRoomRepositoryForTests.create(newScreeningRoom));
        ScreeningRoom newlyCreatedScreeningRoom = screeningRoomRepositoryForTests.findByUUID(newScreeningRoom.getScreeningRoomID());
        assertNotNull(newlyCreatedScreeningRoom);
        assertEquals(newScreeningRoom, newlyCreatedScreeningRoom);
    }

    @Test
    public void createScreeningRoomTestNegative() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(screeningRoomNo1.getScreeningRoomID(), 0, 8, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningWithNullIdRoomTestNegative() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(null, 0, 8, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithFloorNumberLesserThan0() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), -1, 8, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithFloorNumberGreaterThan3() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 4, 8, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithRoomNumberLesserThan1() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 0, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithRoomNumberGreaterThan20() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 21, 30);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 8, -1);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        ScreeningRoom newScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 8, 151);
        assertNotNull(newScreeningRoom);
        assertThrows(RepositoryCreateException.class, () -> screeningRoomRepositoryForTests.create(newScreeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomTestPositive() {
        int numOfAvailableSeatsBefore = screeningRoomNo1.getNumberOfAvailableSeats();
        screeningRoomNo1.setNumberOfAvailableSeats(numOfAvailableSeatsBefore - 1);
        assertDoesNotThrow(() -> screeningRoomRepositoryForTests.update(screeningRoomNo1));
        int numOfAvailableSeatsAfter = screeningRoomRepositoryForTests.findByUUID(screeningRoomNo1.getScreeningRoomID()).getNumberOfAvailableSeats();
        assertNotEquals(numOfAvailableSeatsBefore, numOfAvailableSeatsAfter);
    }

    @Test
    public void updateCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 2, 50);
        assertNotNull(screeningRoom);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.update(screeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(-1);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.update(foundScreeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(151);
        assertThrows(RepositoryUpdateException.class, () -> screeningRoomRepositoryForTests.update(foundScreeningRoom));
    }

    @Test
    public void deleteCertainScreeningRoomTestPositive() {
        UUID removedScreeningRoomID = screeningRoomNo1.getScreeningRoomID();
        int numOfScreeningRoomsBeforeDelete = screeningRoomRepositoryForTests.findAll().size();
        assertDoesNotThrow(() -> screeningRoomRepositoryForTests.delete(screeningRoomNo1));
        int numOfScreeningRoomsAfterDelete = screeningRoomRepositoryForTests.findAll().size();
        assertNotEquals(numOfScreeningRoomsBeforeDelete, numOfScreeningRoomsAfterDelete);
        assertEquals(numOfScreeningRoomsBeforeDelete - 1, numOfScreeningRoomsAfterDelete);
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(removedScreeningRoomID);
        assertNull(foundScreeningRoom);
    }

    @Test
    public void deleteCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0 , 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(RepositoryDeleteException.class, () -> screeningRoomRepositoryForTests.delete(screeningRoom));
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
        ScreeningRoom foundScreeningRoom = screeningRoomRepositoryForTests.findByUUID(screeningRoom.getScreeningRoomID());
        assertNull(foundScreeningRoom);
    }

    @Test
    public void findAllScreeningRoomsTestPositive() {
        List<ScreeningRoom> listOfAllScreeningRooms = screeningRoomRepositoryForTests.findAll();
        assertNotNull(listOfAllScreeningRooms);
        assertEquals(3, listOfAllScreeningRooms.size());
    }
}
