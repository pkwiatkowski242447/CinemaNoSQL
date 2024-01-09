package model.repositories.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.repositories.daos.MovieDao;

@Mapper
public interface MovieMapper {

    @DaoFactory
    MovieDao movieDao();
}
