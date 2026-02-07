package com.parvez.spring_jpa.amqp;

import com.parvez.spring_jpa.config.RabbitMQConfig;
import com.parvez.spring_jpa.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final JavaMailSender mailSender;
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmail(EmailEvent emailEvent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailEvent.getTo());
            message.setSubject(emailEvent.getSubject());
            message.setText(emailEvent.getBody());
            mailSender.send(message);

            log.info("Email sent to {}", emailEvent.getTo());
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw e; // IMPORTANT → triggers retry
        }
    }
}
