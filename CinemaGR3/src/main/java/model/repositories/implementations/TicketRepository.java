package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.validation.ConstraintViolation;
import model.Ticket;
import model.constants.TicketConstants;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.read_exceptions.TicketRepositoryReadException;
import model.exceptions.update_exceptions.TicketRepositoryUpdateException;
import model.repositories.daos.TicketDao;
import model.repositories.interfaces.TicketRepositoryInterface;
import model.repositories.mappers.TicketMapper;
import model.repositories.mappers.TicketMapperBuilder;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class TicketRepository extends CassandraClient implements TicketRepositoryInterface {

    private final CqlSession session;
    private final TicketDao ticketDao;

    public TicketRepository() throws CassandraConfigNotFound {
        this.session = this.initializeCassandraSession();
        this.createTicketsTable();

        TicketMapper ticketMapper = new TicketMapperBuilder(session).build();
        this.ticketDao = ticketMapper.ticketDao();
    }

    private void createTicketsTable() {
        SimpleStatement createTicketsTable = SchemaBuilder
                .createTable(TicketConstants.TICKETS_TABLE_NAME)
                .withPartitionKey(CqlIdentifier.fromCql(TicketConstants.TICKET_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.RESERVATION_TIME), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_BASE_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_FINAL_PRICE), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.MOVIE_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.CLIENT_ID), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql(TicketConstants.TICKET_TYPE_DISCRIMINATOR), DataTypes.TEXT)
                .build();
        session.execute(createTicketsTable);
    }

    // Create methods

    @Override
    public Ticket createNormalTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket normalTicket = new Normal(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        this.validateTicketCreation(normalTicket);
        ticketDao.create(normalTicket);
        try {
            return ticketDao.findByUUID(normalTicket.getTicketID());
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    @Override
    public Ticket createReducedTicket(Instant movieTime, Instant reservationTime, double ticketBasePrice, UUID movieId, UUID clientId) throws TicketRepositoryCreateException {
        Ticket reducedTicket = new Reduced(UUID.randomUUID(), movieTime, reservationTime, ticketBasePrice, movieId, clientId);
        this.validateTicketCreation(reducedTicket);
        ticketDao.create(reducedTicket);
        try {
            return ticketDao.findByUUID(reducedTicket.getTicketID());
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Ticket findByUUID(UUID ticketId) throws TicketRepositoryReadException {
        try {
            return ticketDao.findByUUID(ticketId);
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAll() throws TicketRepositoryReadException {
        try {
            return ticketDao.findAll();
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Ticket ticket) throws TicketRepositoryUpdateException {
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketRepositoryUpdateException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        ticketDao.update(ticket);
    }

    // Delete methods

    @Override
    public void delete(Ticket ticket) throws TicketRepositoryDeleteException {
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketRepositoryDeleteException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        ticketDao.delete(ticket);
    }

    @Override
    public void delete(UUID ticketID) throws TicketRepositoryDeleteException {
        Ticket ticket;
        try {
            ticket = ticketDao.findByUUID(ticketID);
        } catch (TicketRepositoryReadException exception) {
            throw new TicketRepositoryDeleteException(exception.getMessage(), exception);
        }
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketRepositoryDeleteException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
        ticketDao.delete(ticket);
    }

    private void validateTicketCreation(Ticket ticket) throws TicketRepositoryCreateException {
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Ticket> violation : violations) {
                stringBuilder.append(violation.toString()).append(" : ");
            }
            throw new TicketRepositoryCreateException("Bean validation for client object failed. Cause: " + stringBuilder);
        }
    }
}
