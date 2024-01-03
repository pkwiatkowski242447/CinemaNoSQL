package model.repositories.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.repositories.daos.TypeOfTicketDao;

@Mapper
public interface TypeOfTicketMapper {

    @DaoFactory
    TypeOfTicketDao typeOfTicketDao();

    @DaoFactory
    TypeOfTicketDao typeOfTicketDao(@DaoKeyspace String keyspace);
}
