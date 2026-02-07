package com.parvez.spring_jpa.amqp;

import com.parvez.spring_jpa.config.RabbitMQConfig;
import com.parvez.spring_jpa.dto.EmployeeSalaryUpdatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SalaryUpdatedListener {

    @RabbitListener(queues = RabbitMQConfig.SALARY_QUEUE)
    public void onSalaryUpdate(EmployeeSalaryUpdatedEvent event) {
        System.out.println("Salary Updated Event Received:");
        System.out.println(event);
    }
}
