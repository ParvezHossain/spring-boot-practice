package com.parvez.spring_jpa.amqp;

import com.parvez.spring_jpa.config.RabbitMQConfig;
import com.parvez.spring_jpa.dto.EmployeeSalaryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeSalaryEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(EmployeeSalaryUpdatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SALARY_EXCHANGE,
                RabbitMQConfig.SALARY_ROUTING_KEY,
                event
        );
    }
}
