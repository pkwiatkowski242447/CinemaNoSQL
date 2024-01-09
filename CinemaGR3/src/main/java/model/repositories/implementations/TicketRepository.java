package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.validation.ConstraintViolation;
import model.dtos.TicketClientDTO;
import model.dtos.TicketMovieDTO;
import model.dtos.converter.TicketConverter;
import model.exceptions.TicketTypeNotFoundException;
import model.model.Ticket;
import model.constants.TicketConstants;
import model.exceptions.repositories.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.exceptions.repositories.update_exceptions.TicketRepositoryUpdateException;
import model.exceptions.validation.TicketObjectNotValidException;
import model.repositories.daos.TicketClientDao;
import model.repositories.daos.TicketMovieDao;
import model.repositories.interfaces.TicketRepositoryInterface;
import model.repositories.mappers.TicketMapper;
import model.repositories.mappers.TicketMapperBuilder;
import model.model.ticket_types.Normal;
import model.model.ticket_types.Reduced;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class TicketRepository extends CassandraClient implements TicketRepositoryInterface {

    private final CqlSession session;
    private final TicketMapper ticketMapper;
    private final TicketClientDao ticketClientDao;
    private final TicketMovieDao ticketMovieDao;

    public TicketRepository(CqlSession cqlSession) {
        this.session = cqlSession;
        this.createTicketsForClientsTable();
        this.createTicketsForMoviesTable();

        this.ticketMapper = new TicketMapperBuilder(session).build();
        this.ticketClientDao = ticketMapper.ticketClientDao();
        this.ticketMovieDao = ticketMapper.ticketMovieDao();
    }

    private void createTicketsForClientsTable() {
        SimpleStatement createTicketsTable = SchemaBuilder
                .createTable(TicketConstants.TICKETS_CLIENTS_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.RESERVATION_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_TYPE_DISCRIMINATOR), DataTypes.TEXT)
                .build();
        session.execute(createTicketsTable);
    }

    private void createTicketsForMoviesTable() {
        SimpleStatement createTicketsTable = SchemaBuilder
                .createTable(TicketConstants.TICKETS_MOVIES_TABLE_NAME)
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.RESERVATION_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_TYPE_DISCRIMINATOR), DataTypes.TEXT)
                .build();
        session.execute(createTicketsTable);
    }

    // Create methods

    @Override
    public Ticket createNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket normalTicket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        try {
            this.checkIfTicketObjectIsValid(normalTicket);
            ticketClientDao.create(TicketConverter.toTicketClientDTO(normalTicket));
            ticketMovieDao.create(TicketConverter.toTicketMovieDTO(normalTicket));
            return TicketConverter.toTicketFromTicketClientDTO(ticketClientDao.findByUUID(normalTicket.getTicketID()));
        } catch (TicketObjectNotValidException | TicketRepositoryReadException | TicketTypeNotFoundException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    @Override
    public Ticket createReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket reducedTicket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        try {
            this.checkIfTicketObjectIsValid(reducedTicket);
            ticketClientDao.create(TicketConverter.toTicketClientDTO(reducedTicket));
            ticketMovieDao.create(TicketConverter.toTicketMovieDTO(reducedTicket));
            return TicketConverter.toTicketFromTicketClientDTO(ticketClientDao.findByUUID(reducedTicket.getTicketID()));
        } catch (TicketObjectNotValidException | TicketRepositoryReadException | TicketTypeNotFoundException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException {
        try {
            return TicketConverter.toTicketFromTicketClientDTO(ticketClientDao.findByUUID(ticketId));
        } catch (TicketRepositoryReadException | TicketTypeNotFoundException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAll() throws TicketRepositoryReadException {
        try {
            List<Ticket> listOfTickets = new ArrayList<>();
            for (TicketClientDTO ticketClientDTO : ticketClientDao.findAll()) {
                listOfTickets.add(TicketConverter.toTicketFromTicketClientDTO(ticketClientDTO));
            }
            return listOfTickets;
        } catch (TicketRepositoryReadException | TicketTypeNotFoundException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAllTicketsForAGivenClientId(UUID clientId) throws TicketRepositoryReadException{
        try {
            List<Ticket> listOfTickets = new ArrayList<>();
            for (TicketClientDTO ticketClientDTO : ticketClientDao.findAllForAGivenClientId(clientId)) {
                listOfTickets.add(TicketConverter.toTicketFromTicketClientDTO(ticketClientDTO));
            }
            return listOfTickets;
        } catch (TicketTypeNotFoundException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAllTicketsForAGivenMovieId(UUID movieId) throws TicketRepositoryReadException {
        try {
            List<Ticket> listOfTickets = new ArrayList<>();
            for (TicketMovieDTO ticketMovieDTO : ticketMovieDao.findAllForAGivenMovieId(movieId)) {
                listOfTickets.add(TicketConverter.toTicketFromTicketMovieDTO(ticketMovieDTO));
            }
            return listOfTickets;
        } catch (TicketTypeNotFoundException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Ticket ticket) throws TicketRepositoryUpdateException {
        try {
            ticketClientDao.findByUUID(ticket.getTicketID());
            ticketMovieDao.findByUUID(ticket.getTicketID());
            this.checkIfTicketObjectIsValid(ticket);
        } catch (TicketRepositoryReadException | TicketObjectNotValidException exception) {
            throw new TicketRepositoryUpdateException(exception.getMessage(), exception);
        }
        ticketClientDao.update(TicketConverter.toTicketClientDTO(ticket));
        ticketMovieDao.update(TicketConverter.toTicketMovieDTO(ticket));
    }

    // Delete methods

    @Override
    public void delete(Ticket ticket) throws TicketRepositoryDeleteException {
        try {
            ticketClientDao.findByUUID(ticket.getTicketID());
            ticketMovieDao.findByUUID(ticket.getTicketID());
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
        ticketClientDao.delete(TicketConverter.toTicketClientDTO(ticket));
        ticketMovieDao.delete(TicketConverter.toTicketMovieDTO(ticket));
    }

    @Override
    public void delete(UUID ticketID) throws TicketRepositoryDeleteException {
        Ticket ticket;
        try {
            ticket = TicketConverter.toTicketFromTicketClientDTO(ticketClientDao.findByUUID(ticketID));
            ticketMovieDao.findByUUID(ticketID);
        } catch (TicketRepositoryReadException | TicketTypeNotFoundException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
        ticketClientDao.delete(TicketConverter.toTicketClientDTO(ticket));
        ticketMovieDao.delete(TicketConverter.toTicketMovieDTO(ticket));
    }

    public void checkIfTicketObjectIsValid(Ticket ticket) throws TicketObjectNotValidException {
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketObjectNotValidException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}
