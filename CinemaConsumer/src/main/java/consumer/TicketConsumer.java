package consumer;

import constants.KafkaConstants;
import exceptions.MongoConfigNotFoundException;
import jakarta.json.bind.JsonbBuilder;
import model.Ticket;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.TicketRepository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketConsumer {

    private final static Logger logger = LoggerFactory.getLogger(TicketConsumer.class);

    public void receiveClients(KafkaConsumer<UUID, String> consumer, TicketRepository repository) {
        try {
            int messagesReceived = 0;
            Map<Integer, Long> offsets = new HashMap<>();
            Duration pollTimeout = Duration.of(10, ChronoUnit.MILLIS);

            while(true) {
                ConsumerRecords<UUID, String> receivedRecords = consumer.poll(pollTimeout);
                for (ConsumerRecord<UUID, String> record : receivedRecords) {
                    String value = record.value();
                    String ticketJson = value.split(":")[1];
                    Ticket ticket = JsonbBuilder.create().fromJson(ticketJson, Ticket.class);
                    repository.create(ticket);

                    logger.debug("Received message: " + record);
                    logger.debug("Ticket object: " + ticket.toString());
                }
            }
        } catch (WakeupException exception) {
            logger.debug(exception.getMessage());
        }
    }

    public KafkaConsumer<UUID, String> initializeConsumer() {
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.CLIENT_GROUP_NAME);
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka:9292,kafka:9392");
        consumerProperties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, KafkaConstants.CONSUMER_ISOLATION_LEVEL);
        return new KafkaConsumer<UUID, String>(consumerProperties);
    }

    public List<KafkaConsumer<UUID, String>> createGroupOfConsumers() {
        List<KafkaConsumer<UUID, String>> consumers = new ArrayList<>();
        KafkaConsumer<UUID, String> consumerNo1 = this.initializeConsumer();
        KafkaConsumer<UUID, String> consumerNo2 = this.initializeConsumer();
        consumerNo1.subscribe(List.of(KafkaConstants.TICKET_TOPIC));
        consumerNo2.subscribe(List.of(KafkaConstants.TICKET_TOPIC));
        consumers.add(consumerNo1);
        consumers.add(consumerNo2);
        return consumers;
    }

    public void runConsumerGroup(String databaseName) {
        List<KafkaConsumer<UUID, String>> consumers = this.createGroupOfConsumers();
        ExecutorService executorService = Executors.newFixedThreadPool(consumers.size());
        try {
            for (int i = 0; i < consumers.size(); i++) {
                TicketRepository ticketRepository = new TicketRepository(databaseName + "_no" + i);
                int finalIteratorValue = i;
                executorService.execute(() -> receiveClients(consumers.get(finalIteratorValue), ticketRepository));
            }
        } catch (MongoConfigNotFoundException exception) {
            throw new RuntimeException("Connection with the MongoDB database could not be established.");
        }
    }
}
