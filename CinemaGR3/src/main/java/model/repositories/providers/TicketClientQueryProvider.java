package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import model.dtos.TicketClientDTO;
import model.constants.TicketConstants;
import model.exceptions.TicketTypeNotFoundException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketClientQueryProvider {

    private final CqlSession session;
    private final EntityHelper<TicketClientDTO> ticketEntityHelper;

    public TicketClientQueryProvider(MapperContext mapperContext, EntityHelper<TicketClientDTO> ticketEntityHelper) {
        this.session = mapperContext.getSession();
        this.ticketEntityHelper = ticketEntityHelper;
    }

    // Read methods

    public TicketClientDTO findByUUID(UUID ticketClientDTOId) throws TicketRepositoryReadException {
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
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

    public List<TicketClientDTO> findAll() throws TicketRepositoryReadException {
        List<TicketClientDTO> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAll = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
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

    public List<TicketClientDTO> findAllForAGivenClientId(UUID clientId) throws TicketRepositoryReadException {
        List<TicketClientDTO> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAllTicketsForAGivenClient = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID)).isEqualTo(QueryBuilder.literal(clientId)))
                .build();
        List<Row> ticketRows = session.execute(findAllTicketsForAGivenClient).all();

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

    private TicketClientDTO convertRowToTicketClientDTO(Row ticketRow) throws TicketTypeNotFoundException {
        return new TicketClientDTO(
                ticketRow.getUuid(TicketConstants.CLIENT_ID),
                ticketRow.getUuid(TicketConstants.TICKET_ID),
                ticketRow.getInstant(TicketConstants.MOVIE_TIME),
                ticketRow.getInstant(TicketConstants.RESERVATION_TIME),
                ticketRow.getDouble(TicketConstants.TICKET_BASE_PRICE),
                ticketRow.getDouble(TicketConstants.TICKET_FINAL_PRICE),
                ticketRow.getUuid(TicketConstants.MOVIE_ID),
                ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR)
        );
    }

    private List<TicketClientDTO> convertRowsToTicketClientDTOs(List<Row> ticketRows) throws TicketTypeNotFoundException {
        List<TicketClientDTO> ticketList = new ArrayList<>();
        for (Row ticketRow : ticketRows) {
            ticketList.add(this.convertRowToTicketClientDTO(ticketRow));
        }
        return ticketList;
    }
}
