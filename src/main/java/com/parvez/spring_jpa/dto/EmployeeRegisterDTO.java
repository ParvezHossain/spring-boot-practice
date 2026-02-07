package com.parvez.spring_jpa.dto;

import com.parvez.spring_jpa.model.Role;

public record EmployeeRegisterDTO(
        String firstName,
        String lastName,
        String username,
        String email,
        Integer age,
        Double salary,
        String password,
        Role role
) {
}