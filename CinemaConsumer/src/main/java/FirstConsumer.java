import constants.GeneralConstants;
import consumer.TicketConsumer;
import exceptions.MongoConfigNotFoundException;
import repositories.TicketRepository;

public class FirstConsumer {
    public static void main(String[] args) throws MongoConfigNotFoundException {
        String databaseName = GeneralConstants.BACKUP_DB_NAME_NO_1;
        TicketConsumer ticketConsumer = new TicketConsumer();
        ticketConsumer.receiveClients(new TicketRepository(databaseName));
    }
}
