import constants.GeneralConstants;
import consumer.TicketConsumer;

public class Main {
    public static void main(String[] args) {
        TicketConsumer ticketConsumer = new TicketConsumer();
        ticketConsumer.runConsumerGroup(GeneralConstants.BACKUP_DB_NAME);
    }
}
