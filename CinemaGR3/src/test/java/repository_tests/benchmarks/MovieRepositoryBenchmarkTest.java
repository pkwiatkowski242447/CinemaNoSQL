package repository_tests.benchmarks;

import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.RedisConfigNotFoundException;
import model.repositories.implementations.MovieRepository;
import model.repositories.decorators.RedisMovieRepositoryDecorator;
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
public class MovieRepositoryBenchmarkTest {

    private final static String databaseName = "test";
    private static MovieRepository movieRepositoryForTests;
    private static RedisMovieRepositoryDecorator redisMovieRepository;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;

    private ScreeningRoom screeningRoomNo1;
    private ScreeningRoom screeningRoomNo2;
    private ScreeningRoom screeningRoomNo3;

    private Movie movieNo1;
    private Movie movieNo2;
    private Movie movieNo3;

    @Setup
    public void init() throws RedisConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        movieRepositoryForTests = new MovieRepository(databaseName);
        redisMovieRepository = new RedisMovieRepositoryDecorator(movieRepositoryForTests);

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

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        movieNo1 = movieRepositoryForTests.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1);
        movieNo2 = movieRepositoryForTests.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2);
        movieNo3 = movieRepositoryForTests.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3);
    }

    @TearDown
    public void destroy() {
        List<UUID> listOfMoviesID = movieRepositoryForTests.findAllUUIDs();
        for (UUID movieID : listOfMoviesID) {
            movieRepositoryForTests.delete(movieID);
        }

        List<UUID> listOfScreeningRoomsUUIDs = screeningRoomRepositoryForTests.findAllUUIDs();
        for (UUID screeningRoomID : listOfScreeningRoomsUUIDs) {
            screeningRoomRepositoryForTests.delete(screeningRoomID);
        }

        redisMovieRepository.clearEntireCache();

        screeningRoomRepositoryForTests.close();
        movieRepositoryForTests.close();
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
    public void readMovieDataFromRedisCacheWhenItIsInCache() {
        redisMovieRepository.addToCache(movieNo1);
        Movie cacheMovie = redisMovieRepository.findByUUID(movieNo1.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(movieNo1, cacheMovie);
    }

    @Benchmark
    public void readMovieDataFromRedisCacheWhenItIsNotInCache() {
        redisMovieRepository.addToCache(movieNo1);
        redisMovieRepository.clearFromCache(movieNo1.getMovieID());
        Movie cacheMovie = redisMovieRepository.findByUUID(movieNo1.getMovieID());
        assertNotNull(cacheMovie);
        assertEquals(movieNo1, cacheMovie);
    }
}
