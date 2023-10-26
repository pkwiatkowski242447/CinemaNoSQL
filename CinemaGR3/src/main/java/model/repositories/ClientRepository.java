package model.repositories;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Client;
import model.Ticket;
import model.exceptions.repository_exceptions.*;

import java.util.List;
import java.util.UUID;

public class ClientRepository extends Repository<Client> {

    public ClientRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Client create(String clientName, String clientSurname, int clientAge) {
        Client client;
        try {
            client = new Client(UUID.randomUUID(), clientName, clientSurname, clientAge);
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(client);
            getEntityManager().getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryCreateException(exception.getMessage(), exception);
        }
        return client;
    }

    @Override
    public void delete(Client client) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(client, LockModeType.PESSIMISTIC_WRITE);

            CriteriaQuery<Ticket> findAllTicketsForAGivenClient = getEntityManager().getCriteriaBuilder().createQuery(Ticket.class);
            Root<Ticket> ticketRoot = findAllTicketsForAGivenClient.from(Ticket.class);
            findAllTicketsForAGivenClient.select(ticketRoot).where(getEntityManager().getCriteriaBuilder().equal(ticketRoot.get("client"), client));
            List<Ticket> listOfTickets = getEntityManager().createQuery(findAllTicketsForAGivenClient).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();

            for (Ticket ticket : listOfTickets) {
                getEntityManager().remove(ticket);
            }

            getEntityManager().remove(getEntityManager().merge(client));
            getEntityManager().getTransaction().commit();
        } catch(IllegalArgumentException | PersistenceException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public void expire(Client client) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().lock(client, LockModeType.PESSIMISTIC_WRITE);
            client.setClientStatusActive(false);
            getEntityManager().merge(client);
            getEntityManager().getTransaction().commit();
        } catch (PersistenceException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryDeleteException(exception.getMessage(), exception);
        }
    }

    @Override
    public Client findByUUID(UUID identifier) {
        Client clientToBeRead = null;
        try {
            getEntityManager().getTransaction().begin();
            clientToBeRead = getEntityManager().find(Client.class, identifier, LockModeType.PESSIMISTIC_READ);
            getEntityManager().getTransaction().commit();
        } catch (IllegalArgumentException | TransactionRequiredException | OptimisticLockException |
                PessimisticLockException | LockTimeoutException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return clientToBeRead;
    }

    @Override
    public List<Client> findAll() {
        List<Client> listOfAllClients = null;
        try {
            getEntityManager().getTransaction().begin();
            CriteriaQuery<Client> findAllClients = getEntityManager().getCriteriaBuilder().createQuery(Client.class);
            Root<Client> clientRoot = findAllClients.from(Client.class);
            findAllClients.select(clientRoot);
            listOfAllClients = getEntityManager().createQuery(findAllClients).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
            getEntityManager().getTransaction().commit();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllClients;
    }

    @Override
    public List<Client> findAllActive() {
        List<Client> listOfAllActiveClients = null;
        try {
            getEntityManager().getTransaction().begin();
            CriteriaQuery<Client> findAllActiveClients = getEntityManager().getCriteriaBuilder().createQuery(Client.class);
            Root<Client> clientRoot = findAllActiveClients.from(Client.class);
            findAllActiveClients.select(clientRoot).where(getEntityManager().getCriteriaBuilder().equal(clientRoot.get("clientStatusActive"), true));
            listOfAllActiveClients = getEntityManager().createQuery(findAllActiveClients).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
            getEntityManager().getTransaction().commit();
        } catch (IllegalStateException | IllegalArgumentException exception) {
            getEntityManager().getTransaction().rollback();
            throw new ClientRepositoryReadException(exception.getMessage(), exception);
        }
        return listOfAllActiveClients;
    }
}
