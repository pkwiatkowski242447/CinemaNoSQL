package repository_tests.benchmarks;

import model.ScreeningRoom;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
import model.exceptions.repository_exceptions.RedisConfigNotFoundException;
import model.repositories.implementations.ScreeningRoomRepository;
import org.junit.jupiter.api.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class ScreeningRoomRepositoryBenchmarkTest {

    private final static String databaseName = "test";
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;

    private static RedisScreeningRoomRepositoryDecorator redisScreeningRoomRepository;
    private List<ScreeningRoom> listOfScreeningRooms = new ArrayList<>();

    @Setup
    public void init() throws RedisConfigNotFoundException, MongoConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        redisScreeningRoomRepository = new RedisScreeningRoomRepositoryDecorator(screeningRoomRepositoryForTests);

        for (int i = 0; i < 300; i++) {
            listOfScreeningRooms.add(screeningRoomRepositoryForTests.create((i % 3) + 1, (i % 20) + 1, ((i * 10) % 150)));
        }
    }

    @TearDown
    public void destroy() {
        List<UUID> listOfScreeningRoomsUUIDs = screeningRoomRepositoryForTests.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            screeningRoomRepositoryForTests.delete(screeningRoomID);
        }

        redisScreeningRoomRepository.clearEntireCache();

        screeningRoomRepositoryForTests.close();
    }

    @Test
    public void launchBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(2)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void readScreeningRoomDataFromRedisCacheWhenItIsInCache() {
        for (int i = 0; i < 300; i++) {
            redisScreeningRoomRepository.addToCache(listOfScreeningRooms.get(i));
            ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(listOfScreeningRooms.get(i).getScreeningRoomID());
            assertNotNull(cacheScreeningRoom);
            assertEquals(listOfScreeningRooms.get(i), cacheScreeningRoom);
        }
    }

    @Benchmark
    public void readScreeningRoomDataFromRedisCacheWhenItIsNotInCache() {
        for (int i = 0; i < 300; i++) {
            redisScreeningRoomRepository.addToCache(listOfScreeningRooms.get(i));
            redisScreeningRoomRepository.clearFromCache(listOfScreeningRooms.get(i).getScreeningRoomID());
            ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(listOfScreeningRooms.get(i).getScreeningRoomID());
            assertNotNull(cacheScreeningRoom);
            assertEquals(listOfScreeningRooms.get(i), cacheScreeningRoom);
        }
    }
}
