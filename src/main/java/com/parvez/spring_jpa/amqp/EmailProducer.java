package com.parvez.spring_jpa.amqp;

import com.parvez.spring_jpa.config.RabbitMQConfig;
import com.parvez.spring_jpa.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailEvent emailEvent) {
        rabbitTemplate
                .convertAndSend(
                        RabbitMQConfig.EMAIL_EXCHANGE,
                        RabbitMQConfig.EMAIL_ROUTING_KEY,
                        emailEvent
                );
    }
}
