package repository_tests.benchmarks;

import model.Movie;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
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
public class MovieRepositoryBenchmarkTest {

    private final static String databaseName = "test";
    private static MovieRepository movieRepositoryForTests;
    private static RedisMovieRepositoryDecorator redisMovieRepository;
    private static ScreeningRoomRepository screeningRoomRepositoryForTests;

    private List<ScreeningRoom> listOfScreeningRooms = new ArrayList<>();
    private List<Movie> listOfMovies = new ArrayList<>();

    @Setup
    public void init() throws RedisConfigNotFoundException, MongoConfigNotFoundException {
        screeningRoomRepositoryForTests = new ScreeningRoomRepository(databaseName);
        movieRepositoryForTests = new MovieRepository(databaseName);
        redisMovieRepository = new RedisMovieRepositoryDecorator(movieRepositoryForTests);

        for (int i = 0; i < 300; i++) {
            listOfScreeningRooms.add(screeningRoomRepositoryForTests.create((i % 3) + 1, (i % 20) + 1, ((i * 10) % 150)));
        }

        for (int i = 0; i < 300; i++) {
            listOfMovies.add(movieRepositoryForTests.create("ExampleMovieTitle" + i, (i * 10) % 100, listOfScreeningRooms.get(i)));
        }
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
        for (int i = 0; i < 300; i++) {
            redisMovieRepository.addToCache(listOfMovies.get(i));
            Movie cacheMovie = redisMovieRepository.findByUUID(listOfMovies.get(i).getMovieID());
            assertNotNull(cacheMovie);
            assertEquals(listOfMovies.get(i), cacheMovie);
        }
    }

    @Benchmark
    public void readMovieDataFromRedisCacheWhenItIsNotInCache() {
        for (int i = 0; i < 300; i++) {
            redisMovieRepository.addToCache(listOfMovies.get(i));
            redisMovieRepository.clearFromCache(listOfMovies.get(i).getMovieID());
            Movie cacheMovie = redisMovieRepository.findByUUID(listOfMovies.get(i).getMovieID());
            assertNotNull(cacheMovie);
            assertEquals(listOfMovies.get(i), cacheMovie);
        }
    }
}
