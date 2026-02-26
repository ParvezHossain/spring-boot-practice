package com.parvez.spring_jpa.dto;

import java.util.List;

public record ApiErrorResponse(
        String message,
        List<FieldErrorResponse> errors
) {
}
