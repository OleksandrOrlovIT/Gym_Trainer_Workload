package ua.orlov.gymtrainerworkload.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.orlov.gymtrainerworkload.service.messages.MessageReceiver;

@Configuration
public class RabbitMQConfig {

    private final String RABBITMQ_HOST;
    private final Integer RABBITMQ_PORT;
    private final String RABBITMQ_USERNAME;
    private final String RABBITMQ_PASSWORD;
    private final String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;
    private final Integer RETRY_TIME_MS;

    private static final String DLQ_SUFFIX = ".dlq";

    public RabbitMQConfig(@Value("${RABBITMQ.HOST}") String RABBITMQ_HOST,
                          @Value("${RABBITMQ.PORT}")Integer RABBITMQ_PORT,
                          @Value("${RABBITMQ.USERNAME}")String RABBITMQ_USERNAME,
                          @Value("${RABBITMQ.PASSWORD}")String RABBITMQ_PASSWORD,
                          @Value("${RABBITMQ.TRAINER-WORKLOAD-QUEUE-NAME}")String RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME,
                          @Value("${RABBITMQ.RETRY-TIME-MS}") Integer RABBITMQ_RETRY_TIME_MS) {
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.RABBITMQ_PORT = RABBITMQ_PORT;
        this.RABBITMQ_USERNAME = RABBITMQ_USERNAME;
        this.RABBITMQ_PASSWORD = RABBITMQ_PASSWORD;
        this.RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME = RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME;
        this.RETRY_TIME_MS = RABBITMQ_RETRY_TIME_MS;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST, RABBITMQ_PORT);
        connectionFactory.setUsername(RABBITMQ_USERNAME);
        connectionFactory.setPassword(RABBITMQ_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public Queue trainerWorkloadQueue() {
        return QueueBuilder.durable(RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME + DLQ_SUFFIX)
                .withArgument("x-message-ttl", RETRY_TIME_MS)
                .build();
    }

    @Bean
    public Queue trainerWorkloadDLQ() {
        return new Queue(RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME + DLQ_SUFFIX, true);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer dlqContainer(ConnectionFactory connectionFactory,
                                                       MessageListenerAdapter dlqListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RABBITMQ_TRAINER_WORKLOAD_QUEUE_NAME + DLQ_SUFFIX);
        container.setMessageListener(dlqListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public MessageListenerAdapter dlqListenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveDLQMessage");
    }
}
