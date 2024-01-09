package model.repositories.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.repositories.daos.TicketClientDao;
import model.repositories.daos.TicketMovieDao;

@Mapper
public interface TicketMapper {

    @DaoFactory
    TicketClientDao ticketClientDao();

    @DaoFactory
    TicketMovieDao ticketMovieDao();
}
