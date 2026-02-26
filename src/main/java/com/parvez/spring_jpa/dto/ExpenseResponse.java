package com.parvez.spring_jpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        BigDecimal amount,
        LocalDate date,
        UUID categoryId,
        String categoryName,
        String description
) {
}
