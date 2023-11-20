package model.repositories.interfaces;

import model.Client;

public interface ClientRepositoryInterface extends RepositoryInterface<Client> {

    Client create(String clientName, String clientSurname, int clientAge);
}
