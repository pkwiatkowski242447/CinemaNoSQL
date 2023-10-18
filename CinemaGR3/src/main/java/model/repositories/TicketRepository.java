package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Ticket;
import model.exceptions.repository_exceptions.RepositoryReadException;

import java.util.List;
import java.util.UUID;

public class TicketRepository extends Repository<Ticket> {

    public TicketRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Ticket findByUUID(UUID identifier) {
        Ticket ticketToBeRead = null;
        try {
            getEntityManager().getTransaction().begin();
            ticketToBeRead = getEntityManager().find(Ticket.class, identifier, LockModeType.PESSIMISTIC_READ);
            getEntityManager().getTransaction().commit();
        } catch (IllegalArgumentException | TransactionRequiredException | OptimisticLockException |
                 PessimisticLockException | LockTimeoutException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryReadException("Source: TicketRepository ; " + exception.getMessage(), exception);
        }
        return ticketToBeRead;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> listOfAllTickets = null;
        try {
            getEntityManager().getTransaction().begin();
            CriteriaQuery<Ticket> findAllTickets = getEntityManager().getCriteriaBuilder().createQuery(Ticket.class);
            Root<Ticket> ticketRoot = findAllTickets.from(Ticket.class);
            findAllTickets.select(ticketRoot);
            listOfAllTickets = getEntityManager().createQuery(findAllTickets).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
            getEntityManager().getTransaction().commit();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryReadException("Source: TicketRepository ; " + exception.getMessage(), exception);
        }
        return listOfAllTickets;
    }
}
