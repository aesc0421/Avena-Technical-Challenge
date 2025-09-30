package com.avenaio.technical_test.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ queues and exchanges
 */
@Configuration
public class RabbitMQConfig {

    @Value("${habit.queue.main}")
    private String mainQueueName;

    @Value("${habit.queue.individual}")
    private String individualQueueName;

    @Value("${habit.exchange}")
    private String exchangeName;

    // Exchange
    @Bean
    public TopicExchange habitExchange() {
        return new TopicExchange(exchangeName);
    }

    // Main queue for processing all records from previous day
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(mainQueueName)
                .withArgument("x-message-ttl", 300000) // 5 minutes TTL
                .build();
    }

    // Individual queue for processing each habit record
    @Bean
    public Queue individualQueue() {
        return QueueBuilder.durable(individualQueueName)
                .withArgument("x-message-ttl", 600000) // 10 minutes TTL
                .build();
    }

    // Bindings
    @Bean
    public Binding mainQueueBinding() {
        return BindingBuilder
                .bind(mainQueue())
                .to(habitExchange())
                .with("habit.main");
    }

    @Bean
    public Binding individualQueueBinding() {
        return BindingBuilder
                .bind(individualQueue())
                .to(habitExchange())
                .with("habit.individual");
    }

    // Message converter for JSON serialization
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    // Listener container factory
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
}