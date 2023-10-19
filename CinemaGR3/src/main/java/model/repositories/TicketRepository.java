package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Ticket;
import model.exceptions.repository_exceptions.RepositoryCreateException;
import model.exceptions.repository_exceptions.RepositoryDeleteException;
import model.exceptions.repository_exceptions.RepositoryReadException;
import model.ticket_types.TypeOfTicket;

import java.util.List;
import java.util.UUID;

public class TicketRepository extends Repository<Ticket> {

    public TicketRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void create(Ticket ticket) {
        try {
            getEntityManager().getTransaction().begin();

            CriteriaQuery<TypeOfTicket> getAllTypesOfTickets = getEntityManager().getCriteriaBuilder().createQuery(TypeOfTicket.class);
            Root<TypeOfTicket> typeOfTicketRoot = getAllTypesOfTickets.from(TypeOfTicket.class);
            getAllTypesOfTickets.select(typeOfTicketRoot);
            List<TypeOfTicket> listOfTypesOfTickets = getEntityManager().createQuery(getAllTypesOfTickets).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();

            boolean isInTheDB = false;
            for (TypeOfTicket type : listOfTypesOfTickets) {
                if (ticket.getTicketType().getClass().equals(type.getClass())) {
                    ticket.setTypeOfTicket(type);
                    isInTheDB = true;
                }
            }

            if (!isInTheDB) {
                getEntityManager().persist(ticket.getTicketType());
            }

            getEntityManager().persist(ticket);
            getEntityManager().getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryCreateException("Source: TicketRepository ; " + exception.getMessage(), exception);
        }
    }

    @Override
    public void delete(Ticket ticket) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(ticket, LockModeType.PESSIMISTIC_WRITE);
            getEntityManager().remove(getEntityManager().merge(ticket));
            getEntityManager().getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            getEntityManager().getTransaction().rollback();
            throw new RepositoryDeleteException("Source: TicketRepository ; " + exception.getMessage(), exception);
        }
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
