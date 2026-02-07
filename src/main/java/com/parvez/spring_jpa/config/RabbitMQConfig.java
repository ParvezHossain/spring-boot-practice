package com.parvez.spring_jpa.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SALARY_EXCHANGE = "employee.salary.exchange";
    public static final String SALARY_QUEUE = "employee.salary.updated.queue";
    public static final String SALARY_ROUTING_KEY = "employee.salary.updated";

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
}
