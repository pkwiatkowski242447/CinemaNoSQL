package model.producer;

import jakarta.json.bind.JsonbBuilder;
import model.constants.KafkaConstants;
import model.model.Ticket;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TicketProducer {

    private final static Logger logger = LoggerFactory.getLogger(TicketProducer.class);

    private static Producer ticketProducer;

    public static void initializeProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "46d54bc9-8db4-4665-8fcb-df0b8e3e589b");
        ticketProducer = new KafkaProducer(producerConfig);
        ticketProducer.initTransactions();
    }

    public void sendTicket(Ticket ticket) throws ExecutionException, InterruptedException {
        try {
            ticketProducer.beginTransaction();

            String jsonString = JsonbBuilder.create().toJson(ticket);
            jsonString = String.format("[cinema_name:cinema] %s", jsonString);
            logger.debug("Json string: " + jsonString);
            ProducerRecord<UUID, String> ticketRecord = new ProducerRecord<>(KafkaConstants.TICKET_TOPIC, ticket.getTicketID(), jsonString);
            Future<RecordMetadata> ticketRecordSent = ticketProducer.send(ticketRecord);
            RecordMetadata recordMetadata = ticketRecordSent.get();

            ticketProducer.commitTransaction();
        } catch (ProducerFencedException exception) {
            ticketProducer.close();
        } catch (KafkaException exception) {
            ticketProducer.abortTransaction();
        }
    }

    public void createTicketTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");

        try (Admin admin = Admin.create(properties)) {
            NewTopic ticketsTopic = new NewTopic(KafkaConstants.TICKET_TOPIC, KafkaConstants.NUMBER_OF_PARTITIONS, KafkaConstants.REPLICATION_FACTOR);
            CreateTopicsOptions ticketsTopicOptions = new CreateTopicsOptions()
                    .timeoutMs(10000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult ticketsTopicCreateResult = admin.createTopics(List.of(ticketsTopic), ticketsTopicOptions);
            KafkaFuture<Void> futureResult = ticketsTopicCreateResult.values().get(KafkaConstants.TICKET_TOPIC);
            futureResult.get();
        } catch (ExecutionException exception) {
            logger.debug("Execution exception occurred: " + exception.getMessage(), exception);
        } catch (TopicExistsException exception) {
            logger.debug("Topic already exists: " + exception.getMessage(), exception);
        }
    }
}
