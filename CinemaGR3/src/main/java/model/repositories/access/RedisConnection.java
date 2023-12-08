package model.repositories.access;

import model.exceptions.repository_exceptions.RedisConfigNotFoundException;
import model.repositories.decorators.RedisMovieRepositoryDecorator;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConnection {

    public static JedisPool createConnection() throws RedisConfigNotFoundException {
        JedisPool jedisPool;
        Properties properties = new Properties();
        try (InputStream input = RedisMovieRepositoryDecorator.class.getClassLoader().getResourceAsStream("redis.properties")) {
            properties.load(input);
            jedisPool = new JedisPool(new HostAndPort(
                    properties.getProperty("redis.host"),
                    Integer.parseInt(properties.getProperty("redis.port"))
            ), DefaultJedisClientConfig.builder().build());
        } catch (IOException ioException) {
            throw new RedisConfigNotFoundException("Redis config file was not found!");
        }

        return jedisPool;
    }
}
