package repository_tests.redis;

import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;
import model.exceptions.model_docs_exceptions.ScreeningRoomDocNotFoundException;
import model.exceptions.repository_exceptions.*;
import model.repositories.decorators.RedisScreeningRoomRepositoryDecorator;
import model.repositories.implementations.ScreeningRoomRepository;
import model.repositories.interfaces.ScreeningRoomRepositoryInterface;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RedisScreeningRoomRepositoryDecoratorTest {

    private final static String databaseName = "test";
    private static ScreeningRoomRepositoryInterface screeningRoomRepositoryForTests;
    private static RedisScreeningRoomRepositoryDecorator redisScreeningRoomRepository;
    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    @BeforeAll
    public static void init() throws RedisConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        redisScreeningRoomRepository = new RedisScreeningRoomRepositoryDecorator(screeningRoomRepositoryForTests);
    }

    @BeforeEach
    public void insertExampleScreeningRooms() {
        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        screeningRoomNo1 = redisScreeningRoomRepository.create(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        screeningRoomNo2 = redisScreeningRoomRepository.create(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        screeningRoomNo3 = redisScreeningRoomRepository.create(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);
    }

    @AfterEach
    public void deleteExampleScreeningRooms() {
        List<UUID> listOfScreeningRoomsUUIDs = redisScreeningRoomRepository.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            redisScreeningRoomRepository.delete(screeningRoomID);
        }

        redisScreeningRoomRepository.clearEntireCache();
    }

    @AfterAll
    public static void destroy() {
        redisScreeningRoomRepository.close();
    }

    @Test
    public void redisScreeningRoomRepositoryDecoratorConstructorTest() throws RedisConfigNotFoundException {
        ScreeningRoomRepository screeningRoomRepository = new ScreeningRoomRepository(databaseName);
        assertNotNull(screeningRoomRepository);
        RedisScreeningRoomRepositoryDecorator redisScreeningRoomRepository = new RedisScreeningRoomRepositoryDecorator(screeningRoomRepository);
        assertNotNull(redisScreeningRoomRepository);
    }

    @Test
    public void createScreeningRoomTestPositive() {
        ScreeningRoom newScreeningRoom = redisScreeningRoomRepository.create(1, 6, 70);
        ScreeningRoom newlyCreatedScreeningRoom = redisScreeningRoomRepository.findByUUID(newScreeningRoom.getScreeningRoomID());
        assertNotNull(newlyCreatedScreeningRoom);
        assertEquals(newScreeningRoom, newlyCreatedScreeningRoom);
    }

    @Test
    public void createScreeningRoomWithFloorNumberLesserThan0() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(-1, 8, 30));
    }

    @Test
    public void createScreeningRoomWithFloorNumberGreaterThan3() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(4, 8, 30));
    }

    @Test
    public void createScreeningRoomWithRoomNumberLesserThan1() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(0, 0, 30));
    }

    @Test
    public void createScreeningRoomWithRoomNumberGreaterThan20() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(0, 21, 30));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(0, 8, -1));
    }

    @Test
    public void createScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        assertThrows(ScreeningRoomRepositoryCreateException.class, () -> redisScreeningRoomRepository.create(0, 8, 151));
    }

    @Test
    public void updateCertainScreeningRoomTestPositive() {
        int numOfAvailableSeatsBefore = screeningRoomNo1.getNumberOfAvailableSeats();
        screeningRoomNo1.setNumberOfAvailableSeats(numOfAvailableSeatsBefore - 1);
        assertDoesNotThrow(() -> redisScreeningRoomRepository.updateAllFields(screeningRoomNo1));
        int numOfAvailableSeatsAfter = redisScreeningRoomRepository.findByUUID(screeningRoomNo1.getScreeningRoomID()).getNumberOfAvailableSeats();
        assertNotEquals(numOfAvailableSeatsBefore, numOfAvailableSeatsAfter);
    }

    @Test
    public void updateCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 2, 50);
        assertNotNull(screeningRoom);
        assertThrows(RepositoryUpdateException.class, () -> redisScreeningRoomRepository.updateAllFields(screeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsLesserThan0() {
        ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(-1);
        assertThrows(RepositoryUpdateException.class, () -> redisScreeningRoomRepository.updateAllFields(foundScreeningRoom));
    }

    @Test
    public void updateCertainScreeningRoomWithNumberOfAvailableSeatsGreaterThan150() {
        ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findAll().get(0);
        assertNotNull(foundScreeningRoom);
        foundScreeningRoom.setNumberOfAvailableSeats(151);
        assertThrows(RepositoryUpdateException.class, () -> redisScreeningRoomRepository.updateAllFields(foundScreeningRoom));
    }

    @Test
    public void deleteCertainScreeningRoomTestPositive() {
        UUID removedScreeningRoomID = screeningRoomNo1.getScreeningRoomID();
        int numOfScreeningRoomsBeforeDelete = redisScreeningRoomRepository.findAll().size();
        assertDoesNotThrow(() -> redisScreeningRoomRepository.delete(screeningRoomNo1));
        int numOfScreeningRoomsAfterDelete = redisScreeningRoomRepository.findAll().size();
        assertNotEquals(numOfScreeningRoomsBeforeDelete, numOfScreeningRoomsAfterDelete);
        assertEquals(numOfScreeningRoomsBeforeDelete - 1, numOfScreeningRoomsAfterDelete);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findByUUID(removedScreeningRoomID);
        });
    }

    @Test
    public void deleteCertainScreeningRoomThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryDeleteException.class, () -> redisScreeningRoomRepository.delete(screeningRoom));
    }

    @Test
    public void deleteCertainScreeningRoomWithUUIDThatIsNotInTheDatabaseTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 6, 90);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryDeleteException.class, () -> redisScreeningRoomRepository.delete(screeningRoom.getScreeningRoomID()));
    }

    @Test
    public void expireCertainScreeningRoomTestPositive() {
        UUID expiredScreeningRoomUUID = screeningRoomNo1.getScreeningRoomID();
        int beforeExpiringScreeningRoom = redisScreeningRoomRepository.findAll().size();
        int numOfActiveScreeningRoomsBefore = redisScreeningRoomRepository.findAllActive().size();
        redisScreeningRoomRepository.expire(screeningRoomNo1);
        int afterExpiringScreeningRoom = redisScreeningRoomRepository.findAll().size();
        int numOfActiveScreeningRoomsAfter = redisScreeningRoomRepository.findAllActive().size();
        ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findByUUID(expiredScreeningRoomUUID);
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
            redisScreeningRoomRepository.expire(screeningRoom);
        });
    }

    @Test
    public void findCertainScreeningRoomTestPositive() {
        ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findByUUID(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(foundScreeningRoom);
        assertEquals(foundScreeningRoom, screeningRoomNo1);
    }

    @Test
    public void findCertainScreeningRoomTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 0, 2, 50);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            ScreeningRoom foundScreeningRoom = redisScreeningRoomRepository.findByUUID(screeningRoom.getScreeningRoomID());
        });
    }

    @Test
    public void findAllScreeningRoomsTestPositive() {
        List<ScreeningRoom> listOfAllScreeningRooms = redisScreeningRoomRepository.findAll();
        assertNotNull(listOfAllScreeningRooms);
        assertEquals(3, listOfAllScreeningRooms.size());
    }

    @Test
    public void findAllActiveScreeningRoomTestPositive() {
        List<ScreeningRoom> startingListOfScreeningRooms = redisScreeningRoomRepository.findAllActive();
        assertNotNull(startingListOfScreeningRooms);
        redisScreeningRoomRepository.expire(startingListOfScreeningRooms.get(0));
        List<ScreeningRoom> endingListOfScreeningRooms = redisScreeningRoomRepository.findAllActive();
        assertNotNull(endingListOfScreeningRooms);
        assertEquals(startingListOfScreeningRooms.size(), 3);
        assertEquals(endingListOfScreeningRooms.size(), 2);
    }

    // Other

    @Test
    public void mongoRepositoryFindScreeningRoomDocTestPositive() {
        ScreeningRoomDoc screeningRoomDoc = redisScreeningRoomRepository.findScreeningRoomDoc(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(screeningRoomDoc);
        assertEquals(screeningRoomDoc.getScreeningRoomID(), screeningRoomNo1.getScreeningRoomID());
        assertEquals(screeningRoomDoc.getScreeningRoomFloor(), screeningRoomNo1.getScreeningRoomFloor());
        assertEquals(screeningRoomDoc.getScreeningRoomNumber(), screeningRoomNo1.getScreeningRoomNumber());
        assertEquals(screeningRoomDoc.getNumberOfAvailableSeats(), screeningRoomNo1.getNumberOfAvailableSeats());
        assertEquals(screeningRoomDoc.isScreeningRoomStatusActive(), screeningRoomNo1.isScreeningRoomStatusActive());
    }

    @Test
    public void mongoRepositoryFindScreeningRoomDocTestNegative() {
        ScreeningRoom screeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 45);
        assertNotNull(screeningRoom);
        assertThrows(ScreeningRoomDocNotFoundException.class, () -> {
            redisScreeningRoomRepository.findScreeningRoomDoc(screeningRoom.getScreeningRoomID());
        });
    }

    @Test
    public void addScreeningRoomToRedisCacheTestPositive() {
        ScreeningRoom testScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 45);
        assertNotNull(testScreeningRoom);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            redisScreeningRoomRepository.readScreeningRoomFromCache(testScreeningRoom.getScreeningRoomID());
        });
        redisScreeningRoomRepository.addToCache(testScreeningRoom);
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.readScreeningRoomFromCache(testScreeningRoom.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(testScreeningRoom, cacheScreeningRoom);
    }

    @Test
    public void readScreeningRoomFromRedisCacheTestPositive() {
        ScreeningRoom testScreeningRoom = new ScreeningRoom(UUID.randomUUID(), 1, 2, 45);
        assertNotNull(testScreeningRoom);
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            redisScreeningRoomRepository.readScreeningRoomFromCache(testScreeningRoom.getScreeningRoomID());
        });
        redisScreeningRoomRepository.addToCache(testScreeningRoom);
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.readScreeningRoomFromCache(testScreeningRoom.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(testScreeningRoom, cacheScreeningRoom);
        redisScreeningRoomRepository.clearFromCache(testScreeningRoom.getScreeningRoomID());
        assertThrows(ScreeningRoomRepositoryReadException.class, () -> {
            redisScreeningRoomRepository.readScreeningRoomFromCache(testScreeningRoom.getScreeningRoomID());
        });
    }

    @Test
    public void readScreeningRoomDataFromDBEvenWithoutWorkingRedis() {
        // Redis needs to be turned off.
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(screeningRoomNo1, cacheScreeningRoom);
    }

    @Test
    public void readScreeningRoomDataFromRedisCacheWhenItIsInCache() {
        ScreeningRoom testScreeningRoom = redisScreeningRoomRepository.create(1, 2, 45);
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(testScreeningRoom.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(testScreeningRoom, cacheScreeningRoom);
    }

    @Test
    public void readScreeningRoomDataFromRedisCacheWhenItIsNotInCache() {
        ScreeningRoom testScreeningRoom = redisScreeningRoomRepository.create(1, 2, 45);
        redisScreeningRoomRepository.clearFromCache(testScreeningRoom.getScreeningRoomID());
        ScreeningRoom mongoScreeningRoom = redisScreeningRoomRepository.findByUUID(testScreeningRoom.getScreeningRoomID());
        assertNotNull(mongoScreeningRoom);
        assertEquals(testScreeningRoom, mongoScreeningRoom);
    }
}
