package com.parvez.spring_jpa.dto;

public record FieldErrorResponse(
        String field,
        String message
) {}