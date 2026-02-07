package com.parvez.spring_jpa.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ===================== EMPLOYEE SALARY UPDATE =========================
    public static final String SALARY_EXCHANGE = "employee.salary.exchange";
    public static final String SALARY_QUEUE = "employee.salary.updated.queue";
    public static final String SALARY_ROUTING_KEY = "employee.salary.updated";

    //   ================== EMAIL SENDING ======================
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    @Bean
    public TopicExchange salaryExchange() {
        return new TopicExchange(SALARY_EXCHANGE);
    }

    @Bean
    public Queue salaryQueue() {
        return QueueBuilder.durable(SALARY_QUEUE).build();
    }

    @Bean
    public Binding salaryBinding() {
        return BindingBuilder
                .bind(salaryQueue())
                .to(salaryExchange())
                .with(SALARY_ROUTING_KEY);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }


    //RETRY Mechanism
/*
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(true);

        return factory;
    }
*/
}
