package repository_tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.Repository;
import model.repositories.ScreeningRoomRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningRoomRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;
    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(entityManager);
    }

    @AfterAll
    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
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
        assertThrows(ScreeningRoomRepositoryDeleteException.class, () -> screeningRoomRepositoryForTests.delete(screeningRoom));
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
