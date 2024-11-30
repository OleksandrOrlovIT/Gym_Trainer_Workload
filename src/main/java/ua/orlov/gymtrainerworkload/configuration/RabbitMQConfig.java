package ua.orlov.gymtrainerworkload.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.orlov.gymtrainerworkload.service.message.MessageReceiver;

@Configuration
public class RabbitMQConfig {

    private static final String DLQ_SUFFIX = ".dlq";

    @Value("${rabbitmq.trainer-workload-queue-name}")
    private String rabbitmqTrainerWorkloadQueueName;

    @Bean
    public ConnectionFactory connectionFactory(@Value("${rabbitmq.host}") String rabbitHost,
                                               @Value("${rabbitmq.port}") Integer rabbitPort,
                                               @Value("${rabbitmq.username}") String rabbitUsername,
                                               @Value("${rabbitmq.password}") String rabbitPassword) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost, rabbitPort);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        return connectionFactory;
    }

    @Bean
    public Queue trainerWorkloadQueue(@Value("${rabbitmq.retry-time-ms}") Integer retryTimeMs) {
        return QueueBuilder.durable(rabbitmqTrainerWorkloadQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", rabbitmqTrainerWorkloadQueueName + DLQ_SUFFIX)
                .withArgument("x-message-ttl", retryTimeMs)
                .build();
    }

    @Bean
    public Queue trainerWorkloadDLQ() {
        return new Queue(rabbitmqTrainerWorkloadQueueName + DLQ_SUFFIX, true);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rabbitmqTrainerWorkloadQueueName);
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer dlqContainer(ConnectionFactory connectionFactory,
                                                       MessageListenerAdapter dlqListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rabbitmqTrainerWorkloadQueueName + DLQ_SUFFIX);
        container.setMessageListener(dlqListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public MessageListenerAdapter dlqListenerAdapter(MessageReceiver receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveDLQMessage");
        adapter.setMessageConverter(new SimpleMessageConverter());
        return adapter;
    }
}
