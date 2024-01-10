package model.managers.implementations;

import lombok.Getter;
import lombok.Setter;
import model.exceptions.managers.create_exceptions.TicketManagerCreateException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.delete_exceptions.TicketManagerDeleteException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.exceptions.managers.read_exceptions.TicketManagerReadException;
import model.exceptions.managers.update_exceptions.TicketManagerUpdateException;
import model.exceptions.managers.update_exceptions.UpdateManagerException;
import model.exceptions.repositories.create_exceptions.TicketRepositoryCreateException;
import model.exceptions.repositories.delete_exceptions.TicketRepositoryDeleteException;
import model.exceptions.repositories.read_exceptions.MovieRepositoryReadException;
import model.exceptions.repositories.read_exceptions.TicketRepositoryReadException;
import model.exceptions.repositories.update_exceptions.MovieRepositoryUpdateException;
import model.exceptions.repositories.update_exceptions.TicketRepositoryUpdateException;
import model.exceptions.validation.TicketObjectNotValidException;
import model.managers.interfaces.TicketManagerInterface;
import model.model.Movie;
import model.model.Ticket;
import model.producer.TicketProducer;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Getter @Setter
public class TicketManager implements TicketManagerInterface {

    private static final Logger logger = LoggerFactory.getLogger(TicketManager.class);

    private ClientRepository clientRepository;
    private MovieRepository movieRepository;
    private TicketRepository ticketRepository;
    private TicketProducer ticketProducer;

    public TicketManager(ClientRepository clientRepository, MovieRepository movieRepository, TicketRepository ticketRepository) {
        this.clientRepository = clientRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;

        ticketProducer = new TicketProducer();
        try {
            ticketProducer.createTicketTopic();
            TicketProducer.initializeProducer();
        } catch (InterruptedException | ExecutionException exception) {
            logger.debug(exception.getMessage());
        }
    }

    // Create methods

    @Override
    public Ticket createNormalTicket(Instant movieTime, Instant reservationTime, UUID movieId, UUID clientId) throws TicketManagerCreateException {
        try {
            if (movieTime == null || reservationTime == null || movieId == null || clientId == null) {
                throw new TicketObjectNotValidException("One of given parameters is equal to null.");
            }
            Movie movie = movieRepository.findByUUID(movieId);
            movie.setNumberOfAvailableSeats(movie.getNumberOfAvailableSeats() - 1);
            movieRepository.update(movie);
            Ticket ticket = this.ticketRepository.createNormalTicket(movieTime, reservationTime, movie.getMovieBasePrice(), movieId, clientId);
            ticketProducer.sendTicket(ticket);
            return ticket;
        } catch (MovieRepositoryReadException | MovieRepositoryUpdateException | TicketObjectNotValidException | TicketRepositoryCreateException
                 | InterruptedException | ExecutionException exception) {
            throw new TicketManagerCreateException(exception.getMessage(), exception);
        }
    }

    @Override
    public Ticket createReducedTicket(Instant movieTime, Instant reservationTime, UUID movieId, UUID clientId) throws TicketManagerCreateException {
        try {
            if (movieTime == null || reservationTime == null || movieId == null || clientId == null) {
                throw new TicketObjectNotValidException("One of given parameters is equal to null.");
            }
            Movie movie = movieRepository.findByUUID(movieId);
            movie.setNumberOfAvailableSeats(movie.getNumberOfAvailableSeats() - 1);
            movieRepository.update(movie);
            Ticket ticket = this.ticketRepository.createReducedTicket(movieTime, reservationTime, movie.getMovieBasePrice(), movieId, clientId);
            ticketProducer.sendTicket(ticket);
            return ticket;
        } catch (MovieRepositoryReadException | MovieRepositoryUpdateException | TicketObjectNotValidException | TicketRepositoryCreateException
                 | InterruptedException | ExecutionException exception) {
            throw new TicketManagerCreateException(exception.getMessage(), exception);
        }
    }

    // Read methods

    @Override
    public Ticket findByUUID(UUID ticketId) throws ReadManagerException {
        try {
            return this.ticketRepository.findByUUID(ticketId);
        } catch (TicketRepositoryReadException exception) {
            throw new TicketManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAll() throws ReadManagerException {
        try {
            return this.ticketRepository.findAll();
        } catch (TicketRepositoryReadException exception) {
            throw new TicketManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAllForGivenClientId(UUID clientId) throws TicketManagerReadException {
        try {
            return this.ticketRepository.findAllTicketsForAGivenClientId(clientId);
        } catch (TicketRepositoryReadException exception) {
            throw new TicketManagerReadException(exception.getMessage(), exception);
        }
    }

    @Override
    public List<Ticket> findAllForGivenMovieId(UUID movieId) throws TicketManagerReadException {
        try {
            return this.ticketRepository.findAllTicketsForAGivenMovieId(movieId);
        } catch (TicketRepositoryReadException exception) {
            throw new TicketManagerReadException(exception.getMessage(), exception);
        }
    }

    // Update methods

    @Override
    public void update(Ticket ticket) throws UpdateManagerException {
        try {
             this.ticketRepository.update(ticket);
        } catch (TicketRepositoryUpdateException exception) {
            throw new TicketManagerUpdateException(exception.getMessage(), exception);
        }
    }

    // Delete methods

    @Override
    public void delete(Ticket ticket) throws DeleteManagerException {
        try {
            Movie movie = this.movieRepository.findByUUID(ticket.getMovieId());
            movie.setNumberOfAvailableSeats(movie.getNumberOfAvailableSeats() + 1);
            this.movieRepository.update(movie);
            this.ticketRepository.delete(ticket);
        } catch (MovieRepositoryReadException | MovieRepositoryUpdateException | TicketRepositoryDeleteException exception) {
            throw new TicketManagerDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(UUID ticketId) throws DeleteManagerException {
        try {
            Ticket ticket = this.ticketRepository.findByUUID(ticketId);
            Movie movie = this.movieRepository.findByUUID(ticket.getMovieId());
            movie.setNumberOfAvailableSeats(movie.getNumberOfAvailableSeats() + 1);
            this.movieRepository.update(movie);
            this.ticketRepository.delete(ticket);
        } catch (MovieRepositoryReadException | MovieRepositoryUpdateException | TicketRepositoryDeleteException | TicketRepositoryReadException exception) {
            throw new TicketManagerDeleteException(exception.getMessage(), exception);
        }
    }
}
