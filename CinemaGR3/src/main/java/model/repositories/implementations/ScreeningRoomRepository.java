package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlSession;
import model.repositories.daos.ScreeningRoomDao;
import model.repositories.interfaces.ScreeningRoomRepositoryInterface;
import model.repositories.mappers.ScreeningRoomMapper;
import model.repositories.mappers.ScreeningRoomMapperBuilder;

import java.util.List;
import java.util.UUID;

public class ScreeningRoomRepository extends CassandraClient implements ScreeningRoomRepositoryInterface {

    private final CqlSession session;
    private final ScreeningRoomMapper screeningRoomMapper;
    private final ScreeningRoomDao screeningRoomDao;

    public ScreeningRoomRepository() {
        this.session = this.initializeCassandraSession();
        this.screeningRoomMapper = new ScreeningRoomMapperBuilder(session).build();
        this.screeningRoomDao = screeningRoomMapper.screeningRoomDao();
    }

    // Create methods

    @Override
    public ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) {
        return screeningRoomDao.create(screeningRoomFloor, screeningRoomNumber, numberOfSeats);
    }

    // Read methods

    @Override
    public ScreeningRoom findByUUID(UUID screeningRoomId) {
        return screeningRoomDao.findByUUID(screeningRoomId);
    }

    @Override
    public List<ScreeningRoom> findAll() {
        return screeningRoomDao.findAll();
    }

    @Override
    public List<ScreeningRoom> findAllActive() {
        return screeningRoomDao.findAllActive();
    }

    // Update methods

    @Override
    public void update(ScreeningRoom screeningRoom) {
        screeningRoomDao.update(screeningRoom);
    }

    @Override
    public void expire(ScreeningRoom screeningRoom) {
        screeningRoomDao.expire(screeningRoom);
    }

    // Delete methods

    @Override
    public void delete(ScreeningRoom screeningRoom) {
        screeningRoomDao.delete(screeningRoom);
    }

    @Override
    public void delete(UUID screeningRoomID) {
        screeningRoomDao.deleteByUUID(screeningRoomID);
    }

    @Override
    public void close() {
        super.close();
    }
}
