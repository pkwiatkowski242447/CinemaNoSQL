package repository_tests.benchmarks;

import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RedisConfigNotFoundException;
import model.repositories.decorators.RedisScreeningRoomRepositoryDecorator;
import model.repositories.implementations.ScreeningRoomRepository;
import org.junit.jupiter.api.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

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
    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;
    private static RedisScreeningRoomRepositoryDecorator redisScreeningRoomRepository;

    @Setup
    public void init() throws RedisConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        redisScreeningRoomRepository = new RedisScreeningRoomRepositoryDecorator(screeningRoomRepositoryForTests);

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
        redisScreeningRoomRepository.addToCache(screeningRoomNo1);
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(screeningRoomNo1, cacheScreeningRoom);
    }

    @Benchmark
    public void readScreeningRoomDataFromRedisCacheWhenItIsNotInCache() {
        redisScreeningRoomRepository.addToCache(screeningRoomNo1);
        redisScreeningRoomRepository.clearFromCache(screeningRoomNo1.getScreeningRoomID());
        ScreeningRoom cacheScreeningRoom = redisScreeningRoomRepository.findByUUID(screeningRoomNo1.getScreeningRoomID());
        assertNotNull(cacheScreeningRoom);
        assertEquals(screeningRoomNo1, cacheScreeningRoom);
    }
}
