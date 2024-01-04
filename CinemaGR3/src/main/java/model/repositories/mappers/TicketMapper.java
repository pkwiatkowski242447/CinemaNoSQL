package model.repositories.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.repositories.daos.TicketDao;

@Mapper
public interface TicketMapper {

    @DaoFactory
    TicketDao ticketDao();

    @DaoFactory
    TicketDao ticketDao(@DaoKeyspace String keyspace, @DaoTable String table);
}
