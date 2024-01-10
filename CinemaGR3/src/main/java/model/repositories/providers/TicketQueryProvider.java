package model.repositories.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import model.constants.TicketConstants;
import model.dtos.TicketClientDTO;
import model.dtos.TicketMovieDTO;
import model.dtos.converter.TicketConverter;
import model.exceptions.TicketTypeNotFoundException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketQueryProvider {

    private final CqlSession session;
    private final EntityHelper<TicketClientDTO> ticketClientDTOEntityHelper;
    private final EntityHelper<TicketMovieDTO> ticketMovieDTOEntityHelper;

    // Constructor

    public TicketQueryProvider(MapperContext mapperContext, EntityHelper<TicketClientDTO> ticketClientDTOEntityHelper, EntityHelper<TicketMovieDTO> ticketMovieDTOEntityHelper) {
        this.session = mapperContext.getSession();
        this.ticketClientDTOEntityHelper = ticketClientDTOEntityHelper;
        this.ticketMovieDTOEntityHelper = ticketMovieDTOEntityHelper;
    }

    // Create methods

    public void createTicket(Ticket ticket) {

        SimpleStatement createClientTicket = QueryBuilder
                .insertInto(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .value(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID), QueryBuilder.literal(ticket.getClientId()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_ID), QueryBuilder.literal(ticket.getTicketID()))
                .value(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), QueryBuilder.literal(ticket.getMovieTime()))
                .value(CqlIdentifier.fromCql(TicketConstants.RESERVATION_TIME), QueryBuilder.literal(ticket.getReservationTime()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), QueryBuilder.literal(ticket.getTicketBasePrice()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), QueryBuilder.literal(ticket.getTicketFinalPrice()))
                .value(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID), QueryBuilder.literal(ticket.getMovieId()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_TYPE_DISCRIMINATOR), QueryBuilder.literal(ticket.getTicketTypeDiscriminator()))
                .build();

        SimpleStatement createMovieTicket = QueryBuilder
                .insertInto(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .value(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID), QueryBuilder.literal(ticket.getMovieId()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_ID), QueryBuilder.literal(ticket.getTicketID()))
                .value(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), QueryBuilder.literal(ticket.getMovieTime()))
                .value(CqlIdentifier.fromCql(TicketConstants.RESERVATION_TIME), QueryBuilder.literal(ticket.getReservationTime()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), QueryBuilder.literal(ticket.getTicketBasePrice()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), QueryBuilder.literal(ticket.getTicketFinalPrice()))
                .value(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID), QueryBuilder.literal(ticket.getClientId()))
                .value(CqlIdentifier.fromCql(TicketConstants.TICKET_TYPE_DISCRIMINATOR), QueryBuilder.literal(ticket.getTicketTypeDiscriminator()))
                .build();

        BatchStatement batchInsert = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(createClientTicket)
                .addStatement(createMovieTicket)
                .build();

        session.execute(batchInsert);
    }

    // Read methods

    public Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException {
        SimpleStatement findByUUID = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticketId)))
                .build();
        Row ticketRow = session.execute(findByUUID).one();

        if (ticketRow != null) {
            try {
                return TicketConverter.toTicketFromTicketClientDTO(this.convertRowToTicketClientDTO(ticketRow));
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        } else {
            throw new TicketRepositoryReadException("Ticket object with given Id could not be found in the database.");
        }
    }

    public List<Ticket> findAll() throws TicketRepositoryReadException {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAll = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .all()
                .allowFiltering()
                .build();
        List<Row> ticketRows = session.execute(findAll).all();

        if (!ticketRows.isEmpty()) {
            try {
                List<TicketClientDTO> listOfFoundTicketClientDTOs = this.convertRowsToTicketClientDTOs(ticketRows);
                for (TicketClientDTO ticketClientDTO : listOfFoundTicketClientDTOs) {
                    listOfFoundTickets.add(TicketConverter.toTicketFromTicketClientDTO(ticketClientDTO));
                }
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        }
        return listOfFoundTickets;
    }

    public List<Ticket> findAllForAGivenClientId(UUID clientId) throws TicketRepositoryReadException {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAllTicketsForAGivenClient = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID)).isEqualTo(QueryBuilder.literal(clientId)))
                .build();
        List<Row> ticketRows = session.execute(findAllTicketsForAGivenClient).all();

        if (!ticketRows.isEmpty()) {
            try {
                List<TicketClientDTO> listOfFoundTicketMovieDTOs = this.convertRowsToTicketClientDTOs(ticketRows);
                for (TicketClientDTO ticketClientDTO : listOfFoundTicketMovieDTOs) {
                    listOfFoundTickets.add(TicketConverter.toTicketFromTicketClientDTO(ticketClientDTO));
                }
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        }
        return listOfFoundTickets;
    }

    public List<Ticket> findAllForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException {
        List<Ticket> listOfFoundTickets = new ArrayList<>();
        SimpleStatement findAllTicketsForAGivenMovie = QueryBuilder
                .selectFrom(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .all()
                .allowFiltering()
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID)).isEqualTo(QueryBuilder.literal(movieId)))
                .build();
        List<Row> ticketRows = session.execute(findAllTicketsForAGivenMovie).all();

        if (!ticketRows.isEmpty()) {
            try {
                List<TicketMovieDTO> listOfFoundTicketMovieDTOs = this.convertRowsToTicketMovieDTOs(ticketRows);
                for (TicketMovieDTO ticketMovieDTO : listOfFoundTicketMovieDTOs) {
                    listOfFoundTickets.add(TicketConverter.toTicketFromTicketMovieDTO(ticketMovieDTO));
                }
            } catch (TicketTypeNotFoundException exception) {
                throw new TicketRepositoryReadException("Incorrect ticket type was found when reading given object.");
            }
        }

        return listOfFoundTickets;
    }

    // Update methods

    public void update(Ticket ticket) {
        SimpleStatement updateTicketClient = QueryBuilder
                .update(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .setColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), QueryBuilder.literal(ticket.getMovieTime()))
                .setColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), QueryBuilder.literal(ticket.getTicketBasePrice()))
                .setColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), QueryBuilder.literal(ticket.getTicketFinalPrice()))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID)).isEqualTo(QueryBuilder.literal(ticket.getClientId())))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticket.getTicketID())))
                .build();

        SimpleStatement updateTicketMovie = QueryBuilder
                .update(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .setColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), QueryBuilder.literal(ticket.getMovieTime()))
                .setColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), QueryBuilder.literal(ticket.getTicketBasePrice()))
                .setColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), QueryBuilder.literal(ticket.getTicketFinalPrice()))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID)).isEqualTo(QueryBuilder.literal(ticket.getMovieId())))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticket.getTicketID())))
                .build();

        BatchStatement batchUpdate = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateTicketClient)
                .addStatement(updateTicketMovie)
                .build();

        session.execute(batchUpdate);
    }

    // Delete methods

    public void delete(Ticket ticket) {
        SimpleStatement deleteTicketClient = QueryBuilder
                .deleteFrom(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID)).isEqualTo(QueryBuilder.literal(ticket.getClientId())))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticket.getTicketID())))
                .build();

        SimpleStatement deleteTicketMovie = QueryBuilder
                .deleteFrom(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID)).isEqualTo(QueryBuilder.literal(ticket.getMovieId())))
                .where(Relation.column(CqlIdentifier.fromCql(TicketConstants.TICKET_ID)).isEqualTo(QueryBuilder.literal(ticket.getTicketID())))
                .build();

        BatchStatement batchDelete = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteTicketClient)
                .addStatement(deleteTicketMovie)
                .build();

        session.execute(batchDelete);
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

    private TicketMovieDTO convertRowToTicketMovieDTO(Row ticketRow) throws TicketTypeNotFoundException {
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

    private List<TicketMovieDTO> convertRowsToTicketMovieDTOs(List<Row> ticketRows) throws TicketTypeNotFoundException {
        List<TicketMovieDTO> ticketList = new ArrayList<>();
        for (Row ticketRow : ticketRows) {
            ticketList.add(this.convertRowToTicketMovieDTO(ticketRow));
        }
        return ticketList;
    }
}
