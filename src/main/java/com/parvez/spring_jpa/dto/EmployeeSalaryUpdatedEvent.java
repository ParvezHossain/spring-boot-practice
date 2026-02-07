package com.parvez.spring_jpa.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EmployeeSalaryUpdatedEvent(
        Long employeeId,
        Double oldSalary,
        Double newSalary,
        Double increment,
        LocalDateTime changedAt
) implements Serializable {
}
