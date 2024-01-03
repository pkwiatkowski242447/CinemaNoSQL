package model.repositories.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.repositories.daos.ScreeningRoomDao;

@Mapper
public interface ScreeningRoomMapper {

    @DaoFactory
    ScreeningRoomDao screeningRoomDao();

    @DaoFactory
    ScreeningRoomDao screeningRoomDao(@DaoKeyspace String keyspace);
}
