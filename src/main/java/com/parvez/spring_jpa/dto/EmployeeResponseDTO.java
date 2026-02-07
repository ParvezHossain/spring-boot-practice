package com.parvez.spring_jpa.dto;

import java.io.Serializable;

public record EmployeeResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        Integer age,
        Double salary
) implements Serializable {
}
