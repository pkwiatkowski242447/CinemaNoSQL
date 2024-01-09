package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import model.constants.TicketConstants;
import model.dtos.TicketMovieDTO;
import model.exceptions.TicketTypeNotFoundException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketMovieQueryProvider {

    private final CqlSession session;
    private final EntityHelper<TicketMovieDTO> ticketEntityHelper;

    public TicketMovieQueryProvider(MapperContext mapperContext, EntityHelper<TicketMovieDTO> ticketEntityHelper) {
        this.session = mapperContext.getSession();
        this.ticketEntityHelper = ticketEntityHelper;
    }

    // Read methods

    public TicketMovieDTO findByUUID(UUID ticketClientDTOId) throws TicketRepositoryReadException {
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticketClientDTOId)))
                .build();
        Row ticketRow = session.execute(findByUUID).one();

        if (ticketRow != null) {
            try {
                return this.convertRowToTicketClientDTO(ticketRow);
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        } else {
            throw new TicketRepositoryReadException("Ticket object with given Id could not be found in the database.");
        }
    }

    public List<TicketMovieDTO> findAll() throws TicketRepositoryReadException {
        List<TicketMovieDTO> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAll = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .all()
                .allowFiltering()
                .build();
        List<Row> ticketRows = session.execute(findAll).all();

        if (!ticketRows.isEmpty()) {
            try {
                listOfFoundTickets.addAll(this.convertRowsToTicketClientDTOs(ticketRows));
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        }
        return listOfFoundTickets;
    }

    public List<TicketMovieDTO> findAllForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException {
        List<TicketMovieDTO> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAllTicketsForAGivenMovie = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID)).isEqualTo(QueryBuilder.literal(movieId)))
                .build();
        List<Row> ticketRows = session.execute(findAllTicketsForAGivenMovie).all();

        if (!ticketRows.isEmpty()) {
            try {
                listOfFoundTickets.addAll(this.convertRowsToTicketClientDTOs(ticketRows));
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        }
        return listOfFoundTickets;
    }

    // Additional methods

    private TicketMovieDTO convertRowToTicketClientDTO(Row ticketRow) throws TicketTypeNotFoundException {
        return new TicketMovieDTO(
                ticketRow.getUuid(TicketConstants.MOVIE_ID),
                ticketRow.getUuid(TicketConstants.TICKET_ID),
                ticketRow.getInstant(TicketConstants.MOVIE_TIME),
                ticketRow.getInstant(TicketConstants.RESERVATION_TIME),
                ticketRow.getDouble(TicketConstants.TICKET_BASE_PRICE),
                ticketRow.getDouble(TicketConstants.TICKET_FINAL_PRICE),
                ticketRow.getUuid(TicketConstants.CLIENT_ID),
                ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR)
        );
    }

    private List<TicketMovieDTO> convertRowsToTicketClientDTOs(List<Row> ticketRows) throws TicketTypeNotFoundException {
        List<TicketMovieDTO> ticketList = new ArrayList<>();
        for (Row ticketRow : ticketRows) {
            ticketList.add(this.convertRowToTicketClientDTO(ticketRow));
        }
        return ticketList;
    }
}
