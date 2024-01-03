package model.repositories.implementations;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Client;
import model.repositories.daos.ClientDao;
import model.repositories.interfaces.ClientRepositoryInterface;
import model.repositories.mappers.ClientMapper;
import model.repositories.mappers.ClientMapperBuilder;

import java.util.List;
import java.util.UUID;

public class ClientRepository extends CassandraClient implements ClientRepositoryInterface {

    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;

    public ClientRepository() {
        this.session = this.initializeCassandraSession();
        this.clientMapper = new ClientMapperBuilder(session).build();
        clientDao = clientMapper.clientDao();
    }

    // Create methods

    @Override
    public Client create(String clientName, String clientSurname, int clientAge) {
        return clientDao.create(clientName, clientSurname, clientAge);
    }

    // Read methods

    @Override
    public Client findByUUID(UUID clientId) {
        return clientDao.findByUUID(clientId);
    }

    @Override
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    @Override
    public List<Client> findAllActive() {
        return clientDao.findAllActive();
    }

    // Update methods

    @Override
    public void update(Client client) {
        clientDao.update(client);
    }

    @Override
    public void expire(Client client) {
        clientDao.expire(client);
    }

    // Delete methods

    @Override
    public void delete(Client client) {
        clientDao.delete(client);
    }

    @Override
    public void delete(UUID clientID) {
        clientDao.deleteByUUID(clientID);
    }

    @Override
    public void close() {
        super.close();
    }
}
