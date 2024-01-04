package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import model.Ticket;
import model.constants.TicketConstants;
import model.exceptions.TicketTypeNotFoundException;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketQueryProvider {

    private final CqlSession session;
    private final EntityHelper<Ticket> ticketEntityHelper;

    public TicketQueryProvider(MapperContext mapperContext, EntityHelper<Ticket> ticketEntityHelper) {
        this.session = mapperContext.getSession();
        this.ticketEntityHelper = ticketEntityHelper;
    }

    // Read methods

    public Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException {
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_TABLE_NAME)
                .all()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticketId)))
                .allowFiltering()
                .build();
        Row ticketRow = session.execute(findByUUID).one();

        if (ticketRow != null) {
            try {
                return this.convertRowToTicket(ticketRow);
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        } else {
            throw new TicketRepositoryReadException("Ticket object with given Id could not be found in the database.");
        }
    }

    public List<Ticket> findAll() throws TicketRepositoryReadException {
        SimpleStatement findAll = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_TABLE_NAME)
                .all()
                .allowFiltering()
                .build();
        List<Row> ticketRows = session.execute(findAll).all();

        if (!ticketRows.isEmpty()) {
            try {
                return this.convertRowsToTickets(ticketRows);
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        } else {
            throw new TicketRepositoryReadException("Ticket object with given Id could not be found in the database.");
        }
    }

    // Additional methods

    private Ticket convertRowToTicket(Row ticketRow) throws TicketTypeNotFoundException {
        switch (ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR)) {
            case TicketConstants.REDUCED_TICKET -> {
                return new Reduced(
                        ticketRow.getUuid(TicketConstants.TICKET_ID),
                        ticketRow.getInstant(TicketConstants.MOVIE_TIME),
                        ticketRow.getInstant(TicketConstants.RESERVATION_TIME),
                        ticketRow.getDouble(TicketConstants.TICKET_BASE_PRICE),
                        ticketRow.getDouble(TicketConstants.TICKET_FINAL_PRICE),
                        ticketRow.getUuid(TicketConstants.MOVIE_ID),
                        ticketRow.getUuid(TicketConstants.CLIENT_ID),
                        ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR)
                );
            }
            case TicketConstants.NORMAL_TICKET -> {
                return new Normal(
                        ticketRow.getUuid(TicketConstants.TICKET_ID),
                        ticketRow.getInstant(TicketConstants.MOVIE_TIME),
                        ticketRow.getInstant(TicketConstants.RESERVATION_TIME),
                        ticketRow.getDouble(TicketConstants.TICKET_BASE_PRICE),
                        ticketRow.getDouble(TicketConstants.TICKET_FINAL_PRICE),
                        ticketRow.getUuid(TicketConstants.MOVIE_ID),
                        ticketRow.getUuid(TicketConstants.CLIENT_ID),
                        ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR)
                );
            }
            default -> {
                throw new TicketTypeNotFoundException("Could not create ticket for ticket type: " + ticketRow.getString(TicketConstants.TICKET_TYPE_DISCRIMINATOR));
            }
        }
    }

    private List<Ticket> convertRowsToTickets(List<Row> ticketRows) throws TicketTypeNotFoundException {
        List<Ticket> ticketList = new ArrayList<>();
        for (Row ticketRow : ticketRows) {
            ticketList.add(this.convertRowToTicket(ticketRow));
        }
        return ticketList;
    }
}
