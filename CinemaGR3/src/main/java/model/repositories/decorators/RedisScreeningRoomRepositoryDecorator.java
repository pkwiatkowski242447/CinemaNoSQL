package model.repositories.decorators;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import mapping_layer.mappers.ScreeningRoomMapper;
import mapping_layer.model_docs.ScreeningRoomDoc;
import model.ScreeningRoom;
import model.exceptions.repository_exceptions.*;
import model.repositories.interfaces.ScreeningRoomRepositoryInterface;
import model.repositories.redis_access.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.UUID;

public class RedisScreeningRoomRepositoryDecorator extends ScreeningRoomRepositoryDecorator {

    private static JedisPool jedisPool;
    private final static int EXPIRE_TIME = 600;
    private final static String hashPrefix = "{screeningRoom}:";

    public RedisScreeningRoomRepositoryDecorator(ScreeningRoomRepositoryInterface wrappedObject) throws RedisConfigNotFoundException {
        super(wrappedObject);
        jedisPool = RedisConnection.createConnection();
    }

    @Override
    public ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        ScreeningRoom screeningRoom;
        try {
            screeningRoom = super.create(screeningRoomFloor, screeningRoomNumber, numberOfSeats);
            this.addToCache(screeningRoom);
            return screeningRoom;
        } catch (ScreeningRoomRepositoryCreateException exception) {
            throw new ScreeningRoomRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    @Override
    public ScreeningRoom findByUUID(UUID screeningRoomID) {
        ScreeningRoom screeningRoom;
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            String screeningRoomKey = hashPrefix + screeningRoomID.toString();
            ScreeningRoomDoc screeningRoomDoc = jsonb.fromJson(jedis.get(screeningRoomKey), ScreeningRoomDoc.class);
            screeningRoom = ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc);
        } catch (JedisConnectionException exception) {
            screeningRoom = super.findByUUID(screeningRoomID);
        } catch (Exception exception) {
            screeningRoom = super.findByUUID(screeningRoomID);
            this.addToCache(screeningRoom);
        }
        return screeningRoom;
    }

    @Override
    public void updateAllFields(ScreeningRoom screeningRoom) {
        try {
            super.updateAllFields(screeningRoom);
            this.addToCache(screeningRoom);
        } catch (ScreeningRoomRepositoryUpdateException exception) {
            throw new ScreeningRoomRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(ScreeningRoom screeningRoom) {
        this.delete(screeningRoom.getScreeningRoomID());
    }

    @Override
    public void delete(UUID screeningRoomID) {
        try {
            this.clearFromCache(screeningRoomID);
            super.delete(screeningRoomID);
        } catch (ScreeningRoomRepositoryDeleteException exception) {
            throw new ScreeningRoomRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(ScreeningRoom screeningRoom) {
        try {
            super.expire(screeningRoom);
            this.updateAllFields(screeningRoom);
        } catch (ScreeningRoomRepositoryUpdateException exception) {
            throw new ScreeningRoomRepositoryUpdateException(exception.getMessage(), exception);
        }
    }

    public void addToCache(ScreeningRoom screeningRoom) {
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            Transaction tx = jedis.multi();
            try {
                String screeningRoomKey = hashPrefix + screeningRoom.getScreeningRoomID().toString();
                tx.watch(screeningRoomKey);
                String screeningRoomValue = jsonb.toJson(ScreeningRoomMapper.toScreeningRoomDoc(screeningRoom));
                tx.set(screeningRoomKey, screeningRoomValue);
                tx.expire(screeningRoomKey, EXPIRE_TIME);
                tx.exec();
            } catch (JedisException exception) {
                tx.discard();
            }
        } catch (JedisConnectionException ignored) {

        } catch (Exception exception) {
            throw new ScreeningRoomRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    public void clearFromCache(UUID screeningRoomID) {
        try (Jedis jedis = jedisPool.getResource()) {
            Transaction tx = jedis.multi();
            try {
                String screeningRoomKey = hashPrefix + screeningRoomID.toString();
                tx.watch(screeningRoomKey);
                tx.del(screeningRoomKey);
                tx.exec();
            } catch (JedisException exception) {
                tx.discard();
            }
        } catch (JedisConnectionException ignored) {

        }
    }

    public ScreeningRoom readScreeningRoomFromCache(UUID screeningRoomID) {
        ScreeningRoom screeningRoom;
        try (Jsonb jsonb = JsonbBuilder.create();
             Jedis jedis = jedisPool.getResource()) {
            String screeningRoomKey = hashPrefix + screeningRoomID.toString();
            ScreeningRoomDoc screeningRoomDoc = jsonb.fromJson(jedis.get(screeningRoomKey), ScreeningRoomDoc.class);
            screeningRoom = ScreeningRoomMapper.toScreeningRoom(screeningRoomDoc);
        } catch (Exception exception) {
            throw new ScreeningRoomRepositoryReadException(exception.getMessage(), exception);
        }
        return screeningRoom;
    }

    public void clearEntireCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
        } catch (JedisConnectionException ignored) {

        }
    }

    @Override
    public void close() {
        jedisPool.close();
        super.close();
    }
}
